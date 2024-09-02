package org.fugerit.java.doc.val.p7m;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;
import org.fugerit.java.doc.val.core.io.NopOutputStream;

import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class P7MPemValidator extends AbstractDocTypeValidator {

	public static final String EXTENSION = "P7M";

	public static final String MIME_TYPE = "application/pkcs7-mime";

	public P7MPemValidator() {
		super( MIME_TYPE, EXTENSION );
	}

	@Override
	public DocTypeValidationResult validate(InputStream is) {
		return SafeFunction.get( () -> {
			try (OutputStream buffer = new NopOutputStream() ) {
				return  this.validationHelper(() -> P7MUtils.extractContentPEMParser( StreamIO.readBytes( is ), buffer ) );
			}
		} );
	}

}
