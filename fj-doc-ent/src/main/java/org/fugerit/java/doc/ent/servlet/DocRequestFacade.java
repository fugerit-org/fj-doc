package org.fugerit.java.doc.ent.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.text.MessageFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.log.BasicLogObject;
import org.fugerit.java.core.web.servlet.config.ConfigContext;
import org.fugerit.java.core.web.servlet.response.HttpServletResponseByteData;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocConstants;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.helper.DefaultMimeHelper;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.ent.servlet.facade.DocRequestConfig;
import org.w3c.dom.Element;

public class DocRequestFacade extends BasicLogObject {

	//private static final DocHandler DEF = new DefaultDocHandler();
	
	public void handleDoc( HttpServletRequest request, HttpServletResponse response ) throws ServletException {
		this.getLogger().info( "start processing "+DocConfig.VERSION );
		
		
		
		request.setAttribute( "docConsts" , DocConstants.DEF );
		this.getLogger().info( "out-mode    : "+this.getDocRequestConfig().getOutMode() );
		String uri = request.getRequestURI();
		
//		// compress mode
//		int compressMode = ZipFilter.checkMode( uri );
////		if ( compressMode != ZipFilter.COMPRESS_MODE_NONE ) {
////			uri = 
////		}
		
		String render = request.getParameter( "render-type" );
		String truncate = request.getParameter( "truncate" );
		this.getLogger().info( "uri : "+uri );
		this.getLogger().info( "render-type : "+render );
		this.getLogger().info( "truncate    : "+truncate );
		String data = uri.substring( uri.lastIndexOf( "/" )+1 );
		String name = data;
		String fileName = data;
		this.getLogger().info( "filename 1 : "+fileName );
		String type = render;
		int index = data.lastIndexOf( "." );
		if ( render == null ) {
			name = data.substring( 0, index );
			type = data.substring( index+1 );
		} else if ( index != -1 ) {
			name = data.substring( 0, index );	
		}
		if ( truncate != null ) {
			name = name.substring( 0, Integer.parseInt( truncate ) );
		}
		this.getLogger().info( "name : "+name );
		this.getLogger().info( "type : "+type );
		
		DocHandler docHandler = (DocHandler)this.getDocRequestConfig().getDocHandlerMap().get( name );
		DocContext docContext = new DocContext( this.getDocRequestConfig() );
		docContext.setName(name);
		docContext.setFileName( fileName );
		
		if ( DocHandler.MODE_DIRECT.equalsIgnoreCase( docHandler.getMode() ) ) {
			
			try {
				docHandler.handleDoc(request, response, this.getDocRequestConfig().getContext().getContext() );
			} catch (Exception e) {
				throw new ServletException( e );
			}
			
		} else {
			

			request.setAttribute( "doc.render.type" , type );
			
			
			
			String contentType = DefaultMimeHelper.getDefaultMime( type );
			
			if ( !"SERVER".equals( contentType ) ) {
				response.setContentType( contentType );	
			}

			DocTypeHandler docTypeHandler = (DocTypeHandler)this.getDocRequestConfig().getTypeHandlerMap().get( type );

			
			
			// fine processamento
			
			try {
//				//se il doc handler � null usiamo il default doc handler
//				if ( docHandler == null ) {
//					this.getLogger().info( "no doc handler found for document : "+name+", using default doc handler" );
//					docHandler = DEF;
//				}
				if ( docTypeHandler != null ) {
					docTypeHandler.handleDocTypeInit(request, response, docContext);
				}
				
				
				if ( docHandler.getForward() != null ) {
					docHandler.handleDoc( request, response, this.getDocRequestConfig().getContext().getContext() );
					RequestDispatcher rd = request.getRequestDispatcher( docHandler.getForward() );
					this.getLogger().info( "forward : '"+docHandler.getForward()+"'" );
					rd.forward( request , response );	
				} else {
					HttpServletResponse resp = null;
					if ( "pushbody".equalsIgnoreCase( this.getDocRequestConfig().getOutMode() ) ) {
						resp = new HttpServletResponsePush( response );
					} else {
						resp = new HttpServletResponseByteData( response );
					}
					docHandler.handleDoc( request, resp, this.getDocRequestConfig().getContext().getContext() );
					String encoding = docHandler.getEncoding();
					try {
						response.setCharacterEncoding( encoding );	
					} catch ( Throwable t  ) {
						this.getLogger().info( "failed setting character encoding : "+t );
					}
					
					String jspRelative = this.getDocRequestConfig().getJspPath()+"/"+name+".jsp";
					String jspHandler = this.getDocRequestConfig().getContext().getContext().getRealPath( jspRelative );
					File jspFile = new File( jspHandler );
					if ( !jspFile.exists() ) {
						throw new DocException( "01", "Jsp File doesn't exists : '"+jspRelative+"'", null );
					}
					
					
					// use jsp?
					if ( docHandler.isUseJsp() ) {
						String jspPath = this.getDocRequestConfig().getJspPath()+"/doc-handler.jsp";
						if (this.getDocRequestConfig().getProcessingPage() != null ) {
							request.setAttribute( "doc-handler-name" , name );
							jspPath = this.getDocRequestConfig().getJspPath()+"/"+this.getDocRequestConfig().getProcessingPage() ;
						}
						RequestDispatcher rd = request.getRequestDispatcher( jspPath );
						this.getLogger().info( "jspPath : '"+jspPath+"'" );
						rd.forward( request , resp );
						String xmlData = null;
						if ( "pushbody".equalsIgnoreCase( this.getDocRequestConfig().getOutMode() ) ) {
							StringWriter sw = (StringWriter)request.getAttribute( "doc.writer" );
							xmlData = sw.toString();
						} else {
							HttpServletResponseByteData re = (HttpServletResponseByteData) resp;
							re.flush();
							xmlData = re.getBaos().toString();	
						}
						if ( this.getDocRequestConfig().isDebug() ) {
							this.getLogger().info( "xmlData 1 : \n"+xmlData );
						}
						if ( !this.getDocRequestConfig().isSkipFilter() ) {
							if ( this.getDocRequestConfig().isDebug() ) {
								this.getLogger().info( "skip filter : true" );
							}
						}	
						docContext.setXmlData( xmlData );
					
					}
					
					docContext.setType( type );
					docContext.setContentType( contentType );
					docContext.setEncoding( encoding );

					this.handleDocWorker(request, response, docTypeHandler, docContext);
					
					docHandler.handleDocPost(request, response, this.getDocRequestConfig().getContext().getContext());
					if ( docTypeHandler != null ) {
						docTypeHandler.handleDocTypePost(request, response, docContext);
					}
				}

			} catch (Exception e) {
				if ( "document".equalsIgnoreCase( this.getDocRequestConfig().getErrorManager() ) ) {
					DocException de = null;
					if ( e instanceof DocException ) {
						de = (DocException)e;
					} else {
						de = new DocException( "99" , e.getMessage(), e );
					}
					ByteArrayOutputStream error = new ByteArrayOutputStream();
					PrintStream errorStream = new PrintStream( error, true );
					de.printStackTrace( errorStream );
					errorStream.flush();
					errorStream.close();
					this.getLogger().error( "Error generating doc", e );
					Object[] args = { de.getCode(), de.getMessage(), error.toString() };
					String xmlData = MessageFormat.format( ERROR_XML_DATA , args );
					docContext.setXmlData( xmlData );
					docContext.setType( type );
					docContext.setContentType( contentType );
					try {
						handleDocWorker(request, response, docTypeHandler, docContext);
					} catch (Exception e1) {
						throw ( new ServletException( e1 ) );
					}				
				} else {
					throw new ServletException( e );
				}
			}

			
			
		}
		
		this.getLogger().info( "end processing" );
	}
	
	private void handleDocWorker( HttpServletRequest request, HttpServletResponse response, DocTypeHandler docTypeHandler, DocContext docContext ) throws Exception {
		String contentDisposition = "attachment; filename="+docContext.getFileName();
		this.getLogger().info("contentDisposition  : "+contentDisposition );
		if ( contentDisposition != null ) {
			response.addHeader("Content-Disposition", contentDisposition );
		}	
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		docContext.setBufferStream( baos );
		this.getLogger().info( "handleDocWorker - docTypeHandler 1 : "+docTypeHandler );
		if ( docTypeHandler == null ) {
			DocBase docBase = docContext.getDocBase( request );
			if ( docContext.getType().equalsIgnoreCase( "xml" ) ) {
				docContext.getBufferStream().write( docContext.getXmlData().getBytes() );
			} else if ( docContext.getType().equalsIgnoreCase( "pdf" ) ) {
				EntDocFacade.createPDF( docBase , docContext.getBufferStream() );
			} else if ( docContext.getType().equalsIgnoreCase( "rtf" ) ) {
				EntDocFacade.createRTF( docBase , docContext.getBufferStream() );
			} else if ( docContext.getType().equalsIgnoreCase( "html" ) ) {
				EntDocFacade.createHTML( docBase , docContext.getBufferStream() );		
			}
			this.getLogger().info("content type doc  : "+docContext.getContentType()+" : "+docContext.getEncoding() );
		} else {
			docTypeHandler.handleDocType( request, response, docContext );
		}
		OutputStream os = response.getOutputStream();
		baos.writeTo( os );
		baos.flush();
		os.flush();
	}
	
	private DocRequestConfig docRequestConfig;
	
	public void configure( Element root, ConfigContext context ) {
		this.docRequestConfig = new DocRequestConfig();
		this.docRequestConfig.configure(root, context);
	}
	
	public DocRequestConfig getDocRequestConfig() {
		return docRequestConfig;
	}

	private static String init() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			InputStream is = DocRequestFacade.class.getResourceAsStream( "/org/Fugerit/java/mod/doc/res/error-doc.xml" );
			StreamIO.pipeStream( is , baos, StreamIO.MODE_CLOSE_BOTH );	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baos.toString();
	}
	
	private static final String ERROR_XML_DATA = init();
		
}
