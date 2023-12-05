package org.fugerit.java.doc.playground.doc;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.Base64;

import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.util.checkpoint.CheckpointUtils;
import org.fugerit.java.core.util.result.Result;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.lib.simpletable.SimpleTableDocConfig;
import org.fugerit.java.doc.lib.simpletable.SimpleTableFacade;
import org.fugerit.java.doc.lib.simpletable.model.SimpleCell;
import org.fugerit.java.doc.lib.simpletable.model.SimpleRow;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;
import org.fugerit.java.doc.playground.RestHelper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
@Path("/generate")
public class GenerateRest {

	private GenerateFacade facade = new GenerateFacade();
	
	private Throwable findCause( Throwable o ) {
		return o.getCause() != null ? this.findCause( o.getCause() ) : o;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/document")
	public Response document( GenerateInput input) {
		return RestHelper.defaultHandle( () -> {
			long time = System.currentTimeMillis();
			DocTypeHandler handler = facade.findHandler(input);
			GenerateOutput output = new GenerateOutput();
			try {
				byte[] data = facade.generateHelper(input, handler);
				output.setDocOutputBase64( Base64.getEncoder().encodeToString( data ) );
				output.setGenerationTime( CheckpointUtils.formatTimeDiffMillis( time , System.currentTimeMillis() ) );
			} catch ( Exception e ) {
				log.warn( "Error generating document : "+e , e );
				Throwable te = this.findCause(e);
				output.setMessage( te.getClass().getName()+" :\n"+te.getMessage() );
			}
			return Response.ok().entity( output ).build();
		} );
	}
	
	private void addRow( SimpleTable simpleTableModel, int count, String level, String message ) {
		SimpleRow errorRow = new SimpleRow();
		if ( count > 0 ) {
			errorRow.addCell( String.valueOf( count ) );	
		} else {
			errorRow.addCell( "-" );
		}
    	errorRow.addCell( level );
    	SimpleCell messageCell = new SimpleCell(message);
    	messageCell.setAlign( "left" );
    	errorRow.addCell( messageCell );
        simpleTableModel.addRow( errorRow );
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/validate")
	public Response validate(GenerateInput input) {
		return RestHelper.defaultHandle(() -> {
			GenerateOutput output = new GenerateOutput();
			DocParser parser = facade.findParser(input);
			try (StringReader reader = new StringReader(input.getDocContent());
					ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
				DocTypeHandler handler = facade.findHandler(input);
				SimpleTable simpleTableModel = SimpleTableFacade.newTable(10, 20, 70);
				SimpleRow headerRow = new SimpleRow(BooleanUtils.BOOLEAN_TRUE);
				headerRow.addCell("#");
				headerRow.addCell("level");
				headerRow.addCell("message");
				simpleTableModel.addRow(headerRow);
				DocValidationResult result = parser.validateResult(reader);
				int count = 1;
				for (String message : result.getErrorList()) {
					this.addRow(simpleTableModel, count, "error", message);
					count++;
				}
				for (String message : result.getInfoList()) {
					this.addRow(simpleTableModel, count, "info", message);
					count++;
				}
				this.addRow(simpleTableModel, 0, "result",
						"Document valid? : " + (Result.RESULT_CODE_OK == result.getResultCode()));
				SimpleTableDocConfig docConfig = SimpleTableDocConfig.newConfig();
				docConfig.processSimpleTable(simpleTableModel, handler, buffer);
				output.setDocOutputBase64(Base64.getEncoder().encodeToString(buffer.toByteArray()));
			} catch (Exception e) {
				Throwable te = this.findCause(e);
				output.setMessage( te.getClass().getName()+" :\n"+te.getMessage() );
			}
			return Response.ok().entity(output).build();
		});
	}

}