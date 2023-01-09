package org.fugerit.java.doc.mod.fop;

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

public class FreeMarkerFopTypeHandler extends DocTypeHandlerDefault {

	public static final String CHAIN_FREEMARKER = "fop-freemarker";
	
	public static final String ATT_DOCBASE = DocProcessContext.ATT_NAME_DOC_BASE;
	
	public static final DocTypeHandler HANDLER = new FreeMarkerFopTypeHandler();
	
	public static final String MODULE = "fop";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7394516771708L;

	public FreeMarkerFopTypeHandler() {
		this( DocConfig.TYPE_FO );
	}

	public FreeMarkerFopTypeHandler(String type) {
		super(type, MODULE);
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		MiniFilterChain chain = FreeMarkerFopProcess.getInstance().getChainCache( CHAIN_FREEMARKER );
		DocProcessContext context = DocProcessContext.newContext().withDocInput( docInput );
		DocProcessData data = new DocProcessData();
		chain.apply( context, data );
		StreamIO.pipeCharCloseBoth( data.getCurrentXmlReader() , new OutputStreamWriter( docOutput.getOs() ) );
	}
	
}