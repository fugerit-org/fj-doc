package org.fugerit.java.doc.freemarker.helper;

import java.io.OutputStreamWriter;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;

public class FreeMarkerDocHelperTypeHandler extends DocTypeHandlerDefault {

	public static final String ATT_DOCBASE = DocProcessContext.ATT_NAME_DOC_BASE;
	
	public static final String MODULE = "fm";
	
	public static final String CHAIN_FREEMARKER = "html-freemarker";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7394516771708L;

	public FreeMarkerDocHelperTypeHandler(String type) {
		this(type, CHAIN_FREEMARKER);
	}
	
	public FreeMarkerDocHelperTypeHandler(String type, String fmDocChainId) {
		super(type, MODULE);
		this.fmDocChainId = fmDocChainId;
	}

	private String fmDocChainId;
	
	public String getFmDocChainId() {
		return fmDocChainId;
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		MiniFilterChain chain = FreeMarkerDocProcess.getInstance().getChainCache( this.getFmDocChainId() );
		DocProcessContext context = DocProcessContext.newContext().withDocInput( docInput );
		DocProcessData data = new DocProcessData();
		chain.apply( context, data );
		StreamIO.pipeCharCloseBoth( data.getCurrentXmlReader() , new OutputStreamWriter( docOutput.getOs() ) );
	}
	
}