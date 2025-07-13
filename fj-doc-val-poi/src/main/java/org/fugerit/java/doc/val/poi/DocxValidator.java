package org.fugerit.java.doc.val.poi;

import java.io.InputStream;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocxValidator extends AbstractDocTypeValidator {

	private static final Logger logger = LoggerFactory.getLogger(DocxValidator.class);

	public static final String EXTENSION = "DOCX";

	public static final String MIME_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

	public static final DocTypeValidator DEFAULT = new DocxValidator();

	public DocxValidator() {
		super(MIME_TYPE, EXTENSION);
	}

	@Override
	public DocTypeValidationResult validate(InputStream is) {
		DocTypeValidationResult result = DocTypeValidationResult.newFail();
		try (XWPFDocument workbook = new XWPFDocument(is)) {
			result = DocTypeValidationResult.newOk();
		} catch (Exception e) {
			logger.warn("Failed check on pdf : {}", e.toString());
			result.withMainException( e );
		}
		return result;
	}

}
