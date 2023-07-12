package org.fugerit.java.doc.freemarker.process;

import java.io.Reader;
import java.util.Enumeration;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.xml.XmlBeanHelper;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.dom.DOMUtils;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FreemarkerDocProcessConfigFacade {

	public static FreemarkerDocProcessConfig newSimpleConfig( String id, String templatePath ) throws ConfigException {
		FreemarkerDocProcessConfig config = new FreemarkerDocProcessConfig();
		ConfigInitModel model = new ConfigInitModel();
		model.setId(id);
		model.setPath( templatePath );
		try {
			addConfiguration(model);
		} catch (Exception e) {
			throw new ConfigException( "Error configuring FreemarkerDocProcessConfig : "+e , e );
		}
		config.getConfigInitList().add(model);
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
			 NodeList configInitList = doc.getElementsByTagName( "configInit" );
			 for ( int k=0; k<configInitList.getLength(); k++ ) {
				 Element currentTag = (Element) configInitList.item( k );
				 ConfigInitModel model = new ConfigInitModel();
				 XmlBeanHelper.setFromElement( model, currentTag );
				 config.getConfigInitList().add(model);
				 addConfiguration(model);
				 // functions map
				 NodeList functionsMap = currentTag.getElementsByTagName( "functionsMap" );
				 if ( functionsMap.getLength() > 0 ) {
					 for ( int i=0; i<functionsMap.getLength(); i++ ) {
						 Element currentFM = (Element)functionsMap.item(i);
						 Properties currentFMProps = DOMUtils.attributesToProperties( currentFM );
						 Enumeration<Object> efm = currentFMProps.keys();
						 while ( efm.hasMoreElements() ) {
							 String key = efm.nextElement().toString();
							 String value = currentFMProps.getProperty( key );
							 model.getGeneralContext().put( key , ClassHelper.newInstance(value) );
						 }
					 }
				 }
			 }
			 NodeList docChainLisgt = doc.getElementsByTagName( "docChain" );
			 for ( int k=0; k<docChainLisgt.getLength(); k++ ) {
				 Element currentTag = (Element) docChainLisgt.item( k );
				 DocChainModel model = new DocChainModel();
				 XmlBeanHelper.setFromElement( model, currentTag );
				 // attributes mapping
				 if ( DocChainModel.MAP_ATTS_ENUM.equalsIgnoreCase( model.getMapAtts() ) ) {
					 Element mapAttsEnumTag = (Element)currentTag.getElementsByTagName( "mapAttsEnum" ).item( 0 );
					 model.setMapAttsEnum( DOMUtils.attributesToProperties( mapAttsEnumTag ) );
					 log.debug( "chain att enum {} -> {}", model.getId(), model.getMapAttsEnum() );
				 }
				 // chain step
				 NodeList chainStepList = currentTag.getElementsByTagName( "chainStep" );
				 for ( int i=0; i<chainStepList.getLength(); i++ ) {
					 Element currentChainStepTag = (Element) chainStepList.item(i);
					 ChainStepModel chainStepModel = new ChainStepModel();
					 XmlBeanHelper.setFromElement( chainStepModel, currentChainStepTag );
					 model.getChainStepList().add(chainStepModel);
				 }
				 config.getDocChainList().add(model);
			 }
			 result = config;
			 log.info( "loadConfig ok : {}", result );
		 } catch (Exception e) {
			 throw new ConfigException( "Error configuring FreemarkerDocProcessConfig : "+e , e );
		 }
		 return result;
	}
	
	private static void addConfiguration( ConfigInitModel model ) throws Exception {
		 Properties params = new Properties();
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION , model.getVersion() );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_MODE , model.getMode() );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_PATH , model.getPath() );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_CLASS , model.getClassName() );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER , model.getExceptionHandler() );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_LOG_EXCEPTION , model.getLogException() );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_WRAP_UNCHECKED_EXCEPTION , model.getWrapUncheckedExceptions() );
		 params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_FALLBACK_ON_NULL_LOOP_VARIABLE , model.getFallbackOnNullLoopVariable() );
		 Configuration conf = FreemarkerConfigHelper.getConfig( model.getId(), params );
		 model.setFreemarkerConfiguration( conf );
	}
	
}

class FreemarkerConfigHelper extends FreeMarkerConfigStep {

	private static final long serialVersionUID = 8824282827447873553L;
	
	protected static Configuration getConfig( String key, Properties config ) throws Exception {
		return FreeMarkerConfigStep.getConfig(key, config);
	}
	
}
