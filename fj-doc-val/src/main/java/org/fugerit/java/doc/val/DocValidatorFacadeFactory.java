package org.fugerit.java.doc.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.fugerit.java.doc.val.pdf.box.PdfboxValidator;
import org.fugerit.java.doc.val.poi.DocValidator;
import org.fugerit.java.doc.val.poi.DocxValidator;
import org.fugerit.java.doc.val.poi.XlsValidator;
import org.fugerit.java.doc.val.poi.XlsxValidator;

public class DocValidatorFacadeFactory {

	public DocValidatorFacade createFullDocValidatorFacade() {
		return DocValidatorFacade.newFacade( 
				ImageValidator.JPG_VALIDATOR, 
				ImageValidator.PNG_VALIDATOR, 
				ImageValidator.TIFF_VALIDATOR,
				PdfboxValidator.DEFAULT, 
				DocValidator.DEFAULT, 
				DocxValidator.DEFAULT,
				XlsValidator.DEFAULT,
				XlsxValidator.DEFAULT );
	}
	
}
