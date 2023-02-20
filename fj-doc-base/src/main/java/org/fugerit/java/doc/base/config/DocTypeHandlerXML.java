package org.fugerit.java.doc.base.config;

import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.fugerit.java.core.io.StreamIO;

public class DocTypeHandlerXML extends DocTypeHandlerDefault {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5024985828785381015L;

	public static final DocTypeHandler HANDLER = new DocTypeHandlerXML();
	
	public static final DocTypeHandler HANDLER_UTF8 = new DocTypeHandlerXML( StandardCharsets.UTF_8 );
	
	public static final String TYPE = DocConfig.TYPE_XML;
	
	public static final String MODULE = "doc";
	
	public DocTypeHandlerXML( Charset charset ) {
		super( TYPE, MODULE, null, charset );
	}
	
	public DocTypeHandlerXML() {
		super( TYPE, MODULE );
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		StreamIO.pipeCharCloseBoth( docInput.getReader() , new OutputStreamWriter( docOutput.getOs(), this.getCharset() ) );
	}

}
