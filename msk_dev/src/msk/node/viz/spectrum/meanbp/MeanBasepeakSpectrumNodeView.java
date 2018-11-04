package msk.node.viz.spectrum.meanbp;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "MeanBasepeakSpectrum" Node.
 * Node which creates and returns and sketches a mean and a basepeak spectrum.
 *
 * @author Andrew P Talbot
 */
public class MeanBasepeakSpectrumNodeView extends NodeView<MeanBasepeakSpectrumNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link MeanBasepeakSpectrumNodeModel})
     */
    protected MeanBasepeakSpectrumNodeView(final MeanBasepeakSpectrumNodeModel nodeModel) {
        super(nodeModel);
        if (nodeModel.chart != null)
        	setComponent(nodeModel.chart);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
    }

}

