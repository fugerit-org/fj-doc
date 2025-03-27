package test.org.fugerit.java.doc.core.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.XmlValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestXmlValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			XmlValidator.DEFAULT
	);
	
	@Test
	void testXmlAsXml() {
		boolean ok = this.worker(FACADE, "xml_as_xml.xml", true );
		Assertions.assertTrue( ok );
	}
	@Test
	void testXslAsXml() {
		boolean ok = this.worker(FACADE, "xls_as_xml.xml", false );
		Assertions.assertTrue( ok );
	}
	
}
