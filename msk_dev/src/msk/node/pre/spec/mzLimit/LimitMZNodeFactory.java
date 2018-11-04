package msk.node.pre.spec.mzLimit;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "LimitMColumnMZ" Node.
 * This node is used for limitting the m/z values, it takes in imzML datasets and then outputs the same tables but strictly within the range of the m/z specified by the user.
 *
 * @author Andrew P Talbot
 */
public class LimitMZNodeFactory 
        extends NodeFactory<LimitMZNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public LimitMZNodeModel createNodeModel() {
        return new LimitMZNodeModel();
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
    public NodeView<LimitMZNodeModel> createNodeView(final int viewIndex,
            final LimitMZNodeModel nodeModel) {
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
        return new LimitMZNodeDialog();
    }

}

