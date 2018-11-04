package msk.node.viz.clusterViewer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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

import msk.util.DataManipulator;
import msk.util.KeyLogic;
import msk.util.SettingsModelCreate;
import msk.util.cluster.MSKClusterValues;
import msk.util.viz.MZImageFactory;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;

/**
 * This is the model implementation of ClusterViewer. Node used to visualize
 * clusters in KNIME.
 *
 * @author Andrew P Talbot
 */
public class ClusterViewerNodeModel extends NodeModel {

	private final NodeLogger logger = NodeLogger.getLogger(ClusterViewerNodeModel.class);

	public static final String IMG_WIDTH_ID = "img_width_id";
	public static final String IMG_HEIGHT_ID = "img_height_id";

	public SettingsModelIntegerBounded m_width = SettingsModelCreate.createSettingsModelIntegerBounded(IMG_WIDTH_ID,
			500, 1, 3000);
	public SettingsModelIntegerBounded m_height = SettingsModelCreate.createSettingsModelIntegerBounded(IMG_HEIGHT_ID,
			500, 1, 3000);

	private Img<UnsignedByteType> img;

	/**
	 * Constructor for the node model.
	 */
	protected ClusterViewerNodeModel() {
		super(1, 0);
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

		// Get string array of Cluster values, (typically final column in
		// DataTable)
		String[] clusterColumn = DataManipulator.createColumnStringArray(inData[0],
				inData[0].getDataTableSpec().getNumColumns() - 1);

		/*
		 * FROM HERE ON, WE BASICALLY GET ALL THE PARAMETERS REQUIRED FOR THE
		 * MZImageFactory object to be created.
		 */

		// Parse the string array into its integer values.
		double[] clusters = MSKClusterValues.parseClusterArray(clusterColumn);

		// Get the max value
		int max = 0;
		for (int i = 0; i < clusters.length; ++i) {
			if (clusters[i] > max) {
				max = i;
			}
		}
		
		logger.info("MAX VALUE = " + max);

		// Get the number of xpixels and number of ypixels.
		KeyLogic key = new KeyLogic(DataManipulator.getFinalRowKey(inData[0]));
		
		// Now make the image of the cluster values.
		MZImageFactory imgFac = new MZImageFactory(clusters, max, key.getXPixel(), key.getYPixel(),
				m_width.getIntValue(), m_height.getIntValue());

		// Set the img.
		this.img = imgFac.getImage();
		logger.info("Image is created");

		return new BufferedDataTable[] {};
	}

	public Img<UnsignedByteType> getImg() {
		if (this.img == null)
			return null;
		return this.img;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// Set img = null
		this.img = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_width.saveSettingsTo(settings);
		m_height.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_width.loadSettingsFrom(settings);
		m_height.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
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
