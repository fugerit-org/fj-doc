package org.fugerit.java.doc.mod.fop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.io.ResourceResolver;
import org.apache.xmlgraphics.util.MimeConstants;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.xml.dom.DOMUtils;
import org.fugerit.java.doc.base.config.DocCharsetProvider;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.mod.fop.config.FopConfigClassLoader;
import org.fugerit.java.doc.mod.fop.config.FopConfigClassLoaderWrapper;
import org.w3c.dom.Element;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PdfFopTypeHandler extends FreeMarkerFopTypeHandler {

	public static final DocTypeHandler HANDLER = new PdfFopTypeHandler();
	
	public static final String ATT_FOP_CONFIG_MODE = "fop-config-mode";
	public static final String ATT_FOP_CONFIG_MODE_DEFAULT = "default";
	public static final String ATT_FOP_CONFIG_MODE_CLASS_LOADER = "classloader";
	

	/**
	 * @deprecated planned for removal in version 1.6 (see https://github.com/fugerit-org/fj-doc/issues/7)
	 */
	@Deprecated
	public static final String ATT_FOP_CONFIG_MODE_CLASS_LOADER_LEGACY = "classloader-legacy";
	
	public static final String ATT_FOP_CONFIG_CLASSLOADER_PATH = "fop-config-classloader-path";
	
	public static final String ATT_FOP_CONFIG_RESOLVER_TYPE 		= "fop-config-resover-type";

	public static final String ATT_FOP_CONFIG_RESOLVER_TYPE_DEFAULT = FopConfigClassLoaderWrapper.DEFAULT_RESOURCE_RESOLVER.getClass().getName();
	
	public static final String ATT_PDF_A_MODE = "pdf-a-mode";
	public static final String ATT_PDF_A_MODE_PDF_A_1A = DocConfig.FORMAT_PDF_A_1A;
	public static final String ATT_PDF_A_MODE_PDF_A_1B = DocConfig.FORMAT_PDF_A_1B;

	public static final String ATT_PDF_UA_MODE = "pdf-ua-mode";
	public static final String ATT_PDF_UA_MODE_PDF_UA_1 = DocConfig.FORMAT_PDF_UA_1;
	
	private static final String[] VALID_PDF_A = { ATT_PDF_A_MODE_PDF_A_1A, ATT_PDF_A_MODE_PDF_A_1B };
	public static final List<String> VALID_PDF_A_MODES = Arrays.asList( VALID_PDF_A );
	
	private static final String[] VALID_PDF_UA = { ATT_PDF_UA_MODE_PDF_UA_1 };
	public static final List<String> VALID_PDF_UA_MODES = Arrays.asList( VALID_PDF_UA );
	
	/**
	 * @deprecated planned for removal in version 1.6 (see https://github.com/fugerit-org/fj-doc/issues/7)
	 */
	@Deprecated
	public static final String ATT_FONT_BASE_CLASSLOADER_PATH = "font-base-classloader-path";
	
	public static final boolean DEFAULT_ACCESSIBILITY = true;
	
	public static final boolean DEFAULT_KEEP_EMPTY_TAGS = false;

	/**
	 * 
	 */
	private static final long serialVersionUID = -7394516771708L;

	@Getter private boolean accessibility;
	
	@Getter private boolean keepEmptyTags;
	
	private Serializable fopConfig;

	public FopConfig getFopConfig() {
		return (FopConfig)fopConfig;
	}

	public void setFopConfig(FopConfig fopConfig) {
		this.fopConfig = fopConfig;
	}

	@Getter @Setter private String pdfAMode;
	
	@Getter @Setter private String pdfUAMode;
	
	public PdfFopTypeHandler( Charset charset, FopConfig fopConfig, boolean accessibility, boolean keepEmptyTags ) {
		super( DocConfig.TYPE_PDF, charset );
		this.fopConfig = fopConfig;
		this.accessibility = accessibility;
		this.keepEmptyTags = keepEmptyTags;
	}
	
	public PdfFopTypeHandler( Charset charset, FopConfig fopConfig ) {
		this( charset, fopConfig, DEFAULT_ACCESSIBILITY, DEFAULT_KEEP_EMPTY_TAGS );
	}
	
	public PdfFopTypeHandler( FopConfig fopConfig, boolean accessibility, boolean keepEmptyTags ) {
		this( DocCharsetProvider.getDefaultProvider().resolveCharset( null ), fopConfig, accessibility, keepEmptyTags );
	}
	
	public PdfFopTypeHandler( Charset charset, boolean accessibility, boolean keepEmptyTags ) {
		this( charset, FopConfigDefault.DEFAULT, accessibility, keepEmptyTags );
	}
	
	public PdfFopTypeHandler( Charset charset ) {
		this( charset, DEFAULT_ACCESSIBILITY, DEFAULT_KEEP_EMPTY_TAGS );
	}
	
	public PdfFopTypeHandler( boolean accessibility, boolean keepEmptyTags ) {
		this( FopConfigDefault.DEFAULT, accessibility, keepEmptyTags );
	}
	
	public PdfFopTypeHandler() {
		this( DEFAULT_ACCESSIBILITY, DEFAULT_KEEP_EMPTY_TAGS );
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DocOutput bufferOutput = DocOutput.newOutput( buffer );
		super.handle(docInput, bufferOutput);
		// the XML file which provides the input
		StreamSource xmlSource = new StreamSource( new InputStreamReader( new ByteArrayInputStream( buffer.toByteArray() ), this.getCharset() ) );
		// create an instance of fop factory
		FopFactory fopFactory = this.getFopConfig().newFactory();
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		if ( StringUtils.isNotEmpty( this.getPdfAMode() ) ) {
			foUserAgent.getRendererOptions().put( ATT_PDF_A_MODE, this.getPdfAMode() );	
		}
		if ( StringUtils.isNotEmpty( this.getPdfUAMode() ) ) {
			foUserAgent.getRendererOptions().put( ATT_PDF_UA_MODE, this.getPdfAMode() );	
		}
		foUserAgent.setAccessibility( this.isAccessibility() );
		foUserAgent.setKeepEmptyTags( this.isKeepEmptyTags() );
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, docOutput.getOs());
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		Result res = new SAXResult(fop.getDefaultHandler());
		transformer.transform(xmlSource, res);
	}

	@Override
	protected void handleConfigTag(Element config) throws ConfigException {
		super.handleConfigTag(config);
		Properties props = DOMUtils.attributesToProperties( config );
		String fopConfigMode = props.getProperty( ATT_FOP_CONFIG_MODE );
		String fopConfigClassloaderPath = props.getProperty( ATT_FOP_CONFIG_CLASSLOADER_PATH );
		String fopConfigResoverType = props.getProperty( ATT_FOP_CONFIG_RESOLVER_TYPE, ATT_FOP_CONFIG_RESOLVER_TYPE_DEFAULT );
		String fontBaseClassloaderPath = props.getProperty( ATT_FONT_BASE_CLASSLOADER_PATH );
		String pdfUAModConfig = props.getProperty( ATT_PDF_UA_MODE );
		// config pdf ua
		if ( StringUtils.isNotEmpty( pdfUAModConfig ) ) {
			log.info( "pdf ua mode -> {} : {}", ATT_PDF_UA_MODE, pdfUAModConfig );
			if ( VALID_PDF_UA_MODES.contains( pdfUAModConfig ) ) {
				this.setPdfUAMode( pdfUAModConfig );
				this.setFormat( pdfUAModConfig );	
			} else {
				throw new ConfigException( ATT_PDF_UA_MODE+" not valid : "+pdfUAModConfig+"( valid modes are : "+VALID_PDF_A_MODES+")" );
			}
		}
		String pdfAModConfig = props.getProperty( ATT_PDF_A_MODE );
		// config pdf a
		if ( StringUtils.isNotEmpty( pdfAModConfig ) ) {
			log.info( "pdf a mode -> {} : {}", ATT_PDF_A_MODE, pdfAModConfig );
			if ( VALID_PDF_A_MODES.contains( pdfAModConfig ) ) {
				this.setPdfAMode( pdfAModConfig );
				this.setFormat( pdfAModConfig );	
			} else {
				throw new ConfigException( ATT_PDF_A_MODE+" not valid : "+pdfAModConfig+"( valid modes are : "+VALID_PDF_A_MODES+")" );
			}
		}
		// legacy class loader mode
		if ( StringUtils.isEmpty( fopConfigMode ) && StringUtils.isNotEmpty( fopConfigClassloaderPath ) && StringUtils.isNotEmpty( fontBaseClassloaderPath ) ) {
			fopConfigMode = ATT_FOP_CONFIG_MODE_CLASS_LOADER_LEGACY;
			log.warn( "Activated legacy ClassLoader mode. It is strongly recomended to update te configuration {} -> {}", ATT_FOP_CONFIG_MODE_CLASS_LOADER_LEGACY, FopConfigClassLoader.MIN_VERSION_NEW_CLASSLOADER_MODE );
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
			FopConfigClassLoader fopConfigClassLoader = new FopConfigClassLoader(fopConfigClassloaderPath, fontBaseClassloaderPath);
			this.fopConfig = fopConfigClassLoader;
		}
	}
	
}