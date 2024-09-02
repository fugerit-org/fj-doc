package org.fugerit.java.doc.val.p7m;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;
import org.fugerit.java.doc.val.core.io.NopOutputStream;

import java.io.InputStream;

@Slf4j
public class P7MValidator extends AbstractDocTypeValidator {
	
	public static final String EXTENSION = "P7M";
	
	public static final String MIME_TYPE = "application/pkcs7-mime";
	
	public static final DocTypeValidator DEFAULT = new P7MValidator();
	
	public P7MValidator() {
		super( MIME_TYPE, EXTENSION );
	}

	private static final String BEGIN_PKCS7 = "-----BEGIN PKCS7-----";

	@Override
	public DocTypeValidationResult validate(InputStream is) {
		return this.validationHelper(() -> {
			byte[] data = StreamIO.readBytes( is );
			try (NopOutputStream out = new NopOutputStream()) {
				String start = new String( data, 0, BEGIN_PKCS7.length() );
				if ( BEGIN_PKCS7.equals( start ) ) {
					P7MUtils.extractContentPEMParser( data, out );
				} else {
					P7MUtils.extractContentCMSSignedData( data, out );
				}
			}
		} );
	}

}
