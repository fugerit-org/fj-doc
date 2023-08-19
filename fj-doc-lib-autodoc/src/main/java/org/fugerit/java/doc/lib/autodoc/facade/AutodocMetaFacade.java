package org.fugerit.java.doc.lib.autodoc.facade;

import java.io.InputStream;
import java.io.OutputStream;

import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.lib.autodoc.detail.model.AutodocDetail;
import org.fugerit.java.doc.lib.autodoc.meta.model.AutodocMeta;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class AutodocMetaFacade {

	private static final AutodocMetaFacade INSTANCE = new AutodocMetaFacade();
	
	public static AutodocMetaFacade getInstance() {
		return INSTANCE;
	}
	
	private AutodocMetaFacade() {
		
	}
	
	public static final String PROP_OUTPUT_TITLE = "output-title";
	
	public void marshal( AutodocMeta autodocMeta, OutputStream os ) throws DocException {
		this.marshal( autodocMeta, os, true, true );
	}
	
	public void marshal( AutodocMeta autodocMeta, OutputStream os, boolean format, boolean addSchemaLocation ) throws DocException {
		try {
			JAXBContext jc = JAXBContext.newInstance( AutodocDetail.class );
			Marshaller marshaller = jc.createMarshaller();
		    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
		    if ( addSchemaLocation ) {
		    	 marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "https://autodocmeta.fugerit.org, https://www.fugerit.org/data/java/doc/xsd/autodoc-detail-1-0.xsd");
		    }
			marshaller.marshal( autodocMeta , os );	
		} catch (Exception e) {
			throw DocException.convertExMethod( "marshal" , e );
		}
	}
	
	public AutodocMeta unmarshal(InputStream is) throws DocException {
		try {
			JAXBContext jc = JAXBContext.newInstance(AutodocMeta.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			return (AutodocMeta) unmarshaller.unmarshal(is);
		} catch (Exception e) {
			throw DocException.convertExMethod( "unmarshal" , e );
		}
	}
	
}
