package msk.node.pre.spec.normalize;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SpectrumNormalization" Node.
 * This node is used to normalize spectra in the body of a loop
 *
 * @author Andrew P Talbot
 */
public class NormalizationNodeFactory 
        extends NodeFactory<NormalizationNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public NormalizationNodeModel createNodeModel() {
        return new NormalizationNodeModel();
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
    public NodeView<NormalizationNodeModel> createNodeView(final int viewIndex,
            final NormalizationNodeModel nodeModel) {
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
        return new NormalizationNodeDialog();
    }

}

