package msk.util;

import java.util.Iterator;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DoubleValue;
import org.knime.core.data.RowKey;
import org.knime.core.data.StringValue;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;

/**
 * This is a utility class for methods that are commonly used within the nodes.
 * 
 * @author Andrew P Talbot
 * @version 28/06/2018
 */
public class DataManipulator {

	/**
	 * This method creates and returns an empty table, as it is something we
	 * commonly do, it makes sense to wrap it in here.
	 * 
	 * @return an empty BufferedDataTable.
	 */
	public static BufferedDataTable createEmptyTable(final ExecutionContext exec) {
		DataTableSpec spec = new DataTableSpec();
		BufferedDataContainer buf = exec.createDataContainer(spec);
		buf.close();
		return buf.getTable();
	}

	/**
	 * Creates a DataRow from an array of type double.
	 * 
	 * @param arr
	 *            is the array to make a row for
	 * @param rowKey
	 *            is the rowkey for the new row.
	 * @return the DataRow made from the rowKey parameter and
	 */
	public static DataRow createDataRow(double[] arr, RowKey rowKey) {
		int length = arr.length;
		DataCell[] cells = new DataCell[length];
		for (int i = 0; i < length; i++) {
			cells[i] = new DoubleCell(arr[i]);
		}
		return new DefaultRow(rowKey, cells);
	}

	/**
	 * Creates an array of type double from a row
	 * 
	 * @param row
	 *            if the row to make an array for.
	 * @param nCols
	 *            is the length of the row, and thus the array.
	 * @return the array from the Row.
	 */
	public static double[] createRowArray(DataRow row, int nCols) {
		double[] arr = new double[nCols];
		Iterator<DataCell> cellIter = row.iterator();
		int counter = 0;
		while (cellIter.hasNext()) {
			arr[counter] = ((DoubleCell) cellIter.next()).getDoubleValue();
			counter++;
		}
		return arr;
	}

	/**
	 * Creates an array of type double from a row Same as method but doesn't
	 * take in the number of columns also.
	 * 
	 * @param row
	 *            if the row to make an array for.
	 * @return the array from the Row.
	 */
	public static double[] createRowArray(DataRow row) {
		int nCols = row.getNumCells();
		double[] arr = new double[nCols];
		Iterator<DataCell> cellIter = row.iterator();
		int counter = 0;
		while (cellIter.hasNext()) {
			arr[counter] = ((DoubleCell) cellIter.next()).getDoubleValue();
			counter++;
		}
		return arr;
	}

	/**
	 * Returns a double array from a specified column in a BufferedDataTable.
	 * 
	 * REQUIRE : colIndex -1 <= nColumns in the table.
	 * 
	 * @param table
	 *            is the table we create the array from its column
	 * @param col|Index
	 *            is the column of the table we are creating a table for.
	 * @param nRows
	 *            is the number of rows, and hence the length of the array
	 * @return the array.
	 */
	public static double[] createColumnArray(BufferedDataTable table, final int colIndex, int nRows) {
		double[] arr = new double[nRows];
		int counter = 0;
		for (DataRow row : table) {
			arr[counter] = ((DoubleValue) row.getCell(colIndex)).getDoubleValue();
			counter++;
		}
		return arr;
	}
	
	/**
	 * Copy of createColumn array for String column.
	 * 
	 * REQUIRE : colIndex -1 <= nColumns in the table.
	 * 
	 * @param table is the DataTable we get the String column of.
	 * @param colIndex is the index of column we want values of.
	 * @return the String array of the specified column.
	 */
	public static String[] createColumnStringArray(BufferedDataTable table, final int colIndex) {
		String[] arr = new String[(int) table.size()];
		int counter = 0;
		for (DataRow row : table) {
			arr[counter] = ((StringValue) row.getCell(colIndex)).getStringValue();
			counter++;
		}
		return arr;
	}
	
	/**
	 * This method adds a double array to a column in a table.
	 * 
	 * This method does not close the container, must be done later.
	 * 
	 * Can only add one column at a time.
	 * 
	 * @param buf
	 *            is the container that we add to.
	 * @param col
	 *            is the column that we are adding to the container.
	 */
	public static void addColumn(BufferedDataContainer buf, double[] col) {
		int length = col.length;
		for (int i = 0; i < length; i++) {
			DataCell[] cells = new DataCell[] { new DoubleCell(col[i]) };
			DataRow row = new DefaultRow(new RowKey("" + i), cells);
			buf.addRowToTable(row);
		}
	}
	
	/**
	 * This method adds a double array to a column in a table.
	 * 
	 * This method does not close the container, must be done later.
	 * 
	 * Can only add one column at a time.
	 * 
	 * @param buf
	 *            is the container that we add to.
	 * @param col
	 *            is the column that we are adding to the container.
	 */
	public static void addColumn(BufferedDataContainer buf, int[] col) {
		int length = col.length;
		for (int i = 0; i < length; i++) {
			DataCell[] cells = new DataCell[] { new IntCell(col[i]) };
			DataRow row = new DefaultRow(new RowKey("" + i), cells);
			buf.addRowToTable(row);
		}
	}

	/**
	 * Returns Double versions of a DataTables Column names.
	 * 
	 * @return double array of the column names of a table.
	 */
	public static double[] getColumnNameDoubles(BufferedDataTable table) {
		String[] names = table.getDataTableSpec().getColumnNames();
		double[] nums = new double[names.length];
		for (int i = 0; i < nums.length; i++) {
			nums[i] = Double.parseDouble(names[i]);
		}
		return nums;
	}

	/**
	 * Gets an array of the row keys, nb they must be double.
	 * 
	 * @param table
	 * @return
	 */
	public static double[] getRowKeyDoubleArray(BufferedDataTable table) {
		double[] keys = new double[(int) table.size()];
		int counter = 0;
		for (DataRow row : table) {
			keys[counter] = Double.parseDouble(row.getKey().getString());
			counter++;
		}
		return keys;
	}

	/**
	 * Returns whether the node is a row or column.
	 * 
	 * @param nRows
	 * @param nCols
	 * @return
	 */
	public static boolean isRowOrCol(int nRows, int nCols) {
		boolean isRow = false;
		if (nRows == 1 && nCols > 1)
			isRow = true;
		else if (nRows > 1 && nCols == 1)
			isRow = false;
		else
			throw new IllegalArgumentException("input is null or a 1x1 DataTable");
		return isRow;
	}

	/**
	 * Returns the final row key for a table.
	 * 
	 * @param table
	 * @return
	 */
	public static RowKey getFinalRowKey(BufferedDataTable table) {
		CloseableRowIterator rowIter = table.iterator();
		DataRow row = rowIter.next();
		while (rowIter.hasNext())
			row = rowIter.next();

		return row.getKey();
	}

	public static double[] stringArrayToDoubleArray(String[] strArr) {
		double[] arr = new double[strArr.length];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = Double.parseDouble(strArr[i]);
		}
		return arr;
	}

	public static double getMaxValueInRow(DataRow row) {
		Iterator<DataCell> cellIter = row.iterator();
		double max = 0;
		while (cellIter.hasNext()) {
			double tmp = ((DoubleCell) cellIter.next()).getDoubleValue();
			if (tmp > max)
				max = tmp;
		}
		return max;
	}
	
	/**
	 * Require no negative values in the table.
	 * 
	 * @param table
	 * @return
	 */
	public static double getMaxValueInDataTable(BufferedDataTable table) {
		double max = 0;
		for (DataRow row : table) {
			for (DataCell cell : row) {
				double tmp = ((DoubleCell) cell).getDoubleValue();
				if (tmp > max) 
					max = tmp;
			}
		}
		return max;
	}
	
	public static double getMaxValueInRows(BufferedDataTable table, int minIndex, int maxIndex) {
		int counter = 0;
		double max = 0;
		for (DataRow row : table) {
			if (counter < minIndex)
				continue;
			
			if (counter > maxIndex)
				break;
			
			for (DataCell cell : row) {
				double tmp = ((DoubleCell) cell).getDoubleValue();
				if (tmp > max) 
					max = tmp;
			}
		}
		return max;
	}
	
	public static double getMaxValueInColumns(BufferedDataTable table, int minCol, int maxCol) {
		double max = 0;
		for (int i = minCol; i <= maxCol; i++) {
			for (DataRow row : table) {
				double tmp = ((DoubleCell) row.getCell(i)).getDoubleValue();
				if (tmp > max)
					max = tmp;
			}
		}
		return max;
	}

}
