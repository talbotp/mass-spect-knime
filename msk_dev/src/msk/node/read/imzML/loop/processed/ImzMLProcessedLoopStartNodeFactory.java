package msk.node.read.imzML.loop.processed;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ImzMLProcessedLoopStart" Node.
 * Node which is used to read in processed imzML datasets.
 *
 * @author Andrew P Talbot
 */
public class ImzMLProcessedLoopStartNodeFactory 
        extends NodeFactory<ImzMLProcessedLoopStartNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ImzMLProcessedLoopStartNodeModel createNodeModel() {
        return new ImzMLProcessedLoopStartNodeModel();
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
    public NodeView<ImzMLProcessedLoopStartNodeModel> createNodeView(final int viewIndex,
            final ImzMLProcessedLoopStartNodeModel nodeModel) {
        return new ImzMLProcessedLoopStartNodeView(nodeModel);
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
        return new ImzMLProcessedLoopStartNodeDialog();
    }

}

