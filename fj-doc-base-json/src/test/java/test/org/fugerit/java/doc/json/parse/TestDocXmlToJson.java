package test.org.fugerit.java.doc.json.parse;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.json.parse.DocXmlToJson;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

public class TestDocXmlToJson {

	private static final Logger logger = LoggerFactory.getLogger( TestDocXmlToJson.class );
	
	private boolean worker( String path ) {
		return SafeFunction.get( () -> {
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/"+path+".xml" );
					FileOutputStream fos = new FileOutputStream( "target/"+path+".json" )) {
				DocXmlToJson converter = new DocXmlToJson();
				JsonNode tree = converter.convertToJsonNode( new InputStreamReader( is ) );
				logger.info( "xml -> {}", tree);
				fos.write( tree.toPrettyString().getBytes() );
				return true;
			}
		} );
	}
	
	@Test
	public void test01() {
		Assert.assertTrue( this.worker( "doc_test_01" ) );
	}
	
}
