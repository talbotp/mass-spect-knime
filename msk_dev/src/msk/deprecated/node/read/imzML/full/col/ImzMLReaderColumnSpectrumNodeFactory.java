package msk.deprecated.node.read.imzML.full.col;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import msk.util.GenericReaderDialog;
import msk.util.SettingsModelCreate;

/**
 * <code>NodeFactory</code> for the "ImzMLReader" Node. This node is used for
 * reading a whole imzML dataset into memory.
 *
 * @author Andrew P Talbot
 */
public class ImzMLReaderColumnSpectrumNodeFactory extends NodeFactory<ImzMLReaderColumnSpectrumNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ImzMLReaderColumnSpectrumNodeModel createNodeModel() {
		return new ImzMLReaderColumnSpectrumNodeModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNrNodeViews() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeView<ImzMLReaderColumnSpectrumNodeModel> createNodeView(final int viewIndex,
			final ImzMLReaderColumnSpectrumNodeModel nodeModel) {
		return null;
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
		SettingsModelString sms = SettingsModelCreate.createSettingsModelString(ImzMLReaderColumnSpectrumNodeModel.FILE_CHOOSER_ID,
				null);
		return new GenericReaderDialog(sms, null);
	}

}
