/*
 * @(#)HttpServletResponseData.java
 *
 * @project    : serviceapp
 * @package    : net.jsomnium.jlib.mod.web.filter
 * @creation   : 12/lug/07
 * @license	   : META-INF/LICENSE.TXT
 */
package org.fugerit.java.doc.ent.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.fugerit.java.core.log.LogFacade;

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
public class HttpServletResponsePush extends HttpServletResponseWrapper  {

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#getOutputStream()
	 */
	public ServletOutputStream getOriginalOutputStream() throws IOException {
		return super.getOutputStream();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#getWriter()
	 */
	public PrintWriter getOriginalWriter() throws IOException {
		return super.getWriter();
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#getOutputStream()
	 */
	public ServletOutputStream getOutputStream() throws IOException {
		return new DataServletOutputStream( new ByteArrayOutputStream() );
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#getWriter()
	 */
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter( this.getOutputStream() );
	}

	/*
	 * <jdl:section>
	 * 		<jdl:text lang='it'><p>Crea una nuova istanza di HttpServletResponseData.</p></jdl:text>
	 * 		<jdl:text lang='en'><p>Creates a new instance of HttpServletResponseData.</p></jdl:text>
	 * </jdl:section>
	 *
	 * @param arg0
	 */
	public HttpServletResponsePush(HttpServletResponse response) {
		super(response);
	}
	
	public void flush() throws IOException {
	}

	public void setContentType(String type) {
		LogFacade.getLog().debug( "HttpServletResponseByteData.setContentType() do nothing : operation not allowed here" );
	}
		
}
