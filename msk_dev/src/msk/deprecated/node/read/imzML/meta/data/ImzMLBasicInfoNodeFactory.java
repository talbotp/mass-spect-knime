package msk.deprecated.node.read.imzML.meta.data;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import msk.util.GenericReaderDialog;
import msk.util.SettingsModelCreate;

/**
 * <code>NodeFactory</code> for the "ImzMLBasicInfo" Node. This node is used for
 * loading basic information about an imzML dataset into a KNIME table.
 *
 * @author Andrew P Talbot
 */
public class ImzMLBasicInfoNodeFactory extends NodeFactory<ImzMLBasicInfoNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ImzMLBasicInfoNodeModel createNodeModel() {
		return new ImzMLBasicInfoNodeModel();
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
	public NodeView<ImzMLBasicInfoNodeModel> createNodeView(final int viewIndex,
			final ImzMLBasicInfoNodeModel nodeModel) {
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
		SettingsModelIntegerBounded smsIB = SettingsModelCreate.createSettingsModelIntegerBounded(
				"", 1, 1, Integer.MAX_VALUE);
		SettingsModelString sms = SettingsModelCreate.createSettingsModelString(ImzMLBasicInfoNodeModel.FILE_CHOOSER_ID, null);
		return new GenericReaderDialog(sms, smsIB);
	}

}
