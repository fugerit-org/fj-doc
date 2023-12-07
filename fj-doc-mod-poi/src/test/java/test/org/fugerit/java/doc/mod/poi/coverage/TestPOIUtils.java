package test.org.fugerit.java.doc.mod.poi.coverage;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.mod.poi.PoiUtils;
import org.fugerit.java.doc.mod.poi.WorkbookHelper;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestPOIUtils {

	
	@Test
	public void xlsxFormatStyle() {
		SafeFunction.apply( () -> {
			boolean ok = true;
			PoiUtils.xlsxFormatStyle(null, null, null);
			PoiUtils.xlsxFormatStyle(null, new XSSFCellStyle( null), null );
			Assert.assertTrue( ok );
		} );
	}
	
	@Test
	public void xlsxFontStyle() {
		SafeFunction.apply( () -> {
			boolean ok = true;
			PoiUtils.xlsxFontStyle(null, null, null);
			Assert.assertTrue( ok );
		} );
	}
	
	@Test
	public void lsxFormat() {
		SafeFunction.apply( () -> {
			boolean ok = true;
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "coverage/xls_as_xls.xls" ); 
					HSSFWorkbook workbook = new HSSFWorkbook( is ) ) {
				WorkbookHelper helper = new WorkbookHelper(workbook, null);
				PoiUtils.xlsFormatStyle(helper, null, null);
				PoiUtils.xlsFormatStyle(helper, workbook.createCellStyle(), null );
				PoiUtils.xlsFontStyle(helper, null, new DocCell() );
			}
			Assert.assertTrue( ok );
		} );
	}
	
	@Test
	public void testFindColor() {
		SafeFunction.apply( () -> {
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "coverage/xls_as_xls.xls" ); 
					HSSFWorkbook workbook = new HSSFWorkbook( is ) ) {
				short index = PoiUtils.findClosestColorIndex( workbook , "#dddddd" );
				short test = 31;
				log.info( "index - {}", index );
				Assert.assertEquals( test, index );
			}
		} );
	}
	
	@Test
	public void testNewHelper() {
		SafeFunction.apply( () -> {
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "coverage/xls_as_xls.xls" ); 
					WorkbookHelper helper = PoiUtils.newHelper( false , is ) ) {
				log.info( "test -> {}", helper.getIndexedColorMap() );
				Assert.assertNotNull( helper );
			}
		} );
		SafeFunction.apply( () -> {
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "coverage/xlsx_as_xlsx.xlsx" ); 
					WorkbookHelper helper = PoiUtils.newHelper( true , is ) ) {
				log.info( "test -> {}", helper.getIndexedColorMap() );
				Assert.assertNotNull( helper );
			}
		} );
	}
	
	private static final IOException FAIL_EX = new IOException( "scenario exception" );
	
	@Test
	public void testAutoresizeFailhandler() {
		PoiUtils.autoresizeFailHandler( false ).accept( FAIL_EX );
		Consumer<Exception> exHandlerFail = PoiUtils.autoresizeFailHandler( true );
		Assert.assertThrows( ConfigRuntimeException.class , () -> exHandlerFail.accept( FAIL_EX ) );
	}
	
}
