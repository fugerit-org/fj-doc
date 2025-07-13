package org.fugerit.java.doc.val.poi;

import java.io.InputStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocValidator extends AbstractDocTypeValidator {

	private static final Logger logger = LoggerFactory.getLogger( DocValidator.class );
	
	public static final String EXTENSION = "DOC";
	
	public static final String MIME_TYPE = "application/msword";
	
	public static final DocTypeValidator DEFAULT = new DocValidator();
	
	public DocValidator() {
		super( MIME_TYPE, EXTENSION );
	}
	
	@Override
	public DocTypeValidationResult validate(InputStream is) {
		DocTypeValidationResult result = DocTypeValidationResult.newFail();
		try ( HWPFDocument workbook = new HWPFDocument( is ) ) {
			result = DocTypeValidationResult.newOk();
		} catch (Exception e) {
			logger.warn( "Failed check on pdf : {}", e.toString() );
			result.withMainException( e );
		}
		return result;
	}

}
