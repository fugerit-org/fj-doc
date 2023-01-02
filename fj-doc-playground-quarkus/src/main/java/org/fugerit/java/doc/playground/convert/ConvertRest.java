package org.fugerit.java.doc.playground.convert;

import java.io.StringReader;
import java.io.StringWriter;

import org.fugerit.java.doc.json.parse.DocJsonToXml;
import org.fugerit.java.doc.json.parse.DocXmlToJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

@ApplicationScoped
@Path("/convert")
public class ConvertRest {

	private final static Logger logger = LoggerFactory.getLogger(ConvertRest.class);
	
	private final static String FORMAT_XML = "XML";
	private final static String FORMAT_JSON = "JSON";
	private final static String FORMAT_YAML = "YAML";
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/doc")
	public Response convertDoc( ConvertInput input ) {
		Response res = Response.status(Response.Status.BAD_REQUEST).build();
		try {
			ConvertOutput output = new ConvertOutput();
			String inputFormat = input.getInputFormat();
			String outputFormat = input.getOutputFormat();
			String docContent = input.getDocContent();
			String docOutput = null;
			logger.info( "format input : {} -> output : {}", inputFormat, outputFormat );
			ObjectMapper mapper = new ObjectMapper();
			ObjectMapper yamlMapper = new ObjectMapper( new YAMLFactory() );
			if ( FORMAT_XML.equalsIgnoreCase( inputFormat ) ) {
				if ( FORMAT_JSON.equalsIgnoreCase( outputFormat ) ) {
					DocXmlToJson helper = new DocXmlToJson();
					try ( StringReader reader = new StringReader( docContent ) ) {
						JsonNode node = helper.convertToJsonNode( reader );
						docOutput = node.toPrettyString();	
					}
				} else if ( FORMAT_YAML.equalsIgnoreCase( outputFormat ) ) {
					DocXmlToJson helper = new DocXmlToJson( yamlMapper );
					try ( StringReader reader = new StringReader( docContent ) ) {
						JsonNode node = helper.convertToJsonNode( reader );
						docOutput = yamlMapper.writeValueAsString( node );	
					}
				} else {
					output.setMessage( "Invalid output format : "+outputFormat );
				}
			} else if ( FORMAT_JSON.equalsIgnoreCase( inputFormat ) ) {
				if ( FORMAT_XML.equalsIgnoreCase( outputFormat ) ) {
					DocJsonToXml helper = new DocJsonToXml();
					try ( StringReader reader = new StringReader( docContent );
							StringWriter writer = new StringWriter() ) {
						helper.writerAsXml(reader, writer);
						docOutput = writer.toString();
					}
				} else if ( FORMAT_YAML.equalsIgnoreCase( outputFormat ) ) {
					try ( StringReader reader = new StringReader( docContent ) ) {
						JsonNode node = mapper.readTree( reader );
						docOutput = yamlMapper.writeValueAsString( node );
					}
				} else {
					output.setMessage( "Invalid output format : "+outputFormat );
				}
			} else if ( FORMAT_YAML.equalsIgnoreCase( inputFormat ) ) {
				if ( FORMAT_XML.equalsIgnoreCase( outputFormat ) ) {
					DocJsonToXml helper = new DocJsonToXml( yamlMapper );
					try ( StringReader reader = new StringReader( docContent );
							StringWriter writer = new StringWriter() ) {
						helper.writerAsXml(reader, writer);
						docOutput = writer.toString();
					}
				} else if ( FORMAT_JSON.equalsIgnoreCase( outputFormat ) ) {
					try ( StringReader reader = new StringReader( docContent ) ) {
						JsonNode node = yamlMapper.readTree( reader );
						docOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString( node );
					}
				} else {
					output.setMessage( "Invalid output format : "+outputFormat );
				}
			}
			if ( docOutput != null ) {
				output.setDocOutput(docOutput);
				res = Response.ok().entity( output ).build();	
			} else {
				res = Response.status(Response.Status.BAD_REQUEST).entity( output ).build();
			}
		} catch (Exception e) {
			logger.info("Error : " + e, e);
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return res;
	}
	
}