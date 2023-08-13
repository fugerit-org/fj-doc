package test.org.fugerit.java.doc.sample.libdoc;

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
public class GenerateDocHelper {

	protected void docWorker( String sourcePath, String destPath, DocTypeHandler handler ) {
		File destFile = new File( destPath );
		log.info( "handler {}, sourcePath : {}, destFile : {}", handler, sourcePath, destFile );
		try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( sourcePath ) );
				OutputStream os = new FileOutputStream( destFile ) ) {
			DocInput input = DocInput.newInput( handler.getType(), reader );
			DocOutput output = DocOutput.newOutput(os);
			handler.handle(input, output);
		} catch (Exception e) {
			String message = "Error : "+e;
			log.error( message, e );
			fail( message );
		}
	}
	
}
