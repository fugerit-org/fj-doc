package test.org.fugerit.java.doc.tool;

import java.util.Properties;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.util.PropsIO;
import org.fugerit.java.doc.tool.DocTool;

public class TestDocTool {

	protected boolean docToolWorker( String paramsPath ) {
		return SafeFunction.get( () -> {
			Properties params = PropsIO.loadFromFile( paramsPath );
			DocTool.handle( params );
			return true;
		} );
	}
	
}
