package msk.deprecated.node.read.imzML.loop.image;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import msk.util.GenericReaderDialog;
import msk.util.SettingsModelCreate;

/**
 * <code>NodeFactory</code> for the "IonImageLoopStart" Node.
 * This node is a loop start which can be used to read in an imzML dataset one image at a time.
 *
 * @author Andrew P Talbot
 */
public class IonImageLoopStartNodeFactory 
        extends NodeFactory<IonImageLoopStartNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public IonImageLoopStartNodeModel createNodeModel() {
        return new IonImageLoopStartNodeModel();
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
    public NodeView<IonImageLoopStartNodeModel> createNodeView(final int viewIndex,
            final IonImageLoopStartNodeModel nodeModel) {
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
    	SettingsModelString sms = SettingsModelCreate.createSettingsModelString(IonImageLoopStartNodeModel.FILE_CHOOSER_ID, null);
		return new GenericReaderDialog(sms, null);
    }

}

