package msk.buildfakeimzml;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "BuildFakeImzML" Node.
 * yas
 *
 * @author Andrew P Talbot
 */
public class BuildFakeImzMLNodeFactory 
        extends NodeFactory<BuildFakeImzMLNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public BuildFakeImzMLNodeModel createNodeModel() {
        return new BuildFakeImzMLNodeModel();
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
    public NodeView<BuildFakeImzMLNodeModel> createNodeView(final int viewIndex,
            final BuildFakeImzMLNodeModel nodeModel) {
        return new BuildFakeImzMLNodeView(nodeModel);
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
        return new BuildFakeImzMLNodeDialog();
    }

}

