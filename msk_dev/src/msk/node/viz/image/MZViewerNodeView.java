package msk.node.viz.image;

import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

import org.knime.core.node.NodeView;

import ij.ImagePlus;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedByteType;

/**
 * <code>NodeView</code> for the "MZViewer" Node.
 * Node is used for viewing images in grayscale for m/z channels.
 *
 * @author Andrew P Talbot
 */
public class MZViewerNodeView extends NodeView<MZViewerNodeModel> {
	
	private MZViewerNodeModel nodeModel;
	
    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link MZViewerNodeModel})
     */
    protected MZViewerNodeView(final MZViewerNodeModel nodeModel) {
        super(nodeModel);
        this.nodeModel = nodeModel;
        convertToBIAdd();
    }
    
    /**
     * Convert ImageJ Img to BufferedImage and then add it to the View.
     */
    private void convertToBIAdd() {
    	Img<UnsignedByteType> img = nodeModel.getImg();
    	ImagePlus imgPlus = ImageJFunctions.wrap(img, "");
    	BufferedImage bufferedImg = imgPlus.getBufferedImage();
    	this.setComponent(new JLabel(new ImageIcon(bufferedImg)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {
       // do nothing
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
    	// do nothing
    }

}

