package msk.node.viz.spectrum.compare;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SpectrumCmpView" Node.
 * 
 *
 * @author 
 */
public class SpectrumCmpViewNodeFactory 
        extends NodeFactory<SpectrumCmpViewNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SpectrumCmpViewNodeModel createNodeModel() {
        return new SpectrumCmpViewNodeModel();
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
    public NodeView<SpectrumCmpViewNodeModel> createNodeView(final int viewIndex,
            final SpectrumCmpViewNodeModel nodeModel) {
        return new SpectrumCmpViewNodeView(nodeModel);
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
        return new SpectrumCmpViewNodeDialog();
    }

}

