package msk.node.viz.clusterViewer;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ClusterViewer" Node.
 * Node used to visualize clusters in KNIME.
 *
 * @author Andrew P Talbot
 */
public class ClusterViewerNodeFactory 
        extends NodeFactory<ClusterViewerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ClusterViewerNodeModel createNodeModel() {
        return new ClusterViewerNodeModel();
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
    public NodeView<ClusterViewerNodeModel> createNodeView(final int viewIndex,
            final ClusterViewerNodeModel nodeModel) {
        return new ClusterViewerNodeView(nodeModel);
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
        return new ClusterViewerNodeDialog();
    }

}

