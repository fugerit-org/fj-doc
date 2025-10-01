package test.org.fugerit.java.doc.yaml.parse;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.*;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

class TestYamlDocumentProcessing {

	private static final Logger logger = LoggerFactory.getLogger( TestYamlDocumentProcessing.class );

	@Test
	void testGenerateXML() {
		Assertions.assertNotNull( SafeFunction.get( () -> {
				try ( InputStreamReader reader = new InputStreamReader(
						ClassHelper.loadFromDefaultClassLoader( "sample/doc_test_01.yaml" ) );
					  ByteArrayOutputStream buffer = new ByteArrayOutputStream() ) {
					DocInput docInput = DocInput.newInput( DocConfig.TYPE_XML, reader, DocFacadeSource.SOURCE_TYPE_YAML );
					DocTypeHandlerXMLUTF8.HANDLER.handle( docInput, DocOutput.newOutput( buffer ) );
					logger.info( "xml output -> {}", new String( buffer.toByteArray(), StandardCharsets.UTF_8 ) );
					return buffer.toByteArray();
				}
			} )
		);
	}
	
}
