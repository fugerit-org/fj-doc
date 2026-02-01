package org.fugerit.java.doc.val.pdf.box3;

import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Pdfbox3Validator extends AbstractDocTypeValidator {
	
	public static final String EXTENSION = "PDF";
	
	public static final String MIME_TYPE = "application/pdf";
	
	public static final DocTypeValidator DEFAULT = new Pdfbox3Validator();
	
	public Pdfbox3Validator() {
		super( MIME_TYPE, EXTENSION );
	}
	
	@Override
	public DocTypeValidationResult validate(InputStream is) {
		DocTypeValidationResult result = DocTypeValidationResult.newFail();
		try( PDDocument doc = Pdfbox3Utils.parse(is, true) ) {
			result = DocTypeValidationResult.newOk();
		} catch (Exception e) {
			log.warn( "Failed check on pdf : {}", e.toString() );
			result.withMainException( e );
		}
		return result;
	}

}
