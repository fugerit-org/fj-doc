package org.fugerit.java.doc.ent.helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fugerit.java.core.web.servlet.response.HttpServletResponseByteData;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.facade.DocHandlerFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocResponseHelper {

	private static Logger logger = LoggerFactory.getLogger( DocResponseHelper.class );
	
	public static void handle( DocHandlerFacade facade, DocResponseParams params ) throws Exception {
		handle( facade, params, false );
	}
	
	public static void handle( DocHandlerFacade facade, DocResponseParams params, boolean logXml ) throws Exception {
		HttpServletRequest request = params.getRequest();
		HttpServletResponse response = params.getResponse();
		response.setContentType( params.getContentType() );
		String contentDisposition = "filename="+params.getFileName();
		if ( params.isInline() ) {
			contentDisposition = "inline;"+contentDisposition;
		}
		response.setHeader( "content-disposition" , contentDisposition );
		final HttpServletResponseByteData resp = new HttpServletResponseByteData( response );
		RequestDispatcher rd = request.getRequestDispatcher( params.getJspGeneratorPath() );
		rd.forward( request, resp );
		resp.flush();
		byte[] data = resp.getBaos().toByteArray();
		if ( logXml ) {
			logger.info( "XML DATA >\n"+new String( data ) );	
		}
		DocBase docBase = DocFacade.parse( new ByteArrayInputStream( data ) );
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		facade.handle( DocInput.newInput( params.getType() , docBase ) , DocOutput.newOutput( baos ) );
		response.getOutputStream().write( baos.toByteArray() );
	}
	
}
