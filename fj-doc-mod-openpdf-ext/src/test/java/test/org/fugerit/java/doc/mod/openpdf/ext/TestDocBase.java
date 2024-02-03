package test.org.fugerit.java.doc.mod.openpdf.ext;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;

@Slf4j
public class TestDocBase {

	private FreemarkerDocProcessConfig config = FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://fm-test-mod-openpdf-config.xml" );

	protected boolean testDocWorker( String testCase, String type ) {
		boolean ok = false;
		String inputXml = "xml/"+testCase+".xml" ;
		DocTypeHandler handler = this.config.getFacade().findHandler( type );
		File outputFile = new File( "target", testCase+"."+handler.getType() );
		log.info( "inputXml:{}, outputFile:{}", inputXml, outputFile );
		try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( inputXml ) );
				OutputStream os = new FileOutputStream( outputFile ) ) {
			handler.handle( DocInput.newInput( handler.getType() , reader ) , DocOutput.newOutput(os) );
			ok = true;
		} catch (Exception e) {
			String message = "Error : "+e.getMessage();
			log.error( message , e );
			fail( message );
		}
		return ok;
	}
	
}
