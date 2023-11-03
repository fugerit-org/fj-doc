package org.fugerit.java.doc.val.core.basic;

import java.io.IOException;
import java.io.InputStream;

import org.fugerit.java.core.xml.sax.XMLFactorySAX;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.xml.sax.helpers.DefaultHandler;

public class XmlValidator extends AbstractDocTypeValidator {

	public static final String EXTENSION = "XML";
	
	public static final String MIME_TYPE = "text/xml";
	
	public static final DocTypeValidator DEFAULT = new XmlValidator();
	
	public XmlValidator() {
		super(MIME_TYPE, EXTENSION);
	}

	@Override
	public DocTypeValidationResult validate(InputStream is) throws IOException {
		return this.validationHelper( () -> XMLFactorySAX.makeSAXParser( true, true ).parse( is , new DefaultHandler() ) );
	}

}
