package msk.buildfakeimzml;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import msk.util.DataManipulator;
import msk.util.KeyLogic;


/**
 * This is the model implementation of BuildFakeImzML.
 * yas
 *
 * @author Andrew P Talbot
 */
public class BuildFakeImzMLNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected BuildFakeImzMLNodeModel() {
        super(0, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

    	int square = 100;
    	String[] names = new String[square];
    	DataType[] types = new DataType[square];
    	
    	for (int i = 0; i < square; i++) {
    		names[i] = "" + i;
    		types[i] = DoubleCell.TYPE;
    	}
      	
    	DataTableSpec spec = new DataTableSpec(names, types);
    	BufferedDataContainer buf = exec.createDataContainer(spec);
    	
    	double[] cluster1 = new double[square];
    	double[] cluster2 = new double[square];
    	double[] cluster3 = new double[square];
    	
    	for (int i = 0; i < square; i++) {
    		cluster1[i] = 0;
    		cluster2[i] = 10;
    		cluster3[i] = 100;
    	}
    	
    	int size = (int) Math.sqrt(square);
    	
    	// Now add the synthetic spectra.
    	for (int i = 0; i < size; i++) {
    		
    		for (int j = 0; j < size; j++) {
    			
    			KeyLogic key = new KeyLogic(i+ 1, j + 1);
    			
    			if (i < 5) {
    				buf.addRowToTable(DataManipulator.createDataRow(cluster1, new RowKey(key.toString())));
    			} else if (j < 5) {
    				buf.addRowToTable(DataManipulator.createDataRow(cluster2, new RowKey(key.toString())));
    			} else // i >= 5
    				buf.addRowToTable(DataManipulator.createDataRow(cluster3, new RowKey(key.toString())));
    		}
    		
    	}
    	
    	buf.close();
        return new BufferedDataTable[]{ buf.getTable() };
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
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

}

