package test.org.fugerit.java.doc.base.coverage;

import java.io.IOException;
import java.util.Properties;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocBorders;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.typehelper.excel.ExcelHelperConsts;
import org.fugerit.java.doc.base.typehelper.excel.ExcelHelperUtils;
import org.fugerit.java.doc.base.typehelper.excel.MatrixCell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestExcelHelpers {

	@Test
	void testTesoveTemplateStreamOk() throws IOException {
		DocBase docBase = new DocBase();
		docBase.setStableInfo( new Properties() );
		docBase.getStableInfo().setProperty( ExcelHelperConsts.PROP_XLS_TEMPLATE , "cl://txt/test.txt" );
		String text = StreamIO.readString( ExcelHelperUtils.resoveTemplateStream( docBase ) );
		Assertions.assertEquals( "test text" , text );
	}
	
	@Test
	void testTesoveTemplateStreamFail() throws IOException {
		DocBase docBase = new DocBase();
		docBase.setStableInfo( new Properties() );
		docBase.getStableInfo().setProperty( ExcelHelperConsts.PROP_XLS_TEMPLATE , "cl://txt/not_found.txt" );
		Assertions.assertThrows( IOException.class , () -> ExcelHelperUtils.resoveTemplateStream( docBase ) );
	}
	
	@Test
	void testTesoveTemplateStreamNoProp() throws IOException {
		DocBase docBase = new DocBase();
		docBase.setStableInfo( new Properties() );
		Assertions.assertNull( ExcelHelperUtils.resoveTemplateStream( docBase ) );
	}
	
	@Test
	void testPrepareNumber() throws IOException {
		Assertions.assertEquals( "1000.5", ExcelHelperUtils.prepareNumber( "1.000,5" ) );
		Assertions.assertEquals( "10", ExcelHelperUtils.prepareNumber( "10" ) );
	}
	
	@Test
	void testMatrixCell() {
		DocCell cell = new DocCell();
		cell.setDocBorders( new DocBorders() );
		cell.getDocBorders().setBorderWidthBottom( 1 );
		MatrixCell mc = new MatrixCell( cell , cell );
		Assertions.assertNotNull( mc.getBorders() );
		cell.setRSpan( 2 );
		cell.setCSpan( 2 );
		Assertions.assertEquals( 0, mc.getBorders().getBorderWidthBottom() );
		DocCell newCell = new DocCell();
		mc.setCell( newCell );
		Assertions.assertEquals( 0, mc.getBorders().getBorderWidthBottom() );
		cell.setRSpan( 1 );
		cell.setCSpan( 1 );
		Assertions.assertEquals( 1, mc.getBorders().getBorderWidthBottom() );
	}
	
}
