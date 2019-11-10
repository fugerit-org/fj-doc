package org.fugerit.java.doc.mod.poi;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class PoiUtils {

    public static void resizeSheet( Sheet s ) {
        Row row = s.getRow( 0 );
        Iterator<Cell> cells = row.cellIterator();
        while ( cells.hasNext() ) {
                Cell c = cells.next();
                s.autoSizeColumn( c.getColumnIndex() );
        }
    }

	public static void autoSizeColumns(Workbook workbook) {
	    int numberOfSheets = workbook.getNumberOfSheets();
	    for (int i = 0; i < numberOfSheets; i++) {
	        Sheet sheet = workbook.getSheetAt(i);
	        resizeSheet( sheet );
	    }
	}
	
}
