package msk.node.viz.spectrum.compare;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.knime.core.data.DataTableSpec;
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

import msk.util.SettingsModelCreate;
import msk.util.viz.SpectrumImager;

/**
 * This is the model implementation of SpectrumCmpView.
 * 
 * @author Andrew P Talbot
 */
public class SpectrumCmpViewNodeModel extends NodeModel {

	private NodeLogger logger = NodeLogger.getLogger(SpectrumCmpViewNodeModel.class);

	protected static final String PIXELS_ID = "pixels_id";
	protected static final String YES_NO_ID = "yes_no_id";
	protected static final String FILE_CHOOSER_ID = "file_chooser_id";
	protected static final String WIDTH_ID = "width_num_edit_id";
	protected static final String HEIGHT_ID = "height_num_edit_id";

	private SettingsModelString m_pixels = SettingsModelCreate.createSettingsModelString(PIXELS_ID, null);
	private SettingsModelString m_yes_no = SettingsModelCreate.createSettingsModelString(YES_NO_ID, "Yes");
	private SettingsModelString m_file_chooser = SettingsModelCreate.createSettingsModelString(FILE_CHOOSER_ID, null);
	private SettingsModelIntegerBounded m_width = SettingsModelCreate.createSettingsModelIntegerBounded(WIDTH_ID, 1100,
			1, Integer.MAX_VALUE);
	private SettingsModelIntegerBounded m_height = SettingsModelCreate.createSettingsModelIntegerBounded(HEIGHT_ID, 500,
			1, Integer.MAX_VALUE);

	private boolean toSave;
	private String filePath;
	private String pixels;

	private int jpegWidth;
	private int jpegHeight;

	private Point[] pointsToDraw;

	protected SpectrumImager specImager;

	/**
	 * Constructor for the node model.
	 */
	protected SpectrumCmpViewNodeModel() {
		super(1, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		/*
		 * No output, do nothing here.
		 */
		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		setFields();

		setPointsToDraw();

		if (toSave)
			specImager = new SpectrumImager(inData[0], true, pointsToDraw, toSave, jpegWidth, jpegHeight, filePath,
					logger);
		else
			specImager = new SpectrumImager(inData[0], true, pointsToDraw, toSave, SpectrumImager.WIDTH,
					SpectrumImager.HEIGHT, filePath, logger);
		return new BufferedDataTable[] {};
	}

	/**
	 * Parses the pixels input and sets them to be pixels in the list of
	 * spectrums to draw.
	 */
	protected void setPointsToDraw() {
		HashSet<Point> points = new HashSet<>();

		// Create an arraylist of the points in string form.
		ArrayList<String> pixelStrings = new ArrayList<String>();
		setPixelString(pixelStrings, pixels);

		for (String s : pixelStrings) {
			points.add(getPoint(s));
		}

		pointsToDraw = points.toArray(new Point[] {});
	}

	/**
	 * Make a point from a String s which is the points in the DataTable.
	 * 
	 * @param s
	 *            is the input of the pixels to display.
	 * @return the key to the specific pixel that the user has specified they
	 *         wish to be drawn.
	 */
	public static Point getPoint(String s) {
		int commaIndex = s.indexOf(',');
		return new Point(Integer.parseInt(s.substring(0, commaIndex)), Integer.parseInt(s.substring(commaIndex + 1)));
	}

	/**
	 * Set the pixels to be drawn in the image that we make.
	 * 
	 * @param pixelStrings
	 *            is the ArrayList of pixels to be drawn.
	 * @param pixels
	 *            is the String pixel values.
	 */
	public static void setPixelString(ArrayList<String> pixelStrings, String pixels) {
		while (pixels.length() > 0) {
			try {
				int spaceIndex = pixels.indexOf(' ');
				pixelStrings.add(pixels.substring(0, spaceIndex));
				pixels = pixels.substring(spaceIndex + 1);
			} catch (StringIndexOutOfBoundsException e) {
				pixelStrings.add(pixels);
				return;
			}
		}
	}

	/**
	 * Set fields at the start of the execute() method.
	 */
	protected void setFields() {

		if (m_yes_no.getStringValue().equals("Yes"))
			toSave = true;
		else
			toSave = false;

		jpegWidth = m_width.getIntValue();
		jpegHeight = m_height.getIntValue();

		filePath = m_file_chooser.getStringValue();
		pixels = m_pixels.getStringValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// Nothing to do here.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_pixels.saveSettingsTo(settings);
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
		m_pixels.loadSettingsFrom(settings);
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
		m_pixels.validateSettings(settings);
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
