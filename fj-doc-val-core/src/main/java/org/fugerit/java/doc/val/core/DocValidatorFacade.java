package org.fugerit.java.doc.val.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocValidatorFacade {

	private static final Logger logger = LoggerFactory.getLogger( DocValidatorFacade.class );

	private List<DocTypeValidator> validators;
	
	private Map<String, DocTypeValidator> extMapValidator;
	
	private Map<String, DocTypeValidator> mimMapValidator;
	
	private DocValidatorFacade() {
		this.validators = new ArrayList<>();
		this.extMapValidator = new HashMap<>();
		this.mimMapValidator = new HashMap<>();
	}
	
	private boolean addValidator( DocTypeValidator validator ) {
		boolean ok = validator.checkCompatibility();
		if ( ok ) {
			this.validators.add(validator);
			DocTypeValidator previous = this.mimMapValidator.put( validator.getMimeType(), validator );
			if ( previous != null ) {
				ok = false;
				logger.warn( "Validator {} has been overridden for mimeType {}", previous, validator.getMimeType() );
			}
			for ( String ext : validator.getSupportedExtensions() ) {
				previous = this.extMapValidator.put(ext, validator);
				if ( previous != null ) {
					ok = false;
					logger.warn( "Validator {} has been overridden for extension {}", previous, ext );
				}
			}	
		} else {
			logger.info( "Validator compatibility check failed : {}", validator );
		}
		return ok;
	}

	public List<DocTypeValidator> validators() {
		return Collections.unmodifiableList( this.validators );
	}
	
	public List<DocTypeValidator> findAllByMimeType( final String mimeType ) {
		return this.validators().stream().filter( v -> v.isMimeTypeSupported( mimeType ) ).collect( Collectors.toList() );
	}
	
	public List<DocTypeValidator> findAllExtension( final String extension ) {
		return this.validators().stream().filter( v -> v.isExtensionSupported( extension ) ).collect( Collectors.toList() );
	}
	
	public DocTypeValidator findByMimeType( String mimeType ) {
		return this.mimMapValidator.get(mimeType);
	}
	
	public DocTypeValidator findByExtension( String extension ) {
		return this.extMapValidator.get( extension );
	}
	
       public boolean isMimeTypeSupported( String mimeType ) {
               return this.findByMimeType(mimeType) != null;
       }

	public Collection<String> getSupportedMimeTypes() {
		return Collections.unmodifiableCollection( this.mimMapValidator.keySet() );
	}
	
	public Collection<String> getSupportedExtensions() {
		return Collections.unmodifiableCollection( this.extMapValidator.keySet() );
	}
	
	public boolean isExtensionSupported( String extension ) {
		return this.findByExtension(extension) != null;
	}
	
	public boolean check( File file ) throws IOException {
		return this.validate(file).isResultOk();
	}
	
	public DocTypeValidationResult validate( File file ) throws IOException {
		DocTypeValidationResult result = DocTypeValidationResult.newFail();
		try ( InputStream is = new FileInputStream( file ) ) {
			result = this.validate( file.getName(), is );
		}
		return result;
	}
	
	public boolean check( String fileName, InputStream is ) throws IOException {
		return this.validate(fileName, is).isResultOk();
	}
	
	public DocTypeValidationResult validate( String fileName, InputStream is ) throws IOException {
		String extension = fileName.substring( fileName.lastIndexOf( '.' )+1 );
		return validateByExtension(extension, is);
	}

	public boolean checkByExtension( String extension, InputStream is ) throws IOException {
		return this.validateByExtension(extension, is).isResultOk();
	}
	
	public DocTypeValidationResult validateByExtension( String extension, InputStream is ) throws IOException {
		return this.evaluate(this.findByExtension(extension.toUpperCase()) , is);
	}
	
	public boolean checkByMimeType( String mimeType, InputStream is ) throws IOException {
		return this.validateByMimeType(mimeType, is).isResultOk();
	}
	
	public DocTypeValidationResult validateByMimeType( String mimeType, InputStream is ) throws IOException {
		return this.evaluate(this.findByMimeType(mimeType) , is);
	}
	
	private DocTypeValidationResult evaluate( DocTypeValidator validator, InputStream is ) throws IOException {
		DocTypeValidationResult result = DocTypeValidationResult.newFail();
		if ( validator != null ) {
			result = validator.validate( is );
		} else {
			logger.info( "no validator found!" );
		}
		return result;
	}
	
	public static DocValidatorFacade newFacade( DocTypeValidator... validators ) {
		DocValidatorFacade facade = new DocValidatorFacade();
		for ( int k=0; k<validators.length; k++ ) {
			DocTypeValidator v = validators[k];
			logger.info( "add validator {}", v );
			facade.addValidator( v );
		}
		return facade;
	}
	
	public static DocValidatorFacade newFacadeStrict( DocTypeValidator... validators ) {
		DocValidatorFacade facade = new DocValidatorFacade();
		for ( int k=0; k<validators.length; k++ ) {
			DocTypeValidator v = validators[k];
			logger.info( "add validator {}", v );
			boolean ok = facade.addValidator( v );
			if ( !ok ) {
				throw new ConfigRuntimeException( "Facade creation error for validator : "+v );
			}
		}
		return facade;
	}

}
