/*
 * @(#)DefaultDocHandler.java
 *
 * @project    : serviceapp
 * @package    : net.jsomnium.jlib.mod.doc.filter
 * @creation   : 12/lug/07
 * @license	   : META-INF/LICENSE.TXT
 */
package org.fugerit.java.doc.ent.servlet;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.log.BasicLogObject;
import org.fugerit.java.core.xml.dom.DOMUtils;
import org.w3c.dom.Element;

/*
 * <p>
 *	<jdl:section>
 * 		<jdl:text lang='it'>
 * 		</jdl:text>
 * 		<jdl:text lang='en'>
 * 		</jdl:text>  
 *	</jdl:section>
 * </p>
 *
 * @author mfranci
 *
 */
public class DefaultDocHandler extends BasicLogObject implements DocHandler {

	private String forward;
	
	
	private String mode;
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	private String encoding;
	
	/* (non-Javadoc)
	 * @see net.jsomnium.jlib.mod.doc.filter.DocHandler#handleDoc(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.ServletContext)
	 */
	public void handleDoc(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws Exception {
		this.getLogger().info( "handleDoc" );
	}

	public void handleDocPost(HttpServletRequest request,HttpServletResponse response, ServletContext context) throws Exception {
		this.getLogger().info( "handleDocPost" );
	}

	public String getEncoding() {
		return encoding;
	}

	public void init(Element config) throws ConfigException {
		Properties props = DOMUtils.attributesToProperties( config );
		this.encoding = props.getProperty( "encoding", "ISO-8859-15" );
		this.getLogger().info( "init() encofing : "+this.encoding );
		this.setForward( props.getProperty( "forward" ) );
	}
	
	public boolean isUseJsp() {
		return useJsp;
	}

	public void setUseJsp(boolean useJsp) {
		this.useJsp = useJsp;
	}

	public String getJsp() {
		return jsp;
	}

	public void setJsp(String jsp) {
		this.jsp = jsp;
	}

	private boolean useJsp;
	
	private String jsp;
	
}
