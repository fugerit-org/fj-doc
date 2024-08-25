package test.org.fugerit.java.doc.yaml.parse;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.json.parse.DocXmlToJson;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class TestDocYamlToXml {

	private static final Logger logger = LoggerFactory.getLogger( TestDocYamlToXml.class );
	
	private boolean worker( String path ) {
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/"+path+".xml" );
				FileOutputStream fos = new FileOutputStream( "target/"+path+".yaml" )) {
			ObjectMapper yamlMapper = new ObjectMapper( new YAMLFactory() );
			DocXmlToJson converter = new DocXmlToJson( yamlMapper );
			JsonNode tree = converter.convertToJsonNode( new InputStreamReader( is ) );
			logger.info( "xml -> {}", tree);
			yamlMapper.writeValue( fos , tree );
			return true;
		} catch (Exception e) {
			String message = "Error : "+e;
			logger.error( message, e );
			return false;
		}
	}
	
	@Test
	public void test01() {
		Assert.assertTrue( this.worker( "doc_test_01" ) );
	}
	
}
