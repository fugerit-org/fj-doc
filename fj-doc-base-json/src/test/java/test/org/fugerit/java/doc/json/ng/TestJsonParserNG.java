package test.org.fugerit.java.doc.json.ng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocInfo;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandler;
import org.fugerit.java.doc.json.ng.DocJsonParserNG;
import org.fugerit.java.doc.json.ng.DocXmlToJsonNG;
import org.fugerit.java.doc.json.parse.DocJsonParser;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class TestJsonParserNG {

	private boolean parseWorker( String path, String checkInfoKey, String checkinfoValue ) {
		return SafeFunction.get( () -> {
			DocTypeHandler handler = SimpleMarkdownExtTypeHandler.HANDLER;
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/"+path+".json" );
					FileOutputStream fos = new FileOutputStream( "target/"+path+"."+handler.getType() ) ) {
				DocJsonParser parser = new DocJsonParserNG();
				DocBase docBase = parser.parse(is);
				log.info( "docBase -> {}", docBase );
				DocInput input = DocInput.newInput( handler.getType(), docBase, null );
				DocOutput output = DocOutput.newOutput( fos );
				handler.handle( input, output );
				log.info( "info : {}", docBase.getInfo() );
				String infoValue = docBase.getStableInfo().getProperty( checkInfoKey );
				return checkinfoValue.equals( infoValue );
			}
		} );
	}
	
	@Test
	void testParse01() {
		Assertions.assertTrue( this.parseWorker( "doc_test_01_ng", DocInfo.INFO_NAME_MARGINS, "10;10;10;30" ) );
	}

	private boolean worker( String path ) {
		return SafeFunction.get( () -> {
			File outputFile = new File( "target/"+path+"_ng.json" );
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/"+path+".xml" );
					FileOutputStream fos = new FileOutputStream( outputFile ) ) {
				DocXmlToJsonNG converter = new DocXmlToJsonNG();
				JsonNode tree = converter.convertToJsonNode( new InputStreamReader( is ) );
				log.info( "xml -> {}", tree);
				fos.write( tree.toPrettyString().getBytes() );
				return outputFile.exists();
			}
		} );
	}
	
	@Test
	void test01() {
		Assertions.assertTrue( this.worker( "doc_test_01" ) );
	}
	
	
}
