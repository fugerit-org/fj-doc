package test.org.fugerit.java.doc.mod.poi.coverage;

import java.io.*;
import java.util.Arrays;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.function.SimpleValue;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.mod.poi.XlsPoiTypeHandler;
import org.fugerit.java.doc.mod.poi.XlsxPoiTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestPoiCoverage {
	
	private static final String[] TEST_LIST = { "default_doc", "default_doc_alt", "default_doc_fail1" };
	
	private static final DocTypeHandler[] HANDLERS = { XlsPoiTypeHandler.HANDLER, XlsxPoiTypeHandler.HANDLER };
	
	private boolean worker( String path ) {
		SimpleValue<Boolean> res = new SimpleValue<>(false);
		SafeFunction.apply( () -> {
			for ( int k=0; k<HANDLERS.length; k++ ) {
				DocTypeHandler handler = HANDLERS[k];
				try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader(path) );
						ByteArrayOutputStream buffer = new ByteArrayOutputStream() ) {
						handler.handle( DocInput.newInput( handler.getType() , reader ) ,  DocOutput.newOutput( buffer ) );
						res.setValue( buffer.toByteArray().length > 0 );
				}
			}
		} );
		return res.getValue();
	}
	
	@Test
	void test01() {
		Arrays.asList( TEST_LIST ).stream().forEach( c -> {
			Assertions.assertTrue( this.worker( "coverage/xml/"+c+".xml" ) );
		} );
		Assertions.assertTrue( Boolean.TRUE );
	}

	@Test
	void testDoc() {
        SimpleValue<Integer> count = new SimpleValue<>( 0 );
        Arrays.asList( "test_doc", "test_doc_alt" ).forEach( fileName -> {
            SafeFunction.apply( () -> {
                DocTypeHandler handler = XlsxPoiTypeHandler.HANDLER;
                String inputXml = String.format( "coverage/xml/%s.xml", fileName );
                String outputFile = String.format( "target/%s.%s", fileName, handler.getType() );
                File output = new File( outputFile );
                try ( FileOutputStream fos = new FileOutputStream( output );
                      InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader(inputXml) );) {
                    handler.handle( DocInput.newInput( handler.getType(), reader ), DocOutput.newOutput( fos ) );
                }
                Assertions.assertTrue( output.exists() );
                count.setValue( count.getValue()+1 );
            });
        });
        Assertions.assertNotEquals( 0, count.getValue() );
	}
	
}
