package org.fugerit.java.doc.val.poi;

import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XlsxValidator extends AbstractDocTypeValidator {

	private static final Logger logger = LoggerFactory.getLogger( XlsxValidator.class );
	
	public static final String EXTENSION = "XLSX";
	
	public static final String MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	
	public static final DocTypeValidator DEFAULT = new XlsxValidator();
	
	public XlsxValidator() {
		super( MIME_TYPE, EXTENSION );
	}
	
	@Override
	public DocTypeValidationResult validate(InputStream is) {
		DocTypeValidationResult result = DocTypeValidationResult.newFail();
		try ( XSSFWorkbook workbook = new XSSFWorkbook( is ) ) {
			result = DocTypeValidationResult.newOk();
		} catch (Exception e) {
			logger.warn( "Failed check on pdf : "+e );
		}
		return result;
	}

}
