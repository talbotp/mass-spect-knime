package msk.node.viz.spectrum.meanbp;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "MeanBasepeakSpectrum" Node.
 * Node which creates and returns and sketches a mean and a basepeak spectrum.
 *
 * @author Andrew P Talbot
 */
public class MeanBasepeakSpectrumNodeFactory 
        extends NodeFactory<MeanBasepeakSpectrumNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public MeanBasepeakSpectrumNodeModel createNodeModel() {
        return new MeanBasepeakSpectrumNodeModel();
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
    public NodeView<MeanBasepeakSpectrumNodeModel> createNodeView(final int viewIndex,
            final MeanBasepeakSpectrumNodeModel nodeModel) {
        return new MeanBasepeakSpectrumNodeView(nodeModel);
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
        return new MeanBasepeakSpectrumNodeDialog();
    }

}

