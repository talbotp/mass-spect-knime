package msk.node.pre.spec.rebin;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SpectrumRebinner" Node.
 * This node is used for rebinning mass spectrometry datasets.
 *
 * @author Andrew P Talbot
 */
public class RebinnerNodeFactory 
        extends NodeFactory<RebinnerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public RebinnerNodeModel createNodeModel() {
        return new RebinnerNodeModel();
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
    public NodeView<RebinnerNodeModel> createNodeView(final int viewIndex,
            final RebinnerNodeModel nodeModel) {
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
        return new RebinnerNodeDialog();
    }

}

