package org.fugerit.java.doc.base.process;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import org.fugerit.java.core.lang.helpers.AttributesHolder;
import org.fugerit.java.core.util.filterchain.MiniFilterContext;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;

public class DocProcessContext extends MiniFilterContext implements Serializable, AttributesHolder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -110760708709527036L;

	public static final String ATT_NAME_DOC_BASE = "docBase";
	
	public static final String ATT_NAME_DOC_TYPE = "docType";

	public static final String ATT_NAME_DOC_CHARSET = "docCharset";

	@Getter
	private int sourceType;

	public DocProcessContext() {
		this.sourceType = DocFacadeSource.SOURCE_TYPE_DEFAULT;
	}

	public DocProcessContext withSourceType( int sourceType ) {
		this.sourceType = sourceType;
		return this;
	}

	public DocProcessContext withDocInput( DocInput docInput ) {
		return this.withDocBase( docInput.getDoc() ).withDocType( docInput.getType() );
	}
	
	public DocProcessContext withDocBase( DocBase docBase ) {
		return this.withAtt( ATT_NAME_DOC_BASE , docBase );
	}
	
	public DocProcessContext withDocType( String type ) {
		return this.withAtt( ATT_NAME_DOC_TYPE , type );
	}

	public DocProcessContext withDocCharset( String type ) {
		return this.withAtt( ATT_NAME_DOC_CHARSET, type );
	}

	public DocProcessContext withAtt( String key, Object value )  {
		this.setAttribute(key, value);
		return this;
	}
	
	public static DocProcessContext newContext() {
		return new DocProcessContext();
	}
	
	public static DocProcessContext newContext( String key, Object value ) {
		DocProcessContext context = newContext();
		context.setAttribute( key , value);
		return context;
	}

	public static DocProcessContext newContext( Map<String, Object> initValues ) {
		DocProcessContext context = newContext();
		context.setAll( initValues );
		return context;
	}

}
