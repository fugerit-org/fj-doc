package org.fugerit.java.doc.val.core.basic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.function.UnsafeVoid;
import org.fugerit.java.core.util.ObjectUtils;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractDocTypeValidator implements DocTypeValidator {

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"[mimeType=" + mimeType + ", supportedExtensions=" + supportedExtensions + "]";
	}

	public static Set<String> createSet( String... s ) {
		return new HashSet<>( Arrays.asList( s ) );
	}
	
	protected DocTypeValidationResult validationHelper( UnsafeVoid<Exception> fun ) {
		final DocTypeValidationResult resultFail = DocTypeValidationResult.newFail();
		return ObjectUtils.objectWithDefault( SafeFunction.get( () -> {
			fun.apply();
			return DocTypeValidationResult.newOk();
		}, e ->   {
			log.warn( "validation failed {}, {}", this.getMimeType(), e.toString() );
			resultFail.withMainException( e );
		} ), resultFail );
	}
	
	protected AbstractDocTypeValidator(String mimeType, String extension) {
		this( mimeType, createSet(extension) );
	}

	protected AbstractDocTypeValidator(String mimeType, Set<String> supportedExtensions) {
		super();
		this.mimeType = mimeType;
		this.supportedExtensions = Collections.unmodifiableSet( supportedExtensions.stream().map( String::toUpperCase ).collect( Collectors.toSet() ) );
	}

	@Getter private String mimeType;
	
	@Getter private Set<String> supportedExtensions;

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
		return Boolean.TRUE;
	}

}
