package test.org.fugerit.java.doc.sample.format;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

public class TestPOI {

	private void close( Workbook workbook, String test ) throws Exception {
		try ( FileOutputStream fos = new FileOutputStream( new File( BasicFacadeTest.BASIC_OUTPUT_PATH, test+".xlsx" ) ) ) {
			workbook.write( fos );
			workbook.close();
		}
	}
	
	@Test
	public void testCellForegroundColor() throws Exception  {
		String test = "cell_foreground_color";
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet( test );
		Row row = sheet.createRow( 0 );
		Cell cell = row.createCell( 0 );
		cell.setCellValue( test );
		XSSFCellStyle style = (XSSFCellStyle)workbook.createCellStyle();
		
		// set colors
		Font font = workbook.createFont();
	    font.setColor(IndexedColors.RED.getIndex());
	    font.setBold(true);
	    style.setFont(font);
	    style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
	    style.setFillPattern(FillPatternType.ALT_BARS);
		
		cell.setCellStyle( style );
		close( workbook, test );
	}

}
