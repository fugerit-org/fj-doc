package org.fugerit.java.doc.lib.simpletable.model;

import java.util.ArrayList;
import java.util.List;

import org.fugerit.java.doc.lib.simpletable.SimpleTableFacade;

public class SimpleTable {

	public static final String ATT_NAME = "simpleTableModel";
	
	private String columns;
	
	private String sheetName;
	
	private String  colwidths;
	
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
		this.sheetName = "decodifica";
	}
	
	public void addRow( SimpleRow row ) {
		this.getRows().add( row );
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

	
}
