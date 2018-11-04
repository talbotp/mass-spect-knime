package msk.node.post.cluster.normCuts.image;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "NormalizedCutsImage" Node.
 * Perform Normalized Cuts clustering on an image.
 *
 * @author NormalizedCutsImage
 */
public class NormalizedCutsImageNodeView extends NodeView<NormalizedCutsImageNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link NormalizedCutsImageNodeModel})
     */
    protected NormalizedCutsImageNodeView(final NormalizedCutsImageNodeModel nodeModel) {
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

