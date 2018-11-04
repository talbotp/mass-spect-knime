package msk.node.viz.spectrum.jpeg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
import msk.util.SettingsModelCreate;
import msk.util.viz.SpectrumImager;

/**
 * This is the model implementation of SpectrumJPEG. Create JPEG files for each
 * spectrum and then saves them in a specified location.
 *
 * @author Andrew P Talbot
 * @version 19/07/2018
 */
public class SpectrumJPEGNodeModel extends NodeModel {
	
	private NodeLogger logger = NodeLogger.getLogger(SpectrumJPEGNodeModel.class);

	protected static final String FILE_CHOOSER_ID = "file_destination_id";
	protected static final String ROW_COL_ID = "row_col_but_group_id";
	protected static final String WIDTH_ID = "width_num_edit_id";
	protected static final String HEIGHT_ID = "height_num_edit_id";

	private SettingsModelString m_file_dest = SettingsModelCreate.createSettingsModelString(FILE_CHOOSER_ID, null);
	private SettingsModelString m_row_col = SettingsModelCreate.createSettingsModelString(ROW_COL_ID, "Row");
	private SettingsModelIntegerBounded m_width = SettingsModelCreate.createSettingsModelIntegerBounded(WIDTH_ID, 1100, 1,
			Integer.MAX_VALUE);
	private SettingsModelIntegerBounded m_height = SettingsModelCreate.createSettingsModelIntegerBounded(HEIGHT_ID, 500, 1,
			Integer.MAX_VALUE);

	private boolean isFirstPass;
	private boolean isRow;
	private String fileDest;
	private int width;
	private int height;

	private int nRows;
	private int nCols;

	/**
	 * Constructor for the node model.
	 */
	protected SpectrumJPEGNodeModel() {
		/**
		 * Inputs : mz, and intensity. Outputs : same as input.
		 */
		super(2, 2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		nRows = (int) inData[0].size();
		nCols = inData[0].getDataTableSpec().getNumColumns();
		if (nRows == 1 && nCols > 1)
			isRow = true;
		else if (nRows > 1 && nCols == 1)
			isRow = false;
		else
			return new BufferedDataTable[] { inData[0], inData[1] };
		
		// Check if first pass to reset everything.
		if (isFirstPass) {
			// Set fields.
			if (m_row_col.getStringValue().equals("Row"))
				isRow = true;
			else
				isRow = false;
			fileDest = m_file_dest.getStringValue();
			width = m_width.getIntValue();
			height = m_height.getIntValue();

			nCols = inData[0].getDataTableSpec().getNumColumns();
			nRows = (int) inData[0].size();
			
			isFirstPass = false;
		}

		if (isRow) {
			logger.info("THERE ARE CURRENTLY " + nRows);
			CloseableRowIterator mzIter = inData[0].iterator();
			CloseableRowIterator intensityIter = inData[1].iterator();
			// Iterate through every row.
			while (mzIter.hasNext()) {
				DataRow mzRow = mzIter.next();
				DataRow intensityRow = intensityIter.next();
				
				double[] mz = DataManipulator.createRowArray(mzRow);
				double[] intensity = DataManipulator.createRowArray(intensityRow);
				String key = mzRow.getKey().getString();
				
				ArrayList<double[]> intensities = new ArrayList<>(Arrays.asList(intensity));
				
				SpectrumImager.saveSpectraJPEG(key, mz, intensities, width, height, fileDest + "/" + key);
			}
			
			mzIter.close();
			intensityIter.close();
			
		} 
		/*
		 * Now the column case.
		 */
		else {
			// Loop through the columns
			for (int i = 0; i < nCols; i++) {
				double[] mz = DataManipulator.createColumnArray(inData[0], i, nRows);
				double[] intensity = DataManipulator.createColumnArray(inData[1], i, nRows);
				String key = inData[0].getDataTableSpec().getColumnNames()[i];
				
				ArrayList<double[]> intensities = new ArrayList<>(Arrays.asList(intensity));	
				SpectrumImager.saveSpectraJPEG(key, mz, intensities, width, height, fileDest + "/" + key);
			}
		}
			return new BufferedDataTable[] { inData[0], inData[1] };
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
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {

		// TODO: generated method stub
		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_file_dest.saveSettingsTo(settings);
		m_row_col.saveSettingsTo(settings);
		m_width.saveSettingsTo(settings);
		m_height.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_file_dest.loadSettingsFrom(settings);
		m_row_col.loadSettingsFrom(settings);
		m_width.loadSettingsFrom(settings);
		m_height.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_file_dest.validateSettings(settings);
		m_row_col.validateSettings(settings);
		m_width.validateSettings(settings);
		m_height.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO: generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO: generated method stub
	}

}
