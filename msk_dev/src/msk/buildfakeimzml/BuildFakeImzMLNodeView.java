package msk.buildfakeimzml;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "BuildFakeImzML" Node.
 * yas
 *
 * @author Andrew P Talbot
 */
public class BuildFakeImzMLNodeView extends NodeView<BuildFakeImzMLNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link BuildFakeImzMLNodeModel})
     */
    protected BuildFakeImzMLNodeView(final BuildFakeImzMLNodeModel nodeModel) {
        super(nodeModel);

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

