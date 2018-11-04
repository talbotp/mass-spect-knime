package msk.util.viz;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.knime.core.data.DataRow;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;

import msk.util.DataManipulator;
import msk.util.KeyLogic;

/**
 * Class for viewing and saving JPEG.
 * 
 * TODO : Rejig this using inheritence etc.
 * 
 * @author Andrew P Talbot
 * @version 21/07/2018
 *
 */
public class SpectrumImager {

	private NodeLogger logger;

	public static final int WIDTH = 1100;
	public static final int HEIGHT = 500;

	// KNIME related fields here
	private boolean isRow;

	private int nToDraw;
	private Point[] toDraw;
	private int[] indexes;
	private int nXPixels;
	private int nYPixels;

	private String[] title;
	private double[] mz;
	private ArrayList<double[]> intensities;
	private JPanel panel;

	/**
	 * This is the constructor for when we wish to draw an image into a JPanel.
	 * 
	 * @param mzData
	 *            is the x coordinates
	 * @param intensityData
	 *            is the y Coordinates
	 * @param isRow
	 *            is the data stored in rows.
	 * @param saveImage
	 *            stores whether we wish to save the image or not.
	 * @param filePath
	 *            is the location of where we store the image.
	 * @throws InvalidSettingsException
	 *             is the data tables are less than a specific size.
	 */
	public SpectrumImager(BufferedDataTable mzData, BufferedDataTable intensityData, boolean isRow, Point[] toDraw,
			boolean saveImage, String filePath, NodeLogger logger) throws InvalidSettingsException {
		// Require data to draw.
		if (mzData.size() == 0 || intensityData.size() == 0)
			throw new InvalidSettingsException("Please input with at least one row.");

		this.logger = logger;
		this.isRow = isRow;

		setNPixels(mzData);

		nToDraw = toDraw.length;
		setValidPointsIndexes(toDraw);

		title = new String[nToDraw];

		mz = getArray(mzData, 1, 1, 0);
		intensities = new ArrayList<>();

		for (int i = 0; i < nToDraw; i++) {
			logger.info("We add to the intensities ArrayList number " + i);
			intensities.add(getArray(intensityData, this.toDraw[i].x, this.toDraw[i].y, i));
		}

		String panelTitle = "";
		for (int i = 0; i < title.length; i++) {
			panelTitle += title[i] + " ";
		}

		panel = getSpectrumPanel(panelTitle, mz, intensities, WIDTH, HEIGHT, saveImage, filePath, title);
	}

	/**
	 * Constructor for when the data has been combined into one datatable.
	 * 
	 */
	public SpectrumImager(BufferedDataTable data, boolean isRow, Point[] toDraw, boolean saveImage, int width,
			int height, String filePath, NodeLogger logger) throws InvalidSettingsException {
		// Require data to draw.
		if (data.size() == 0)
			throw new InvalidSettingsException("Please input with at least one row.");

		/*
		 * Use mzData table as no need to make another DataTable object.
		 */
		this.logger = logger;
		this.isRow = isRow;

		setNPixels(data);

		nToDraw = toDraw.length;
		setValidPointsIndexes(toDraw);

		title = new String[nToDraw];

		mz = getMZ(data);
		intensities = new ArrayList<>();

		for (int i = 0; i < nToDraw; i++) {
			intensities.add(getArray(data, this.toDraw[i].x, this.toDraw[i].y, i));
		}

		String panelTitle = "";
		for (int i = 0; i < title.length; i++) {
			panelTitle += title[i] + " ";
		}

		panel = getSpectrumPanel(panelTitle, mz, intensities, width, height, saveImage, filePath, title);
		logger.fatal("We have saved the image to " + filePath);
	}

	/**
	 * To be used only in the case when
	 * 
	 * @param data
	 * @return
	 */
	public double[] getMZ(BufferedDataTable data) {
		double[] mz;
		if (isRow) {
			String[] mzStr = data.getDataTableSpec().getColumnNames();
			mz = new double[mzStr.length];
			for (int i = 0; i < mzStr.length; ++i) {
				mz[i] = Double.parseDouble(mzStr[i]);
			}
		} else {
			mz = new double[(int) data.size()];
			CloseableRowIterator rowIter = data.iterator();

			int counter = 0;

			while (rowIter.hasNext()) {
				DataRow row = rowIter.next();
				mz[counter] = Double.parseDouble(row.getKey().getString());
				counter++;
			}

			rowIter.close();
		}
		return mz;
	}

	/**
	 * Returns the number of x pixels and the number of y pixels.
	 * 
	 * @param inData
	 *            is the incoming DataTable
	 * @return a length 2 integer array, 0th element is the number of x pixels,
	 *         the 1st element is the number of y pixels.
	 */
	protected void setNPixels(BufferedDataTable mzData) throws InvalidSettingsException {
		KeyLogic key;
		// Get the key for the final column / row.
		if (isRow) {

			CloseableRowIterator rowIter = mzData.iterator();
			DataRow row = rowIter.next();

			while (rowIter.hasNext())
				row = rowIter.next();

			rowIter.close();

			key = new KeyLogic(row.getKey());
		} else {
			// Column case

			int nCols = mzData.getDataTableSpec().getNumColumns();
			String keyString = mzData.getDataTableSpec().getColumnNames()[nCols - 1];

			key = new KeyLogic(keyString);
		}

		nXPixels = key.getXPixel();
		nYPixels = key.getYPixel();
	}

	/**
	 * Set the pixel values that we will be drawing.
	 * 
	 * @param toDraw
	 *            is the points we are going to draw.
	 */
	public void setValidPointsIndexes(Point[] toDraw) {
		for (Point p : toDraw) {
			if (p.x > nXPixels)
				p.x = nXPixels;

			if (p.y > nYPixels)
				p.y = nYPixels;
		}

		this.toDraw = toDraw;

		int length = toDraw.length;

		indexes = new int[length];

		// Set the indexes used to get the right array later on down the line.
		for (int i = 0; i < length; i++) {
			indexes[i] = this.toDraw[i].x + (this.toDraw[i].y - 1) * nXPixels;
		}
	}

	/**
	 * Returns an array of the m/z and intensity to a corresponding pixel
	 * values.
	 * 
	 * We also set the title here.
	 * 
	 * @param table
	 *            is the incoming table.
	 * @param xPixel
	 *            the x pixel we consider.
	 * @param yPixel
	 *            the y pixel we consider.
	 * @return the array.
	 */
	protected double[] getArray(BufferedDataTable table, int xPixel, int yPixel, int index) {
		double[] arr;
		// This row always has the right value.
		if (isRow) {
			CloseableRowIterator rowIter = table.iterator();
			DataRow row = rowIter.next();
			int tmp = indexes[index];
			while (tmp > 1) {
				row = rowIter.next();
				tmp--;
			}
			arr = DataManipulator.createRowArray(row);

			if (title[index] == null)
				title[index] = row.getKey().getString();

		} else {
			arr = DataManipulator.createColumnArray(table, indexes[index] - 1, nXPixels * nYPixels);

			if (title[index] == null)
				title[index] = table.getDataTableSpec().getColumnNames()[indexes[index]];
		}

		return arr;
	}

	/**
	 * Saves a Spectrum in the form of a JPEG
	 * 
	 * REQUIRES : mzArr.length == intensityArr.length
	 * 
	 * @param mzArr
	 *            is the x axis, typically the m/z array.
	 * @param intensityArr
	 *            is the y axis, typically the intensity array.
	 * @param filePath
	 *            is the path to store the jpeg files.
	 * @param title
	 *            is the title of the plot.
	 * @param xSize
	 *            is the width for the plot to be.
	 * @param ySize
	 *            is the height of the plot.
	 */
	public static void saveSpectraJPEG(String title, double[] mz, ArrayList<double[]> intensity, int xSize, int ySize,
			String filePath) {
		// Make the Line Chart.
		JFreeChart chart = getLineChart(title, "m/z", "Intensity", mz, intensity, new String[] { title });

		File file = new File(filePath);
		try {
			file.createNewFile();
			ChartUtilities.saveChartAsJPEG(file, chart, xSize, ySize);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a JPanel of a LineChart and saves a JPEG of it if toSave == true.
	 * 
	 * REQUIRES : mzArr.length == intensityArr.length
	 * 
	 * @param mzArr
	 *            is the m/z data.
	 * @param intensityArr
	 *            is the intensity data.
	 * @param filePath
	 *            is the path to where we save the JPEG.
	 * @param title
	 *            is the title on the chart.
	 * @param xSize
	 *            is the width of the chart..
	 * @param ySize
	 *            is the height of the chart.
	 * @param toSave
	 *            is true to save the JPEG, false otherwise.
	 * @return a JPamel with a Line Chart on it.
	 */
	public JPanel getSpectrumPanel(String title, double[] mz, ArrayList<double[]> intensity, int xSize,
			int ySize, boolean toSave, String filePath, String[] titles) {

		// Make the chart.
		JFreeChart chart = getLineChart(title, "m/z", "Intensity", mz, intensity, titles);

		JPanel panel = new JPanel();
		panel.setLayout(new java.awt.BorderLayout());

		ChartPanel chartPanel = new ChartPanel(chart);

		// If we are meant to the we save it.
		if (toSave) {
			File file = new File(filePath + "/" + title.replaceAll("\\s+",""));
			try {
				file.createNewFile();
				ChartUtilities.saveChartAsJPEG(file, chart, xSize, ySize);
				logger.info("We have successfully saved the spectrum.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		panel.add(chartPanel, java.awt.BorderLayout.CENTER);
		panel.validate();
		return panel;
	}

	/**
	 * Creates a JFreeChart LineChart.
	 * 
	 * REQUIRES : - xData.length == yData.length
	 * 
	 * @param title
	 *            is the title to appear on the panel.
	 * @param xLab
	 *            is the x axis label.
	 * @param yLab
	 *            is the y axis label.
	 * @param xData
	 *            is the x axis data.
	 * @param yData
	 *            is the y axis data
	 * @return a Line Chart of the given parameters.
	 */
	public static JFreeChart getLineChart(String title, String xLab, String yLab, double[] xData,
			ArrayList<double[]> yData, String[] titles) {
		XYSeries[] data = new XYSeries[yData.size()];

		// Should be equal to yData.length.
		int length = xData.length;

		// Add the data.
		for (int i = 0; i < yData.size(); i++) {
			final XYSeries d = new XYSeries(titles[i]);
			double[] yVals = yData.get(i);

			// Add the values.
			for (int j = 0; j < length; j++) {
				d.add(xData[j], yVals[j]);
			}
			data[i] = d;
		}

		// Make the dataset.
		final XYSeriesCollection dataset = new XYSeriesCollection();

		for (int i = 0; i < yData.size(); i++) {
			dataset.addSeries(data[i]);
		}

		JFreeChart chart = ChartFactory.createXYLineChart(title, xLab, yLab, dataset, PlotOrientation.VERTICAL, true,
				true, false);

		if (yData.size() == 1)
			chart.removeLegend();

		chart.setBackgroundPaint(Color.WHITE);

		return chart;
	}

	public JPanel getPanel() {
		return panel;
	}
	
	public static JPanel getSpectrumPanelStatic(String title, double[] mz, ArrayList<double[]> intensity, int xSize,
			int ySize, boolean toSave, String filePath, String[] titles) {

		// Make the chart.
		JFreeChart chart = getLineChart(title, "m/z", "Intensity", mz, intensity, titles);

		JPanel panel = new JPanel();
		panel.setLayout(new java.awt.BorderLayout());

		ChartPanel chartPanel = new ChartPanel(chart);

		// If we are meant to the we save it.
		if (toSave) {
			File file = new File(filePath + "/" + title.replaceAll("\\s+","") + ".jpeg");
			try {
				file.createNewFile();
				ChartUtilities.saveChartAsJPEG(file, chart, xSize, ySize);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		panel.add(chartPanel, java.awt.BorderLayout.CENTER);
		panel.validate();
		return panel;
	}

}
