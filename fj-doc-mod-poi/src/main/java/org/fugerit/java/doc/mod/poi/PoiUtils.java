package org.fugerit.java.doc.mod.poi;

import java.awt.Color;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.xml.DocModelUtils;

public class PoiUtils {

	private PoiUtils() {}
	
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
	
	public static void xlsxFormatStyle( WorkbookHelper helper, CellStyle style, DocCell cell ) {
		if ( style instanceof XSSFCellStyle ) {
			XSSFCellStyle realStyle = (XSSFCellStyle) style;
			if ( cell != null && StringUtils.isNotEmpty( cell.getBackColor() ) ) {
				Color c = DocModelUtils.parseHtmlColor( cell.getBackColor() );
				realStyle.setFillForegroundColor( new XSSFColor( c , helper.getIndexedColorMap() ) );
				realStyle.setFillPattern( FillPatternType.SOLID_FOREGROUND );
			}
		}
	}
	
	public static void xlsxFontStyle( WorkbookHelper helper, Font font, CellStyle style, DocCell cell ) {
		if ( font instanceof XSSFFont ) {
			XSSFFont realFont = (XSSFFont) font;
			if ( StringUtils.isNotEmpty( cell.getForeColor() ) ) {
				Color c = DocModelUtils.parseHtmlColor( cell.getForeColor() );
				realFont.setColor( new XSSFColor( c, helper.getIndexedColorMap() ) );
			}
		}
	}
	
	public static short findClosestColorIndex( HSSFWorkbook workbook, String color ) {
		Color c = DocModelUtils.parseHtmlColor( color );
		HSSFPalette palette = workbook.getCustomPalette();
		HSSFColor current = palette.findSimilarColor( c.getRed() , c.getGreen(), c.getBlue() );
		return current.getIndex();
	}
	
	public static void xlsFormatStyle( WorkbookHelper helper, CellStyle style, DocCell cell ) {
		Workbook workbook = helper.getWorkbook();
		if ( style instanceof HSSFCellStyle && workbook instanceof HSSFWorkbook ) {
			HSSFCellStyle realStyle = (HSSFCellStyle) style;
			if ( cell != null && StringUtils.isNotEmpty( cell.getBackColor() ) ) {
				short index = findClosestColorIndex( (HSSFWorkbook) workbook , cell.getBackColor() );
				realStyle.setFillForegroundColor( index );
				realStyle.setFillPattern( FillPatternType.SOLID_FOREGROUND );
			}
		}
	}
	
	public static void xlsFontStyle( WorkbookHelper helper, Font font, CellStyle style, DocCell cell ) {
		Workbook workbook = helper.getWorkbook();
		if ( workbook instanceof HSSFWorkbook && StringUtils.isNotEmpty( cell.getForeColor() ) ) {
			font.setColor( findClosestColorIndex( (HSSFWorkbook)workbook , cell.getForeColor() ) );	
		}
	}
	
}
