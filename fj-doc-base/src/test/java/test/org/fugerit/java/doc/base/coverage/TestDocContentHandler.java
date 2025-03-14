package test.org.fugerit.java.doc.base.coverage;

import org.fugerit.java.doc.base.model.DocHelper;
import org.fugerit.java.doc.base.parser.DocParserContext;
import org.fugerit.java.doc.base.xml.DocContentHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

class TestDocContentHandler {
	
	@Test
	void testCoverage() throws SAXException {
		DocContentHandler handler = new DocContentHandler();
		Assertions.assertNotNull( handler );
		Assertions.assertNotNull( new DocContentHandler( new DocHelper() ) );
		Assertions.assertNotNull( new DocParserContext() );
		// they are all currently do nothing impl
		handler.skippedEntity( null );
		handler.processingInstruction(null, null);
		// same as "characters" : 
		handler.ignorableWhitespace( "test".toCharArray() , 0, 3);
	}
	
}
