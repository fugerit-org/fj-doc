package org.fugerit.java.doc.freemarker.html;

import java.io.OutputStreamWriter;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;

public class FreeMarkerHtmlTypeHandler extends DocTypeHandlerDefault {

	public static final String CHAIN_FREEMARKER = "html-freemarker";
	
	public static final String ATT_DOCBASE = "docBase";
	
	public static DocTypeHandler HANDLER = new FreeMarkerHtmlTypeHandler();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7394516771708L;

	public FreeMarkerHtmlTypeHandler() {
		this( DocConfig.TYPE_HTML );
	}

	public FreeMarkerHtmlTypeHandler(String type) {
		super(type);
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		MiniFilterChain chain = FreeMarkerHtmlProcess.getInstance().getChainCache( CHAIN_FREEMARKER );
		DocProcessContext context = DocProcessContext.newContext( ATT_DOCBASE , docInput.getDoc() );
		DocProcessData data = new DocProcessData();
		chain.apply( context, data );
		StreamIO.pipeCharCloseBoth( data.getCurrentXmlReader() , new OutputStreamWriter( docOutput.getOs() ) );
	}
	
}