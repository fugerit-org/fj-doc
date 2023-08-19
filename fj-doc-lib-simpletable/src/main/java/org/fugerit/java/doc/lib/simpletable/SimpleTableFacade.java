package org.fugerit.java.doc.lib.simpletable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;

public class SimpleTableFacade {

	private SimpleTableFacade() {}
	
	/**
	 * Creates a new {@link SimpleTable}
	 * 
	 * A minimum of one column must be provided.
	 * Sum of column widths must be 100
	 * 
	 * @param   colWidths	columns width percentage (minimum column width must be 1, sum must be 100)
	 * @return	the new table model initialized
	 */
	public static SimpleTable newTable( Integer... colWidths ) {
		if ( colWidths == null ) {
			throw new ConfigRuntimeException( "Minimum one colunm must be provided" );
		}
		return newTable( Arrays.asList( colWidths ) );
	}

	/**
	 * Creates a new {@link SimpleTable}
	 * 
	 * A minimum of one column must be provided.
	 * Sum of column widths must be 100
	 * 
	 * @param	colWidths	columns width percentage (minimum column width must be 1, sum must be 100)
	 * @return	the new table model initialized
	 */
	public static SimpleTable newTable( List<Integer> colWidths ) {
		if ( colWidths == null ) {
			throw new ConfigRuntimeException( "Minimum one colunm must be provided" );
		} else {
			int sum = 0;
			for ( int v : colWidths ) {
				sum+=v;
			}
			if ( sum != 100 ) {
				throw new ConfigRuntimeException( "Column width sum must be 100, while is : "+sum );
			}
		}
		return new SimpleTable( StringUtils.concat( ";" , colWidths.stream().map( v -> v.toString() ).collect( Collectors.toList() ) ) );
	}
	
	/**
	 * Creates a new {@link SimpleTable} , made of a single column with width 100.
	 * 
	 * @return	the new table model initialized
	 */
	public static SimpleTable newTableSingleColumn() {
		return newTable( 100 );
	}

	public static SimpleTableHelper newHelper() {
		return new SimpleTableHelper();
	}
	
}
