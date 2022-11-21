package org.fugerit.java.doc.mod.fop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.xmlgraphics.io.ResourceResolver;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.xml.dom.DOMUtils;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.mod.fop.config.ClassLoaderResourceResolver;
import org.fugerit.java.doc.mod.fop.config.ClassLoaderResourceResolverWrapper;
import org.fugerit.java.doc.mod.fop.config.FopConfigClassLoader;
import org.fugerit.java.doc.mod.fop.config.FopConfigClassLoaderWrapper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PdfFopTypeHandler extends FreeMarkerFopTypeHandler {

	public static final DocTypeHandler HANDLER = new PdfFopTypeHandler();
	
	public static final String ATT_FOP_CONFIG_MODE = "fop-config-mode";
	public static final String ATT_FOP_CONFIG_MODE_DEFAULT = "default";
	public static final String ATT_FOP_CONFIG_MODE_CLASS_LOADER = "classloader";
	@Deprecated
	public static final String ATT_FOP_CONFIG_MODE_CLASS_LOADER_LEGACY = "classloader-legacy";
	
	public static final String ATT_FOP_CONFIG_CLASSLOADER_PATH = "fop-config-classloader-path";
	
	public static final String ATT_FOP_CONFIG_RESOLVER_TYPE 		= "fop-config-resover-type";
	public static final String ATT_FOP_CONFIG_RESOLVER_TYPE_DEFAULT = ClassLoaderResourceResolverWrapper.class.getName();
	
	@Deprecated
	public static final String ATT_FONT_BASE_CLASSLOADER_PATH = "font-base-classloader-path";
	
	public static final boolean DEFAULT_ACCESSIBILITY = true;
	
	public static final boolean DEFAULT_KEEP_EMPTY_TAGS = false;
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -7394516771708L;

	private boolean accessibility;
	
	private boolean keepEmptyTags;
	
	private FopConfig fopConfig;
	
	public PdfFopTypeHandler( FopConfig fopConfig, boolean accessibility, boolean keepEmptyTags ) {
		super( DocConfig.TYPE_PDF );
		this.fopConfig = fopConfig;
		this.accessibility = accessibility;
		this.keepEmptyTags = keepEmptyTags;
	}
	
	public PdfFopTypeHandler( boolean accessibility, boolean keepEmptyTags ) {
		this( FopConfigDefault.DEFAULT, accessibility, keepEmptyTags );
	}
	
	public PdfFopTypeHandler() {
		this( DEFAULT_ACCESSIBILITY, DEFAULT_KEEP_EMPTY_TAGS );
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DocOutput bufferOutput = DocOutput.newOutput( buffer );
		super.handle(docInput, bufferOutput);
		// the XML file which provides the input
		StreamSource xmlSource = new StreamSource( new InputStreamReader( new ByteArrayInputStream( buffer.toByteArray() ) ) );
		// create an instance of fop factory
		FopFactory fopFactory = this.fopConfig.newFactory();
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		foUserAgent.setAccessibility( this.isAccessibility() );
		foUserAgent.setKeepEmptyTags( this.isKeepEmptyTags() );
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, docOutput.getOs());
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		Result res = new SAXResult(fop.getDefaultHandler());
		transformer.transform(xmlSource, res);
	}

	public boolean isAccessibility() {
		return accessibility;
	}

	public boolean isKeepEmptyTags() {
		return keepEmptyTags;
	}

	public FopConfig getFopConfig() {
		return fopConfig;
	}

	public void setFopConfig(FopConfig fopConfig) {
		this.fopConfig = fopConfig;
	}

	@Override
	public void configure(Element tag) throws ConfigException {
		NodeList nl = tag.getElementsByTagName( "config" );
		if ( nl.getLength() > 0 ) {
			Element config = (Element)nl.item( 0 );
			Properties props = DOMUtils.attributesToProperties( config );
			String fopConfigMode = props.getProperty( ATT_FOP_CONFIG_MODE );
			String fopConfigClassloaderPath = props.getProperty( ATT_FOP_CONFIG_CLASSLOADER_PATH );
			String fopConfigResoverType = props.getProperty( ATT_FOP_CONFIG_RESOLVER_TYPE, ATT_FOP_CONFIG_RESOLVER_TYPE_DEFAULT );
			String fontBaseClassloaderPath = props.getProperty( ATT_FONT_BASE_CLASSLOADER_PATH );
			// legacy class loader mode
			if ( StringUtils.isEmpty( fopConfigMode ) && StringUtils.isNotEmpty( fopConfigClassloaderPath ) && StringUtils.isNotEmpty( fontBaseClassloaderPath ) ) {
				fopConfigMode = ATT_FOP_CONFIG_MODE_CLASS_LOADER_LEGACY;
				logger.warn( "Activated legacy ClassLoader mode. It is strongly recomended to update te configuration {} -> {}", ATT_FOP_CONFIG_MODE_CLASS_LOADER_LEGACY, ClassLoaderResourceResolver.MIN_VERSION_NEW_CLASSLOADER_MODE );
			}
			if ( ATT_FOP_CONFIG_MODE_CLASS_LOADER.equalsIgnoreCase( fopConfigMode ) ) {
				try {
					ResourceResolver customResourceResolver = (ResourceResolver) ClassHelper.newInstance( fopConfigResoverType );
					FopConfigClassLoaderWrapper fopConfigClassLoaderWrapper = new FopConfigClassLoaderWrapper(fopConfigClassloaderPath, customResourceResolver);
					this.fopConfig = fopConfigClassLoaderWrapper;	
				} catch (Exception e) {
					throw new ConfigException( PdfFopTypeHandler.class.getSimpleName()+" configuration error : "+e, e );
				}
			} else if ( ATT_FOP_CONFIG_MODE_CLASS_LOADER_LEGACY.equalsIgnoreCase( fopConfigMode ) ) {
				ClassLoaderResourceResolver customResourceResolver = new ClassLoaderResourceResolver( fontBaseClassloaderPath );
				FopConfigClassLoader fopConfigClassLoader = new FopConfigClassLoader(fopConfigClassloaderPath, customResourceResolver);
				this.fopConfig = fopConfigClassLoader;
			}
		}
	}
	
}