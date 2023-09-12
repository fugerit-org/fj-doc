package org.fugerit.java.doc.lib.simpletable;

import java.util.List;

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
		return newHelper().newTable( colWidths );
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
		return newHelper().newTable( colWidths );
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
