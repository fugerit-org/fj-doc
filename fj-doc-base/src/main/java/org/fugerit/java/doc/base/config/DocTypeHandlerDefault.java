package org.fugerit.java.doc.base.config;

import java.io.Serializable;

import org.fugerit.java.doc.base.helper.DefaultMimeHelper;

public class DocTypeHandlerDefault implements DocTypeHandler, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5024985828785381015L;

	private String type;
	
	private String key;
	
	private String mime;
	
	@Override
	public String getMime() {
		String res = this.mime;
		if ( res == null ) {
			res = DefaultMimeHelper.getDefaultMime( this.getType() );
		}
		return res;
	}

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		
	}

	public DocTypeHandlerDefault(String type, String key, String mime) {
		super();
		this.type = type;
		this.key = key;
		this.mime = mime;
	}

	public DocTypeHandlerDefault(String type, String mime) {
		this( type, type, mime );
	}
	
	public DocTypeHandlerDefault(String type) {
		this( type, type, null );
	}

}
