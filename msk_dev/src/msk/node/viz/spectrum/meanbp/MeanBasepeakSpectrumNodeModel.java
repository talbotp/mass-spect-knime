package msk.node.viz.spectrum.meanbp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import msk.util.DataManipulator;
import msk.util.SettingsModelCreate;
import msk.util.preprocess.TypeSpectrum;
import msk.util.viz.SpectrumImager;

/**
 * This is the model implementation of MeanBasepeakSpectrum. Node which creates
 * and returns and sketches a mean and a basepeak spectrum.
 *
 * @author Andrew P Talbot
 */
public class MeanBasepeakSpectrumNodeModel extends NodeModel {

	public static final String FILE_CHOOSER_ID = "file_chooser_id_meanBP";
	public static final String TYPE_ID = "Type_id_meanBP";
	public static final String YES_NO_ID = "Save_Yes_No_id_meanBP";
	public static final String WIDTH_ID = "Img_Width_id_meanBP";
	public static final String HEIGHT_ID = "Img_Height_id_meanBP";

	private SettingsModelString m_file_chooser = SettingsModelCreate
			.createSettingsModelString(MeanBasepeakSpectrumNodeModel.FILE_CHOOSER_ID, null);
	private SettingsModelString m_type = SettingsModelCreate
			.createSettingsModelString(MeanBasepeakSpectrumNodeModel.TYPE_ID, "Mean");
	private SettingsModelString m_yes_no_save = SettingsModelCreate
			.createSettingsModelString(MeanBasepeakSpectrumNodeModel.YES_NO_ID, "Yes");
	private SettingsModelIntegerBounded m_width_size = SettingsModelCreate
			.createSettingsModelIntegerBounded(MeanBasepeakSpectrumNodeModel.WIDTH_ID, 1100, 1, Integer.MAX_VALUE);
	private SettingsModelIntegerBounded m_height_size = SettingsModelCreate
			.createSettingsModelIntegerBounded(MeanBasepeakSpectrumNodeModel.HEIGHT_ID, 500, 1, Integer.MAX_VALUE);

	protected JPanel chart;

	/**
	 * Constructor for the node model.
	 */
	protected MeanBasepeakSpectrumNodeModel() {
		/**
		 * incoming is combined intensity and m.z data. outgoing is special
		 * spectra, i.e mean or basepeak.
		 */
		super(1, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		/*
		 * Outgoing data is same form as incoming, same number of columns also.
		 */
		return new DataTableSpec[] { inSpecs[0] };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		int specType = m_type.getStringValue().equals("Mean") ? TypeSpectrum.MEAN : TypeSpectrum.BASEPEAK;
		boolean toSave = m_yes_no_save.getStringValue().equals("Yes") ? true : false;

		int lengthSpec = inData[0].getDataTableSpec().getNumColumns();

		// Create either a mean spectrum or a Basepeak spectrum calculator.
		TypeSpectrum specCalc = TypeSpectrum.typeSpectrumFactory(specType, lengthSpec);

		long nRows = inData[0].size();
		long currentNProcessed = 0;
		
		// Update the spectra for every row in the table.
		for (DataRow row : inData[0]) {
			
			//Check the execution status
			exec.checkCanceled();
			
			// Update the spectrum.
			specCalc.update(DataManipulator.createRowArray(row));
			
			// Update the execution progress
			currentNProcessed++;
			exec.setProgress(currentNProcessed / nRows);
		}

		// Get mz and intensity
		double[] mz = DataManipulator.getColumnNameDoubles(inData[0]);
		double[] calculatedSpectra = specCalc.getSpecialSpectrum();

		// Make the chart
		String typeSpecStr = m_type.getStringValue().equals("Mean") ? TypeSpectrum.MEAN_STR : TypeSpectrum.BASEPEAK_STR;
		chart = SpectrumImager.getSpectrumPanelStatic(typeSpecStr, mz,
				new ArrayList<double[]>(Arrays.asList(calculatedSpectra)), m_width_size.getIntValue(),
				m_height_size.getIntValue(), toSave, m_file_chooser.getStringValue(), new String[] { typeSpecStr });

		// Make return table.
		DataTableSpec spec = inData[0].getDataTableSpec();
		BufferedDataContainer buf = exec.createDataContainer(spec);
		buf.addRowToTable(DataManipulator.createDataRow(calculatedSpectra, new RowKey(typeSpecStr)));
		buf.close();

		return new BufferedDataTable[] { buf.getTable() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		chart = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_file_chooser.saveSettingsTo(settings);
		m_type.saveSettingsTo(settings);
		m_yes_no_save.saveSettingsTo(settings);
		m_width_size.saveSettingsTo(settings);
		m_height_size.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_file_chooser.loadSettingsFrom(settings);
		m_type.loadSettingsFrom(settings);
		m_yes_no_save.loadSettingsFrom(settings);
		m_width_size.loadSettingsFrom(settings);
		m_height_size.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_file_chooser.validateSettings(settings);
		m_type.validateSettings(settings);
		m_yes_no_save.validateSettings(settings);
		m_width_size.validateSettings(settings);
		m_height_size.validateSettings(settings);
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
