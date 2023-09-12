package org.fugerit.java.doc.playground.convert;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.json.parse.DocJsonToXml;
import org.fugerit.java.doc.json.parse.DocXmlToJson;
import org.fugerit.java.doc.playground.RestHelper;
import org.fugerit.java.doc.playground.facade.InputFacade;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@Path("/convert")
public class ConvertRest {

	private static final String INVALID_FORMAT_MESSAGE = "Invalid output format : ";
	
	private String handleXml( String docContent, String outputFormat,  ObjectMapper yamlMapper ) throws IOException, ConfigException {
		String docOutput = null;
		if ( InputFacade.FORMAT_JSON.equalsIgnoreCase( outputFormat ) ) {
			DocXmlToJson helper = new DocXmlToJson();
			try ( StringReader reader = new StringReader( docContent ) ) {
				JsonNode node = helper.convertToJsonNode( reader );
				docOutput = node.toPrettyString();	
			}
		} else if ( InputFacade.FORMAT_YAML.equalsIgnoreCase( outputFormat ) ) {
			DocXmlToJson helper = new DocXmlToJson( yamlMapper );
			try ( StringReader reader = new StringReader( docContent ) ) {
				JsonNode node = helper.convertToJsonNode( reader );
				docOutput = yamlMapper.writeValueAsString( node );	
			}
		}
		return docOutput;
	}
	
	private String handleJson( String docContent, String outputFormat, ObjectMapper mapper, ObjectMapper yamlMapper ) throws IOException, ConfigException {
		String docOutput = null;
		if ( InputFacade.FORMAT_XML.equalsIgnoreCase( outputFormat ) ) {
			DocJsonToXml helper = new DocJsonToXml();
			try ( StringReader reader = new StringReader( docContent );
					StringWriter writer = new StringWriter() ) {
				helper.writerAsXml(reader, writer);
				docOutput = writer.toString();
			}
		} else if ( InputFacade.FORMAT_YAML.equalsIgnoreCase( outputFormat ) ) {
			try ( StringReader reader = new StringReader( docContent ) ) {
				JsonNode node = mapper.readTree( reader );
				docOutput = yamlMapper.writeValueAsString( node );
			}
		} 
		return docOutput;
	}
	
	private String handleYaml( String docContent, String outputFormat, ObjectMapper mapper, ObjectMapper yamlMapper ) throws IOException, ConfigException {
		String docOutput = null;
		if ( InputFacade.FORMAT_XML.equalsIgnoreCase( outputFormat ) ) {
			DocJsonToXml helper = new DocJsonToXml( yamlMapper );
			try ( StringReader reader = new StringReader( docContent );
					StringWriter writer = new StringWriter() ) {
				helper.writerAsXml(reader, writer);
				docOutput = writer.toString();
			}
		} else if ( InputFacade.FORMAT_JSON.equalsIgnoreCase( outputFormat ) ) {
			try ( StringReader reader = new StringReader( docContent ) ) {
				JsonNode node = yamlMapper.readTree( reader );
				docOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString( node );
			}
		} 
		return docOutput;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/doc")
	public Response convertDoc( ConvertInput input ) {
		return RestHelper.defaultHandle( () -> {
			ConvertOutput output = new ConvertOutput();
			String inputFormat = input.getInputFormat();
			String outputFormat = input.getOutputFormat();
			String docContent = input.getDocContent();
			String docOutput = null;
			log.info( "format input : {} -> output : {}", inputFormat, outputFormat );
			ObjectMapper mapper = new ObjectMapper();
			ObjectMapper yamlMapper = new ObjectMapper( new YAMLFactory() );
			if ( InputFacade.FORMAT_LIST.contains( outputFormat ) ) {
				if ( InputFacade.FORMAT_XML.equalsIgnoreCase( inputFormat ) ) {
					docOutput = this.handleXml( docContent, outputFormat, yamlMapper);
				} else if ( InputFacade.FORMAT_JSON.equalsIgnoreCase( inputFormat ) ) {
					docOutput = this.handleJson( docContent, outputFormat, mapper, yamlMapper);
				} else if ( InputFacade.FORMAT_YAML.equalsIgnoreCase( inputFormat ) ) {
					docOutput = this.handleYaml( docContent, outputFormat, mapper, yamlMapper);
				}
			} else {
				output.setMessage( INVALID_FORMAT_MESSAGE+outputFormat );
			}
			Response res = null;
			if ( docOutput != null ) {
				output.setDocOutput(docOutput);
				res = Response.ok().entity( output ).build();	
			} else {
				res = Response.status(Response.Status.BAD_REQUEST).entity( output ).build();
			}
			return res;
		} );
	}
	
}