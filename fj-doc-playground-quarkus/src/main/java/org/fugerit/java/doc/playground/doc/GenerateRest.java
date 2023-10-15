package org.fugerit.java.doc.playground.doc;

import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.util.checkpoint.CheckpointUtils;
import org.fugerit.java.core.util.result.Result;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlFragmentTypeHandler;
import org.fugerit.java.doc.lib.simpletable.SimpleTableDocConfig;
import org.fugerit.java.doc.lib.simpletable.SimpleTableFacade;
import org.fugerit.java.doc.lib.simpletable.model.SimpleRow;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;
import org.fugerit.java.doc.mod.poi.XlsxPoiTypeHandler;
import org.fugerit.java.doc.playground.RestHelper;
import org.fugerit.java.doc.playground.config.InitPlayground;
import org.fugerit.java.doc.playground.facade.BasicInput;
import org.fugerit.java.doc.playground.facade.InputFacade;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
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

	private void doHandle( DocTypeHandler handler, String type, int sourceType, Reader reader, ByteArrayOutputStream baos ) {
		SafeFunction.apply( () -> {
			DocInput docInput = DocInput.newInput( type, reader , sourceType );
			DocOutput docOutput = DocOutput.newOutput( baos );
			handler.handle(docInput, docOutput);
		} );
	}
	
	private void handleConfiguration( Configuration configuration, String freemarkerJsonData, String ftlData, String chainId ) {
		StringTemplateLoader loader = new StringTemplateLoader();
		String chainData = "<#assign ftlData = "+freemarkerJsonData+">"+ftlData;
		loader.putTemplate( chainId , chainData );
		configuration.setTemplateLoader( loader );
	}
	
	private void handleFtlx( DocTypeHandler handler, String type, int sourceType, Reader reader, ByteArrayOutputStream baos, String freemarkerJsonData ) {
		SafeFunction.apply( () -> {
			// volatile FreeMarker Template configuration
			String chainId = "current_"+System.currentTimeMillis();
			Configuration configuration = new Configuration( new Version( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION_LATEST ) );
			ObjectMapper mapper = new ObjectMapper();
			try ( StringReader jsonReader = new StringReader(freemarkerJsonData) ) {
				JsonNode node = mapper.readTree( jsonReader ); // parse json node to sanitize input
				this.handleConfiguration(configuration, mapper.writeValueAsString( node ), StreamIO.readString( reader ), chainId );
				Template currentChain = configuration.getTemplate( chainId );
				Map<Object, Object> data = new HashMap<>();
				try ( StringWriter writer = new StringWriter() ) {
					currentChain.process( data , writer );
					try ( StringReader ftlReader = new StringReader( writer.toString() ) ) {
						this.doHandle(handler, type, sourceType, ftlReader, baos);
					}
				}
			}
			configuration.clearTemplateCache();
		} );
	}
	
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
				log.info( "output format : {}", type );
				if ( InputFacade.FORMAT_FTLX.equalsIgnoreCase( input.getInputFormat() ) ) {
					this.handleFtlx(handler, type, sourceType, reader, baos, input.getFreemarkerJsonData());
				} else {
					this.doHandle(handler, type, sourceType, reader, baos);
				}
				result =  baos.toByteArray();
			}
		}
		return result;
	}

	private DocTypeHandler findHandler( BasicInput input ) {
		DocTypeHandler handler = InitPlayground.PDF_FOP_TYPE_HANDLER;
		if ( "XLSX".equalsIgnoreCase( input.getOutputFormat() ) ) {
			handler = XlsxPoiTypeHandler.HANDLER;
		} else if ( "HTML".equalsIgnoreCase( input.getOutputFormat() ) ) {
			handler = FreeMarkerHtmlFragmentTypeHandler.HANDLER;
		} else if ( "PDFA".equalsIgnoreCase( input.getOutputFormat() ) ) {
			handler = InitPlayground.PDFA_FOP_TYPE_HANDLER;
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
		return RestHelper.defaultHandle( () -> {
			long time = System.currentTimeMillis();
			DocTypeHandler handler = this.findHandler(input);
			byte[] data = this.generateHelper(input, handler);
			GenerateOutput output = new GenerateOutput();
			output.setDocOutputBase64( Base64.getEncoder().encodeToString( data ) );
			output.setGenerationTime( CheckpointUtils.formatTimeDiffMillis( time , System.currentTimeMillis() ) );
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
    	errorRow.addCell( message );
        simpleTableModel.addRow( errorRow );
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/validate")
	public Response validate( GenerateInput input) {
		return RestHelper.defaultHandle( () -> {
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
		        this.addRow(simpleTableModel, 0, "result", "Document valid? : "+ ( Result.RESULT_CODE_OK == result.getResultCode() ) );
		        SimpleTableDocConfig docConfig = SimpleTableDocConfig.newConfig();
		        docConfig.processSimpleTable(simpleTableModel, handler, buffer);
				GenerateOutput output = new GenerateOutput();
				output.setDocOutputBase64( Base64.getEncoder().encodeToString( buffer.toByteArray() ) );
				return Response.ok().entity( output ).build();
			}
		}
		);
	}

}