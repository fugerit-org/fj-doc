package org.fugerit.java.doc.lib.simpletable.model;

import java.util.ArrayList;
import java.util.List;

import org.fugerit.java.doc.lib.simpletable.SimpleTableFacade;

public class SimpleTable {

	public static final String ATT_NAME = "simpleTableModel";
	
	public static final String DEFAULT_SHEET_NAME = "Table";
	
	public static final int DEFAULT_BORDER_WIDTH = 0;
	
	private String columns;
	
	private String sheetName;
	
	private String  colwidths;

	private int defaultBorderWidth;
	
	private List<SimpleRow> rows;

	public List<SimpleRow> getRows() {
		return rows;
	}
	
	/**
	 * Creates a new SimpleTable.
	 * 
	 * NOTE: betteer using the factory method in {@link SimpleTableFacade}
	 * 
	 * @param colwidths	the semicolons separeted columns widh in percentage
	 */
	public SimpleTable( String colwidths ) {
		this.rows = new ArrayList<SimpleRow>();
		this.columns = String.valueOf( colwidths.split( ";" ).length );
		this.colwidths = colwidths;
		this.sheetName = DEFAULT_SHEET_NAME;
		this.defaultBorderWidth = DEFAULT_BORDER_WIDTH;
	}
	
	public void addRow( SimpleRow row ) {
		this.getRows().add( row );
		for ( SimpleCell cell : row.getCells() ) {
			if ( cell.getBorderWidth() == SimpleCell.BORDER_WIDTH_UNSET ) {
				cell.setBorderWidth( this.defaultBorderWidth );
			}
		}
	}

	public String getColumns() {
		return columns;
	}

	public String getColwidths() {
		return colwidths;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public int getDefaultBorderWidth() {
		return defaultBorderWidth;
	}

	public void setDefaultBorderWidth(int defaultBorderWidth) {
		this.defaultBorderWidth = defaultBorderWidth;
	}

}
