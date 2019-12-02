package org.fugerit.java.doc.base.config;

import java.io.Serializable;

import org.fugerit.java.doc.base.helper.DefaultMimeHelper;

public class DocTypeHandlerDefault implements DocTypeHandler, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5024985828785381015L;

	private String type;
	
	private String module;
	
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
		return createKey( this.getType() , this.getModule() ) ;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public String getModule() {
		return module;
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		
	}

	public DocTypeHandlerDefault(String type, String module, String mime) {
		super();
		this.type = type;
		this.module = module;
		this.mime = mime;
	}

	public DocTypeHandlerDefault(String type, String module ) {
		this( type, module, null );
	}
	
	public static final String createKey( String type, String mod ) {
		return type+"-"+mod;
	}

}
