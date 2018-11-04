package msk.node.viz.spectrum.single;

import javax.swing.JPanel;

import org.knime.core.node.NodeView;


/**
 * <code>NodeView</code> for the "SpectrumView" Node. This node is used to plot
 * and view a single spectrum.
 *
 * @author Andrew P Talbot
 */
public class SpectrumViewNodeView extends NodeView<SpectrumViewNodeModel> {

	private JPanel panel;

	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link SpectrumViewNodeModel})
	 */
	protected SpectrumViewNodeView(final SpectrumViewNodeModel nodeModel) {
		super(nodeModel);
		
		if (nodeModel.specImager.getPanel() != null) {
			panel = nodeModel.specImager.getPanel();
			setComponent(panel);
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onClose() {
		panel = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onOpen() {
	}

}
