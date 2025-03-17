package test.org.fugerit.java.doc.freemarker.coverage;

import java.io.*;
import java.util.Arrays;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.function.SimpleValue;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.freemarker.asciidoc.FreeMarkerAsciidocTypeHandler;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlFragmentTypeHandler;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlFragmentTypeHandlerUTF8;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandler;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandlerUTF8;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class TestFreemarkerCoverage {
	
	private final static TestEntry[] TEST_LIST = {
			new TestEntry( "default_doc" , true ),
			new TestEntry( "default_doc_alt" , true ),
			new TestEntry( "default_doc_fail1" , true )
	};
	
	private final static DocTypeHandler[] HANDLERS = { FreeMarkerHtmlTypeHandler.HANDLER,
														FreeMarkerHtmlTypeHandlerUTF8.HANDLER,
														FreeMarkerHtmlFragmentTypeHandler.HANDLER,
														FreeMarkerHtmlFragmentTypeHandlerUTF8.HANDLER,
														FreeMarkerAsciidocTypeHandler.HANDLER_UTF8 };
	
	private boolean worker( String path, boolean result ) {
		SimpleValue<Boolean> res = new SimpleValue<>(false);
		SafeFunction.apply( () -> {
			for ( int k=0; k<HANDLERS.length; k++ ) {
				DocTypeHandler handler = HANDLERS[k];
				try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader(path) );
						ByteArrayOutputStream buffer = new ByteArrayOutputStream() ) {
						handler.handle( DocInput.newInput( handler.getType() , reader ) ,  DocOutput.newOutput( buffer ) );
						if ( result ) {
							res.setValue( buffer.toByteArray().length > 0 );
						} else {
							res.setValue( buffer.toByteArray().length == 0 );	
						}
				}
			}
		} );
		return res.getValue();
	}
	
	@Test
	void test01() {
		Arrays.asList( TEST_LIST ).stream().forEach( c -> {
			log.info( "test -> {}", c );
			Assertions.assertTrue( this.worker( "coverage/xml/"+c.getId()+".xml", c.isResult() ) );
		} );
		Assertions.assertTrue( Boolean.TRUE );
	}

	@Test
	void testAsciidoc() {
		String docId = "asciidoc";
		DocTypeHandler handler = FreeMarkerAsciidocTypeHandler.HANDLER_UTF8;
		String type = DocConfig.TYPE_ADOC;
		File outputFile = new File( "target/asciidoc."+type );
		SafeFunction.apply( () -> {
			try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "coverage/xml/"+docId+".xml" ) );
				  FileOutputStream fos = new FileOutputStream( outputFile ) ) {
				handler.handle( DocInput.newInput( handler.getType() , reader ) ,  DocOutput.newOutput( fos ) );
			}
		} );
		Assertions.assertTrue( outputFile.exists() );
	}

}

@AllArgsConstructor
@ToString
class TestEntry {
	
	@Getter private String id;
	
	@Getter private boolean result;
	
}