package msk.node.viz.spectrum.single;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import msk.util.DataManipulator;
import msk.util.KeyLogic;
import msk.util.SettingsModelCreate;
import msk.util.viz.SpectrumImager;

/**
 * This is the model implementation of SpectrumView. This node is used to plot
 * and view a single spectrum.
 *
 * @author Andrew P Talbot
 * @version 19/07/2018
 */
public class SpectrumViewNodeModel extends NodeModel {

	private NodeLogger logger = NodeLogger.getLogger(SpectrumViewNodeModel.class);

	protected static final String X_PIXEL_ID = "x_pixel_num_edit_id";
	protected static final String Y_PIXEL_ID = "y_pixel_num_edit_id";
	protected static final String YES_NO_ID = "save_yes_no_buttongroup_id";
	protected static final String FILE_CHOOSER_ID = "file_chooser_id";
	protected static final String WIDTH_ID = "width_num_edit_id";
	protected static final String HEIGHT_ID = "height_num_edit_id";

	private SettingsModelIntegerBounded m_x_pixel = SettingsModelCreate.createSettingsModelIntegerBounded(X_PIXEL_ID, 1,
			1, Integer.MAX_VALUE);
	private SettingsModelIntegerBounded m_y_pixel = SettingsModelCreate.createSettingsModelIntegerBounded(Y_PIXEL_ID, 1,
			1, Integer.MAX_VALUE);
	private SettingsModelString m_yes_no = SettingsModelCreate.createSettingsModelString(YES_NO_ID, null);
	private SettingsModelString m_file_chooser = SettingsModelCreate.createSettingsModelString(FILE_CHOOSER_ID, null);
	private SettingsModelIntegerBounded m_width = SettingsModelCreate.createSettingsModelIntegerBounded(WIDTH_ID, 1100,
			1, Integer.MAX_VALUE);
	private SettingsModelIntegerBounded m_height = SettingsModelCreate.createSettingsModelIntegerBounded(HEIGHT_ID, 500,
			1, Integer.MAX_VALUE);

	private boolean isRow;
	private int nXPixels;
	private int nYPixels;
	private int xPixel;
	private int yPixel;
	private int index;
	private int jpegWidth;
	private int jpegHeight;

	protected String title;
	protected double[] mz;
	protected double[] intensity;
	protected boolean toSave;
	protected String filePath;

	protected SpectrumImager specImager;

	/**
	 * Constructor for the node model.
	 */
	protected SpectrumViewNodeModel() {
		/*
		 * input are m/z and intensity array.
		 * 
		 * No output
		 */
		super(1, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		setFields();

		Point[] toDraw = new Point[] { new Point(xPixel, yPixel) };

		logger.info("___________________________________________________________________________________________");
		System.out.println(xPixel + " ____________________ " + yPixel);
		logger.info("___________________________________________________________________________________________");

		logger.info("We sketch the pixel " + xPixel + ", " + yPixel);

		
		logger.fatal("toSave == " + toSave);
		if (toSave) {
			specImager = new SpectrumImager(inData[0], isRow, toDraw, toSave, jpegWidth, jpegHeight, filePath, logger);
		} else {
			specImager = new SpectrumImager(inData[0], isRow, toDraw, toSave, SpectrumImager.WIDTH,
					SpectrumImager.HEIGHT, filePath, logger);
		}

		// All fields are ready, now we make the view.
		return new BufferedDataTable[] {};
	}

	/**
	 * Set fields at the start of the execute() method.
	 */
	protected void setFields() {

		isRow = true;
		
		if (m_yes_no.getStringValue().equals("Yes"))
			toSave = true;
		else
			toSave = false;

		jpegWidth = m_width.getIntValue();
		jpegHeight = m_height.getIntValue();

		filePath = m_file_chooser.getStringValue();
		xPixel = m_x_pixel.getIntValue();
		yPixel = m_y_pixel.getIntValue();
	}

	/**
	 * Returns the number of x pixels and the number of y pixels.
	 * 
	 * @param inData
	 *            is the incoming DataTable
	 * @return a length 2 integer array, 0th element is the number of x pixels,
	 *         the 1st element is the number of y pixels.
	 */
	protected int[] getNPixels(final BufferedDataTable[] inData) throws InvalidSettingsException {
		if (inData[0].size() == 0 || inData[1].size() == 0)
			throw new InvalidSettingsException("Please input with at least one row.");

		KeyLogic key;
		// Get the key for the final column / row.
		if (isRow) {

			CloseableRowIterator rowIter = inData[0].iterator();
			DataRow row = rowIter.next();

			while (rowIter.hasNext())
				row = rowIter.next();

			rowIter.close();

			logger.info("KEY VALUE IS " + row.getKey().getString() + "    ______________________________________");
			key = new KeyLogic(row.getKey());
		} else {
			// Column case

			int nCols = inData[0].getDataTableSpec().getNumColumns();
			String keyString = inData[0].getDataTableSpec().getColumnNames()[nCols - 1];

			logger.info("KEYSTRING VALUE IS " + keyString + "    ______________________________________");
			key = new KeyLogic(keyString);
		}
		return new int[] { key.getXPixel(), key.getYPixel() };
	}

	/**
	 * Use index to find the value of the correct Title.
	 * 
	 * @return the title for the given panel.
	 */
	protected String getTitle(BufferedDataTable table) {
		String title;
		if (isRow) {
			CloseableRowIterator rowIter = table.iterator();
			DataRow row = rowIter.next();
			int tmp = index;
			while (tmp > 1)
				row = rowIter.next();
			title = row.getKey().getString();
		} else {
			title = table.getDataTableSpec().getColumnNames()[index];
		}
		return title;
	}

	/**
	 * Returns an array of the m/z and intensity to a corresponding pixel
	 * values.
	 * 
	 * @param table
	 *            is the incoming table.
	 * @param xPixel
	 *            the x pixel we consider.
	 * @param yPixel
	 *            the y pixel we consider.
	 * @return an array for the corresponding.
	 */
	protected double[] getArray(BufferedDataTable table, int xPixel, int yPixel) {
		double[] arr;

		// This row always has the right value.
		if (isRow) {
			CloseableRowIterator rowIter = table.iterator();
			DataRow row = rowIter.next();
			int tmp = index;
			while (tmp > 1) {
				row = rowIter.next();
			}
			arr = DataManipulator.createRowArray(row);
		} else {
			arr = DataManipulator.createColumnArray(table, index - 1, nXPixels * nYPixels);
		}
		return arr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// Do Nothing.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		/*
		 * Do nothing :)
		 */
		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_x_pixel.saveSettingsTo(settings);
		m_y_pixel.saveSettingsTo(settings);
		m_yes_no.saveSettingsTo(settings);
		m_file_chooser.saveSettingsTo(settings);
		m_width.saveSettingsTo(settings);
		m_height.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_x_pixel.loadSettingsFrom(settings);
		m_y_pixel.loadSettingsFrom(settings);
		m_yes_no.loadSettingsFrom(settings);
		m_file_chooser.loadSettingsFrom(settings);
		m_width.loadSettingsFrom(settings);
		m_height.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_x_pixel.validateSettings(settings);
		m_y_pixel.validateSettings(settings);
		m_yes_no.validateSettings(settings);
		m_file_chooser.validateSettings(settings);
		m_width.validateSettings(settings);
		m_height.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

}
