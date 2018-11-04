package msk.node.pre.spec.smooth;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SavitzkyGolaySmoother" Node.
 * This node is to be used for smoothing a set of rows or a set of columns using Savitzky-Golay smoothing, using a filter width specified by the user in the dialog.
 *
 * @author Andrew P Talbot
 */
public class SmootherNodeFactory 
        extends NodeFactory<SmootherNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SmootherNodeModel createNodeModel() {
        return new SmootherNodeModel();
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
    public NodeView<SmootherNodeModel> createNodeView(final int viewIndex,
            final SmootherNodeModel nodeModel) {
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
        return new SmootherNodeDialog();
    }

}

