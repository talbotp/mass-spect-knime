package msk.node.viz.clusterViewer;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.knime.core.node.NodeView;

import ij.ImagePlus;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedByteType;

/**
 * <code>NodeView</code> for the "ClusterViewer" Node.
 * Node used to visualize clusters in KNIME.
 *
 * @author Andrew P Talbot
 */
public class ClusterViewerNodeView extends NodeView<ClusterViewerNodeModel> {

	private ClusterViewerNodeModel nodeModel;
	private JComponent jcomponent;
	
    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link ClusterViewerNodeModel})
     */
    protected ClusterViewerNodeView(final ClusterViewerNodeModel nodeModel) {
        super(nodeModel);
        this.nodeModel = nodeModel;
        convertToBIAdd();
        setComponent(jcomponent);
    }
    
    /**
     * Convert ImageJ Img to BufferedImage and then add it to the View.
     */
    private void convertToBIAdd() {
    	Img<UnsignedByteType> img = nodeModel.getImg();
    	ImagePlus imgPlus = ImageJFunctions.wrap(img, "");
    	BufferedImage bufferedImg = imgPlus.getBufferedImage();
    	this.jcomponent = (new JLabel(new ImageIcon(bufferedImg)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {
    	jcomponent = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    	jcomponent = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
    	convertToBIAdd();
    	setComponent(jcomponent);
    }

}

