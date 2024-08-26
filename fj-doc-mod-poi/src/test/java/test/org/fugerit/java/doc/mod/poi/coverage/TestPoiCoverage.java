package test.org.fugerit.java.doc.mod.poi.coverage;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.function.SimpleValue;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.mod.poi.XlsPoiTypeHandler;
import org.fugerit.java.doc.mod.poi.XlsxPoiTypeHandler;
import org.junit.Assert;
import org.junit.Test;

public class TestPoiCoverage {
	
	private final static String[] TEST_LIST = { "default_doc", "default_doc_alt", "default_doc_fail1" };
	
	private final static DocTypeHandler[] HANDLERS = { XlsPoiTypeHandler.HANDLER, XlsxPoiTypeHandler.HANDLER };
	
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
	public void test01() {
		Arrays.asList( TEST_LIST ).stream().forEach( c -> {
			Assert.assertTrue( this.worker( "coverage/xml/"+c+".xml" ) );
		} );
		Assert.assertTrue( Boolean.TRUE );
	}
	
}
