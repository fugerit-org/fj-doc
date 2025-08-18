package org.fugerit.java.doc.base.xml;

import java.io.Reader;

import javax.xml.parsers.SAXParser;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.xml.sax.SAXParseResult;
import org.fugerit.java.core.xml.sax.XMLFactorySAX;
import org.fugerit.java.core.xml.sax.dh.DefaultHandlerComp;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.feature.FeatureConfig;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocHelper;
import org.fugerit.java.doc.base.parser.AbstractDocParser;
import org.fugerit.java.doc.base.parser.DocParserContext;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.xml.sax.InputSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocXmlParser extends AbstractDocParser {
	
	public static void fillDocValidationResultHelper( SAXParseResult result, DocValidationResult docResult ) {
		for ( Exception e : result.fatalsAndErrors() ) {
			docResult.getErrorList().add( e.toString() );
		}
		for ( Exception e : result.warnings() ) {
			docResult.getInfoList().add( e.toString() );
		}
	}
	
	private DocHelper docHelper;


	public boolean isFailWhenElementNotFound() {
		return this.getFeatureConfig().isFailWhenElementNotFound();
	}

	public DocXmlParser(DocHelper docHelper, final boolean failWhenElementNotFound ) {
		this( docHelper, FeatureConfig.fromFailWhenElementNotFound( failWhenElementNotFound ) );
	}

	public DocXmlParser(DocHelper docHelper, FeatureConfig featureConfig) {
		super( DocFacadeSource.SOURCE_TYPE_XML );
		this.docHelper = docHelper;
		super.setFeatureConfig( featureConfig );
	}
	
	public DocXmlParser( boolean failOnElementNotFound ) {
		this( DocHelper.DEFAULT, failOnElementNotFound );
	}
	
	public DocXmlParser( DocHelper docHelper ) {
		this( docHelper, DocParserContext.FAIL_WHEN_ELEMENT_NOT_FOUND_DEFAULT );
	}

	public DocXmlParser() {
		this( DocHelper.DEFAULT );
	}

	@Override
	protected DocBase parseWorker(Reader reader) throws DocException {
		return SafeFunction.get( () -> {
			DocContentHandler dch =  new DocContentHandler( this.docHelper, this.isFailWhenElementNotFound() );	
			SAXParser parser = XMLFactorySAX.makeSAXParserSecure( false ,  true );
			DefaultHandlerComp dh = new DefaultHandlerComp( dch );
			parser.parse( new InputSource(reader), dh);
			return dch.getDocBase();
		} );
	}
	
	@Override
	protected DocValidationResult validateWorker(Reader reader, boolean parseVersion) throws DocException {
		return SafeFunction.get( () -> {
			DocValidationResult docResult = DocValidationResult.newDefaultNotDefinedResult();
			SAXParseResult result = null;
			if ( parseVersion ) {
				result = DocValidator.validateVersion( reader );
			} else {
				result = DocValidator.validate( reader );
			}
			fillDocValidationResultHelper(result, docResult);
			docResult.evaluateResult();
			log.debug( "Validation result {}", docResult );
			return docResult;
		} );

	}

}
