package org.fugerit.java.doc.playground.convert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.io.helper.HelperIOException;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.json.parse.DocJsonToXml;
import org.fugerit.java.doc.json.parse.DocXmlToJson;
import org.fugerit.java.doc.playground.facade.InputFacade;
import org.w3c.dom.Element;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConvertFacade {

	public String handleConvert( String inputFormat, String docContent, String outputFormat, boolean prettyPrint ) throws ConfigException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectMapper yamlMapper = new ObjectMapper( new YAMLFactory() );
		String docOutput = null;
		if ( InputFacade.FORMAT_XML.equalsIgnoreCase( inputFormat ) ) {
			docOutput = this.handleXml( docContent, outputFormat, mapper, yamlMapper, prettyPrint );
		} else if ( InputFacade.FORMAT_JSON.equalsIgnoreCase( inputFormat ) ) {
			docOutput = this.handleJson( docContent, outputFormat, mapper, yamlMapper, prettyPrint );
		} else if ( InputFacade.FORMAT_YAML.equalsIgnoreCase( inputFormat ) ) {
			docOutput = this.handleYaml( docContent, outputFormat, mapper, yamlMapper, prettyPrint );
		}
		return docOutput;
	}
	
	private String mapperValueHelper( ObjectMapper mapper, JsonNode node, boolean prettyPrint ) throws JsonProcessingException {
		return prettyPrint ? mapper.writerWithDefaultPrettyPrinter().writeValueAsString( node ) : mapper.writeValueAsString( node );
	}
	
	private String xmlValueHelper( DocJsonToXml helper , Reader jsonReader, boolean prettyPrint ) throws IOException {
		try ( ByteArrayOutputStream writer = new ByteArrayOutputStream() ) {
			return HelperIOException.get( () -> {
				Element doc = helper.convertToElement( jsonReader );
				if ( prettyPrint ) {
					DOMIO.writeDOMIndent( doc , writer );
				} else {
					DOMIO.writeDOM( doc, writer );
				}
				return writer.toString();
			} );
		}
	}
	
	private String handleXml( String docContent, String outputFormat, ObjectMapper mapper,  ObjectMapper yamlMapper, boolean prettyPrint ) throws IOException, ConfigException {
		String docOutput = null;
		if ( InputFacade.FORMAT_JSON.equalsIgnoreCase( outputFormat ) ) {
			DocXmlToJson helper = new DocXmlToJson();
			try ( StringReader reader = new StringReader( docContent ) ) {
				JsonNode node = helper.convertToJsonNode( reader );
				docOutput = mapperValueHelper(mapper, node, prettyPrint);
			}
		} else if ( InputFacade.FORMAT_YAML.equalsIgnoreCase( outputFormat ) ) {
			DocXmlToJson helper = new DocXmlToJson( yamlMapper );
			try ( StringReader reader = new StringReader( docContent ) ) {
				JsonNode node = helper.convertToJsonNode( reader );
				docOutput = mapperValueHelper(yamlMapper, node, prettyPrint);
			}
		}
		return docOutput;
	}
	
	private String handleJson( String docContent, String outputFormat, ObjectMapper mapper, ObjectMapper yamlMapper, boolean prettyPrint ) throws IOException {
		String docOutput = null;
		if ( InputFacade.FORMAT_XML.equalsIgnoreCase( outputFormat ) ) {
			try ( StringReader jsonReader = new StringReader( docContent ) ) {
				docOutput = this.xmlValueHelper(new DocJsonToXml(), jsonReader, prettyPrint);
			}
		} else if ( InputFacade.FORMAT_YAML.equalsIgnoreCase( outputFormat ) ) {
			try ( StringReader reader = new StringReader( docContent ) ) {
				JsonNode node = mapper.readTree( reader );
				docOutput = this.mapperValueHelper(yamlMapper, node, prettyPrint);
			}
		} 
		return docOutput;
	}
	
	private String handleYaml( String docContent, String outputFormat, ObjectMapper mapper, ObjectMapper yamlMapper, boolean prettyPrint ) throws IOException {
		String docOutput = null;
		if ( InputFacade.FORMAT_XML.equalsIgnoreCase( outputFormat ) ) {
			try ( StringReader jsonReader = new StringReader( docContent ) ) {
				docOutput = this.xmlValueHelper(new DocJsonToXml( yamlMapper ), jsonReader, prettyPrint);
			}
		} else if ( InputFacade.FORMAT_JSON.equalsIgnoreCase( outputFormat ) ) {
			try ( StringReader reader = new StringReader( docContent ) ) {
				JsonNode node = yamlMapper.readTree( reader );
				docOutput = this.mapperValueHelper(mapper, node, prettyPrint);
			}
		} 
		return docOutput;
	}

	
}
