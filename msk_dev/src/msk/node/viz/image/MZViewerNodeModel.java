package msk.node.viz.image;

import java.io.File;
import java.io.IOException;

import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.img.Img;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import msk.util.DataManipulator;
import msk.util.KeyLogic;
import msk.util.SettingsModelCreate;
import msk.util.viz.MZImageFactory;

/**
 * This is the model implementation of MZViewer. Node is used for viewing images
 * in grayscale for m/z channels.
 *
 * @author Andrew P Talbot
 */
public class MZViewerNodeModel extends NodeModel {
	
	private NodeLogger logger = NodeLogger.getLogger(MZViewerNodeModel.class);

	protected static final String BUTTON_GROUP_ID = "single_range_id";
	protected static final String MZ_ID = "m/z_id";
	protected static final String MZ_MIN_ID = "m/z minimum";
	protected static final String MZ_MAX_ID = "m/z maximum";
	protected static final String MAX_TYPE_ID = "max_type_id";
	protected static final String IMG_WIDTH_ID = "img_width_id";
	protected static final String IMG_HEIGHT_ID = "img_height_id";

	private SettingsModelString m_single_range = SettingsModelCreate.createSettingsModelString(BUTTON_GROUP_ID,
			"Single");
	private SettingsModelDoubleBounded m_mz = SettingsModelCreate.createSettingsModelDoubleBounded(MZ_ID, 0, 0,
			Integer.MAX_VALUE);
	private SettingsModelDoubleBounded m_mz_min = SettingsModelCreate.createSettingsModelDoubleBounded(MZ_MIN_ID, 0, 0,
			Integer.MAX_VALUE);
	private SettingsModelDoubleBounded m_mz_max = SettingsModelCreate.createSettingsModelDoubleBounded(MZ_MAX_ID, 0, 0,
			Integer.MAX_VALUE);
	private SettingsModelString m_max_type = SettingsModelCreate.createSettingsModelString(MAX_TYPE_ID, "Local");
	private SettingsModelIntegerBounded m_width = SettingsModelCreate.createSettingsModelIntegerBounded(IMG_WIDTH_ID, 1,
			1, 3000);
	private SettingsModelIntegerBounded m_height = SettingsModelCreate.createSettingsModelIntegerBounded(IMG_HEIGHT_ID,
			1, 1, 3000);

	private boolean isSingleMZ;
	private boolean isLocalMax;
	private boolean isRow = true; // DELETE THIS FIELD. 
	private double mz;
	private double minMZ;
	private double maxMZ;

	private double[] mzArr;
	private int[] indexMZToDraw;
	double[] intensitiesForImage;
	double maxIntensity;

	private int width;
	private int height;
	
	private Img<UnsignedByteType> img;

	/**
	 * Constructor for the node model.
	 */
	protected MZViewerNodeModel() {
		super(1, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		setFields(inData[0]);

		setMZArrValidMZValues(inData[0]);

		setIndexMZToDraw();

		// Handle the single m/z and m/z channel views seperately.
		if (isSingleMZ) {

			if (isRow) {
//				logger.fatal("isSingle and isRow has popped, we set the image intensities");
//				logger.fatal("indexMZToDraw[0] = " + indexMZToDraw[0]);
				intensitiesForImage = DataManipulator.createColumnArray(inData[0], indexMZToDraw[0],
						(int) inData[0].size());
				
			
				// DONE HERE ITHINK.
			} else {
				int index = indexMZToDraw[0];
				CloseableRowIterator rowIter = inData[0].iterator();
				DataRow row = rowIter.next();
				while (index > 0) {
					row = rowIter.next();
					index--;
				}

				rowIter.close();
				intensitiesForImage = DataManipulator.createRowArray(row);
			}

		} else {
			/*
			 * Now for the case where we consider multiple m/z channels.
			 */
			if (isRow) {
				intensitiesForImage = new double[(int) inData[0].size()];

				// Sum the values forthe intensities.
				for (int i = 0; i < indexMZToDraw.length; i++) {
					double[] tmp = DataManipulator.createColumnArray(inData[0], indexMZToDraw[0],
							(int) inData[0].size());
					for (int j = 0; j < intensitiesForImage.length; j++) {
						intensitiesForImage[j] += tmp[j];
					}
				}

				// Now we normalize the mz to draw.
				int indexMZToDrawLength = indexMZToDraw.length;
				for (int i = 0; i < intensitiesForImage.length; i++) {
					intensitiesForImage[i] /= indexMZToDrawLength;
				}

			} else {

				intensitiesForImage = new double[inData[0].getDataTableSpec().getNumColumns()];
				for (int i = 0; i < indexMZToDraw.length; i++) {
					int index = indexMZToDraw[i];
					CloseableRowIterator rowIter = inData[0].iterator();
					DataRow row = rowIter.next();
					while (index > 0) {
						row = rowIter.next();
						index--;
					}
					rowIter.close();
					double[] tmp = DataManipulator.createRowArray(row);
					for (int j = 0; j < intensitiesForImage.length; j++) {
						intensitiesForImage[i] += tmp[i];
					}
				}

				// Now we noemalize.
				int normalizer = indexMZToDraw.length;
				for (int i = 0; i < intensitiesForImage.length; i++) {
					intensitiesForImage[i] /= normalizer;
				}
			}
		}

		/**
		 * Now we have the intensites to image data. We now find the max value.
		 */
		if (isLocalMax) {
			logger.fatal("We are about to set max intensity value");
			for (int i = 0; i < intensitiesForImage.length; i++) {
				logger.fatal("intensitiesForImage[" + i + "] = " + intensitiesForImage[i]);
			}
			
			logger.fatal("The isLocalMax has popped.");
			maxIntensity = 0; 
			for (int i = 0; i < intensitiesForImage.length; i++) {
				if (intensitiesForImage[i] > maxIntensity) {
					maxIntensity = intensitiesForImage[i];
				}
			}
			
			logger.fatal("We just set max intensity value");
			for (int i = 0; i < intensitiesForImage.length; i++) {
				logger.fatal("intensitiesForImage[" + i + "] = " + intensitiesForImage[i]);
			}

		} else {
			maxIntensity = DataManipulator.getMaxValueInDataTable(inData[0]);
		}
		
		
		int nXPixels;
		int nYPixels;
		
		/*
		 * Now we get the number of 
		 */
		if (isRow) {
			KeyLogic key = new KeyLogic(DataManipulator.getFinalRowKey(inData[0]));
			nXPixels = key.getXPixel();
			nYPixels = key.getYPixel();
		} else {
			int tmp = inData[0].getDataTableSpec().getNumColumns();
			KeyLogic key = new KeyLogic(inData[0].getDataTableSpec().getColumnNames()[tmp]);
			nXPixels = key.getXPixel();
			nYPixels = key.getYPixel();
		}
		
		logger.info("We now have just done the key values and what not.");
		for (int i = 0; i < intensitiesForImage.length; i++) {
			logger.info("intensitiesForImage[" + i + "] = " + intensitiesForImage[i]);
		}	
		logger.info("maxIntensity = " + maxIntensity);
		
		MZImageFactory imgFac = new MZImageFactory(intensitiesForImage, maxIntensity, nXPixels, nYPixels, width, height);

		this.img = imgFac.getImage();
		
		return new BufferedDataTable[] {};
	}

	protected void setFields(BufferedDataTable table) {
		/*
		 * Set fields
		 */
		if (m_single_range.getStringValue().equals("Single"))
			isSingleMZ = true;
		else
			isSingleMZ = false;

		if (m_max_type.getStringValue().equals("Local"))
			isLocalMax = true;
		else
			isLocalMax = false;

		mz = m_mz.getDoubleValue();
		minMZ = m_mz_min.getDoubleValue();
		maxMZ = m_mz_max.getDoubleValue();
		width = m_width.getIntValue();
		height = m_height.getIntValue();
		
		mzArr = DataManipulator.getColumnNameDoubles(table);
	}

	public void setMZArrValidMZValues(BufferedDataTable in) {

		double actualMZMin = mzArr[0];
		double actualMZMax = mzArr[mzArr.length - 1];

		/*
		 * Set valid values of mz and mzMin and mzMax.
		 */
		if (minMZ < actualMZMin)
			minMZ = actualMZMin;

		if (maxMZ > actualMZMax)
			maxMZ = actualMZMax;

		if (mz < actualMZMin)
			mz = actualMZMin;

		if (mz > actualMZMax)
			mz = actualMZMax;
	}

	public void setIndexMZToDraw() {
		if (isSingleMZ) {
			indexMZToDraw = new int[1];
			indexMZToDraw[0] = msk.util.MathMSK.binarySearchClosest(mzArr, mz);
		} else {
			int indexMinMZ = msk.util.MathMSK.binarySearchClosest(mzArr, minMZ);
			int indexMaxMZ = msk.util.MathMSK.binarySearchClosest(mzArr, maxMZ);

			indexMZToDraw = new int[indexMaxMZ - indexMinMZ + 1];
			for (int i = 0; i < indexMZToDraw.length; i++) {
				indexMZToDraw[i] = indexMinMZ + i;
			}
		}
	}
	
	/**
	 * Getter for the Image object.
	 * 
	 * @return Image of calculated m/z channel.
	 */
	public Img<UnsignedByteType> getImg() {
		if (this.img != null)
			return this.img;
		else
			return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_single_range.saveSettingsTo(settings);
		m_mz.saveSettingsTo(settings);
		m_mz_min.saveSettingsTo(settings);
		m_mz_max.saveSettingsTo(settings);
		m_max_type.saveSettingsTo(settings);
		m_width.saveSettingsTo(settings);
		m_height.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_single_range.loadSettingsFrom(settings);
		m_mz.loadSettingsFrom(settings);
		m_mz_min.loadSettingsFrom(settings);
		m_mz_max.loadSettingsFrom(settings);
		m_max_type.loadSettingsFrom(settings);
		m_width.loadSettingsFrom(settings);
		m_height.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_single_range.validateSettings(settings);
		m_mz.validateSettings(settings);
		m_mz_min.validateSettings(settings);
		m_mz_max.validateSettings(settings);
		m_max_type.validateSettings(settings);
		m_width.validateSettings(settings);
		m_height.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

}
