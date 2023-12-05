package org.fugerit.java.doc.val.p7m;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;

import lombok.Getter;
import lombok.Setter;

public class P7MContentValidator extends AbstractDocTypeValidator {
	
	@Getter @Setter private DocValidatorFacade facade;
	
	public P7MContentValidator(DocValidatorFacade facade) {
		super( P7MValidator.MIME_TYPE, P7MValidator.EXTENSION );
		this.facade = facade;
	}

	public P7MContentValidator() {
		this( null );
	}
	
	@Override
	public DocTypeValidationResult validate(InputStream is) {
		return this.validationHelper( () -> {
			try ( ByteArrayOutputStream os = new ByteArrayOutputStream() ) {
				P7MUtils.extractContent(is, os);
				if ( this.getFacade() != null ) {
					boolean isValid = false;
					for ( DocTypeValidator validator : this.getFacade().validators() ) {
						try ( ByteArrayInputStream bis = new ByteArrayInputStream( os.toByteArray() ) ) {
							if ( validator.check( bis ) ) {
								isValid = true;
								break;
							}
						}
					}
					if ( !isValid ) {
						throw new ConfigRuntimeException( "Content not valid for this validator facade!" );
					}
				}
			}
		} );
	}
	
	public P7MContentValidator withDocValidatorFacade( DocValidatorFacade facade ) {
		this.setFacade( facade );
		return this;
	}
	
	public static P7MContentValidator newValidator( DocValidatorFacade facade ) {
		return new P7MContentValidator().withDocValidatorFacade(facade);
	}
	
}
