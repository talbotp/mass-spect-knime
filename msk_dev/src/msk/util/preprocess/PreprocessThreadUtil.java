package msk.util.preprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.knime.core.data.DataRow;
import org.knime.core.data.RowKey;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;

import msk.util.DataManipulator;
import msk.util.KeyLogic;

/**
 * Specific Static methods required for us to use our PreprocessingThreadPool in
 * KNIME.
 * 
 * @author Andrew P Talbot
 */
public class PreprocessThreadUtil {

	/**
	 * Add every row in the DataTable to ArrayList
	 * 
	 * @param table
	 *            is the table we add rows to
	 * @return the List of spectra.
	 */
	public static List<MSKSpectrum> addAllRowsToMSKSpectrumList(BufferedDataTable table) {
		List<MSKSpectrum> list = new ArrayList<>();

		// Add each row to the List.
		for (DataRow row : table) {

			list.add(new MSKSpectrum(new KeyLogic(row.getKey()), DataManipulator.createRowArray(row)));

		}
		return list;
	}

	/**
	 * Map needs to be sorted, so we convert it to TreeMap. and then we add values.
	 * 
	 * We CANNOT use TreeMap for concurrency issues, so we have to sort it later.
	 * 
	 * @param buf is the container we add the row to.
	 * @param processedIntensity is the Map of spectra we are adding.
	 */
	public static void AddRowsToDataContainer(BufferedDataContainer buf, Map<KeyLogic, double[]> processed) {
		SortedMap<KeyLogic, double[]> sortedMap = new TreeMap<>(processed);
		
		sortedMap.forEach((k, v) -> {
			buf.addRowToTable(DataManipulator.createDataRow(v, new RowKey(k.toString())));
		});
	}

}
