package test.org.fugerit.java.doc.yaml.parse;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.yaml.parse.DocYamlToXml;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class TestDocXmlToYaml {

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
	public void test01() {
		Assert.assertTrue( this.worker( "doc_test_01", new DocYamlToXml() ) );
	}
	
	@Test
	public void test01Alt() {
		Assert.assertTrue( this.worker( "doc_test_01", new DocYamlToXml( new ObjectMapper( new YAMLFactory() ) ) ) );
	}
	
}
