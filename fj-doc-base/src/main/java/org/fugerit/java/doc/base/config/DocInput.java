package org.fugerit.java.doc.base.config;

import java.io.Reader;

import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;

public class DocInput {

	private String type;
	
	private DocBase doc;
	
	private Reader reader;

	private int source;
	
	public Reader getReader() {
		return reader;
	}

	public String getType() {
		return type;
	}

	public DocBase getDoc() {
		DocBase res = doc;
		if ( res == null && reader != null ) {
			res = DocFacadeSource.getInstance().parseRE( reader, this.getSource() );
		}
 		return res;
	}

	public int getSource() {
		return source;
	}


	public DocInput(String type, DocBase doc, Reader reader) {
		this(type, doc, reader, DocFacadeSource.SOURCE_TYPE_DEFAULT);
	}
	
	public DocInput(String type, DocBase doc, Reader reader, int source) {
		super();
		this.type = type;
		this.reader = reader;
		this.doc = doc;
		this.source = source;
	}

	public static DocInput newInput( String type, DocBase doc ) {
		return newInput( type, doc, null );
	}
	
	public static DocInput newInput( String type, Reader reader ) {
		return newInput( type, null, reader );
	}
	
	public static DocInput newInput( String type, DocBase doc, Reader reader ) {
		return newInput( type, doc, reader, DocFacadeSource.SOURCE_TYPE_DEFAULT );
	}
	
	public static DocInput newInput( String type, Reader reader, int source ) {
		return newInput( type, null, reader, source );
	}
	
	public static DocInput newInput( String type, DocBase doc, Reader reader, int source ) {
		return new DocInput( type, doc, reader, source );
	}
	
}
