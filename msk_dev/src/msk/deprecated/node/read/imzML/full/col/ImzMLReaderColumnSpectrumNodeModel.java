package msk.deprecated.node.read.imzML.full.col;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import imzML.ImzML;
import msk.util.ImzMLUtil;
import msk.util.SettingsModelCreate;

/**
 * This is the model implementation of ImzMLReader. This node is used for
 * reading a whole imzML dataset into memory where columns are the spectrums.
 * 
 * This currently works only for continuous imzML datasets.
 *
 * @author Andrew P Talbot
 * @version 28/06/2018
 */
public class ImzMLReaderColumnSpectrumNodeModel extends NodeModel {

	// Create the ID for the file chooser.
	protected static final String FILE_CHOOSER_ID = "file_chooser_id_imzML";

	// Here, we create the settingsModel for the choice in the dialopg.
	// Not caps lock for readability later on.
	private final SettingsModelString m_file_chooser = SettingsModelCreate.createSettingsModelString(FILE_CHOOSER_ID, null);

	/**
	 * Constructor for the node model.
	 */
	protected ImzMLReaderColumnSpectrumNodeModel() {
		/**
		 * Out port 0 : returns mzArray Data Table. Out port 1 : returns
		 * intensity Array Data Table.
		 */
		super(0, 2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		// Get the ImzML object from the file chooser
		String filename = m_file_chooser.getStringValue();
		ImzML imzML = ImzMLUtil.getImzML(filename);

		int pixelWidth = imzML.getWidth();
		int pixelHeight = imzML.getHeight();

		// Total number of columns in our data table is given here.
		int nColumns = pixelWidth * pixelHeight;

		// Set the datatype for the columns of our tables.
		DataType type = (new DoubleCell(5)).getType();

		// Create the variables for the spec of the DataTable 1
		// As it is continuous it has only one row.
		String[] names1 = new String[] { "pixel" };
		double[] mzArr = imzML.getSpectrum(1, 1).getmzArray();
		int mzArrLen = mzArr.length;

		// As continuous only one col spec type.
		// TODO : Change this when considering processed datasets.
		DataType[] colSpec1 = new DataType[] { type };

		// As continuous only one col spec type.

		// Set the current pixel so we can loop through them both.
		int currentXPixel = 1;
		int currentYPixel = 1;

		// Create the table spec and the container 2.
		DataTableSpec spec1 = new DataTableSpec(names1, colSpec1);
		BufferedDataContainer bufMZ = exec.createDataContainer(spec1);

		// Set the DataTable rows - as its continuous only right now we are
		// setting 1 column per row.
		for (int i = 0; i < mzArrLen; i++) {
			DataCell[] cells = new DataCell[1];
			cells[0] = new DoubleCell(imzML.getSpectrum(currentXPixel, currentYPixel).getmzArray()[i]);
			DataRow row = new DefaultRow(new RowKey("m/z row " + i), cells);
			bufMZ.addRowToTable(row);
		}
		// Close this container so we can get the table later.
		bufMZ.close();

		// Create the column specification for the output tables. The column
		// specification will be exactly the same for all output tables.
		DataType[] colSpec2 = new DataType[nColumns];
		String[] names2 = new String[nColumns];

		/*
		 * Loop Invariant : 1 <= currentXPixel < pixelWidth 1 <= currentYPixel <
		 * pixelHeight
		 */
		currentXPixel = 1;
		currentYPixel = 1;
		// for loop to set all of the column specifications.
		for (int i = 0; i < nColumns; i++) {

			// Each column is of type double
			colSpec2[i] = type;

			// Need tp set the names of the columns correctly.
			if (currentXPixel > pixelWidth) {
				currentXPixel = 1;
				currentYPixel++;
			}

			names2[i] = "X: " + currentXPixel + " Y: " + currentYPixel;

			// At end, we must increment the xPixel
			currentXPixel++;
		}

		// Create the table spec and the container 2.
		DataTableSpec spec = new DataTableSpec(names2, colSpec2);
		BufferedDataContainer bufIntensity = exec.createDataContainer(spec);

		// Reestablish these before the next one as they will be used again.
		currentXPixel = 1;
		currentYPixel = 1;
		int nRows = imzML.getSpectrum(1, 1).getIntensityArray().length;

		// Loop through every mz to get the intensity at each spectrum, and then
		// load it into a DataRow through
		for (int i = 0; i < nRows; i++) {
			DataCell[] cells = new DataCell[nColumns];
			
			// Check the execution contect
			exec.checkCanceled();

			// loop through every pixel
			for (int j = 0; j < nColumns; j++) {
				if (currentXPixel > pixelWidth) {
					currentXPixel = 1;
					currentYPixel++;
					if (currentYPixel > pixelHeight) {
						currentYPixel = 1;
					}
				}
				cells[j] = new DoubleCell(imzML.getSpectrum(currentXPixel, currentYPixel).getIntensityArray()[i]);
				currentXPixel++;

			}
			// Add the row to the table.
			DataRow row = new DefaultRow(new RowKey("" + i), cells);
			bufIntensity.addRowToTable(row);
		}
		// Close the container so we can get the table.
		bufIntensity.close();

		return new BufferedDataTable[] { bufMZ.getTable(), bufIntensity.getTable() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// TODO: generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		/*
		 * Nothing to do here as we require no in ports.
		 */
		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_file_chooser.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_file_chooser.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_file_chooser.validateSettings(settings);
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
