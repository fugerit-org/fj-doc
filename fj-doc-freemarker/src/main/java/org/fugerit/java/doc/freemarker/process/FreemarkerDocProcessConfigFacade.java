package org.fugerit.java.doc.freemarker.process;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.cfg.ConfigurableObject;
import org.fugerit.java.core.cfg.helpers.UnsafeHelper;
import org.fugerit.java.core.cfg.xml.XmlBeanHelper;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.filterchain.MiniFilter;
import org.fugerit.java.core.util.filterchain.MiniFilterBase;
import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.core.xml.dom.DOMUtils;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.freemarker.config.FreeMarkerComplexProcessStep;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;
import org.fugerit.java.doc.freemarker.config.FreeMarkerFunctionStep;
import org.fugerit.java.doc.freemarker.config.FreeMarkerMapStep;
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
	
	public static final String STEP_TYPE_CONFIG = "config";
	
	public static final String STEP_TYPE_FUNCTION = "function";
	
	public static final String STEP_TYPE_COMPLEX = "complex";
	
	public static final String STEP_TYPE_MAP = "map";
	
	public static FreemarkerDocProcessConfig newSimpleConfig( String id, String templatePath ) throws ConfigException {
		FreemarkerDocProcessConfig config = new FreemarkerDocProcessConfig();
		config.setDefaultChain(
				new  DefaultChainProvider() {
					@Override
					public MiniFilterChain newDefaultChain(String id) {
						MiniFilterChain defaultChain = new MiniFilterChain( "DEFAULT_CHAIN_"+id+"_"+System.currentTimeMillis(), MiniFilter.CONTINUE );
						defaultChain.setChainId( defaultChain.getKey() );
						// config step
						FreeMarkerConfigStep configStep = new FreeMarkerConfigStep();
						Properties configParams = new Properties();
						configParams.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_PATH , templatePath );
						configStep.setParam01( id );
						configStep.setCustomConfig( convertConfiguration( configParams ) );
						defaultChain.getFilterChain().add( configStep );
						// default step
						FreeMarkerComplexProcessStep processStep = new FreeMarkerComplexProcessStep();
						Properties processAtts = new Properties();
						processAtts.setProperty( "template-path" , "${chainId}.ftl" );
						processAtts.setProperty( "map-atts" , "simpleTableModel" );
						processStep.setCustomConfig( processAtts );
						processStep.setChainId( id );
						defaultChain.getFilterChain().add( processStep );
						return defaultChain;
					}
				}
		);
		return config;
	}
	
	private static DocTypeHandler createHelper( Element docHandlerConfig ) throws ConfigException {
		String type = docHandlerConfig.getAttribute( "type" );
		log.info( "factoryType : {} , resultType : {}", docHandlerConfig, type );
		DocTypeHandler res = null;
		try {
			res = (DocTypeHandler)ClassHelper.newInstance( type );
			if ( res instanceof ConfigurableObject ) {
				log.info( "ConfigurableObject -> try configure()" );
				((ConfigurableObject)res).configure( (Element)docHandlerConfig );
			}
		} catch (Exception | NoClassDefFoundError e) {
			UnsafeHelper.handleUnsafe( new ConfigException( "Type cannot be loaded : "+e, e ), docHandlerConfig.getAttribute( "unsafe"), docHandlerConfig.getAttribute( "unsafeMode") );	
		}
		return res;
	}
	
	public static FreemarkerDocProcessConfig loadConfigSafe( String configPath ) {
		log.info( "loadConfigSafe config path : {}", configPath );
		FreemarkerDocProcessConfig config = null;
		try ( Reader xmlReader = new InputStreamReader(StreamHelper.resolveStream( configPath ) ) ) {
			config = loadConfig(xmlReader);
		} catch (Exception e) {
			throw new ConfigRuntimeException( e );
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
	
	private static void handleChainStepList( DocChainModel model, Element currentTag ) throws Exception {
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
					 throw new ConfigException( "Expcted only one config tag : "+configList.getLength() );
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
	
	private static void handleStepList( FreemarkerDocProcessConfig config, Document doc ) throws Exception {
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
	
	private static void handleMiniFilter( FreemarkerDocProcessConfig config ) throws Exception {
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
			 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			 dbf.setNamespaceAware( true );
			 DocumentBuilder db = dbf.newDocumentBuilder();
			 Document doc = db.parse( new InputSource( xmlReader ) );
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
	
	private static final Properties BUILT_IN_STEPS = new Properties();
	public static final Properties BUILT_IN_STEPS_REVERSE = new Properties();
	static {
		BUILT_IN_STEPS.setProperty( STEP_TYPE_CONFIG , FreeMarkerConfigStep.class.getName() );
		BUILT_IN_STEPS.setProperty( STEP_TYPE_FUNCTION , FreeMarkerFunctionStep.class.getName() );
		BUILT_IN_STEPS.setProperty( STEP_TYPE_COMPLEX , FreeMarkerComplexProcessStep.class.getName() );
		BUILT_IN_STEPS.setProperty( STEP_TYPE_MAP , FreeMarkerMapStep.class.getName() );
		BUILT_IN_STEPS.keySet().stream().forEach( k -> BUILT_IN_STEPS_REVERSE.put( BUILT_IN_STEPS.get( k ) , k ) );
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
				 props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_CLASS, FreemarkerDocProcessConfigFacade.class.getName() ) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER , 
				 props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER, FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_DEFAULT ) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_LOG_EXCEPTION , 
				 props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_LOG_EXCEPTION, FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_LOG_EXCEPTION_DEFAULT ) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_WRAP_UNCHECKED_EXCEPTION , 
				 props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_WRAP_UNCHECKED_EXCEPTION, FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_WRAP_UNCHECKED_EXCEPTION_DEFAULT ) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_FALLBACK_ON_NULL_LOOP_VARIABLE , 
				 props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_FALLBACK_ON_NULL_LOOP_VARIABLE, FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_FALLBACK_ON_NULL_LOOP_VARIABLE_DEFAULT ) );
		 return params;
	}
	
}

class FreemarkerConfigHelper extends FreeMarkerConfigStep {

	private static final long serialVersionUID = 8824282827447873553L;
	
	protected static Configuration getConfig( String key, Properties config ) throws Exception {
		return FreeMarkerConfigStep.getConfig(key, config);
	}
	
}

