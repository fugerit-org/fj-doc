package org.fugerit.java.doc.mod.fop;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.io.ResourceResolver;
import org.apache.xmlgraphics.util.MimeConstants;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.function.UnsafeConsumer;
import org.fugerit.java.core.function.UnsafeSupplier;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.mvn.MavenProps;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.core.xml.dom.DOMUtils;
import org.fugerit.java.doc.base.config.*;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.mod.fop.config.FopConfigClassLoaderWrapper;
import org.fugerit.java.doc.mod.fop.utils.ConfigUtils;
import org.fugerit.java.doc.mod.fop.utils.PoolUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PdfFopTypeHandler extends FreeMarkerFopTypeHandler {

	public static final DocTypeHandler HANDLER = new PdfFopTypeHandler();

	public static final String ATT_FOP_CONFIG_MODE = "fop-config-mode";
	public static final String ATT_FOP_CONFIG_MODE_DEFAULT = "default";
	public static final String ATT_FOP_CONFIG_MODE_CLASS_LOADER = "classloader";
	public static final String ATT_FOP_CONFIG_MODE_INLINE = "inline";

	private static final String ATT_FOP_CONFIG_MODE_CLASS_LOADER_LEGACY = "classloader-legacy";  // removed as of v2.0.1

	public static final String ATT_FOP_CONFIG_CLASSLOADER_PATH = "fop-config-classloader-path";

	public static final String ATT_FOP_CONFIG_RESOLVER_TYPE 		= "fop-config-resolver-type";

	public static final String ATT_FOP_CONFIG_RESOLVER_TYPE_DEFAULT = FopConfigClassLoaderWrapper.DEFAULT_RESOURCE_RESOLVER.getClass().getName();

	public static final String ATT_PDF_A_MODE = "pdf-a-mode";
	public static final String ATT_PDF_A_MODE_PDF_A_1A = DocConfig.FORMAT_PDF_A_1A;
	public static final String ATT_PDF_A_MODE_PDF_A_1B = DocConfig.FORMAT_PDF_A_1B;
	public static final String ATT_PDF_A_MODE_PDF_A_2A = DocConfig.FORMAT_PDF_A_2A;
	public static final String ATT_PDF_A_MODE_PDF_A_3A = DocConfig.FORMAT_PDF_A_3A;

	public static final String ATT_PDF_UA_MODE = "pdf-ua-mode";
	public static final String ATT_PDF_UA_MODE_PDF_UA_1 = DocConfig.FORMAT_PDF_UA_1;

	private static final String[] VALID_PDF_A = { ATT_PDF_A_MODE_PDF_A_1A, ATT_PDF_A_MODE_PDF_A_1B, ATT_PDF_A_MODE_PDF_A_2A };
	public static final List<String> VALID_PDF_A_MODES = Collections.unmodifiableList( Arrays.asList( VALID_PDF_A ) );

	private static final String[] VALID_PDF_UA = { ATT_PDF_UA_MODE_PDF_UA_1 };
	public static final List<String> VALID_PDF_UA_MODES = Collections.unmodifiableList( Arrays.asList( VALID_PDF_UA ) );

	private static final String ATT_FONT_BASE_CLASSLOADER_PATH = "font-base-classloader-path";  // removed as of v2.0.1

	public static final String ATT_ACCESSIBILITY = "accessibility";
	public static final boolean DEFAULT_ACCESSIBILITY = true;

	public static final String ATT_KEEP_EMPTY_TAGS  = "keep-empty-tags";
	public static final boolean DEFAULT_KEEP_EMPTY_TAGS = false;

	public static final String ATT_FOP_SUPPRESS_EVENTS = "fop-suppress-events";

	public static final String ATT_FOP_POOL_MIN = "fop-pool-min";

	public static final String ATT_FOP_POOL_MAX = "fop-pool-max";

	private static final String FOP_CONFIG_ROOT = "fop";

    private static final boolean LEGACY_CLASS_LOADER_MODE_CONFIG_HANDLER_FAIL_WITH_EXCEPTION = Boolean.TRUE;

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

	@Getter @Setter private boolean suppressEvents;

	@Getter @Setter private transient int fopPoolMin;

	@Getter @Setter private transient int fopPoolMax;

	private transient List<FopConfigWrap> pool;

	private transient UnsafeSupplier<FopConfigWrap, ConfigException> fopWrapSupplier;

	private transient UnsafeConsumer<FopConfigWrap, ConfigException> fopWrapConsumer;

	private static String getModuleVersion() {
		return VenusVersion.getFjDocModuleVersionS( "fj-doc-mod-fop");
	}

	private static String getApacheFOPVersion() {
		return MavenProps.getProperty( "org.apache.xmlgraphics", "fop", MavenProps.VERSION );
	}

	private static final String PRODUCER_DEFAULT = String.format( VenusVersion.VENUS_PRODUCER_FORMAT, DocConfig.FUGERIT_VENUS_DOC , getModuleVersion() , "Apache FOP", getApacheFOPVersion() );

	public PdfFopTypeHandler( Charset charset, FopConfig fopConfig, boolean accessibility, boolean keepEmptyTags ) {
		super( DocConfig.TYPE_PDF, charset );
		this.fopConfig = fopConfig;
		this.accessibility = accessibility;
		this.keepEmptyTags = keepEmptyTags;
		this.pool = Collections.synchronizedList( new ArrayList<>() );
		this.fopWrapSupplier = this::newFopWrap;
		this.fopWrapConsumer = fc -> {};
		this.fopPoolMin = 0;
		this.fopPoolMax = 0;
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

	private FopConfigWrap newFopWrap() throws ConfigException {
		// create an instance of fop factory
		FopFactory fopFactory = this.getFopConfig().newFactory();
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		if ( this.isSuppressEvents() ) {
			foUserAgent.getEventBroadcaster().addEventListener( e -> {} );
		}
		if ( StringUtils.isNotEmpty( this.getPdfAMode() ) ) {
			foUserAgent.getRendererOptions().put( ATT_PDF_A_MODE, this.getPdfAMode() );
		}
		if ( StringUtils.isNotEmpty( this.getPdfUAMode() ) ) {
			foUserAgent.getRendererOptions().put( ATT_PDF_UA_MODE, this.getPdfUAMode() );
		}
		foUserAgent.setAccessibility( this.isAccessibility() );
		foUserAgent.setKeepEmptyTags( this.isKeepEmptyTags() );
		foUserAgent.setProducer( PRODUCER_DEFAULT );
		foUserAgent.setCreator( VenusVersion.VENUS_CREATOR );
		return new FopConfigWrap( fopFactory, foUserAgent );
	}

	private FopConfigWrap handleFopWrap(FopConfigWrap toRelease ) throws ConfigException {
		return PoolUtils.handleFopWrap( toRelease, this.pool, this.getFopPoolMin(), this.getFopPoolMax(), this::newFopWrap );
	}

	private byte[] getXlsFoContent(DocInput docInput) throws Exception {
		try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
			DocOutput bufferOutput = DocOutput.newOutput( buffer );
			super.handle(docInput, bufferOutput);
			return buffer.toByteArray();
		}
	}

	private Transformer newTransformer( TransformerFactory factory, DocBase docBase ) throws TransformerConfigurationException, IOException {
		String xsltPath = docBase.getStableInfo().getProperty( FopHelperConstants.INFO_KEY_MOD_FOP_XSLT_PATH );
		log.debug( "newTransformer {} -> {}", FopHelperConstants.INFO_KEY_MOD_FOP_XSLT_PATH, xsltPath );
		if ( StringUtils.isNotEmpty( xsltPath ) ) {
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( xsltPath ) ) {
				return factory.newTransformer( new StreamSource( is ) );
			}
		} else {
			return factory.newTransformer();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		try ( ByteArrayInputStream input = new ByteArrayInputStream(this.getXlsFoContent(docInput) ) ) {
			// the XML file which provides the input
			StreamSource xmlSource = new StreamSource( new InputStreamReader( input, this.getCharset() ) );
			FopConfigWrap fopWrap = this.fopWrapSupplier.get();
			Fop fop = fopWrap.getFopFactory().newFop(MimeConstants.MIME_PDF, fopWrap.getFoUserAgent(), docOutput.getOs());
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = this.newTransformer( factory, docInput.getDoc() );
			Result res = new SAXResult(fop.getDefaultHandler());
			transformer.transform(xmlSource, res);
			this.fopWrapConsumer.accept( fopWrap );
		}
	}

	@Override
	protected void handleConfigTag(Element config) throws ConfigException {
		super.handleConfigTag(config);
		Properties props = DOMUtils.attributesToProperties( config );
		String fopConfigMode = props.getProperty( ATT_FOP_CONFIG_MODE );
		String fopConfigClassloaderPath = props.getProperty( ATT_FOP_CONFIG_CLASSLOADER_PATH );
		String fopConfigResolverType = props.getProperty( ATT_FOP_CONFIG_RESOLVER_TYPE, ATT_FOP_CONFIG_RESOLVER_TYPE_DEFAULT );
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
				this.setFormat( StringUtils.concat( "_", Arrays.asList( pdfAModConfig, pdfUAModConfig ).stream().filter( s -> s!=null ).collect(Collectors.toList()) ) );
			} else {
				throw new ConfigException( ATT_PDF_A_MODE+" not valid : "+pdfAModConfig+"( valid modes are : "+VALID_PDF_A_MODES+")" );
			}
		}
		// legacy class loader mode
		if ( StringUtils.isEmpty( fopConfigMode ) && StringUtils.isNotEmpty( fopConfigClassloaderPath ) && StringUtils.isNotEmpty( fontBaseClassloaderPath ) ) {
            ConfigUtils.LEGACY_CLASS_LOADER_MODE_CONFIG_HANDLER.accept( LEGACY_CLASS_LOADER_MODE_CONFIG_HANDLER_FAIL_WITH_EXCEPTION );
		}
		// setup fop config mode
		this.setupFopConfigMode(fopConfigMode, fopConfigResolverType, fopConfigClassloaderPath, config);
		// setup suppress events
		this.setSuppressEvents( BooleanUtils.isTrue( props.getProperty( ATT_FOP_SUPPRESS_EVENTS ) ) );
		// setup pool
		this.setupPool( props );
		// extra setup
		this.extraSetup( props );
	}

	private void setupPool( Properties props ) throws ConfigException {
		if ( StringUtils.isNotEmpty( props.getProperty( ATT_FOP_POOL_MIN ) ) ) {
			this.setFopPoolMin( Integer.parseInt( props.getProperty( ATT_FOP_POOL_MIN ) ) );
			for ( int k=0; k<this.getFopPoolMin(); k++ ) {
				this.pool.add( this.newFopWrap() );
			}
			this.fopWrapSupplier = () -> this.handleFopWrap( null );
			this.fopWrapConsumer = this::handleFopWrap;
		}
		if ( StringUtils.isNotEmpty( props.getProperty( ATT_FOP_POOL_MAX ) ) ) {
			this.setFopPoolMax( Integer.parseInt( props.getProperty( ATT_FOP_POOL_MAX ) ) );
		} else if ( this.getFopPoolMin() > 0 ) {
			this.setFopPoolMax( this.getFopPoolMin() );
		}
	}

	private void extraSetup( Properties props ) throws ConfigException {
		String valueAccessibility = props.getProperty( ATT_ACCESSIBILITY );
		if ( StringUtils.isNotEmpty( valueAccessibility ) ) {
			log.debug( "override accessibility {} -> {}", this.isAccessibility(), valueAccessibility );
			this.accessibility = BooleanUtils.isTrue( valueAccessibility );
		}
		String valueKeepEnptyTags = props.getProperty( ATT_KEEP_EMPTY_TAGS);
		if ( StringUtils.isNotEmpty( valueKeepEnptyTags ) ) {
			log.debug( "override keep-empty-tags {} -> {}", this.isKeepEmptyTags(), valueKeepEnptyTags );
			this.keepEmptyTags = BooleanUtils.isTrue( valueKeepEnptyTags );
		}
	}

	private void setupFopConfigMode( String fopConfigMode, String fopConfigResoverType, String fopConfigClassloaderPath, Element config ) throws ConfigException {
		if ( ATT_FOP_CONFIG_MODE_CLASS_LOADER.equalsIgnoreCase( fopConfigMode ) ) {
			ConfigException.apply( () -> {
				ResourceResolver customResourceResolver = (ResourceResolver) ClassHelper.newInstance( fopConfigResoverType );
				FopConfigClassLoaderWrapper fopConfigClassLoaderWrapper = new FopConfigClassLoaderWrapper(fopConfigClassloaderPath, customResourceResolver);
				this.fopConfig = fopConfigClassLoaderWrapper;
			} );
		} else if ( ATT_FOP_CONFIG_MODE_INLINE.equalsIgnoreCase( fopConfigMode ) ) {
			ConfigException.apply( () -> {
				NodeList nl = config.getElementsByTagName( FOP_CONFIG_ROOT );
				if ( nl.getLength() != 1 ) {
					throw new ConfigException( String.format( "wrong number of tag : %s - %s" , FOP_CONFIG_ROOT, nl.getLength() ) );
				}
				byte[] fopConfigInlineData = null;
				try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
					DOMIO.writeDOM( nl.item( 0 ) , baos);
					fopConfigInlineData = baos.toByteArray();
				}
				ResourceResolver customResourceResolver = (ResourceResolver) ClassHelper.newInstance( fopConfigResoverType );
				FopConfigClassLoaderWrapper fopConfigClassLoaderWrapper = new FopConfigClassLoaderWrapper(fopConfigInlineData, customResourceResolver);
				this.fopConfig = fopConfigClassLoaderWrapper;
			} );
		} else if ( ATT_FOP_CONFIG_MODE_CLASS_LOADER_LEGACY.equalsIgnoreCase( fopConfigMode ) ) {
            ConfigUtils.LEGACY_CLASS_LOADER_MODE_CONFIG_HANDLER.accept( LEGACY_CLASS_LOADER_MODE_CONFIG_HANDLER_FAIL_WITH_EXCEPTION );
		}
	}

	@Override
	public String toString() {
		return super.toString()+String.format( "[pdfAmode=%s,pdfUAmode=,%s,poolMin=%s,poolMax=%s]", this.getPdfAMode(), this.getPdfUAMode(), this.getFopPoolMin(), this.getFopPoolMax() );
	}

}

