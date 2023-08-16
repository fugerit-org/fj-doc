package test.org.fugerit.java.doc.sample.handlertest;

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

@Slf4j
public class TestHandleBase {

	private File getBaseDir() {
		File file = new File( "target", "handler_test_out" );
		if ( !file.exists() ) {
			file.mkdir();
		}
		return file;
	}
	
	protected void testWorker( String name, DocTypeHandler ...handlers ) {
		String sourcePath = "sample_docs/junit_base/"+name+".xml";
		for ( int k=0; k<handlers.length; k++ ) {
			DocTypeHandler handler = handlers[k];
			File outputFile = new File( this.getBaseDir(), name+"."+handler.getType() ); 
			log.info( "source -> {}, output -> {}", sourcePath, outputFile );
			try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( sourcePath ) );
					OutputStream os = new FileOutputStream( outputFile ) ) {
				handler.handle( DocInput.newInput( handler.getType() , reader ), DocOutput.newOutput(os) );
			} catch (Exception e) {
				String message = "Error : "+e.getMessage();
				log.error( message, e );
				fail( message );
			}
		}
	}
	
}
