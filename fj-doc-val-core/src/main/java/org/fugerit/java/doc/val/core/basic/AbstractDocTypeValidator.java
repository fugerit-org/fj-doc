package org.fugerit.java.doc.val.core.basic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;

public abstract class AbstractDocTypeValidator implements DocTypeValidator {

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"[mimeType=" + mimeType + ", supportedExtensions=" + supportedExtensions + "]";
	}

	public static Set<String> createSet( String... s ) {
		return new HashSet<>( Arrays.asList( s ) );
	}
	
	protected AbstractDocTypeValidator(String mimeType, String extension) {
		this( mimeType, createSet(extension) );
	}

	protected AbstractDocTypeValidator(String mimeType, Set<String> supportedExtensions) {
		super();
		this.mimeType = mimeType;
		this.supportedExtensions = Collections.unmodifiableSet( supportedExtensions.stream().map( s -> s.toUpperCase() ).collect( Collectors.toSet() ) );
	}

	private String mimeType;
	
	private Set<String> supportedExtensions;
	
	@Override
	public Set<String> getSupportedExtensions() {
		return this.supportedExtensions;
	}

	@Override
	public String getMimeType() {
		return this.mimeType;
	}

	@Override
	public boolean isExtensionSupported(String ext) {
		return this.supportedExtensions.contains( ext.toUpperCase() );
	}

	@Override
	public boolean isMimeTypeSupported(String mime) {
		return this.getMimeType().equalsIgnoreCase( mime );
	}

	@Override
	public boolean check(InputStream is) throws IOException {
		return this.validate(is).isResultOk();
	}

	@Override
	public boolean checkCompatibility() {
		return true;
	}

	@Override
	public abstract DocTypeValidationResult validate(InputStream is) throws IOException;
	
}
