package org.fugerit.java.doc.base.config;

import java.io.Serializable;
import java.nio.charset.Charset;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.helpers.XMLConfigurableObject;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.helper.DefaultMimeHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DocTypeHandlerDefault extends XMLConfigurableObject implements DocTypeHandler, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5024985828785381015L;

	public static final String TAG_NAME_CONFIG = "config";
	
	public static final String ATT_NAME_CHARSET = "charset";
	
	private String type;
	
	private String module;
	
	private String mime;
	
	private Charset charset;
	
	@Override
	public String getMime() {
		String res = this.mime;
		if ( res == null ) {
			res = DefaultMimeHelper.getDefaultMime( this.getType() );
		}
		return res;
	}

	@Override
	public String getKey() {
		return createKey( this.getType() , this.getModule() ) ;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public String getModule() {
		return module;
	}

	@Override
	public Charset getCharset() {
		return charset;
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		
	}
	
	public DocTypeHandlerDefault(String type, String module, String mime, Charset charset) {
		super();
		this.type = type;
		this.module = module;
		this.mime = mime;
		this.charset = DocCharsetProvider.getDefaultProvider().resolveCharset(charset);
	}
	
	public DocTypeHandlerDefault(String type, String module, String mime) {
		this( type, module, mime, null );
	}

	public DocTypeHandlerDefault(String type, String module ) {
		this( type, module, null );
	}
	
	public static final String createKey( String type, String mod ) {
		return type+"-"+mod;
	}

	protected void handleConfigTag( Element config ) throws ConfigException {
		
	}
	
	@Override
	public void configure(Element tag) throws ConfigException {
		NodeList nl = tag.getElementsByTagName( TAG_NAME_CONFIG );
		if ( nl.getLength() > 0 ) {
			Element config = (Element)nl.item( 0 );
			String charsetAtt = config.getAttribute( ATT_NAME_CHARSET );
			if ( StringUtils.isNotEmpty( charsetAtt ) ) {
				this.charset = Charset.forName( charsetAtt );
			}
			this.handleConfigTag(config);
		}
	}

}
