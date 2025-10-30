package org.fugerit.java.doc.val.poi;

import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;

public class XlsValidator extends AbstractDocTypeValidator {

	public static final String EXTENSION = "XLS";
	
	public static final String MIME_TYPE = "application/vnd.ms-excel";
	
	public static final DocTypeValidator DEFAULT = new XlsValidator();
	
	public XlsValidator() {
		super( MIME_TYPE, EXTENSION );
	}
	
	@Override
	public DocTypeValidationResult validate(InputStream is) {
		DocTypeValidationResult result = DocTypeValidationResult.newFail();
		try ( HSSFWorkbook workbook = new HSSFWorkbook( is ) ) {
			result = DocTypeValidationResult.newOk();
		} catch (Exception e) {
            this.logFailedCheck( EXTENSION, e );
			result.withMainException( e );
		}
		return result;
	}

}
