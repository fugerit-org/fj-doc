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

public class TestPOI extends TestFormatBase {

	private void close( Workbook workbook, String test ) throws Exception {
		File file = this.getFile( BasicFacadeTest.BASIC_OUTPUT_PATH, test+".xlsx" );
		try ( FileOutputStream fos = new FileOutputStream( file ) ) {
			workbook.write( fos );
			workbook.close();
		}
		logger.info( "File generated! {}", file.getCanonicalPath() );
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
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		cell.setCellStyle( style );
		close( workbook, test );
	}

}
