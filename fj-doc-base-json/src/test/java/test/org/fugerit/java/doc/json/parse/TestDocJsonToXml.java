package test.org.fugerit.java.doc.json.parse;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.function.SimpleValue;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.json.parse.DocJsonToXml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

class TestDocJsonToXml {

	private static final Logger logger = LoggerFactory.getLogger( TestDocJsonToXml.class );
	
	private boolean worker( String path ) {
		SimpleValue<Boolean> res = new SimpleValue<>( true );
		SafeFunction.apply( () -> {
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/"+path+".json" );
					FileOutputStream fos = new FileOutputStream( "target/"+path+".xml" )) {
				DocJsonToXml converter = new DocJsonToXml();
				Element root = converter.convertToElement( new InputStreamReader( is ) );
				logger.info( "xml -> {}", root);
				DOMIO.writeDOMIndent(root, fos);
			}
		} );
		return res.getValue();
	}
	
	@Test
	void test01() {
		Assertions.assertTrue( this.worker( "doc_test_01" ) );
	}
	
	@Test
	void testConvert() {
		Assertions.assertNotEquals( "" ,
		SafeFunction.get( () -> {
			try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "sample/doc_test_01.json" ) );
					StringWriter writer = new StringWriter() ) {
				DocJsonToXml converter = new DocJsonToXml();
				converter.writerAsXml(reader, writer);
				return writer.toString();
			}
		} ) );
	}
	
	@Test
	void testFail01() {
		Assertions.assertThrows( Exception.class , () -> {
			DocJsonToXml converter = new DocJsonToXml();
			try ( InputStreamReader reader = new InputStreamReader(
					ClassHelper.loadFromDefaultClassLoader( "sample/doc_test_fail_01.json" ) ) ) {
				converter.convertToElement( reader );
			}
		} );
	}
	
	@Test
	void testFail02() {
		Assertions.assertThrows( Exception.class , () -> {
			DocJsonToXml converter = new DocJsonToXml();
			try ( InputStreamReader reader = new InputStreamReader(
					ClassHelper.loadFromDefaultClassLoader( "sample/doc_test_fail_02.json" ) ) ) {
				converter.convertToElement( reader );
			}
		} );
	}
	
}
