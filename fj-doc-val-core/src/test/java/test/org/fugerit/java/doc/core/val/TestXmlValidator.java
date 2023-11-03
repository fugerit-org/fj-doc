package test.org.fugerit.java.doc.core.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.XmlValidator;
import org.junit.Assert;
import org.junit.Test;

public class TestXmlValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			XmlValidator.DEFAULT
	);
	
	@Test
	public void testXmlAsXml() {
		boolean ok = this.worker(FACADE, "xml_as_xml.xml", true );
		Assert.assertTrue( ok );
	}
	@Test
	public void testXslAsXml() {
		boolean ok = this.worker(FACADE, "xls_as_xml.xml", false );
		Assert.assertTrue( ok );
	}
	
}
