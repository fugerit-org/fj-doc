package org.fugerit.java.doc.val.p7m;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cms.CMSException;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.DocValidatorTypeCheck;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;

import lombok.Getter;
import lombok.Setter;

@Slf4j
public class P7MContentValidator extends AbstractDocTypeValidator {

	public static final boolean DEFAULT_PROCEED_ON_INNTER_CHECK = Boolean.FALSE;

	@Getter @Setter private DocValidatorTypeCheck facade;

	@Getter private boolean proceedOnInnerTypeCheck;
	
	public P7MContentValidator(DocValidatorFacade facade, boolean proceedOnInnerTypeCheck) {
		super( P7MValidator.MIME_TYPE, P7MValidator.EXTENSION );
		this.facade = DocValidatorTypeCheck.newInstance( facade );
		this.proceedOnInnerTypeCheck = proceedOnInnerTypeCheck;
	}

	public P7MContentValidator(DocValidatorFacade facade) {
		this( facade, DEFAULT_PROCEED_ON_INNTER_CHECK );
	}

	public P7MContentValidator() {
		this( null );
	}
	
	@Override
	public DocTypeValidationResult validate(InputStream is) {
		return this.validationHelper( () -> this.checkInnerType( is ) );
	}

	public String checkInnerType(InputStream is ) {
		return SafeFunction.get( () -> {
			try ( ByteArrayOutputStream os = new ByteArrayOutputStream() ) {
				P7MUtils.extractContent(is, os);
				if ( this.facade.getFacade() != null ) {
					String mimeType = this.facade.checkType( os.toByteArray() );
					if ( mimeType != null || this.proceedOnInnerTypeCheck ) {
						return mimeType;
					} else {
						throw new ConfigRuntimeException( "Content not valid for this validator facade!" );
					}
				}
			} catch (CMSException e) {
				log.warn( String.format( "Error on inner check : %s", e ) );
			}
			return null;
		} );
	}
	
	public P7MContentValidator withDocValidatorFacade( DocValidatorFacade facade ) {
		this.setFacade( DocValidatorTypeCheck.newInstance( facade ) );
		return this;
	}

	public P7MContentValidator withProceedOnInnerTypeCheck( boolean proceedOnInnerTypeCheck ) {
		this.proceedOnInnerTypeCheck = proceedOnInnerTypeCheck;
		return this;
	}

	public static P7MContentValidator newValidator( DocValidatorFacade facade ) {
		return newValidator( facade, DEFAULT_PROCEED_ON_INNTER_CHECK );
	}

	public static P7MContentValidator newValidator( DocValidatorFacade facade, boolean proceedOnInnerTypeCheck ) {
		return new P7MContentValidator().withDocValidatorFacade(facade).withProceedOnInnerTypeCheck( proceedOnInnerTypeCheck );
	}
	
}
