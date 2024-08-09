package org.fugerit.java.doc.playground.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.util.checkpoint.CheckpointUtils;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.playground.RestHelper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@ApplicationScoped
@Slf4j
@Path("/config")
public class ConvertConfigRest {

	private ConvertConfigFacade facade = new ConvertConfigFacade();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/convert")
	public Response document( ConvertConfigInput input) {
		return RestHelper.defaultHandle( () -> {
			long time = System.currentTimeMillis();
			ConvertConfigOutput output = new ConvertConfigOutput();
			try {
				String data = facade.convertHelper(input);
				output.setDocOutputBase64( Base64.getEncoder().encodeToString( data.getBytes( StandardCharsets.UTF_8 ) ) );
				output.setGenerationTime( CheckpointUtils.formatTimeDiffMillis( time , System.currentTimeMillis() ) );
			} catch ( Exception e ) {
				log.warn( String.format( "Error converting configuration : %s", e.getMessage() ) , e );
				Throwable te = RestHelper.findCause(e);
				output.setMessage( te.getClass().getName()+" :\n"+te.getMessage() );
			}
			return Response.ok().entity( output ).build();
		} );
	}

}