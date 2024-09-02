package org.fugerit.java.doc.val.p7m;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;

import java.io.InputStream;

@Slf4j
public class P7MLegacyValidator extends AbstractDocTypeValidator {

	public static final String EXTENSION = "P7M";

	public static final String MIME_TYPE = "application/pkcs7-mime";

	public P7MLegacyValidator() {
		super( MIME_TYPE, EXTENSION );
	}

	@Override
	public DocTypeValidationResult validate(InputStream is) {
		return this.validationHelper(() -> P7MUtils.extractContent( is ) );
	}

}
