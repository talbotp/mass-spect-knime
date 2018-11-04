package msk.node.viz.spectrum.jpeg;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SpectrumJPEG" Node.
 * Create JPEG files for each spectrum and then saves them in a specified location.
 *
 * @author Andrew P Talbot
 */
public class SpectrumJPEGNodeFactory 
        extends NodeFactory<SpectrumJPEGNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SpectrumJPEGNodeModel createNodeModel() {
        return new SpectrumJPEGNodeModel();
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
    public NodeView<SpectrumJPEGNodeModel> createNodeView(final int viewIndex,
            final SpectrumJPEGNodeModel nodeModel) {
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
        return new SpectrumJPEGNodeDialog();
    }

}

