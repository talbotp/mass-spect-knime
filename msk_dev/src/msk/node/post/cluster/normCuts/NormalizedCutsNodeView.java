package msk.node.post.cluster.normCuts;

import org.knime.core.node.NodeView;

import msk.node.post.cluster.normCuts.image.NormalizedCutsImageNodeModel;

/**
 * <code>NodeView</code> for the "NormalizedCutsImage" Node.
 * Perform Normalized Cuts clustering on an image.
 *
 * @author NormalizedCutsImage
 */
public class NormalizedCutsNodeView extends NodeView<NormalizedCutsNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link NormalizedCutsImageNodeModel})
     */
    protected NormalizedCutsNodeView(final NormalizedCutsNodeModel nodeModel) {
        super(nodeModel);
        setComponent(nodeModel.getClusterImage());
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

