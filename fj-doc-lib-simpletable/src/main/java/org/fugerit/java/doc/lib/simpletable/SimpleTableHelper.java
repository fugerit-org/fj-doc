package org.fugerit.java.doc.lib.simpletable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.lib.simpletable.model.SimpleCell;
import org.fugerit.java.doc.lib.simpletable.model.SimpleRow;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;

public class SimpleTableHelper {

	private Integer defaultBorderWidth;
	
	public SimpleTableHelper withDefaultBorderWidth( int defaultBorderWidth ) {
		this.defaultBorderWidth = defaultBorderWidth;
		return this;
	}

	public Integer getDefaultBorderWidth() {
		return defaultBorderWidth;
	}

	/**
	 * Creates a new {@link SimpleTable}
	 * 
	 * A minimum of one column must be provided.
	 * Sum of column widths must be 100
	 * 
	 * @param   colWidths	columns width percentage (minimum column width must be 1, sum must be 100)
	 * @return	the new table model initialized
	 */
	public SimpleTable newTable( Integer... colWidths ) {
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
	public SimpleTable newTable( List<Integer> colWidths ) {
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
	public SimpleTable newTableSingleColumn() {
		return newTable( 100 );
	}
	
	public SimpleCell newCell( String content ) {
		SimpleCell simpleCell = new SimpleCell( content );
		if ( this.getDefaultBorderWidth() != null ) {
			simpleCell.setBorderWidth( this.getDefaultBorderWidth() );	
		}
		return simpleCell;
	}
	
	public SimpleRow newHeaderWorker( String isHeader, String... cells ) {
		SimpleRow row = new SimpleRow( isHeader );
		if ( cells != null ) {
			for ( int k=0; k<cells.length; k++ ) {
				row.addCell( this.newCell( cells[k] ) );
			}
		}
		return row;
	}
	
	public SimpleRow newHeaderRow( String... cells ) {
		return this.newHeaderWorker( BooleanUtils.BOOLEAN_TRUE , cells );
	}
	
	public SimpleRow newNormalRow( String... cells ) {
		return this.newHeaderWorker( BooleanUtils.BOOLEAN_FALSE , cells );
	}
	
}
