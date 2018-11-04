package msk.node.pre.spec.baseline;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Baseline" Node.
 * This node is used for baseline subtraction in the preprocessing workflow.
 *
 * @author Andrew P Talbot
 */
public class BaselineNodeFactory 
        extends NodeFactory<BaselineNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public BaselineNodeModel createNodeModel() {
        return new BaselineNodeModel();
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
    public NodeView<BaselineNodeModel> createNodeView(final int viewIndex,
            final BaselineNodeModel nodeModel) {
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
        return new BaselineNodeDialog();
    }

}

