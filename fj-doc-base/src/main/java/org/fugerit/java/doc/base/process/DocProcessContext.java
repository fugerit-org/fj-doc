package org.fugerit.java.doc.base.process;

import java.io.Serializable;
import java.util.Map;

import org.fugerit.java.core.lang.helpers.AttributesHolder;
import org.fugerit.java.core.util.filterchain.MiniFilterContext;

public class DocProcessContext extends MiniFilterContext implements Serializable, AttributesHolder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -110760708709527036L;

	public static DocProcessContext newContext( String key, Object value ) {
		DocProcessContext context = new DocProcessContext();
		context.setAttribute( key , value);
		return context;
	}

	public static DocProcessContext newContext( Map<String, Object> initValues ) {
		DocProcessContext context = new DocProcessContext();
		context.setAll( initValues );
		return context;
	}

}
