package msk.node.read.imzML.loop.rowSpec;

import org.knime.core.node.NodeView;

import msk.node.viz.spectrum.single.SpectrumViewNodeModel;

/**
 * NodeView to view data about the imzML file we are reading in.
 * 
 * @author Andrew P Talbot
 */
public class ImzMLChunkReaderRowSpectrumNodeView extends NodeView<ImzMLChunkReaderRowSpectrumNodeModel> {
	
	private ImzMLChunkReaderRowSpectrumNodeModel nodeModel;
	
	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link SpectrumViewNodeModel})
	 */
	protected ImzMLChunkReaderRowSpectrumNodeView(final ImzMLChunkReaderRowSpectrumNodeModel nodeModel) {
		super(nodeModel);
		this.nodeModel = nodeModel;
		update();
	}
	
	public void update() {
		setComponent(nodeModel.getView());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {
		update();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onClose() {
		setComponent(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onOpen() {

	}

}
