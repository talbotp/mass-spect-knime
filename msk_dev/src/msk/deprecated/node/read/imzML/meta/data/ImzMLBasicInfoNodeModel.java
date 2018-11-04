package msk.deprecated.node.read.imzML.meta.data;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
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
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import imzML.ImzML;
import msk.util.ImzMLUtil;
import msk.util.SettingsModelCreate;

/**
 * This is the model implementation of ImzMLBasicInfo. This node is used for
 * loading basic information about an imzML data set into a KNIME table.
 *
 * @author Andrew P Talbot
 */
public class ImzMLBasicInfoNodeModel extends NodeModel {
	
	private NodeLogger logger = NodeLogger.getLogger(ImzMLBasicInfoNodeModel.class);

	// Create the ID for the file chooser.
	protected static final String FILE_CHOOSER_ID = "file_chooser_id_imzML";

	// Here, we create the settingsModel for the choice in the dialog.
	// Not caps lock for readability later on.
	private final SettingsModelString m_file_chooser = SettingsModelCreate.createSettingsModelString(FILE_CHOOSER_ID, null);

	/**
	 * Constructor for the node model.
	 */
	protected ImzMLBasicInfoNodeModel() {
		/*
		 * First output port is the table containing basic information about the
		 * imzML data set.
		 */
		super(0, 1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		/*
		 * No incoming tables so nothing to do here.
		 */
		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		String filename = m_file_chooser.getStringValue();
		logger.info("We make the ImzML Object for " + filename);
	
		ImzML imzML = ImzMLUtil.getImzML(filename, logger);
		
		// int nCols = 2;
		boolean isContinuous = imzML.isContinuous();

		// Type String for the column specification.
		// NB The underscored are to signify it is the title.
		DataType type = (new StringCell("")).getType();
		String[] names = new String[] { "__Name__", "__Value__" };
		DataType[] types = new DataType[] { type, type };

		DataTableSpec spec = new DataTableSpec(names, types);
		BufferedDataContainer buf = exec.createDataContainer(spec);

		int nRows = 10;

		// Make the String RowKeys
		String[] rowKeys = new String[nRows];
		for (int i = 0; i < nRows; i++) {
			rowKeys[i] = "" + i;
		}

		// Now we add rows to the Container.

		// Row : Filename
		DataCell[] currentRow = new DataCell[2];
		currentRow[0] = new StringCell("File Name : ");
		String filePWD = ImzMLUtil.getFileName(filename);
		int slashIndex = filePWD.lastIndexOf('/');
		currentRow[1] = new StringCell(filePWD.substring(slashIndex));
		DataRow row = new DefaultRow(rowKeys[0], currentRow);
		buf.addRowToTable(row);

		// Row : Version
		currentRow[0] = new StringCell("Version : ");
		currentRow[1] = new StringCell(imzML.getVersion());
		row = new DefaultRow(rowKeys[1], currentRow);
		buf.addRowToTable(row);

		// Row : Description
		currentRow[0] = new StringCell("Minimum value of m/z : ");
		currentRow[1] = new StringCell("" + imzML.getFileDescription().toString());
		row = new DefaultRow(rowKeys[2], currentRow);
		buf.addRowToTable(row);

		// Row : continuous / processed.
		currentRow[0] = new StringCell("Data Format : ");
		if (isContinuous)
			currentRow[1] = new StringCell("Continuous");
		else
			currentRow[1] = new StringCell("Processed");
		row = new DefaultRow(rowKeys[3], currentRow);
		buf.addRowToTable(row);

		// Row : number of x pixels
		currentRow[0] = new StringCell("Number of x Pixels : ");
		currentRow[1] = new StringCell("" + imzML.getWidth());
		row = new DefaultRow(rowKeys[4], currentRow);
		buf.addRowToTable(row);

		// Row : number of y pixels
		currentRow[0] = new StringCell("Number of y Pixels : ");
		currentRow[1] = new StringCell("" + imzML.getHeight());
		row = new DefaultRow(rowKeys[5], currentRow);
		buf.addRowToTable(row);

		// Row : number of m/z
		currentRow[0] = new StringCell("Number of m/z : ");
		if (isContinuous)
			currentRow[1] = new StringCell("" + imzML.getSpectrum(1, 1).getIntensityArray().length);
		else
			currentRow[1] = new StringCell("n/a : data is processed.");
		row = new DefaultRow(rowKeys[6], currentRow);
		buf.addRowToTable(row);

		// Row : minimum m/z
		currentRow[0] = new StringCell("Minimum value of m/z : ");
		if (isContinuous)
			currentRow[1] = new StringCell("" + imzML.getSpectrum(1, 1).getmzArray()[0]);
		else
			currentRow[1] = new StringCell("n/a : data is processed.");
		row = new DefaultRow(rowKeys[7], currentRow);
		buf.addRowToTable(row);

		// Row : maximum m/z
		currentRow[0] = new StringCell("Maximum value of m/z : ");
		if (isContinuous) {
			int l = imzML.getSpectrum(1, 1).getmzArray().length;
			currentRow[1] = new StringCell("" + imzML.getSpectrum(1, 1).getmzArray()[l - 1]);
		} else
			currentRow[1] = new StringCell("n/a : data is processed.");
		row = new DefaultRow(rowKeys[8], currentRow);
		buf.addRowToTable(row);

		// Row : minimum m/z
		currentRow[0] = new StringCell("Minimum value of m/z : ");
		if (isContinuous)
			currentRow[1] = new StringCell("" + imzML.getSpectrum(1, 1).getmzArray()[0]);
		else
			currentRow[1] = new StringCell("n/a : data is processed.");
		row = new DefaultRow(rowKeys[9], currentRow);
		buf.addRowToTable(row);

		buf.close();
		return new BufferedDataTable[] { buf.getTable() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// Do nothing.
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
		// Do Nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// Do nothing
	}

}
