package org.fugerit.java.doc.freemarker.process;

import java.io.Reader;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.xml.XmlBeanHelper;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.filterchain.MiniFilterBase;
import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.core.xml.dom.DOMUtils;
import org.fugerit.java.doc.base.config.DocException;
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

	public static final String ATT_DOC_CHAIN = "docChain";
	
	public static final String ATT_CHAIN_STEP = "chainStep";
	
	public static final String STEP_TYPE_CONFIG = "config";
	
	public static final String STEP_TYPE_FUNCTION = "function";
	
	public static final String STEP_TYPE_MAP = "map";
	
	public static FreemarkerDocProcessConfig newSimpleConfig( String id, String templatePath ) throws ConfigException {
		FreemarkerDocProcessConfig config = new FreemarkerDocProcessConfig();
		config.setDefaultChain(
				new  DefaultChainProvider() {
					@Override
					public MiniFilterChain newDefaultChain(String id) {
						MiniFilterChain defaultChain = new MiniFilterChain( "DEFAULT_CHAIN_"+id+"_"+System.currentTimeMillis(), MiniFilterChain.CONTINUE );
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
	
	public static FreemarkerDocProcessConfig loadConfig( Reader xmlReader ) throws ConfigException {
		FreemarkerDocProcessConfig result = null;
		 try {
			 FreemarkerDocProcessConfig config = new FreemarkerDocProcessConfig();
			 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			 dbf.setNamespaceAware( true );
			 DocumentBuilder db = dbf.newDocumentBuilder();
			 Document doc = db.parse( new InputSource( xmlReader ) );
			 // docChain reading
			 NodeList docChainLisgt = doc.getElementsByTagName( ATT_DOC_CHAIN );
			 for ( int k=0; k<docChainLisgt.getLength(); k++ ) {
				 Element currentTag = (Element) docChainLisgt.item( k );
				 DocChainModel model = new DocChainModel();
				 XmlBeanHelper.setFromElement( model, currentTag );
				 if ( StringUtils.isNotEmpty( model.getParent() ) ) {
					 DocChainModel parent = config.getDocChainList().get( model.getParent() );
					 if ( parent == null ) {
						 throw new DocException( "No parent found : "+model.getParent() );
					 } else {
						 model.getChainStepList().addAll( parent.getChainStepList() );
					 }
				 }
				 // chain step
				 NodeList chainStepList = currentTag.getElementsByTagName( ATT_CHAIN_STEP );
				 for ( int i=0; i<chainStepList.getLength(); i++ ) {
					 Element currentChainStepTag = (Element) chainStepList.item(i);
					 ChainStepModel chainStepModel = new ChainStepModel();
					 Properties atts = DOMUtils.attributesToProperties( currentChainStepTag );
					 chainStepModel.setStepType( atts.getProperty( "stepType" ) );
					 atts.remove( "stepType" );
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
				 config.getDocChainList().add(model);
			 }
			 result = config;
			 log.info( "loadConfig ok : {}", result );
			 // populate mini filter chain model
			 for ( DocChainModel docChainModel : config.getDocChainList() ) {
				 MiniFilterChain chain = new MiniFilterChain( docChainModel.getId(), MiniFilterChain.CONTINUE );
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
				 config.addAdditionalChain(chain);
			 }
		 } catch (Exception e) {
			 throw new ConfigException( "Error configuring FreemarkerDocProcessConfig : "+e , e );
		 }
		 return result;
	}
	
	private static final Properties BUILT_IN_STEPS = new Properties();
	static {
		BUILT_IN_STEPS.setProperty( STEP_TYPE_CONFIG , FreeMarkerConfigStep.class.getName() );
		BUILT_IN_STEPS.setProperty( STEP_TYPE_FUNCTION , FreeMarkerFunctionStep.class.getName() );
		BUILT_IN_STEPS.setProperty( "complex" , FreeMarkerComplexProcessStep.class.getName() );
		BUILT_IN_STEPS.setProperty( "map" , FreeMarkerMapStep.class.getName() );
	}
	
	private static Properties convertConfiguration( Properties props ) {
		 Properties params = new Properties();
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION , props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION, ConfigInitModel.DEFAULT_VERSION ) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_MODE , props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_MODE, ConfigInitModel.DEFAULT_MODE) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_PATH , props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_PATH ) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_CLASS , props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_CLASS, ConfigInitModel.DEFAULT_CLASS_NAME) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER , props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER, ConfigInitModel.DEFAULT_EXCEPTION_HANDLER) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_LOG_EXCEPTION , props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_LOG_EXCEPTION, ConfigInitModel.DEFAULT_LOG_EXCEPTION) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_WRAP_UNCHECKED_EXCEPTION , props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_WRAP_UNCHECKED_EXCEPTION, ConfigInitModel.DEFAULT_WRAP_UNCHECKED_EXCEPTION) );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_FALLBACK_ON_NULL_LOOP_VARIABLE , props.getProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_FALLBACK_ON_NULL_LOOP_VARIABLE, ConfigInitModel.DEFAULT_FALL_BACK_ON_NULL_LOOP_VARIABLE) );
		 return params;
	}
	
}

class FreemarkerConfigHelper extends FreeMarkerConfigStep {

	private static final long serialVersionUID = 8824282827447873553L;
	
	protected static Configuration getConfig( String key, Properties config ) throws Exception {
		return FreeMarkerConfigStep.getConfig(key, config);
	}
	
}

