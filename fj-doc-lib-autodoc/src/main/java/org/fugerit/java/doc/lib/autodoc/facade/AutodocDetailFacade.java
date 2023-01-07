package org.fugerit.java.doc.lib.autodoc.facade;

import java.io.OutputStream;

import org.fugerit.java.doc.lib.autodoc.detail.model.AdAttribute;
import org.fugerit.java.doc.lib.autodoc.detail.model.AdElement;
import org.fugerit.java.doc.lib.autodoc.detail.model.AutodocDetail;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocAttribute;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocElement;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

public class AutodocDetailFacade {

	public AutodocDetail populateStub( AutodocModel autodocModel ) {
		AutodocDetail detail = new AutodocDetail();
		for ( AutodocElement autodocElement : autodocModel.getElements() ) {
			AdElement adElement = new AdElement();
			adElement.setName( autodocElement.getName() );
			detail.getAdElement().add( adElement );
			for ( AutodocAttribute autodocAttribute : autodocElement.getAutodocAttributes() ) {
				AdAttribute adAttribute = new AdAttribute();
				adAttribute.setName( autodocAttribute.getName() );
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
	
}
