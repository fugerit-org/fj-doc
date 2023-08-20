package org.fugerit.java.doc.base.facade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.core.xml.sax.SAXParseResult;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.process.DocProcessConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.base.xml.DocValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessDocFacade {

	private DocHandlerFacade docHandlerFacade;
	
	private DocProcessConfig docProcessConfig;

	public ProcessDocFacade(DocHandlerFacade docHandlerFacade, DocProcessConfig docProcessConfig) {
		super();
		this.docHandlerFacade = docHandlerFacade;
		this.docProcessConfig = docProcessConfig;
	}
	
	public DocHandlerFacade getDocHandlerFacade() {
		return docHandlerFacade;
	}

	public DocProcessConfig getDocProcessConfig() {
		return docProcessConfig;
	}

	public static ProcessDocFacade newFacade( String pathConfigDocProcessConfig, String pathConfigDocHandlerFaactory, String useCatalog ) throws Exception {
		ProcessDocFacade docProcessDocFacade = null;
		try ( InputStream configDocProcessConfig = StreamHelper.resolveStream( pathConfigDocProcessConfig );
				InputStream configDocHandlerFaactory = StreamHelper.resolveStream( pathConfigDocHandlerFaactory ) ) {
			docProcessDocFacade = newFacade(configDocProcessConfig, configDocHandlerFaactory, useCatalog);
		}
		return docProcessDocFacade;
	}
	
	public static ProcessDocFacade newFacade( InputStream configDocProcessConfig , InputStream configDocHandlerFaactory, String useCatalog ) throws Exception {
		DocHandlerFactory docHandlerFatory = DocHandlerFactory.newInstance( configDocHandlerFaactory );
		DocHandlerFacade docHandlerFacade = docHandlerFatory.get( useCatalog );
		DocProcessConfig docProcessConfig = DocProcessConfig.loadConfig( configDocProcessConfig );
		ProcessDocFacade facade = new ProcessDocFacade(docHandlerFacade, docProcessConfig);
		return facade;
	}
	
	public void process( String chainId, String type, DocProcessContext context, File dest ) throws Exception {
		try ( FileOutputStream fos = new FileOutputStream( dest ) ) {
			process(chainId, type, context, fos);
		}
	}
	
	public void process( String chainId, String type, DocProcessContext context, OutputStream os ) throws Exception {
		process(chainId, type, context, os, false);
	}
	
	public SAXParseResult process( String chainId, String type, DocProcessContext context, OutputStream os, boolean validate ) throws Exception {
		SAXParseResult result = null;
		MiniFilterChain chain = this.getDocProcessConfig().getChain( chainId );
		DocProcessData data = new DocProcessData();
		chain.apply(context, data);
		if ( validate ) {
			result = DocValidator.validate( data.getCurrentXmlReader() );
			if ( !result.isPartialSuccess() ) {
				DocValidator.logResult(result, log );
			}
		}
		DocBase docBase = null;
		try ( Reader reader = data.getCurrentXmlReader() ) {
			docBase = DocFacade.parse( reader );
		}
		DocInput input = DocInput.newInput( type, docBase, data.getCurrentXmlReader() );
		DocOutput output = DocOutput.newOutput( os );
		this.getDocHandlerFacade().handle(input, output);
		return result;
	}
	
}
