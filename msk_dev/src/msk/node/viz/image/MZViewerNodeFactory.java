package msk.node.viz.image;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "MZViewer" Node.
 * Node is used for viewing images in grayscale for m/z channels.
 *
 * @author Andrew P Talbot
 */
public class MZViewerNodeFactory 
        extends NodeFactory<MZViewerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public MZViewerNodeModel createNodeModel() {
        return new MZViewerNodeModel();
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
    public NodeView<MZViewerNodeModel> createNodeView(final int viewIndex,
            final MZViewerNodeModel nodeModel) {
        return new MZViewerNodeView(nodeModel);
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
        return new MZViewerNodeDialog();
    }

}

