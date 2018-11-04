package msk.node.util.combiner;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "IntensityMZColumnCombine" Node.
 * This node is used to combine m/z and intensity values into one table. This is only to be used if the data is continuous.
 *
 * @author Andrew P Talbot
 */
public class CombinerNodeFactory 
        extends NodeFactory<CombinerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CombinerNodeModel createNodeModel() {
        return new CombinerNodeModel();
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
    public NodeView<CombinerNodeModel> createNodeView(final int viewIndex,
            final CombinerNodeModel nodeModel) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return null;
    }

}

