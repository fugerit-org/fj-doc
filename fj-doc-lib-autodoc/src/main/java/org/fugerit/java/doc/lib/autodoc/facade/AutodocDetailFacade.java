package org.fugerit.java.doc.lib.autodoc.facade;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.base.config.DocVersion;
import org.fugerit.java.doc.lib.autodoc.detail.model.AdAttribute;
import org.fugerit.java.doc.lib.autodoc.detail.model.AdChangelog;
import org.fugerit.java.doc.lib.autodoc.detail.model.AdElement;
import org.fugerit.java.doc.lib.autodoc.detail.model.AdInfo;
import org.fugerit.java.doc.lib.autodoc.detail.model.AdProperty;
import org.fugerit.java.doc.lib.autodoc.detail.model.AutodocDetail;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocAttribute;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocElement;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocUtils;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class AutodocDetailFacade {

	private static final AutodocDetailFacade INSTANCE = new AutodocDetailFacade();
	
	public static AutodocDetailFacade getInstance() {
		return INSTANCE;
	}
	
	private AutodocDetailFacade() {
		
	}
	
	public static final String PROP_OUTPUT_TITLE = "output-title";
	
	public static final String PROP_DOC_XSD_VERSION = "doc-xsd-version";
	
	public static final String PROP_AUTODOC_DETAIL_XSD_VERSION = "autodoc-detail-xsd-version";
	public static final String AUTODOC_DETAIL_XSD_CURRENT_VERSION = "1-0";
	
	public static final String PROP_AUTODOC_DETAIL_FACACE_VERSION = "autodoc-detail-facade-version";
	public static final String AUTODOC_DETAIL_FACADE_CURRENT_VERSION = "1.0.0";
	
	public static final String PROP_AUTODOC_DETAIL_MODULE_NAME = "autodoc-detail-module-name";
	public static final String PROP_AUTODOC_DETAIL_MODULE_VERSION = "autodoc-detail-module-version";
	
	private static final String PLACEHOLDRE_TOKEN = "FILL ME";
	
	private static AdProperty createProperty( String name, String description ) {
		AdProperty adProperty = new AdProperty();
		adProperty.setName( name );
		adProperty.setDescription( description );
		return adProperty;
	}
	
	public AutodocDetail populateStub( AutodocModel autodocModel ) {
		AutodocDetail detail = new AutodocDetail();
		// ad property section
		detail.getAdProperty().add( createProperty( PROP_OUTPUT_TITLE, PLACEHOLDRE_TOKEN ) );
		detail.getAdProperty().add( createProperty( PROP_DOC_XSD_VERSION, DocVersion.CURRENT_VERSION.stringVersion() ) );
		detail.getAdProperty().add( createProperty( PROP_AUTODOC_DETAIL_XSD_VERSION, AUTODOC_DETAIL_XSD_CURRENT_VERSION ) );
		detail.getAdProperty().add( createProperty( PROP_AUTODOC_DETAIL_FACACE_VERSION, AUTODOC_DETAIL_FACADE_CURRENT_VERSION ) );
		detail.getAdProperty().add( createProperty( PROP_AUTODOC_DETAIL_MODULE_NAME, PLACEHOLDRE_TOKEN ) );
		detail.getAdProperty().add( createProperty( PROP_AUTODOC_DETAIL_MODULE_VERSION, PLACEHOLDRE_TOKEN ) );
		// ad changelog section
		AdChangelog adChangelog = new AdChangelog();
		try {
			Calendar c = Calendar.getInstance();
			XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendarDate( c.get( Calendar.YEAR ), c.get( Calendar.MONTH )+1, c.get( Calendar.DAY_OF_MONTH ), 0 );
			
			adChangelog.setDate( date );
		} catch (DatatypeConfigurationException e) {
			throw new ConfigRuntimeException( e );
		}
		adChangelog.setVersion( "1.0.0" );
		adChangelog.getDescription().add( "Sample changelog entry (can be 1+)" );
		detail.getAdChangelog().add( adChangelog );
		// ad info section
		AdInfo adInfo = new AdInfo();
		adInfo.setName( PLACEHOLDRE_TOKEN );
		adInfo.setDescription( PLACEHOLDRE_TOKEN );
		adInfo.setDetail( PLACEHOLDRE_TOKEN );
		adInfo.setStatus( PLACEHOLDRE_TOKEN );
		detail.getAdInfo().add( adInfo );
		// ad element section
		for ( AutodocElement autodocElement : autodocModel.getElements() ) {
			AdElement adElement = new AdElement();
			adElement.setName( autodocElement.getName() );
			adElement.setDescription( AutodocUtils.annotationAsSingleStringHelper( autodocElement.getXsdAnnotationDeep() ) );
			adElement.setDetail( PLACEHOLDRE_TOKEN );
			adElement.setStatus( PLACEHOLDRE_TOKEN );
			detail.getAdElement().add( adElement );
			// ad attribute of the element
			for ( AutodocAttribute autodocAttribute : autodocElement.getAutodocAttributes() ) {
				AdAttribute adAttribute = new AdAttribute();
				adAttribute.setName( autodocAttribute.getName() );
				adAttribute.setDescription( AutodocUtils.annotationAsSingleStringHelper( autodocAttribute.getXsdAnnotationDeep() ) );
				adAttribute.setDetail( PLACEHOLDRE_TOKEN );
				adAttribute.setStatus( PLACEHOLDRE_TOKEN );
				adElement.getAdAttribute().add( adAttribute );
			}
		}
		return detail;
	}
	
	public void marshal( AutodocDetail autodocDetail, OutputStream os ) throws Exception {
		this.marshal( autodocDetail, os, true, true );
	}
	
	public void marshal( AutodocDetail autodocDetail, OutputStream os, boolean format, boolean addSchemaLocation ) throws Exception {
		JAXBContext jc = JAXBContext.newInstance( AutodocDetail.class );
		Marshaller marshaller = jc.createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
	    if ( addSchemaLocation ) {
	    	 marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://autodoc.fugerit.org, https://www.fugerit.org/data/java/doc/xsd/autodoc-detail-1-0.xsd");
	    }
		marshaller.marshal( autodocDetail , os );
	}
	
	public AutodocDetail unmarshal(InputStream is) throws Exception {
		JAXBContext jc = JAXBContext.newInstance(AutodocDetail.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		return (AutodocDetail) unmarshaller.unmarshal(is);
	}
	
}
