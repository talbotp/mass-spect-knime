package msk.node.read.imzML.full.rowSpec;

import org.knime.core.node.NodeView;

/**
 * NodeView to view data about the imzML file we are reading in.
 * 
 * @author Andrew P Talbot
 */
public class ImzMLReaderRowSpectrumNodeView extends NodeView<ImzMLReaderRowSpectrumNodeModel> {
	
	private ImzMLReaderRowSpectrumNodeModel nodeModel;
	
	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link SpectrumViewNodeModel})
	 */
	protected ImzMLReaderRowSpectrumNodeView(final ImzMLReaderRowSpectrumNodeModel nodeModel) {
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
		update();
	}

}
