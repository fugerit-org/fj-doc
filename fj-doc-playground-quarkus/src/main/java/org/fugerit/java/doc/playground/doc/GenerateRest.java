package org.fugerit.java.doc.playground.doc;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandler;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.fugerit.java.doc.mod.poi.XlsxPoiTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/generate")
public class GenerateRest {

	private final static Logger logger = LoggerFactory.getLogger(GenerateRest.class);
	
	private byte[] generateHelper( GenerateInput input, DocTypeHandler handler ) throws Exception {
		byte[] result = null;
		if ( input.getDocContent() != null ) {
			try ( StringReader reader = new StringReader( input.getDocContent() );
					ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
				DocInput docInput = DocInput.newInput( DocConfig.TYPE_PDF , reader );
				DocOutput docOutput = DocOutput.newOutput( baos );
				handler.handle(docInput, docOutput);
				result =  baos.toByteArray();
			}
		}
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/PDF")
	public Response pdf( GenerateInput input) {
		Response res = Response.status(Response.Status.BAD_REQUEST).build();
		try {
			PdfFopTypeHandler handler = new PdfFopTypeHandler();
			byte[] data = this.generateHelper(input, handler);
			res = Response.ok().entity( data ).build();
		} catch (Exception e) {
			logger.info("Error : " + e, e);
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return res;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_HTML)
	@Path("/HTML")
	public Response html( GenerateInput input) {
		Response res = Response.status(Response.Status.BAD_REQUEST).build();
		try {
			byte[] data = this.generateHelper(input, FreeMarkerHtmlTypeHandler.HANDLER);
			res = Response.ok().entity( data ).build();
		} catch (Exception e) {
			logger.info("Error : " + e, e);
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return res;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_HTML)
	@Path("/XLSX")
	public Response xlsx( GenerateInput input) {
		Response res = Response.status(Response.Status.BAD_REQUEST).build();
		try {
			byte[] data = this.generateHelper(input, XlsxPoiTypeHandler.HANDLER);
			res = Response.ok().entity( data ).build();
		} catch (Exception e) {
			logger.info("Error : " + e, e);
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return res;
	}

}