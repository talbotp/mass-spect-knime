package msk.node.pre.spec.roi;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.node.BufferedDataContainer;
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

import msk.util.KeyLogic;
import msk.util.SettingsModelCreate;

/**
 * This is the model implementation of IonImageRegionOfInterest.
 * 
 * This node is used for limiting the x and y considered when generating ion
 * images of MSI data into Regions of Interest.
 *
 * @author Andrew P Talbot
 * @version 01/07/2018
 */
public class ROINodeModel extends NodeModel {
	
	private NodeLogger logger = NodeLogger.getLogger(ROINodeModel.class);

	protected static final String INTEGER_MIN_WIDTH_ID = "min_width_roi_EditDouble";
	protected static final String INTEGER_MAX_WIDTH_ID = "max_width_roi_EditDouble";
	protected static final String INTEGER_MIN_HEIGHT_ID = "min_height_roi_EditDouble";
	protected static final String INTEGER_MAX_HEIGHT_ID = "max_height_roi_EditDouble";

	// Create the SettingsModels for the user input
	private final SettingsModelIntegerBounded m_min_width = SettingsModelCreate
			.createSettingsModelIntegerBounded(INTEGER_MIN_WIDTH_ID, 1, 1, Integer.MAX_VALUE);
	private final SettingsModelIntegerBounded m_max_width = SettingsModelCreate
			.createSettingsModelIntegerBounded(INTEGER_MAX_WIDTH_ID, 1, 1, Integer.MAX_VALUE);
	private final SettingsModelIntegerBounded m_min_height = SettingsModelCreate
			.createSettingsModelIntegerBounded(INTEGER_MIN_HEIGHT_ID, 1, 1, Integer.MAX_VALUE);
	private final SettingsModelIntegerBounded m_max_height = SettingsModelCreate
			.createSettingsModelIntegerBounded(INTEGER_MAX_HEIGHT_ID, 1, 1, Integer.MAX_VALUE);

	private boolean isFirstPass = true;

	private int minWidth;
	private int maxWidth;
	private int minHeight;
	private int maxHeight;

	/**
	 * Constructor for the node model.
	 */
	protected ROINodeModel() {
		/*
		 * m/z table and intensity table incoming and outgoing.
		 */
		super(2, 2);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		// Check if data is empty.
		long nSpectra = inData[0].size();
		if (nSpectra < 1) {
			logger.info("The number of incoming spectra is less than 1, so return an empty table.");
			return new BufferedDataTable[] { inData[0], inData[0] };
		}
		
		if (isFirstPass) {

			minWidth = m_min_width.getIntValue();
			maxWidth = m_max_width.getIntValue();
			minHeight = m_min_height.getIntValue();
			maxHeight = m_max_height.getIntValue();

			isFirstPass = false;
		}
		
		// Create outgoing objects.
		BufferedDataContainer mzTable = exec.createDataContainer(inData[0].getDataTableSpec());
		BufferedDataContainer intensityTable = exec.createDataContainer(inData[1].getDataTableSpec());
		
		CloseableRowIterator mziter = inData[0].iterator();
		CloseableRowIterator intensityiter = inData[1].iterator();
		
		double spectraCounter = 0;
		// Loop through every spectra in table.
		while (mziter.hasNext() && intensityiter.hasNext()) {
			
			// Check and set the execution progress.
			exec.checkCanceled();
			exec.setProgress(spectraCounter / nSpectra);
			
			// Now we do the work for each row.
			DataRow mzRow = mziter.next();
			DataRow intensityRow = intensityiter.next();
			
			KeyLogic key = new KeyLogic(mzRow.getKey());
			
			int key_x = key.getXPixel();
			int key_y = key.getYPixel();
			
			// Check if its a valid pixel in our ROI, add to the outgoing tables if true.
			if (key_x < minWidth || key_x > maxWidth || key_y < minHeight || key_y > maxHeight) {
				continue;
			} else {
				mzTable.addRowToTable(mzRow);
				intensityTable.addRowToTable(intensityRow);
			}

			// Increment the progress monitor.
			spectraCounter++;
		}

		// Close the iterators, containers, and return the tables.
		mziter.close();
		intensityiter.close();
		
		mzTable.close();
		intensityTable.close();
		
		return new BufferedDataTable[] {mzTable.getTable(), intensityTable.getTable()};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		isFirstPass = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_min_width.saveSettingsTo(settings);
		m_max_width.saveSettingsTo(settings);
		m_min_height.saveSettingsTo(settings);
		m_max_height.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_min_width.loadSettingsFrom(settings);
		m_max_width.loadSettingsFrom(settings);
		m_min_height.loadSettingsFrom(settings);
		m_max_height.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_min_width.validateSettings(settings);
		m_max_width.validateSettings(settings);
		m_min_height.validateSettings(settings);
		m_max_height.validateSettings(settings);
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
