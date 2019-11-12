package org.fugerit.java.doc.base.config;

import java.io.Reader;

import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.model.DocBase;

public class DocInput {

	private String type;
	
	private DocBase doc;
	
	private Reader reader;

	public Reader getReader() {
		return reader;
	}

	public String getType() {
		return type;
	}

	public DocBase getDoc() {
		DocBase res = doc;
		if ( res == null && reader != null ) {
			res = DocFacade.parseRE( reader );
		}
 		return res;
	}

	public DocInput(String type, DocBase doc, Reader reader) {
		super();
		this.type = type;
		this.reader = reader;
		this.doc = doc;
	}

	public static DocInput newInput( String type, DocBase doc ) {
		return newInput( type, doc, null );
	}
	
	public static DocInput newInput( String type, Reader reader ) {
		return newInput( type, null, reader );
	}
	
	public static DocInput newInput( String type, DocBase doc, Reader reader ) {
		return new DocInput( type, doc, reader );
	}
	
}
