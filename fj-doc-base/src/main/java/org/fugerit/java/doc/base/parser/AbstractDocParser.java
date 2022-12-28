package org.fugerit.java.doc.base.parser;

import java.io.Reader;

import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.config.DocVersion;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDocParser implements DocParser {

	private static final Logger logger = LoggerFactory.getLogger( AbstractDocParser.class );
	
	private int sourceType;
	
	public AbstractDocParser(int sourceType) {
		super();
		this.sourceType = sourceType;
	}

	@Override
	public int getSourceType() {
		return this.sourceType;
	}

	@Override
	public DocBase parse(Reader reader) throws DocException {
		DocBase docBase = null;
		try {
			docBase = this.parseWorker(reader);
			String xsdVersion = docBase.getXsdVersion();
			try {
				if ( StringUtils.isNotEmpty( xsdVersion ) && DocVersion.compare( xsdVersion, DocFacade.CURRENT_VERSION ) > 0 ) {
					logger.warn( "Document version {} is higher than maximum version supported by this release od fj-doc {}, some feature may be not supported.", xsdVersion, DocFacade.CURRENT_VERSION  );
				}	
			} catch (Exception e) {
				logger.warn( "Failed to check xsd version : {} (current version: {})", xsdVersion, DocFacade.CURRENT_VERSION );
			}
		} catch (Exception e) {
			throw new DocException( "Parsing exception : "+e, e );
		}
		return docBase;
	}
	
	@Override
	public DocValidationResult validateResult(Reader reader) throws DocException {
		DocValidationResult result = null;
		try {
			result = this.validateWorker(reader);
		} catch (Exception e) {
			throw new DocException( "Parsing exception : "+e, e );
		}
		return result;
	}

	@Override
	public int validate(Reader reader) throws DocException {
		return this.validateResult(reader).evaluateResult();
	}

	protected abstract DocValidationResult validateWorker( Reader reader ) throws Exception;
	
	protected abstract DocBase parseWorker( Reader reader ) throws Exception;

}
