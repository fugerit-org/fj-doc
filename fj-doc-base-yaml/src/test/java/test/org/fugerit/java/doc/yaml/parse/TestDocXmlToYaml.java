package test.org.fugerit.java.doc.yaml.parse;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.yaml.parse.DocYamlToXml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

class TestDocXmlToYaml {

	private static final Logger logger = LoggerFactory.getLogger( TestDocXmlToYaml.class );
	
	private boolean worker( String path, DocYamlToXml converter ) {
		return SafeFunction.get( () -> {
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/"+path+".yaml" );
					FileOutputStream fos = new FileOutputStream( "target/"+path+".xml" )) {
				Element root = converter.convertToElement( new InputStreamReader( is ) );
				logger.info( "xml -> {}", root);
				DOMIO.writeDOMIndent(root, fos);
			}
			return true;
		} );
	}
	
	@Test
	void test01() {
		Assertions.assertTrue( this.worker( "doc_test_01", new DocYamlToXml() ) );
	}
	
	@Test
	void test01Alt() {
		Assertions.assertTrue( this.worker( "doc_test_01", new DocYamlToXml( new ObjectMapper( new YAMLFactory() ) ) ) );
	}
	
}
