package org.fugerit.java.doc.freemarker.tool;

import java.io.InputStream;
import java.io.Writer;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.freemarker.config.FreeMarkerProcessStep;
import org.fugerit.java.doc.freemarker.helper.FreeMarkerDocProcess;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.fugerit.java.doc.freemarker.tool.model.ChainModel;
import org.fugerit.java.doc.freemarker.tool.model.ConfigModel;
import org.fugerit.java.doc.freemarker.tool.model.StepModel;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConvertConfig {
	
	public static final String ATT_CONFIG_MODEL = "configModel";
	
	public static void generate( InputStream is, Writer out, Properties params ) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware( true );
		DocumentBuilder parser = dbf.newDocumentBuilder();
		Document inputDoc = parser.parse( is );
		NodeList chainTagList = inputDoc.getDocumentElement().getElementsByTagName( "chain" );
		ConfigModel configModel = new ConfigModel();
		for ( int k=0; k<chainTagList.getLength(); k++ ) {
			Element currentChainTag = (Element) chainTagList.item( k );
			String chainId = currentChainTag.getAttribute( "id" );
			log.info( "current chain id {}", chainId );
			ChainModel chainModel = new ChainModel(chainId);
			configModel.getChainList().add(chainModel);
			NodeList stepTagList = currentChainTag.getElementsByTagName( "step" );
			for ( int i=0; i<stepTagList.getLength(); i++ ) {
				Element currentStepTag = (Element) stepTagList.item(i);
				String type = currentStepTag.getAttribute( "type" );
				String convertType = FreemarkerDocProcessConfigFacade.BUILT_IN_STEPS_REVERSE.getProperty( type, type );
				StepModel stepModel = new StepModel(convertType);
				chainModel.getStepList().add(stepModel);
				if ( FreemarkerDocProcessConfigFacade.STEP_TYPE_CONFIG.equalsIgnoreCase( convertType ) ) {
					stepModel.getAtts().put( "id" , currentStepTag.getAttribute( "param01" ) );
				} else if ( FreeMarkerProcessStep.class.getName().equalsIgnoreCase( convertType ) ) {
					stepModel.setType( FreemarkerDocProcessConfigFacade.STEP_TYPE_COMPLEX );
					stepModel.getAtts().put( "template-path" , currentStepTag.getAttribute( "param01" ) );
				}
				log.info( "current step type {}", stepModel.getType() );
				NodeList propertiesTagList = currentStepTag.getElementsByTagName( "properties" );
				if ( propertiesTagList.getLength() > 0 ) {
					Element propertyTag = (Element) propertiesTagList.item( 0 );
					NamedNodeMap attMap = propertyTag.getAttributes();
					for ( int j=0; j<attMap.getLength(); j++ ) {
						Attr currentAtt = (Attr)attMap.item( j );
						log.info( "current att {} -> {}", currentAtt.getName() , currentAtt.getValue() );
						stepModel.getAtts().put( currentAtt.getName() , currentAtt.getValue() );
					}
				}
			}
		}
		DocProcessData data = new DocProcessData();
		DocProcessContext context = DocProcessContext.newContext( GenerateStub.ATT_STUB_PARAMS, params ).withAtt( ATT_CONFIG_MODEL , configModel );
		FreeMarkerDocProcess.getInstance().process( GenerateStub.CONFIG_STUB_CHAIN_ID, context, data );
		StreamIO.pipeChar( data.getCurrentXmlReader() , out, StreamIO.MODE_CLOSE_IN_ONLY );
	}
	
}
