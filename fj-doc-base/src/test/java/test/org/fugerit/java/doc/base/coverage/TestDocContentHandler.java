package test.org.fugerit.java.doc.base.coverage;

import org.fugerit.java.doc.base.model.DocHelper;
import org.fugerit.java.doc.base.parser.DocParserContext;
import org.fugerit.java.doc.base.xml.DocContentHandler;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestDocContentHandler {
	
	@Test
	public void testCoverage() throws SAXException {
		DocContentHandler handler = new DocContentHandler();
		Assert.assertNotNull( handler );
		Assert.assertNotNull( new DocContentHandler( new DocHelper() ) );
		Assert.assertNotNull( new DocParserContext() );
		// they are all currently do nothing impl
		handler.skippedEntity( null );
		handler.processingInstruction(null, null);
		// same as "characters" : 
		handler.ignorableWhitespace( "test".toCharArray() , 0, 3);
	}
	
}
