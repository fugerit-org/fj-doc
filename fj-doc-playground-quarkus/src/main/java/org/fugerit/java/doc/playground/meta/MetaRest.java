package org.fugerit.java.doc.playground.meta;

import java.util.Properties;

import org.fugerit.java.core.util.PropsIO;
import org.fugerit.java.doc.playground.RestHelper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/meta")
public class MetaRest {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/version")
	public Response getVersion() {
		return RestHelper.defaultHandle( () -> {
			Properties buildProps = PropsIO.loadFromClassLoader( "build.properties" );
			return Response.ok().entity( buildProps ).build();
		} );
	}

}