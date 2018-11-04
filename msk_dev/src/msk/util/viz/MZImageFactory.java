package msk.util.viz;

import java.util.ArrayList;

import org.knime.core.node.NodeLogger;

import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;

/**
 * Creates a MZ Image using ImgLib2.
 * 
 * Wraps the image into an ImageJ Application currently, but will instead later
 * return a JPanel to be used in KNIME.
 * 
 * Currently makes a grayscale image. TODO : Make Generic for any type of
 * Coloring.
 * 
 * Uses grayscale to do this.
 * 
 * @author Andrew P Talbot
 * @version 23/07/2018
 */
public class MZImageFactory {
	
	private NodeLogger logger = NodeLogger.getLogger(MZImageFactory.class);

	private double maxIntensity;
	private Img<UnsignedByteType> img;

	/**
	 * Create and set an Img<UnsignedByteType> of the given intensity values.
	 * 
	 * @param intensity
	 *            is the intensity array of values for the image we create. (1d
	 *            pixel intensity array)
	 * @param maxIntensity
	 *            is the max intensity in our image.
	 * @param nXPixels
	 *            is the width (number of pixels spanning the width of our
	 *            image).
	 * @param nYPixels
	 *            is the height (number of pixels spanning the height of our
	 *            image).
	 * @param imgWidth
	 *            is the image width of the displayed image we wish to view.
	 * @param imgHeight
	 *            is the image height of the displayed image we wish to view.
	 */
	public MZImageFactory(double[] intensity, double maxIntensity, int nXPixels, int nYPixels, int imgWidth,
			int imgHeight) {
		this.maxIntensity = maxIntensity;

		// This scales the width to the height of the image.
		// Remove to make it normal
		double factor = (double) nXPixels / nYPixels;
		imgWidth = (int) (factor * imgWidth);

		// Here we set an appropriate image width and height that is compatible
		// with the size of the image. Find their next multiple.
		while (isValidSize(imgWidth, nXPixels))
			imgWidth++;

		while (isValidSize(imgHeight, nYPixels))
			imgHeight++;

		final int[] dimensions = { imgWidth, imgHeight };

		final Img<UnsignedByteType> img = new ArrayImgFactory<UnsignedByteType>().create(dimensions,
				new UnsignedByteType());

		final Cursor<UnsignedByteType> cursor = img.cursor();

		// See how big each part is
		int xBlockSize = imgWidth / nXPixels;
		int yBlockSize = imgWidth / nYPixels;

		System.out.println(xBlockSize);
		System.out.println(yBlockSize);
		// System.exit(-1);

		int currentXPixel = 0;
		int currentYPixel = 0;

		int currentXBlock = 0;
		int currentYBlock = 0;

		ArrayList<Integer> bytes = new ArrayList<>();
		
		while (cursor.hasNext()) {
			cursor.fwd();

			cursor.get().set(getValidByteType(intensity[currentYBlock * nXPixels + currentXBlock]));
			bytes.add(getValidByteType(intensity[currentYBlock * nXPixels + currentXBlock]));

			// Iterate through the actual pixels.
			currentXPixel++;
			if (currentXPixel >= imgWidth) {
				currentXPixel = 0;
				currentYPixel++;
				if (currentYPixel >= imgHeight)
					currentYPixel = 0;
			}

			currentXBlock = Math.floorDiv(currentXPixel, xBlockSize);
			currentYBlock = Math.floorDiv(currentYPixel, yBlockSize);
			// System.out.println("currentXBlock is " + currentXBlock);
			// System.out.println("currentYBlock is " + currentYBlock);
		}

		//logger.info("Pixels are = " + bytes.toString());
		this.img = img;
	}

	public int getValidByteType(double intensityVal) {
		return (int) ((intensityVal / maxIntensity) * 255);
	}

	public boolean isValidSize(int imgWidth, int nPixels) {
		return ((double) imgWidth / nPixels) % 2 != 0;
	}

	public Img<UnsignedByteType> getImage() {
		return img;
	}

	/**
	 * When visualizing we scale the cluster values.
	 * 
	 * @param clusters
	 *            is the unscaled cluster value array
	 * @return the scaled cluster values to grayscale (0 - 255).
	 */
	public static double[] scaleClusters(double[] clusters) {
		double[] scaledClusters = new double[clusters.length];

		double max = 0;
		for (Double i : clusters)
			if (i > 0)
				max = i;

		// Now we have max we can scale.

		int index = 0;
		for (Double i : clusters)
			scaledClusters[index++] = ((int) (i / max)) * 255;

		return scaledClusters;
	}

}
