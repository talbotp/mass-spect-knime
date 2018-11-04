package msk.node.post.cluster.som;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SOM" Node.
 * This node is useful for performing Self orgranizng map clustering in KNIIME.
 *
 * @author Andrew P Talbot
 */
public class SOMNodeFactory 
        extends NodeFactory<SOMNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SOMNodeModel createNodeModel() {
        return new SOMNodeModel();
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
    public NodeView<SOMNodeModel> createNodeView(final int viewIndex,
            final SOMNodeModel nodeModel) {
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
        return new SOMNodeDialog();
    }

}

