package msk.node.viz.spectrum.single;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SpectrumView" Node.
 * This node is used to plot and view a single spectrum.
 *
 * @author Andrew P Talbot
 */
public class SpectrumViewNodeFactory 
        extends NodeFactory<SpectrumViewNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SpectrumViewNodeModel createNodeModel() {
        return new SpectrumViewNodeModel();
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
    public NodeView<SpectrumViewNodeModel> createNodeView(final int viewIndex,
            final SpectrumViewNodeModel nodeModel) {
        return new SpectrumViewNodeView(nodeModel);
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
        return new SpectrumViewNodeDialog();
    }

}

