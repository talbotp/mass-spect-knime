package msk.node.read.imzML.loop.processed;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "ImzMLProcessedLoopStart" Node.
 * Node which is used to read in processed imzML datasets.
 *
 * @author Andrew P Talbot
 */
public class ImzMLProcessedLoopStartNodeView extends NodeView<ImzMLProcessedLoopStartNodeModel> {

	private ImzMLProcessedLoopStartNodeModel nodeModel;
	
    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link ImzMLProcessedLoopStartNodeModel})
     */
    protected ImzMLProcessedLoopStartNodeView(final ImzMLProcessedLoopStartNodeModel nodeModel) {
        super(nodeModel);
        update();
    }
    
	public void update() {
		setComponent(nodeModel.getView());
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
    	setComponent(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
       update();
    }

}

