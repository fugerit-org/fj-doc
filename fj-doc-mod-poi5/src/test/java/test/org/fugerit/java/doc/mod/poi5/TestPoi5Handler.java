package test.org.fugerit.java.doc.mod.poi5;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.mod.poi5.XlsPoi5TypeHandler;
import org.fugerit.java.doc.mod.poi5.XlsxPoi5TypeHandler;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestPoi5Handler {

	private void testPoi( String baseName, DocTypeHandler handler ) {
		try ( Reader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "sample/"+baseName+".xml" ) );
				OutputStream os = new FileOutputStream( new File( "target/"+baseName+"."+handler.getType() ) ) ) {
			handler.handle( DocInput.newInput( handler.getType() , reader ) , DocOutput.newOutput( os ) );
		} catch (Exception e) {
			String message = "Error "+e;
			log.error( message, e );
			fail( message );
		}
	}
	
	@Test
	public void testXlsx() {
		String baseName = "doc_test_01";
		this.testPoi(baseName, XlsxPoi5TypeHandler.HANDLER);
	}
	
	@Test
	public void testXls() {
		String baseName = "doc_test_01";
		this.testPoi(baseName, XlsPoi5TypeHandler.HANDLER);
	}
	
}
