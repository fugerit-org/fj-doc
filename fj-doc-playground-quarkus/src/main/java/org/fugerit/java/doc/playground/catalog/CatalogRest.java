package org.fugerit.java.doc.playground.catalog;

import java.io.Reader;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.NewCookie;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.collection.OptionItem;
import org.fugerit.java.doc.playground.RestHelper;
import org.fugerit.java.doc.sample.facade.DocCatalogEntry;
import org.fugerit.java.doc.sample.facade.DocCatalogSample;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/catalog")
public class CatalogRest {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list/type/{type}")
	public Response catalogListFilterType( @PathParam("type") String type ) {

		return RestHelper.defaultHandle( () -> Response.ok().entity( 
					DocCatalogSample.getInstance().getPlaygroundCoreCatalog()
						.stream().filter( e -> type.equalsIgnoreCase( e.getType() ) ).map( 
								e -> new OptionItem( e.getId(), e.getDescription() ) 
						).toList() ).build()
		);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list")
	public Response catalogList() {
		return RestHelper.defaultHandle( () -> Response.ok().entity( 
					DocCatalogSample.getInstance().getPlaygroundCoreCatalog()
						.stream().map( 
								e -> new OptionItem( e.getId(), e.getDescription() ) 
						).toList() ).build()
		);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/entry/{id}")
	public Response catalogEntry( @PathParam("id") String id ) {
		return RestHelper.defaultHandle( () -> {
			DocCatalogSample catalog = DocCatalogSample.getInstance();
			DocCatalogEntry entry = catalog.getPlaygroundCoreCatalog().get( id );
			try ( Reader reader = catalog.entryReader( entry ) ) {
				CatalogOutput output = new CatalogOutput();
				output.setDocOutput( StreamIO.readString( reader ) );
				if ( StringUtils.isNotEmpty( entry.getJsonDataPath() ) ) {
					output.setJsonData( StreamIO.readString( catalog.entryReaderJsonData( entry ) ) );
				}
				return Response.ok().entity( output ).build();
			}
		} );
	}
	
}