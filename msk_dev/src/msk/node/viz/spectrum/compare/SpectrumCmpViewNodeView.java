package msk.node.viz.spectrum.compare;

import javax.swing.JPanel;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "SpectrumCmpView" Node.
 * 
 *
 * @author 
 */
public class SpectrumCmpViewNodeView extends NodeView<SpectrumCmpViewNodeModel> {

	private JPanel panel;
	
    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link SpectrumCmpViewNodeModel})
     */
    protected SpectrumCmpViewNodeView(final SpectrumCmpViewNodeModel nodeModel) {
        super(nodeModel);
        
        if (nodeModel.specImager.getPanel() != null) {
        
        	panel = nodeModel.specImager.getPanel();
        
        	setComponent(panel);
        	
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {

        // update the view.
        SpectrumCmpViewNodeModel nodeModel = 
            (SpectrumCmpViewNodeModel)getNodeModel();
        assert nodeModel != null;
        
        // be aware of a possibly not executed nodeModel! The data you retrieve
        // from your nodemodel could be null, emtpy, or invalid in any kind.
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    	panel = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
    }

}

