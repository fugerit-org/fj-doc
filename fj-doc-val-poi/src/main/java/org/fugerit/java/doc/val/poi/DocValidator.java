package org.fugerit.java.doc.val.poi;

import java.io.InputStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;

public class DocValidator extends AbstractDocTypeValidator {

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
			this.logFailedCheck( EXTENSION, e );
			result.withMainException( e );
		}
		return result;
	}

}
