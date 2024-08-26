package test.org.fugerit.java.doc.mod.opencsv.coverage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.function.SimpleValue;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.mod.opencsv.OpenCSVTypeHandlerUTF8;
import org.junit.Assert;
import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestOpencsvCoverage {
	
	private final static TestEntry[] TEST_LIST = {
			new TestEntry( "default_doc" , true ),
			new TestEntry( "default_doc_alt" , true ),
			new TestEntry( "default_doc_fail1" , false )
	};
	
	private final static DocTypeHandler[] HANDLERS = { OpenCSVTypeHandlerUTF8.HANDLER };
	
	private boolean worker( String id, String path, boolean result ) {
		SimpleValue<Boolean> res = new SimpleValue<>(false);
		SafeFunction.apply( () -> {
			for ( int k=0; k<HANDLERS.length; k++ ) {
				DocTypeHandler handler = HANDLERS[k];
				try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader(path) );
						ByteArrayOutputStream buffer = new ByteArrayOutputStream() ) {
						handler.handle( DocInput.newInput( handler.getType() , reader ) ,  DocOutput.newOutput( buffer ) );
						if ( result ) {
							res.setValue( buffer.toByteArray().length > 0 );
							FileIO.writeBytes( buffer.toByteArray() , new File( "target/", id+".csv" ) );
						} else {
							res.setValue( buffer.toByteArray().length == 0 );	
						}
						
				}
			}
		} );
		return res.getValue();
	}
	
	@Test
	public void test01() {
		Arrays.asList( TEST_LIST ).stream().forEach( c -> {
			log.info( "test -> {}", c );
			Assert.assertTrue( this.worker( c.getId(), "coverage/xml/"+c.getId()+".xml", c.isResult() ) );
		} );
		Assert.assertTrue( Boolean.TRUE );
	}
	
}

@AllArgsConstructor
@ToString
class TestEntry {
	
	@Getter private String id;
	
	@Getter private boolean result;
	
}