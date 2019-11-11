package org.fugerit.java.doc.base.config;

import java.io.OutputStreamWriter;

import org.fugerit.java.core.io.StreamIO;

public class DocTypeHandlerXML extends DocTypeHandlerDefault {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5024985828785381015L;

	public static final DocTypeHandler HANDLER = new DocTypeHandlerXML();
	
	public DocTypeHandlerXML() {
		super( DocConfig.TYPE_XML );
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		StreamIO.pipeCharCloseBoth( docInput.getReader() , new OutputStreamWriter( docOutput.getOs() ) );
	}

}
