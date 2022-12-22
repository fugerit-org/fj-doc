package org.fugerit.java.doc.val.pdf.box;

import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfboxValidator extends AbstractDocTypeValidator {

	private static final Logger logger = LoggerFactory.getLogger( PdfboxValidator.class );
	
	public static final String EXTENSION = "PDF";
	
	public static final String MIME_TYPE = "application/pdf";
	
	public static final DocTypeValidator DEFAULT = new PdfboxValidator();
	
	public PdfboxValidator() {
		super( MIME_TYPE, EXTENSION );
	}
	
	@Override
	public DocTypeValidationResult validate(InputStream is) {
		DocTypeValidationResult result = DocTypeValidationResult.newFail();
		try {
			PDDocument.load( is );
			result = DocTypeValidationResult.newOk();
		} catch (Exception e) {
			logger.warn( "Failed check on pdf : "+e );
		}
		return result;
	}

}
