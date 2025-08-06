package org.fugerit.java.doc.freemarker.process;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.cfg.ConfigurableObject;
import org.fugerit.java.core.cfg.helpers.UnsafeHelper;
import org.fugerit.java.core.cfg.xml.XmlBeanHelper;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.filterchain.MiniFilter;
import org.fugerit.java.core.util.filterchain.MiniFilterBase;
import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.core.xml.dom.DOMUtils;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;
import org.fugerit.java.doc.freemarker.config.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FreemarkerDocProcessConfigFacade {

	private FreemarkerDocProcessConfigFacade() {}
	
	public static final String ATT_DOC_HANDLER_CONFIG = "docHandlerConfig";
	
	public static final String ATT_DOC_CHAIN = "docChain";
	
	public static final String ATT_CHAIN_STEP = "chainStep";
	
	public static final String ATT_STEP_TYPE = "stepType";

	/**
	 * Corresponding to type : org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep
	 */
	public static final String STEP_TYPE_CONFIG = "config";

	/**
	 * Corresponding to type : org.fugerit.java.doc.freemarker.config.FreeMarkerFunctionStep
	 */
	public static final String STEP_TYPE_FUNCTION = "function";

	/**
	 * Corresponding to type : org.fugerit.java.doc.freemarker.config.FreeMarkerComplexProcessStep
	 */
	public static final String STEP_TYPE_COMPLEX = "complex";

	/**
	 * Corresponding to type : org.fugerit.java.doc.freemarker.config.FreeMarkerSkipProcessStep
	 */
	public static final String STEP_TYPE_SKIPFM = "skipfm";

	/**
	 * Corresponding to type : org.fugerit.java.doc.freemarker.config.FreeMarkerKotlinProcessStep
	 */
	public static final String STEP_TYPE_KOTLIN = "kotlin";


	/**
	 * Corresponding to type : org.fugerit.java.doc.freemarker.config.FreeMarkerMapStep
	 */
	public static final String STEP_TYPE_MAP = "map";

	/**
	 * If set to 'true' the FreemarkerDocProcessConfig will try to validate the source. (default: false)
	 * NOTE: if active, source reader will be buffered, potentially resulting in higher memory usage.
	 */
	public static final String GENERAL_ATTRIBUTE_VALIDATING = "validating";

	/**
	 * If set to 'true' the FreemarkerDocProcessConfig will fail in case of validation errors, if 'false' will just print the result as warning.
	 * NOTE: NOTE: 'validating' is set to true, this attribute is ignored.
	 */
	public static final String GENERAL_ATTRIBUTE_FAIL_ON_VALIDATE = "failOnValidate";

	/**
	 * If set to 'true' the FreemarkerDocProcessConfig will try to clean the source (i.e. DocXmlUtils.cleanXml()). (default: false)
	 * NOTE: if active, source reader will be buffered, potentially resulting in higher memory usage.
	 */
	public static final String GENERAL_ATTRIBUTE_CLEAN_SOURCE = "cleanSource";

	/**
	 * If set to 'true' the FreemarkerDocProcessConfig will try to check table integrity. (default: false)
	 */
	public static final String GENERAL_ATTRIBUTE_TABLE_CHECK_INTEGRITY = GenericConsts.DOC_TABLE_CHECK_INTEGRITY;

	public static final String ERROR_CONFIG_PATH_NOT_FOUND_BASE_MESSAGE = "FreemarkerDocProcessConfig configuration path not found";

	public static final Function<Throwable, ConfigRuntimeException> EX_CONSUMER_LOAD_CONFIG = e -> {
		Throwable ex = e;
		while ( ex.getCause() != null ) {
			ex = ex.getCause();
		}
		log.warn( "****************************************************************************" );
		log.warn( "* Configuration error (going to throw a ConfigRuntimeException)," );
		log.warn( "* Original exception is {} : {} *", ex.getClass().getName(), ex.getMessage() );
		log.warn( "****************************************************************************" );
		return new ConfigRuntimeException(e);
	};

	public static FreemarkerDocProcessConfig newSimpleConfigMode( String id, String templatePath, String mode ) throws ConfigException {
		return newSimpleConfig( id, templatePath, null, mode );
	}

	public static FreemarkerDocProcessConfig newSimpleConfig( String id, String templatePath, String version ) throws ConfigException {
		return newSimpleConfig( id, templatePath, version, null );
	}

	public static FreemarkerDocProcessConfig newSimpleConfig( String id, String templatePath, String version, String mode ) throws ConfigException {
		return ConfigException.get( () -> {
			FreemarkerDocProcessConfig config = new FreemarkerDocProcessConfig();
			config.setDefaultChain(
					idChain -> {
						MiniFilterChain defaultChain = new MiniFilterChain( "DEFAULT_CHAIN_"+idChain+"_"+System.currentTimeMillis(), MiniFilter.CONTINUE );
						defaultChain.setChainId( defaultChain.getKey() );
						// config step
						FreeMarkerConfigStep configStep = new FreeMarkerConfigStep();
						Properties configParams = new Properties();
						configParams.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_PATH , templatePath );
						SafeFunction.applyIfNotNull( mode, () -> configParams.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_MODE , mode ) );
						SafeFunction.applyIfNotNull( version, () -> configParams.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION , version ) );
						configStep.setParam01( id );
						configStep.setCustomConfig( convertConfiguration( configParams ) );
						defaultChain.getFilterChain().add( configStep );
						// default step
						FreeMarkerComplexProcessStep processStep = new FreeMarkerComplexProcessStep();
						Properties processAtts = new Properties();
						processAtts.setProperty( "template-path" , "${chainId}.ftl" );
						processAtts.setProperty( FreeMarkerComplexProcessStep.ATT_MAP_ALL , BooleanUtils.BOOLEAN_TRUE );
						processStep.setCustomConfig( processAtts );
						processStep.setChainId( idChain );
						defaultChain.getFilterChain().add( processStep );
						return defaultChain;
					}
			);
			return config;	
		} );
	}
	
	public static FreemarkerDocProcessConfig newSimpleConfig( String id, String templatePath ) throws ConfigException {
		return newSimpleConfig(id, templatePath, null );
	}
	
	private static DocTypeHandler createHelper( Element docHandlerConfig ) throws ConfigException {
		String type = docHandlerConfig.getAttribute( "type" );
		log.info( "factoryType : {} , resultType : {}", docHandlerConfig, type );
		DocTypeHandler res = null;
		try {
			res = (DocTypeHandler)ClassHelper.newInstance( type );
			if ( res instanceof ConfigurableObject ) {
				log.info( "ConfigurableObject -> try configure()" );
				((ConfigurableObject)res).configure( docHandlerConfig );
			}
		} catch (Exception | NoClassDefFoundError e) {
			UnsafeHelper.handleUnsafe( new ConfigException( "Type cannot be loaded : "+e, e ), docHandlerConfig.getAttribute( "unsafe"), docHandlerConfig.getAttribute( "unsafeMode") );	
		}
		return res;
	}
	
	public static FreemarkerDocProcessConfig loadConfigSafe( String configPath ) {
		log.info( "loadConfigSafe config path : {}", configPath );
		FreemarkerDocProcessConfig config = null;
		try (InputStream is = StreamHelper.resolveStream( configPath ) ) {
			if ( is != null ) {
				try ( Reader xmlReader = new InputStreamReader( is ) ) {
					config = loadConfig(xmlReader);
				}
			} else {
				throw new ConfigRuntimeException( String.format( "%s, configPath : %s", ERROR_CONFIG_PATH_NOT_FOUND_BASE_MESSAGE, configPath ) );
			}
		} catch (Exception | NoClassDefFoundError | ExceptionInInitializerError e) {
			throw EX_CONSUMER_LOAD_CONFIG.apply( e );
		}
		return config;
	}
	
	private static void handleNodeList( FreemarkerDocProcessConfig config, Document doc ) throws Exception {
		 NodeList docHandlerConfigList = doc.getElementsByTagName( ATT_DOC_HANDLER_CONFIG );
		 if ( docHandlerConfigList.getLength() == 1 ) {
			 Element docHandlerConfigTag = (Element) docHandlerConfigList.item( 0 );
			 boolean registerById = BooleanUtils.isTrue( docHandlerConfigTag.getAttribute( "registerById" ) );
			 boolean allowDuplicatedId = BooleanUtils.isTrue( docHandlerConfigTag.getAttribute( "allowDuplicatedId" ) );
			 NodeList docHandlerList = docHandlerConfigTag.getElementsByTagName( "docHandler" );
			 log.info( "docHandlerList -> {}", docHandlerList.getLength() );
			 for ( int k=0; k<docHandlerList.getLength(); k++ ) {
				 Element currentHandlerTag = (Element)docHandlerList.item( k );
				 DocTypeHandler handler = createHelper( currentHandlerTag );
				 if ( handler != null ) {
					 String id = currentHandlerTag.getAttribute( "id" );
					 log.info( "register handler id {}, handler {}", id, handler );
					 if ( registerById ) {
						 config.getFacade().registerHandlerAndId( id, handler, allowDuplicatedId );
					 } else {
						 config.getFacade().registerHandler( handler); 
					 }
				 }
			 }
		 }
	}
	
	private static void handleChainStepList( DocChainModel model, Element currentTag ) throws ConfigException {
		NodeList chainStepList = currentTag.getElementsByTagName( ATT_CHAIN_STEP );
		 for ( int i=0; i<chainStepList.getLength(); i++ ) {
			 Element currentChainStepTag = (Element) chainStepList.item(i);
			 ChainStepModel chainStepModel = new ChainStepModel();
			 Properties atts = DOMUtils.attributesToProperties( currentChainStepTag );
			 chainStepModel.setStepType( atts.getProperty( ATT_STEP_TYPE ) );
			 atts.remove( ATT_STEP_TYPE );
			 chainStepModel.setAttributes(atts);
			 if ( STEP_TYPE_CONFIG.equalsIgnoreCase( chainStepModel.getStepType() ) ) {
				 NodeList configList = currentChainStepTag.getElementsByTagName( STEP_TYPE_CONFIG );
				 if ( configList.getLength() != 1 ) {
					 throw new ConfigException( "Expected only one config tag : "+configList.getLength() );
				 } else {
					 Element configTag = (Element)configList.item( 0 );
					 atts.putAll( DOMUtils.attributesToProperties( configTag ) );
				 }
			 } else if ( STEP_TYPE_FUNCTION.equalsIgnoreCase( chainStepModel.getStepType() ) 
					 ||  STEP_TYPE_MAP.equalsIgnoreCase( chainStepModel.getStepType() )  ) {
				 NodeList subList = currentChainStepTag.getElementsByTagName( chainStepModel.getStepType() );
				 for ( int j=0; j<subList.getLength(); j++ ) {
					 Element currentFunctionTag = (Element)subList.item(j);
					 String key = currentFunctionTag.getAttribute( "name" );
					 String value = currentFunctionTag.getAttribute( "value" );
					 atts.setProperty(key, value);
				 }
			 }
			 model.getChainStepList().add(chainStepModel);
		 }
	}
	
	private static void handleStepList( FreemarkerDocProcessConfig config, Document doc ) throws DocException, ConfigException {
		 NodeList docChainLisgt = doc.getElementsByTagName( ATT_DOC_CHAIN );
		 for ( int k=0; k<docChainLisgt.getLength(); k++ ) {
			 Element currentTag = (Element) docChainLisgt.item( k );
			 DocChainModel model = new DocChainModel();
			 XmlBeanHelper.setFromElement( model, currentTag );
			 if ( StringUtils.isNotEmpty( model.getParent() ) ) {
				 String[] parentList = model.getParent().split( "," );
				 for ( int p=0; p<parentList.length; p++) {
					 DocChainModel parent = config.getDocChainList().get( parentList[p] );
					 if ( parent == null ) {
						 throw new DocException( "No parent found : "+model.getParent() );
					 } else {
						 model.getChainStepList().addAll( parent.getChainStepList() );
					 }	 
				 }
			 }
			 // chain step
			 handleChainStepList(model, currentTag);
			 config.getDocChainList().add(model);
		 }
	}
	
	private static void handleMiniFilter( FreemarkerDocProcessConfig config ) throws ClassNotFoundException, NoSuchMethodException, ConfigException {
		for ( DocChainModel docChainModel : config.getDocChainList() ) {
			 MiniFilterChain chain = new MiniFilterChain( docChainModel.getId(), MiniFilter.CONTINUE );
			 chain.setChainId( docChainModel.getId() );
			 for ( ChainStepModel chainStepModel : docChainModel.getChainStepList() ) {
				 String type = BUILT_IN_STEPS.getProperty( chainStepModel.getStepType(), chainStepModel.getStepType() );
				 MiniFilterBase step = (MiniFilterBase) ClassHelper.newInstance( type );
				 step.setCustomConfig( chainStepModel.getAttributes() );
				 if ( FreeMarkerConfigStep.class.getName().equalsIgnoreCase( type ) ) {
					step.setParam01( chainStepModel.getAttributes().getProperty( "id" ) );
					Properties configProps = convertConfiguration( chainStepModel.getAttributes() ) ;
					step.setCustomConfig( configProps );
				 }
				 step.setChainId( chain.getChainId() );
				 chain.getFilterChain().add( step );
			 }
			 config.setChain(chain.getChainId(), chain);
		 }
	}
	
	public static FreemarkerDocProcessConfig loadConfig( Reader xmlReader ) throws ConfigException {
		FreemarkerDocProcessConfig result = null;
		 try {
			 FreemarkerDocProcessConfig config = new FreemarkerDocProcessConfig();
			 DocumentBuilderFactory dbf = DOMIO.newSafeDocumentBuilderFactory();
			 dbf.setNamespaceAware( true );
			 DocumentBuilder db = dbf.newDocumentBuilder();
			 Document doc = db.parse( new InputSource( xmlReader ) );
			 // config general attributes
			 generalPropertiesSetup( config, doc.getDocumentElement() );
			 // docHandlerConfig reading
			 handleNodeList(config, doc);
			 // docChain reading
			 handleStepList(config, doc);
			 result = config;
			 log.info( "loadConfig ok : {}", result );
			 // populate mini filter chain model
			 handleMiniFilter(config);
		 } catch (Exception e) {
			 throw new ConfigException( "Error configuring FreemarkerDocProcessConfig : "+e , e );
		 }
		 return result;
	}

	private static String setupGeneralAttribute( String name, Element element ) {
		String value = element.getAttribute( name );
		log.info( "setupGeneralAttribute : name : {} value : {}", name, value );
		return value;
	}

	private static void generalPropertiesSetup( FreemarkerDocProcessConfig config , Element root) {
		config.setValidating( BooleanUtils.isTrue(  setupGeneralAttribute( GENERAL_ATTRIBUTE_VALIDATING, root ) ) );
		config.setFailOnValidate( BooleanUtils.isTrue(  setupGeneralAttribute( GENERAL_ATTRIBUTE_FAIL_ON_VALIDATE, root ) ) );
		config.setCleanSource( BooleanUtils.isTrue(  setupGeneralAttribute( GENERAL_ATTRIBUTE_CLEAN_SOURCE, root ) ) );
		config.setTableCheckIntegrity( setupGeneralAttribute( GENERAL_ATTRIBUTE_TABLE_CHECK_INTEGRITY, root ) );
		config.initDocInputProcess();
	}

	private static final Properties BUILT_IN_STEPS = new Properties();
	private static final Properties BUILT_IN_STEPS_REVERSE = new Properties();
	static {
		BUILT_IN_STEPS.setProperty( STEP_TYPE_CONFIG , FreeMarkerConfigStep.class.getName() );
		BUILT_IN_STEPS.setProperty( STEP_TYPE_FUNCTION , FreeMarkerFunctionStep.class.getName() );
		BUILT_IN_STEPS.setProperty( STEP_TYPE_COMPLEX , FreeMarkerComplexProcessStep.class.getName() );
		BUILT_IN_STEPS.setProperty( STEP_TYPE_SKIPFM , FreeMarkerSkipProcessStep.class.getName() );
		BUILT_IN_STEPS.setProperty( STEP_TYPE_MAP , FreeMarkerMapStep.class.getName() );
		BUILT_IN_STEPS.setProperty( STEP_TYPE_KOTLIN , FreeMarkerKotlinStep.class.getName() );
		BUILT_IN_STEPS.keySet().stream().forEach( k -> BUILT_IN_STEPS_REVERSE.put( BUILT_IN_STEPS.get( k ) , k ) );
	}
	public static Properties getBuiltInStepsReverse() {
		return BUILT_IN_STEPS_REVERSE;
	}
	
	private static Properties convertConfiguration( Properties props ) {
		 Properties params = new Properties();
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION , 
				 props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION, FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION_DEFAULT ) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_MODE , 
				 props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_MODE, FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_MODE_CLASS ) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_PATH , 
				 props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_PATH ) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_CLASS , 
				 props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_CLASS, DocChainModel.class.getName() ) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER , 
				 props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER, FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_DEFAULT ) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_LOG_EXCEPTION , 
				 props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_LOG_EXCEPTION, FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_LOG_EXCEPTION_DEFAULT ) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_WRAP_UNCHECKED_EXCEPTION , 
				 props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_WRAP_UNCHECKED_EXCEPTION, FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_WRAP_UNCHECKED_EXCEPTION_DEFAULT ) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_FALLBACK_ON_NULL_LOOP_VARIABLE , 
				 props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_FALLBACK_ON_NULL_LOOP_VARIABLE, FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_FALLBACK_ON_NULL_LOOP_VARIABLE_DEFAULT ) );
		params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_LOAD_BUNDLED_FUN ,
				props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_LOAD_BUNDLED_FUN, BooleanUtils.BOOLEAN_FALSE ) );
		 return params;
	}
	
}
