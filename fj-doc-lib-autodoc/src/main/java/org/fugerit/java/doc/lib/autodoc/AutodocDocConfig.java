package org.fugerit.java.doc.lib.autodoc;

import java.io.OutputStream;

import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandler;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.fugerit.java.doc.lib.autodoc.detail.AutodocDetailModel;
import org.fugerit.java.doc.lib.autodoc.meta.AutodocMetaModel;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;

public class AutodocDocConfig {

	private FreemarkerDocProcessConfig config;
	
	private AutodocDocConfig( FreemarkerDocProcessConfig config ) {
		this.config = config;
	}
	
	private static final AutodocDocConfig INSTANCE = 
			new AutodocDocConfig( FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://fj_doc_lib_autodoc/fm-doc-process-config-autodoc.xml" ) );
	
	public static AutodocDocConfig getInstance() {
		return INSTANCE;
	}
	
	public static AutodocDocConfig newConfig() {
		return getInstance();
	}

	public static final String CHAIN_ID_AUTODOC = "autodoc";

	public static final String CHAIN_ID_AUTODOC_SCHEMA = "autodoc_schema";

	public static final String CHAIN_ID_AUTODOC_DETAIL = "autodoc_detail";
	
	public static final String CHAIN_ID_AUTODOC_META = "autodoc_meta";
	
	public FreemarkerDocProcessConfig getConfig() {
		return config;
	}
	
	private void process( String chainId, DocProcessContext context, DocProcessData data ) throws Exception {
		MiniFilterChain chain = this.getConfig().getChainCache( chainId );
		chain.apply( context , data );
	}
	
	public void processAutodoc(  AutodocModel autodocModel, DocTypeHandler handler, OutputStream os ) throws DocException {
		DocException.applyWithMessage( () -> {
			DocProcessData data = new DocProcessData();
			DocProcessContext context = DocProcessContext.newContext( AutodocModel.ATT_NAME, autodocModel );
			process( CHAIN_ID_AUTODOC , context, data );
			DocBase docBase = DocFacade.parse( data.getCurrentXmlReader() );
			DocInput docInput = DocInput.newInput( handler.getType() , docBase );
			DocOutput docOutput = DocOutput.newOutput( os );
			handler.handle( docInput , docOutput );
		}, "Autodoc generation error" );
	}
	
	public void processAutodocHtmlDefault(  AutodocModel autodocModel, OutputStream os ) throws DocException {
		this.processAutodoc( autodocModel, FreeMarkerHtmlTypeHandler.HANDLER, os );
	}

	public void processAutodocSchema(  AutodocModel autodocModel, DocTypeHandler handler, OutputStream os ) throws DocException {
		DocException.applyWithMessage( () -> {
			DocProcessData data = new DocProcessData();
			DocProcessContext context = DocProcessContext.newContext( AutodocModel.ATT_NAME, autodocModel );
			process( CHAIN_ID_AUTODOC_SCHEMA , context, data );
			DocBase docBase = DocFacade.parse( data.getCurrentXmlReader() );
			DocInput docInput = DocInput.newInput( handler.getType() , docBase );
			DocOutput docOutput = DocOutput.newOutput( os );
			handler.handle( docInput , docOutput );
		}, "Autodoc generation error" );
	}

	public void processAutodocSchemaHtmlDefault(  AutodocModel autodocModel, OutputStream os ) throws DocException {
		this.processAutodocSchema( autodocModel, FreeMarkerHtmlTypeHandler.HANDLER, os );
	}

	public void processAutodocDetail(  AutodocDetailModel autoDetailModel, DocTypeHandler handler, OutputStream os ) throws DocException {
		DocException.applyWithMessage( () -> {
			DocProcessData data = new DocProcessData();
			DocProcessContext context = DocProcessContext.newContext( AutodocDetailModel.ATT_NAME, autoDetailModel );
			process( CHAIN_ID_AUTODOC_DETAIL , context, data );
			DocBase docBase = DocFacade.parse( data.getCurrentXmlReader() );
			DocInput docInput = DocInput.newInput( handler.getType() , docBase );
			DocOutput docOutput = DocOutput.newOutput( os );
			handler.handle( docInput , docOutput );
		}, "Autodoc detail generation error" );
	}
	
	public void processAutodocDetailHtmlDefault(  AutodocDetailModel autoDetailModel, OutputStream os ) throws DocException {
		this.processAutodocDetail( autoDetailModel, FreeMarkerHtmlTypeHandler.HANDLER, os );
	}

	public void processAutodocMeta(  AutodocMetaModel autodocMetaModel, DocTypeHandler handler, OutputStream os ) throws DocException {
		DocException.applyWithMessage( () -> {
			DocProcessData data = new DocProcessData();
			DocProcessContext context = DocProcessContext.newContext( AutodocMetaModel.ATT_NAME, autodocMetaModel );
			process( CHAIN_ID_AUTODOC_META , context, data );
			DocBase docBase = DocFacade.parse( data.getCurrentXmlReader() );
			DocInput docInput = DocInput.newInput( handler.getType() , docBase );
			DocOutput docOutput = DocOutput.newOutput( os );
			handler.handle( docInput , docOutput );
		}, "Autodoc meta generation error" );
	}
	
}
