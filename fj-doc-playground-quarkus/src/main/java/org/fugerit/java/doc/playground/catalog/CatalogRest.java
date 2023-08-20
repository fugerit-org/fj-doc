package org.fugerit.java.doc.playground.catalog;

import java.io.Reader;
import java.util.stream.Collectors;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.util.collection.OptionItem;
import org.fugerit.java.doc.playground.convert.ConvertOutput;
import org.fugerit.java.doc.sample.facade.DocCatalogSample;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@Path("/catalog")
public class CatalogRest {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list")
	public Response catalogList() {
		Response res = Response.status(Response.Status.BAD_REQUEST).build();
		try {
			res = Response.ok().entity( 
					DocCatalogSample.getInstance().getPlaygroundCoreCatalog()
						.stream().map( 
								e -> new OptionItem( e.getId(), e.getDescription() ) 
						).collect( Collectors.toList() )	
			).build();
		} catch (Exception e) {
			log.info("Error : " + e, e);
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return res;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/entry/{id}")
	public Response catalogEntry( @PathParam("id") String id ) {
		Response res = Response.status(Response.Status.BAD_REQUEST).build();
		DocCatalogSample catalog = DocCatalogSample.getInstance();
		try ( Reader reader = catalog.entryReader( catalog.getPlaygroundCoreCatalog().get( id ) ) ) {
			ConvertOutput output = new ConvertOutput();
			output.setDocOutput( StreamIO.readString( reader ) );
			res = Response.ok().entity( output ).build();
		} catch (Exception e) {
			log.info("Error : " + e, e);
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return res;
	}
	
}