package org.fugerit.java.doc.val.poi;

import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XlsValidator extends AbstractDocTypeValidator {

	private static final Logger logger = LoggerFactory.getLogger( XlsValidator.class );
	
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
			logger.warn( "Failed check on pdf : {}", e );
		}
		return result;
	}

}
