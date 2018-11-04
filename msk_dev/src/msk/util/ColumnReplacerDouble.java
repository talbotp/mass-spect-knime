package msk.util;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.container.AbstractCellFactory;
import org.knime.core.data.def.DoubleCell;

public class ColumnReplacerDouble extends AbstractCellFactory {
	
	private double[] newColVals;

	/**
	 * Construct the column replacer.
	 * 
	 * @param processConcurrently should it process concurrently?
	 * @param newColSpec is the DataColumnSpec of the new column
	 */
	public ColumnReplacerDouble(boolean processConcurrently, DataColumnSpec newColSpec, double[] newColVals) {
		super(processConcurrently, newColSpec);
		this.newColVals = newColVals;
	}

	@Override
	public DataCell[] getCells(DataRow row) {
		DoubleCell[] cells = new DoubleCell[newColVals.length];
		for (int i = 0; i < cells.length; i++) {
			cells[i] = new DoubleCell(this.newColVals[i]);
		}
		return cells;
	}

}
