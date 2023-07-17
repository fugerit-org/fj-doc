package test.org.fugerit.java.doc.tool;

import static org.junit.Assert.fail;

import java.util.Properties;

import org.fugerit.java.core.util.PropsIO;
import org.fugerit.java.doc.tool.DocTool;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocTool {

	protected void docToolWorker( String paramsPath ) {
		try {
			Properties params = PropsIO.loadFromFile( paramsPath );
			DocTool.handle( params );	
		} catch (Exception e) {
			String message = "Error : "+e;
			log.error( message, e );
			fail( message );
		}
	}
	
}
