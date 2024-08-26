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
		Assert.assertTrue( SafeFunction.get( () -> {
			boolean ok = Boolean.TRUE;
			PoiUtils.xlsxFormatStyle(null, null, null);
			PoiUtils.xlsxFormatStyle(null, new XSSFCellStyle( null), null );
			return ok;
		} ) );
	}
	
	@Test
	public void xlsxFontStyle() {
		Assert.assertTrue( SafeFunction.get( () -> {
			boolean ok = Boolean.TRUE;
			PoiUtils.xlsxFontStyle(null, null, null);
			 return ok;
		} ) );
	}
	
	@Test
	public void lsxFormat() {
		Assert.assertTrue( SafeFunction.get( () -> {
			boolean ok = Boolean.TRUE;
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "coverage/xls_as_xls.xls" ); 
					HSSFWorkbook workbook = new HSSFWorkbook( is ) ) {
				WorkbookHelper helper = new WorkbookHelper(workbook, null);
				PoiUtils.xlsFormatStyle(helper, null, null);
				PoiUtils.xlsFormatStyle(helper, workbook.createCellStyle(), null );
				PoiUtils.xlsFontStyle(helper, null, new DocCell() );
			}
			return ok;
		} ) );
	}
	
	@Test
	public void testFindColor() {
		Short test = 31;
		Assert.assertEquals( test, SafeFunction.get( () -> {
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "coverage/xls_as_xls.xls" ); 
					HSSFWorkbook workbook = new HSSFWorkbook( is ) ) {
				short index = PoiUtils.findClosestColorIndex( workbook , "#dddddd" );
				log.info( "index - {}", index );
				return index;
			}
		} ) );
	}
	
	@Test
	public void testNewHelper() {
		SafeFunction.apply( () -> {
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "coverage/xls_as_xls.xls" ); 
					WorkbookHelper helper = PoiUtils.newHelper( false , is ) ) {
				log.info( "test 1 -> {}", helper.getIndexedColorMap() );
				Assert.assertNotNull( helper );
			}
		} );
		Assert.assertNotNull( SafeFunction.get( () -> {
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "coverage/xlsx_as_xlsx.xlsx" ); 
					WorkbookHelper helper = PoiUtils.newHelper( true , is ) ) {
				log.info( "test 2 -> {}", helper.getIndexedColorMap() );
				return helper;
			}
		} ) );
	}
	
	private static final IOException FAIL_EX = new IOException( "scenario exception" );
	
	@Test
	public void testAutoresizeFailhandler() {
		PoiUtils.autoresizeFailHandler( false ).accept( FAIL_EX );
		Consumer<Exception> exHandlerFail = PoiUtils.autoresizeFailHandler( true );
		Assert.assertThrows( ConfigRuntimeException.class , () -> exHandlerFail.accept( FAIL_EX ) );
	}
	
}
