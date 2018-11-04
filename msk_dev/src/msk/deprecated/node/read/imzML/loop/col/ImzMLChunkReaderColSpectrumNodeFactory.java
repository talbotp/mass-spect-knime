package msk.deprecated.node.read.imzML.loop.col;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import msk.util.GenericReaderDialog;
import msk.util.SettingsModelCreate;

/**
 * <code>NodeFactory</code> for the "ImzMLChunkReaderColSpectrum" Node.
 * This node is used for reading in imzML datasets in chunks of spectra. We note that we store the spectrums in the columns of the output ports.
 *
 * @author Andrew P Talbot
 */
public class ImzMLChunkReaderColSpectrumNodeFactory 
        extends NodeFactory<ImzMLChunkReaderColSpectrumNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ImzMLChunkReaderColSpectrumNodeModel createNodeModel() {
        return new ImzMLChunkReaderColSpectrumNodeModel();
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
    public NodeView<ImzMLChunkReaderColSpectrumNodeModel> createNodeView(final int viewIndex,
            final ImzMLChunkReaderColSpectrumNodeModel nodeModel) {
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
    	SettingsModelString sms = SettingsModelCreate.createSettingsModelString(ImzMLChunkReaderColSpectrumNodeModel.FILE_CHOOSER_ID, null);
        return new GenericReaderDialog(sms, null);
    }

}

