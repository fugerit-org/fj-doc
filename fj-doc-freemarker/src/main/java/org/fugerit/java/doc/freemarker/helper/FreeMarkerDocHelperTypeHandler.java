package org.fugerit.java.doc.freemarker.helper;

import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.core.xml.dom.DOMUtils;
import org.fugerit.java.doc.base.config.DocCharsetProvider;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.w3c.dom.Element;

import lombok.Getter;
import lombok.Setter;

public class FreeMarkerDocHelperTypeHandler extends DocTypeHandlerDefault {

	public static final String ATT_DOCBASE = DocProcessContext.ATT_NAME_DOC_BASE;
	
	public static final String MODULE = "fm";
	
	public static final String CHAIN_FREEMARKER = "html-freemarker";
	
	public static final String ATT_ESCAPE_TEXT_AS_HTML = "escapeTextAsHtml";
	public static final boolean ATT_ESCAPE_TEXT_AS_HTML_DEFAULT = false;
	
	public static final String MIME = "text/html";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7394516771708L;

	public FreeMarkerDocHelperTypeHandler(String type) {
		this(type, CHAIN_FREEMARKER);
	}
	
	public FreeMarkerDocHelperTypeHandler(String type, String fmDocChainId) {
		this(type, DocCharsetProvider.getDefaultProvider().resolveCharset( null ), fmDocChainId);
	}
	
	public FreeMarkerDocHelperTypeHandler(String type, Charset charset) {
		this(type, charset, CHAIN_FREEMARKER);
	}
	
	public FreeMarkerDocHelperTypeHandler(String type, Charset charset, String fmDocChainId) {
		super(type, MODULE, MIME, charset);
		this.fmDocChainId = fmDocChainId;
		this.escapeTextAsHtml = ATT_ESCAPE_TEXT_AS_HTML_DEFAULT;
	}

	@Getter private String fmDocChainId;

	@Getter @Setter private boolean escapeTextAsHtml;
	
	public FreeMarkerDocHelperTypeHandler withEscapeTextAsHtml( boolean value ) {
		this.setEscapeTextAsHtml(value);
		return this;
	}
	
	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		MiniFilterChain chain = FreeMarkerDocProcess.getInstance().getChainCache( this.getFmDocChainId() );
		DocProcessContext context = DocProcessContext.newContext()
				.withDocInput( docInput ).withAtt( ATT_ESCAPE_TEXT_AS_HTML ,this.isEscapeTextAsHtml() );
		DocProcessData data = new DocProcessData();
		chain.apply( context, data );
		StreamIO.pipeCharCloseBoth( data.getCurrentXmlReader() , new OutputStreamWriter( docOutput.getOs(), this.getCharset() ) );
	}

	@Override
	public void configure(Element tag) throws ConfigException {
		super.configure(tag);
		if ( tag != null ) {
			Properties atts = DOMUtils.attributesToProperties( tag );
			this.setEscapeTextAsHtml( BooleanUtils.isTrue( atts.getProperty( ATT_ESCAPE_TEXT_AS_HTML ) ) );
		}
	}
	
	
	
}