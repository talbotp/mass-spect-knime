package msk.node.read.imzML.full.rowSpec;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import msk.util.GenericReaderDialog;
import msk.util.SettingsModelCreate;

/**
 * <code>NodeFactory</code> for the "ImzMLReaderColumnMZ" Node. This is a reader
 * for imzML datasets which reads in the whole dataset where columns are the mz,
 * and the rows are the pixels.
 *
 * @author Andrew P Talbot
 */
public class ImzMLReaderRowSpectrumNodeFactory extends NodeFactory<ImzMLReaderRowSpectrumNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ImzMLReaderRowSpectrumNodeModel createNodeModel() {
		return new ImzMLReaderRowSpectrumNodeModel();
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
	public NodeView<ImzMLReaderRowSpectrumNodeModel> createNodeView(final int viewIndex,
			final ImzMLReaderRowSpectrumNodeModel nodeModel) {
		return new ImzMLReaderRowSpectrumNodeView(nodeModel);
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
		SettingsModelString sms = SettingsModelCreate.createSettingsModelString(ImzMLReaderRowSpectrumNodeModel.FILE_CHOOSER_ID,
				null);
		return new GenericReaderDialog(sms, null);
	}

}
