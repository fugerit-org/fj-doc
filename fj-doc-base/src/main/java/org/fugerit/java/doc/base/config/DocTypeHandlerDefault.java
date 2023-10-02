package org.fugerit.java.doc.base.config;

import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.ConfigurableObject;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.base.helper.DefaultMimeHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocTypeHandlerDefault implements DocTypeHandler, ConfigurableObject, Serializable {

	@Override
	public void configureProperties(InputStream source) throws ConfigException {
		throw new ConfigException( "Property configuration not supported!" );
	}

	@Override
	public void configureXML(InputStream source) throws ConfigException {
		try {
			this.configure( DOMIO.loadDOMDoc( source ).getDocumentElement() );
		} catch (Exception e) {
			throw new ConfigException( e );
		}
	}

	@Override
	public void configure(Properties props) throws ConfigException {
		throw new ConfigException( "Property configuration not supported!" );
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5024985828785381015L;

	public static final String TAG_NAME_CONFIG = "config";
	
	public static final String TAG_NAME_CONFIG_ALT = "docHandlerCustomConfig";
	
	public static final String ATT_NAME_CHARSET = "charset";
	
	private String type;
	
	private String module;
	
	private String mime;
	
	@Setter(value = AccessLevel.PROTECTED) private String format;
	
	private transient Charset charset;
	
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
		return createKey( this.getFormat() , this.getModule() ) ;
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
	public String getFormat() {
		return StringUtils.valueWithDefault( this.format , this.getType() );
	}
		
	@Override
	public Charset getCharset() {
		return charset;
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		// this is a do nothing implementation
		// sub classes should always override it
	}
	
	public DocTypeHandlerDefault(String type, String module, String mime, Charset charset) {
		this( type, module, mime, charset, null );
	}
	
	public DocTypeHandlerDefault(String type, String module, String mime, Charset charset, String format) {
		super();
		this.type = type;
		this.module = module;
		this.mime = mime;
		this.format = format;
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
		// this is a do nothing implementation
		// sub classes should override it if needed
	}
	
	private Element lookupConfig( Element tag, String tagName ) {
		Element configTag = null;
		NodeList nl = tag.getElementsByTagName( tagName );
		if ( nl.getLength() > 0 ) {
			configTag = (Element)nl.item( 0 );
			String charsetAtt = configTag.getAttribute( ATT_NAME_CHARSET );
			if ( StringUtils.isNotEmpty( charsetAtt ) ) {
				this.charset = Charset.forName( charsetAtt );
			}
		}
		return configTag;
	}
	
	@Override
	public void configure(Element tag) throws ConfigException {
		log.info( "configure : {}", tag.getAttribute( "id" ) );
		Element configTag = this.lookupConfig(tag, TAG_NAME_CONFIG_ALT );
		if ( configTag == null ) {
			configTag = this.lookupConfig(tag, TAG_NAME_CONFIG );
		}
		if ( configTag != null ) {
			this.handleConfigTag(configTag);	
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+" [type=" + type + ", module=" + module + ", format=" + format + "]";
	}
	
}
