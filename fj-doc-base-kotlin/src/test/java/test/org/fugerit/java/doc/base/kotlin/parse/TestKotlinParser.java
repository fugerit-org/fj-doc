package test.org.fugerit.java.doc.base.kotlin.parse;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.kotlin.parse.DocKotlinParser;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.DocEvalWithDataModel;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandler;
import org.fugerit.java.script.helper.EvalScript;
import org.fugerit.java.script.helper.EvalScriptWithDataModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class TestKotlinParser {

	static final boolean VALID = true;
	static final boolean NOT_VALID = false;
	
	static final boolean NO_EXCEPTION = false;
	static final boolean EXCEPTION = true;
	
	private static final Logger logger = LoggerFactory.getLogger( TestKotlinParser.class );

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
				Assertions.assertEquals( valid, result.isResultOk() );
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
	void testValidate() {
		Assertions.assertTrue( this.validateWorker( "sample-2", true, false, false ) );
		Assertions.assertTrue( this.validateWorker( "sample-2", true, false, true ) );
	}

	@Test
	void testParse() {
		Assertions.assertTrue( this.parseWorker( "sample-2" ) );
		Assertions.assertTrue( this.parseWorker( "sample-2-coverage" ) );
	}

	@Test
	void testDslDocToXml() throws IOException {
		Map<String, Object> dataModel = new HashMap<>();
		DocEvalWithDataModel eval = new DocKotlinParser();
		try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "doc-dsl-sample/sample-2.kts" ) ) ) {
			String xml = eval.evalWithDataModel( reader, dataModel );
			Assertions.assertNotNull( xml );
		}
	}

	@Test
	void testWithComplexDataModel() throws IOException {
		Map<String, Object> dataModel = new HashMap<>();
		dataModel.put( "docTitle", "Complex map conversion example title" );
		dataModel.put( "listPeople", Arrays.asList(new People("Luthien", "Tinuviel", "Queen"), new People("Thorin", "Oakshield", "King")));
		EvalScript eval = new EvalScriptWithDataModel( "kts" );
		try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "doc-dsl-sample/sample-map.kts" ) ) ) {
			String xml = eval.handle( reader, dataModel ).toString();
			logger.info( "xml : \n{}", xml );
			Assertions.assertNotNull( xml );
		}
	}

}
