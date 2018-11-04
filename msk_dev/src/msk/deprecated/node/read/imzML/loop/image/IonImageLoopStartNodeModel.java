package msk.deprecated.node.read.imzML.loop.image;

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
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.workflow.LoopStartNodeTerminator;

import imzML.ImzML;
import msk.util.ImzMLUtil;
import msk.util.SettingsModelCreate;

/**
 * This is the model implementation of IonImageLoopStart. This node is a loop
 * start which can be used to read in an imzML dataset one image at a time.
 * 
 * This works ONLY for continuous datasets.
 *
 * @author Andrew P Talbot
 */
public class IonImageLoopStartNodeModel extends NodeModel implements LoopStartNodeTerminator {
	
	// Logger instance
	NodeLogger logger = NodeLogger.getLogger(IonImageLoopStartNodeModel.class);

	// Create the ID for the file chooser.
	protected static final String FILE_CHOOSER_ID = "file_chooser_id_imzML";

	// Here, we create the settingsModel for the choice in the dialopg.
	// Not caps lock for readability later on.
	private final SettingsModelString m_file_chooser = SettingsModelCreate.createSettingsModelString(FILE_CHOOSER_ID, null);

	// Here are the booleans regarding the passes of the data.
	private boolean isFirstPass = true;
	private boolean isTerminated = false;

	private ImzML imzML;

	private int nXPixels;
	private int nYPixels;

	private int currentIteration;
	private int nIterations;

	// DataTable fields
	private DataTableSpec spec;

	/**
	 * Constructor for the node model.
	 */
	protected IonImageLoopStartNodeModel() {
		/*
		 * First outPort is the node ion image for the next mz
		 */
		super(0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		// If first pass then we must initialize everything.
		if (isFirstPass) {
			String filename = m_file_chooser.getStringValue();
			imzML = ImzMLUtil.getImzML(filename);

			nXPixels = imzML.getWidth();
			nYPixels = imzML.getHeight();

			currentIteration = 0;
			nIterations = imzML.getSpectrum(1, 1).getmzArray().length;

			// Create the spec for the outgoing tables
			String[] names = new String[nXPixels];
			DataType[] colSpec = new DataType[nXPixels];
			DataType type = (new DoubleCell(5)).getType();

			// Set the names and types of the columns
			for (int i = 0; i < nXPixels; i++) {
				names[i] = "" + (i + 1);
				colSpec[i] = type;
			}
			spec = new DataTableSpec(names, colSpec);
			isTerminated = false;
			isFirstPass = false;
		}
		
		exec.checkCanceled();

		// One table to return
		BufferedDataContainer buf = exec.createDataContainer(spec);

		/*
		 * Now we make the image.
		 */
		for (int i = 1; i <= nYPixels; i++) {
			DataCell[] cells = new DataCell[nXPixels];
			double mz = 0;
			;
			for (int j = 1; j <= nXPixels; j++) {
				mz = imzML.getSpectrum(i, j).getmzArray()[currentIteration];
				double intensity = imzML.getSpectrum(i, j).getIntensityArray()[currentIteration];
				cells[j - 1] = new DoubleCell(intensity);
			}
			DataRow row = new DefaultRow(new RowKey("m/z" + ": " + mz + " :" + i), cells);
			logger.info("NOW WE ADD THE ROW " + mz + " WE ARE ON ITERATION " + currentIteration );
			buf.addRowToTable(row);
		}

		buf.close();

		// Do the loop termination pieces.
		if (currentIteration >= nIterations - 1) {
			logger.info("WE HAVE SET IT TO BE FALSE, IT SHOULD TERMINATE");
			isTerminated = true;
		}
		currentIteration++;

		return new BufferedDataTable[] { buf.getTable() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// TODO Code executed on reset.
		// Models build during execute are cleared here.
		// Also data handled in load/saveInternals will be erased here.
		isFirstPass = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {

		// TODO: check if user settings are available, fit to the incoming
		// table structure, and the incoming types are feasible for the node
		// to execute. If the node can execute in its current state return
		// the spec of its output data table(s) (if you can, otherwise an array
		// with null elements), or throw an exception with a useful user message

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

		// TODO load internal data.
		// Everything handed to output ports is loaded automatically (data
		// returned by the execute method, models loaded in loadModelContent,
		// and user settings set through loadSettingsFrom - is all taken care
		// of). Load here only the other internals that need to be restored
		// (e.g. data used by the views).

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {

		// TODO save internal models.
		// Everything written to output ports is saved automatically (data
		// returned by the execute method, models saved in the saveModelContent,
		// and user settings saved through saveSettingsTo - is all taken care
		// of). Save here only the other internals that need to be preserved
		// (e.g. data used by the views).

	}

	/**
	 * isTerminated is true only when every image runs has been gone through.
	 * This means this node should execute once for each mz.
	 */
	@Override
	public boolean terminateLoop() {
		return isTerminated;
	}

}
