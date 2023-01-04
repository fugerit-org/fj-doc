package org.fugerit.java.doc.playground.doc;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.Base64;

import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlFragmentTypeHandler;
import org.fugerit.java.doc.lib.simpletable.SimpleTableDocConfig;
import org.fugerit.java.doc.lib.simpletable.SimpleTableFacade;
import org.fugerit.java.doc.lib.simpletable.model.SimpleRow;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.fugerit.java.doc.mod.poi.XlsxPoiTypeHandler;
import org.fugerit.java.doc.playground.facade.BasicInput;
import org.fugerit.java.doc.playground.facade.InputFacade;
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
	
	private byte[] generateHelper( GenerateInput input, DocTypeHandler handler) throws Exception {
		byte[] result = null;
		if ( input.getDocContent() != null ) {
			try ( StringReader reader = new StringReader( input.getDocContent() );
					ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
				int sourceType = DocFacadeSource.SOURCE_TYPE_XML;
				if ( InputFacade.FORMAT_JSON.equalsIgnoreCase( input.getInputFormat() ) ) {
					sourceType = DocFacadeSource.SOURCE_TYPE_JSON;
				} else if ( InputFacade.FORMAT_YAML.equalsIgnoreCase( input.getInputFormat() ) ) {
					sourceType = DocFacadeSource.SOURCE_TYPE_YAML;
				}
				String type = input.getOutputFormat().toLowerCase();
				logger.info( "output format : {}", type );
				DocInput docInput = DocInput.newInput( type, reader , sourceType );
				DocOutput docOutput = DocOutput.newOutput( baos );
				handler.handle(docInput, docOutput);
				result =  baos.toByteArray();
			}
		}
		return result;
	}

	private DocTypeHandler findHandler( BasicInput input ) {
		DocTypeHandler handler = new PdfFopTypeHandler();
		if ( "XLSX".equalsIgnoreCase( input.getOutputFormat() ) ) {
			handler = XlsxPoiTypeHandler.HANDLER;
		} else if ( "HTML".equalsIgnoreCase( input.getOutputFormat() ) ) {
			handler = FreeMarkerHtmlFragmentTypeHandler.HANDLER;
		}
		return handler;
	}
	
	private DocParser findParser( BasicInput input ) {
		int sourceType = DocFacadeSource.SOURCE_TYPE_XML;
		if ( InputFacade.FORMAT_JSON.equalsIgnoreCase( input.getInputFormat() ) ) {
			sourceType = DocFacadeSource.SOURCE_TYPE_JSON;
		} else if ( InputFacade.FORMAT_YAML.equalsIgnoreCase( input.getInputFormat() ) ) {
			sourceType = DocFacadeSource.SOURCE_TYPE_YAML;
		}
		return DocFacadeSource.getInstance().getParserForSource( sourceType );
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/document")
	public Response document( GenerateInput input) {
		Response res = Response.status(Response.Status.BAD_REQUEST).build();
		try {
			DocTypeHandler handler = this.findHandler(input);
			byte[] data = this.generateHelper(input, handler);
			GenerateOutput output = new GenerateOutput();
			output.setDocOutputBase64( Base64.getEncoder().encodeToString( data ) );
			res = Response.ok().entity( output ).build();
		} catch (Exception e) {
			logger.info("Error : " + e, e);
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return res;
	}
	
	private void addRow( SimpleTable simpleTableModel, int count, String level, String message ) {
		SimpleRow errorRow = new SimpleRow();
		if ( count > 0 ) {
			errorRow.addCell( String.valueOf( count ) );	
		} else {
			errorRow.addCell( "-" );
		}
    	errorRow.addCell( level );
    	errorRow.addCell( message );
        simpleTableModel.addRow( errorRow );
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/validate")
	public Response validate( GenerateInput input) {
		Response res = Response.status(Response.Status.BAD_REQUEST).build();
		try {
			DocParser parser = this.findParser(input);
			try ( StringReader reader = new StringReader( input.getDocContent() );
					ByteArrayOutputStream buffer = new ByteArrayOutputStream() ) {
				DocValidationResult result = parser.validateResult( reader );
				DocTypeHandler handler = this.findHandler(input);
				SimpleTable simpleTableModel = SimpleTableFacade.newTable( 15, 20, 65 );
		        SimpleRow headerRow = new SimpleRow( BooleanUtils.BOOLEAN_TRUE );
		        headerRow.addCell( "#" );
		        headerRow.addCell( "level" );
		        headerRow.addCell( "message" );
		        simpleTableModel.addRow( headerRow );
		        int count = 1;
		        for ( String message : result.getErrorList() ) {
		        	this.addRow(simpleTableModel, count, "error", message);
			        count++;
		        }
		        for ( String message : result.getInfoList() ) {
		        	this.addRow(simpleTableModel, count, "info", message);
			        count++;
		        }
		        this.addRow(simpleTableModel, 0, "result", "Document valid? : "+ ( DocValidationResult.RESULT_CODE_OK == result.getResultCode() ) );
		        SimpleTableDocConfig docConfig = SimpleTableDocConfig.newConfig();
		        docConfig.processSimpleTable(simpleTableModel, handler, buffer);
				GenerateOutput output = new GenerateOutput();
				output.setDocOutputBase64( Base64.getEncoder().encodeToString( buffer.toByteArray() ) );
				res = Response.ok().entity( output ).build();
			}
		} catch (Exception e) {
			logger.info("Error : " + e, e);
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return res;
	}

}