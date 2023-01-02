package test.org.fugerit.java.doc.yaml.parse;

import static org.junit.Assert.fail;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.yaml.parse.DocYamlToXml;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class TestDocXmlToYaml {

	private static final Logger logger = LoggerFactory.getLogger( TestDocXmlToYaml.class );
	
	private void worker( String path ) {
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/"+path+".yaml" );
				FileOutputStream fos = new FileOutputStream( "target/"+path+".xml" )) {
			DocYamlToXml converter = new DocYamlToXml();
			Element root = converter.convertToElement( new InputStreamReader( is ) );
			DOMIO.writeDOMIndent( root, fos);
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
