package org.fugerit.java.doc.base.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.config.DocVersion;
import org.fugerit.java.doc.base.model.DocBase;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractDocParser implements DocParser {

	private int sourceType;
	
	protected AbstractDocParser(int sourceType) {
		super();
		this.sourceType = sourceType;
	}

	@Override
	public int getSourceType() {
		return this.sourceType;
	}

	@Override
	public DocBase parse(InputStream is) throws DocException {
		return this.parse( new InputStreamReader(is) );
	}
	
	private void handleVersion( String xsdVersion ) {
		try {
			if ( StringUtils.isNotEmpty( xsdVersion ) && DocVersion.compare( xsdVersion, DocVersion.CURRENT_VERSION_S ) > 0 ) {
				log.warn( "Document version {} is higher than maximum version supported by this release od fj-doc {}, some feature may be not supported.", xsdVersion, DocVersion.CURRENT_VERSION_S  );
			}	
		} catch (Exception e) {
			log.warn( "Failed to check xsd version : {} (current version: {})", xsdVersion, DocVersion.CURRENT_VERSION_S );
		}
	}
	
	@Override
	public DocBase parse(Reader reader) throws DocException {
		DocBase docBase = null;
		try {
			docBase = this.parseWorker(reader);
			String xsdVersion = docBase.getXsdVersion();
			this.handleVersion(xsdVersion);
		} catch (Exception e) {
			throw DocException.convertExMethod( "parse", e);
		}
		return docBase;
	}
	
	@Override
	public DocValidationResult validateResult(Reader reader) throws DocException {
		DocValidationResult result = null;
		try {
			result = this.validateWorker(reader, false);
		} catch (Exception e) {
			throw DocException.convertExMethod( "validateResult", e);
		}
		return result;
	}

	@Override
	public int validate(Reader reader) throws DocException {
		return this.validateResult(reader).evaluateResult();
	}
	
	@Override
	public DocValidationResult validateVersionResult(Reader reader) throws DocException {
		DocValidationResult result = null;
		try {
			result = this.validateWorker(reader, true);
		} catch (Exception e) {
			throw DocException.convertExMethod( "validateVersionResult", e);
		}
		return result;
	}

	@Override
	public int validateVersion(Reader reader) throws DocException {
		return this.validateVersionResult(reader).evaluateResult();
	}

	protected abstract DocValidationResult validateWorker( Reader reader, boolean parseVersion ) throws DocException;
	
	protected abstract DocBase parseWorker( Reader reader ) throws DocException;

}
