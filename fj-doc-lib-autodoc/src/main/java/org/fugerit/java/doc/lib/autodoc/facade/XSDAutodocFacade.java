package org.fugerit.java.doc.lib.autodoc.facade;

import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.lib.autodoc.model.XsdDocModel;
import org.fugerit.java.doc.lib.autodoc.model.XsdTagModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XSDAutodocFacade {

	private final static Logger logger = LoggerFactory.getLogger( XSDAutodocFacade.class );
	
	private void handleSimpleElement( XsdDocModel xsdDocModel, XsdTagModel elementTagModel, XsdTagModel typeTagModel,  Element typeTag ) {
		
	}
	
	private void handleComplexElement( XsdDocModel xsdDocModel, XsdTagModel elementTagModel, XsdTagModel typeTagModel,  Element typeTag ) {
		NodeList kids = typeTag.getChildNodes();
		for ( int k=0; k<kids.getLength(); k++ ) {
			
		}
	}
	
	private void handleElements( XsdDocModel xsdDocModel ) throws Exception {
		for ( XsdTagModel currentXsdTagModel : xsdDocModel.getElements() ) {
			Element currentTag = currentXsdTagModel.getElement();
			XsdTagModel complexTagModel = null;
			XsdTagModel simpleTagModel = null;
			Element complexType = null;
			Element simpleType = null;
			String type = currentTag.getAttribute( "type" );
			if ( StringUtils.isNotEmpty( type ) ) {
				complexTagModel = xsdDocModel.getComplexType( type );
				if ( complexTagModel != null ) {
					complexType = complexTagModel.getElement();
				} else {
					simpleTagModel = xsdDocModel.getSimpleType( type );
					if ( simpleTagModel != null ) {
						simpleType = simpleTagModel.getElement();
					}
				}
			} else {
				NodeList kids = currentTag.getChildNodes();
				for ( int k=0; k<kids.getLength(); k++ ) {
					Node currentNode = kids.item( k );
					if ( currentNode instanceof Element ) {
						Element currentKid = (Element) currentNode;
						if ( "complexType".equalsIgnoreCase( currentKid.getLocalName() ) ) {
							complexType = currentKid;
						} else if ( "simpleType".equalsIgnoreCase( currentKid.getLocalName() ) ) {
							simpleType = currentKid;
						}
					}
				}
			}
			if ( simpleType == null && complexType == null ) {
				throw new ConfigException( "No type found for element : "+currentXsdTagModel.getKey()+" [type:'"+type+"']" );
			} else {
				logger.info( "current element {} -> {} / {}", currentXsdTagModel.getKey(), simpleType, complexType );
				if ( complexType != null ) {
					this.handleComplexElement(xsdDocModel, currentXsdTagModel, complexTagModel, complexType );
				} else {
					this.handleSimpleElement(xsdDocModel, currentXsdTagModel, simpleTagModel, simpleType);
				}
			}
		}
	}
	
	public XsdDocModel parse( Reader xsdReader ) throws ConfigException {
		XsdDocModel model = null;
		try {
			model = new XsdDocModel();
			model.setTypePrefix( "doc:" );
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
			factory.setNamespaceAware( true );
	        DocumentBuilder parser = factory.newDocumentBuilder();
			Document document = parser.parse( new InputSource( xsdReader ) );
			Element root = document.getDocumentElement();
			NodeList children = root.getChildNodes();
			for ( int k=0; k<children.getLength(); k++ ) {
				Node currentKid = children.item( k );
				if ( currentKid instanceof Element ) {
					Element currentElement = (Element) children.item( k );
					String type = currentElement.getLocalName();
					if ( type.equals( "element" ) ) {
						XsdTagModel xsdTagModel = model.addElement( currentElement );
						logger.debug( "current element name : {}", xsdTagModel.getKey() );
					} else if ( type.equals( "complexType" ) ) {
						XsdTagModel xsdTagModel = model.addCompleType( currentElement );
						logger.debug( "current complex type name : {}", xsdTagModel.getKey() );
					} else if ( type.equals( "simpleType" ) ) {
						XsdTagModel xsdTagModel = model.addSimpleType( currentElement );
						logger.debug( "current simple type name : {}", xsdTagModel.getKey() );
					} else {
						throw new ConfigException( "Element not handled : "+type );
					}
				}
			}
			this.handleElements(model);
		} catch (Exception e ) {
			throw new ConfigException( "Error parsing xsd : "+e, e );
		}
		return model;
	}
	
}
