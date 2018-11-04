package msk.node.pre.spec.roi;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "IonImageRegionOfInterest" Node.
 * 
 *
 * @author 
 */
public class ROINodeFactory 
        extends NodeFactory<ROINodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ROINodeModel createNodeModel() {
        return new ROINodeModel();
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
    public NodeView<ROINodeModel> createNodeView(final int viewIndex,
            final ROINodeModel nodeModel) {
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
        return new ROINodeDialog();
    }

}

