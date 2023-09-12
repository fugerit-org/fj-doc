package test.org.fugerit.java.doc.mod.poi.coverage;

import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.mod.poi.PoiUtils;
import org.fugerit.java.doc.mod.poi.WorkbookHelper;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestPOIUtils {

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
	
}
