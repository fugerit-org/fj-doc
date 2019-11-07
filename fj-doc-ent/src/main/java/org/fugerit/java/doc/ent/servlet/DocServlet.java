package org.fugerit.java.doc.ent.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fugerit.java.core.log.LogFacade;
import org.fugerit.java.core.web.log.helpers.LogObjectServlet;
import org.fugerit.java.core.web.servlet.config.ConfigContext;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.ent.config.DocServletConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DocServlet extends LogObjectServlet {

	/*
	 * 
	 */
	private static final long serialVersionUID = -3048440498921813465L;

	private DocRequestFacade configFacade;
	
	/* (non-Javadoc)
	 * @see org.opinf.jlib.ent.servlet.filter.HttpFilter#destroy()
	 */
	public void destroy() {
		this.configFacade = null;
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
	
	/* (non-Javadoc)
	 * @see org.opinf.jlib.ent.servlet.filter.HttpFilter#doFilter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getLogger().info( "TESTDOC" );
		if ( this.configFacade == null ) {
			this.configFacade = (DocRequestFacade)this.getServletContext().getAttribute( DocServletConfig.ATT_NAME_DOCFACADE );
		}
		this.configFacade.handleDoc(request, response);
	}

	/* (non-Javadoc)
	 * @see org.opinf.jlib.ent.servlet.filter.HttpFilter#init(javax.servlet.FilterConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init( config );
		this.logInit( "start : "+DocConfig.VERSION );
		String skipInit = config.getInitParameter( "skip-init" );
		if ( "true".equalsIgnoreCase(skipInit) ) {
			this.logInit( "docservlet init skip "+skipInit );
		} else {
			try {
				String configPath = config.getInitParameter( "config" );
				File configFile = new File( config.getServletContext().getRealPath( "/" ), configPath );
				Document doc = DOMIO.loadDOMDoc( configFile );
				Element root = doc.getDocumentElement();
				this.configFacade = new DocRequestFacade();
				this.configFacade.configure( root , new ConfigContext( config.getServletContext() ) );
			} catch (Throwable t) {
				LogFacade.handleError( t );
			}			
		}		
		this.logInit( "end" );
	}	
	
}
