package test.org.fugerit.java.doc.base.kotlin.parse;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.kotlin.parse.DocKotlinParser;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandler;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class TestKotlinParser {

	public static final boolean VALID = true;
	public static final boolean NOT_VALID = false;
	
	public static final boolean NO_EXCEPTION = false;
	public static final boolean EXCEPTION = true;
	
	private static final Logger logger = LoggerFactory.getLogger( TestKotlinParser.class );
	
	private boolean validateWorker( String path, boolean valid, boolean exception ) {
		return this.validateWorker(path, valid, exception, false);
	}
	
	private boolean validateWorker( String path, boolean valid, boolean exception, boolean parseVersion ) {
		String fullPath = "doc-dsl-sample/"+path+".kts";
		logger.info( "validate -> {}, valid : {}, exception : {}, parseVersion : {}", fullPath, valid, exception, parseVersion );
		DocParser parser = new DocKotlinParser();
		return SafeFunction.get( () -> {
			try ( Reader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( fullPath ) ) ) {
				DocValidationResult result =  parseVersion ? parser.validateVersionResult( reader ) : parser.validateResult( reader );
				logger.info( "Validation result {}", result.isResultOk() );
				for ( String error : result.getErrorList() ) {
					logger.info( "Validation error {}", error );
				}
				for ( String error : result.getInfoList() ) {
					logger.info( "Validation info {}", error );
				}
				Assert.assertEquals( "Validation result" , valid, result.isResultOk() );
				return result.isResultOk();
			}
		} );
	}
	
	private boolean parseWorker( String path ) {
		return SafeFunction.get( () -> {
			DocTypeHandler handler = SimpleMarkdownExtTypeHandler.HANDLER;
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "doc-dsl-sample/"+path+".kts" );
					FileOutputStream fos = new FileOutputStream( "target/"+path+"."+handler.getType() ) ) {
				DocParser parser = DocFacadeSource.getInstance().getParserForSource( DocFacadeSource.SOURCE_TYPE_KOTLIN );
				DocBase docBase = parser.parse(is);
				logger.info( "docBase -> {}", docBase );
				DocInput input = DocInput.newInput( handler.getType(), docBase, null );
				DocOutput output = DocOutput.newOutput( fos );
				handler.handle( input, output );
				return VALID;
			}
		} );
	}

	@Test
	public void testValidate() {
		Assert.assertTrue( this.validateWorker( "sample-2", true, false, false ) );
		Assert.assertTrue( this.validateWorker( "sample-2", true, false, true ) );
	}

	@Test
	public void testParse() {
		Assert.assertTrue( this.parseWorker( "sample-2" ) );
		Assert.assertTrue( this.parseWorker( "sample-2-coverage" ) );
	}

}
