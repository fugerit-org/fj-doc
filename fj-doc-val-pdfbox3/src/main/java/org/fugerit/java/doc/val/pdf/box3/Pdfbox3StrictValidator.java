package org.fugerit.java.doc.val.pdf.box3;

import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Pdfbox3StrictValidator extends AbstractDocTypeValidator {

	public static final DocTypeValidator DEFAULT = new Pdfbox3StrictValidator();
	
	public Pdfbox3StrictValidator() {
		super( Pdfbox3Validator.MIME_TYPE, Pdfbox3Validator.EXTENSION );
	}
	
	@Override
	public DocTypeValidationResult validate(InputStream is) {
		DocTypeValidationResult result = DocTypeValidationResult.newFail();
		try( PDDocument doc = Pdfbox3Utils.parse(is, false) ) {
			result = DocTypeValidationResult.newOk();
		} catch (Exception e) {
			log.warn( "Failed check on pdf : {}", e.toString() );
			result.withMainException( e );
		}
		return result;
	}

}
