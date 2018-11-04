package msk.node.read.imzML.loop.rowSpec;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import msk.util.GenericReaderDialog;
import msk.util.SettingsModelCreate;

/**
 * <code>NodeFactory</code> for the "ImzMLChunkReaderRowSpectrum" Node. This
 * node is used for reading in imzML datasets where the rows are the spectra. It
 * is useful for large datasets.
 *
 * @author Andrew P Talbot
 */
public class ImzMLChunkReaderRowSpectrumNodeFactory extends NodeFactory<ImzMLChunkReaderRowSpectrumNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ImzMLChunkReaderRowSpectrumNodeModel createNodeModel() {
		return new ImzMLChunkReaderRowSpectrumNodeModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNrNodeViews() {
		return 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeView<ImzMLChunkReaderRowSpectrumNodeModel> createNodeView(final int viewIndex,
			final ImzMLChunkReaderRowSpectrumNodeModel nodeModel) {
		return new ImzMLChunkReaderRowSpectrumNodeView(nodeModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasDialog() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeDialogPane createNodeDialogPane() {
		SettingsModelIntegerBounded smsIB = SettingsModelCreate.createSettingsModelIntegerBounded(
				ImzMLChunkReaderRowSpectrumNodeModel.N_SPECTRA_ID, 1, 1, Integer.MAX_VALUE);
		SettingsModelString sms = SettingsModelCreate
				.createSettingsModelString(ImzMLChunkReaderRowSpectrumNodeModel.FILE_CHOOSER_ID, null);
		return new GenericReaderDialog(sms, smsIB);
	}

}
