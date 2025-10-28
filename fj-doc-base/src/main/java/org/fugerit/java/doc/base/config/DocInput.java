package org.fugerit.java.doc.base.config;

import java.io.Reader;

import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;

import lombok.Getter;

public class DocInput {

	@Getter private String type;
	
	@Getter private Reader reader;

	@Getter private int source;

	private DocBase doc;
		
	public DocBase getDoc() {
		DocBase res = this.doc;
		if ( res == null && reader != null ) {
			res = DocFacadeSource.getInstance().parseRE( reader, this.getSource() );
			this.doc = res;
		}
 		return res;
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
