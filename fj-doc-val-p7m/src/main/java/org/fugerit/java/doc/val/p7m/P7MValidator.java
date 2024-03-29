package org.fugerit.java.doc.val.p7m;

import java.io.InputStream;

import org.bouncycastle.cms.CMSSignedData;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;

public class P7MValidator extends AbstractDocTypeValidator {
	
	public static final String EXTENSION = "P7M";
	
	public static final String MIME_TYPE = "application/pkcs7-mime";
	
	public static final DocTypeValidator DEFAULT = new P7MValidator();
	
	public P7MValidator() {
		super( MIME_TYPE, EXTENSION );
	}
	
	@Override
	public DocTypeValidationResult validate(InputStream is) {
		return this.validationHelper( () -> new CMSSignedData( is ) );
	}
	
}
