package org.fugerit.java.doc.playground.meta;

import java.util.Properties;

import org.fugerit.java.core.util.PropsIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/meta")
public class MetaRest {

	private final static Logger logger = LoggerFactory.getLogger(MetaRest.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/version")
	public Response getVersion() {
		Response res = Response.status(Response.Status.BAD_REQUEST).build();
		try {
			Properties buildProps = PropsIO.loadFromClassLoader( "build.properties" );
			res = Response.ok().entity( buildProps ).build();
		} catch (Exception e) {
			logger.info("Error : " + e, e);
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return res;
	}

}