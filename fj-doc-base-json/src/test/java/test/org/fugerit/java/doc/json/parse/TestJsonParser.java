package test.org.fugerit.java.doc.json.parse;

import static org.junit.Assert.fail;

import java.io.FileOutputStream;
import java.io.InputStream;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandler;
import org.fugerit.java.doc.json.parse.DocJsonParser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJsonParser {

	private static final Logger logger = LoggerFactory.getLogger( TestJsonParser.class );
	
	private void worker( String path ) {
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/"+path+".json" );
				FileOutputStream fos = new FileOutputStream( "target/"+path+".md" )) {
			DocJsonParser parser = new DocJsonParser();
			DocBase docBase = parser.parse(is);
			logger.info( "docBase -> {}", docBase );
			DocInput input = DocInput.newInput( SimpleMarkdownExtTypeHandler.TYPE, docBase, null );
			DocOutput output = DocOutput.newOutput( fos );
			SimpleMarkdownExtTypeHandler.HANDLER.handle( input, output );
		} catch (Exception e) {
			String message = "Error : "+e;
			logger.error( message, e );
			fail( message ) ;
		}
	}
	
	@Test
	public void test01() {
		this.worker( "doc_test_01" );
	}
	
}
