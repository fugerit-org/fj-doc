package org.fugerit.java.doc.playground.doc;

import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.checkpoint.SimpleCheckpoint;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;
import org.fugerit.java.doc.freemarker.fun.SimpleMessageFun;
import org.fugerit.java.doc.playground.InitPlayground;
import org.fugerit.java.doc.playground.facade.BasicInput;
import org.fugerit.java.doc.playground.facade.InputFacade;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Slf4j
@ApplicationScoped
public class GenerateFacade {


	private static final String FTL_DIRECTIVE = "<#ftl";

	private void doHandle( DocTypeHandler handler, String type, int sourceType, Reader reader, ByteArrayOutputStream baos ) {
		SafeFunction.apply( () -> {
			SimpleCheckpoint checkpoint = new SimpleCheckpoint();
			DocInput docInput = DocInput.newInput( type, reader , sourceType );
			DocOutput docOutput = DocOutput.newOutput( baos );
			handler.handle(docInput, docOutput);
			log.info( "actual render, handler : {}, type : {}, sourceType : {}, time : {}", handler.getClass().getSimpleName(), type, sourceType, checkpoint.getFormatTimeDiffMillis() );
		} );
	}
	
	private void handleConfiguration( Configuration configuration, JsonNode node, String ftlData, String chainId ) {
		StringTemplateLoader loader = new StringTemplateLoader();
		StringBuilder chainData = new StringBuilder();
		ObjectNode oNode = (ObjectNode) node;
		Iterator<String> fieldNames = oNode.fieldNames();
		int ftlDirectiveStartIndex = ftlData.indexOf( FTL_DIRECTIVE );
		if ( ftlDirectiveStartIndex != -1 ) {
			int ftlDirectiveEndIndex = ftlData.indexOf( ">" );
			String ftlDirective = ftlData.substring( 0, ftlDirectiveEndIndex+1 );
			log.debug( "ftlDirective : {}", ftlDirective );
			chainData.append( ftlDirective );
			ftlData = ftlData.substring( ftlDirectiveEndIndex+1 );
		}
		while ( fieldNames.hasNext() ) {
			String currentName = fieldNames.next();
			String currentValue = node.get( currentName ).toString();
			log.debug( "handleConfiguration() set var {} -> {}", currentName, currentValue );
			chainData.append( "<#assign " );
			chainData.append( currentName );
			chainData.append( "=" );
			chainData.append( currentValue );
			chainData.append( ">");
		}
		chainData.append( ftlData );
		loader.putTemplate( chainId , chainData.toString() );
		configuration.setTemplateExceptionHandler( TemplateExceptionHandler.RETHROW_HANDLER );
		configuration.setTemplateLoader( loader );
	}

	private void handleFtlx( DocTypeHandler handler, String type, int sourceType, Reader reader, ByteArrayOutputStream baos, String freemarkerJsonData ) {
		SafeFunction.apply( () -> {
			SimpleCheckpoint checkpoint = new SimpleCheckpoint();
			// volatile FreeMarker Template configuration
			String chainId = "current_"+System.currentTimeMillis();
			Configuration configuration = new Configuration( new Version( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION_LATEST ) );
			log.info( "ftlx create configuration : {}", checkpoint.getFormatTimeDiffMillis() );
			ObjectMapper mapper = new ObjectMapper();
			try ( StringReader jsonReader = new StringReader(freemarkerJsonData) ) {
				JsonNode node = mapper.readTree( jsonReader ); // parse json node to sanitize input
				log.info( "read json : {}", checkpoint.getFormatTimeDiffMillis() );
				this.handleConfiguration(configuration, node, StreamIO.readString( reader ), chainId );
				log.info( "ftlx handle configuration : {}", checkpoint.getFormatTimeDiffMillis() );
				Template currentChain = configuration.getTemplate( chainId );
				log.info( "ftlx get template : {}", checkpoint.getFormatTimeDiffMillis() );
				Map<Object, Object> data = new HashMap<>();
				data.put( "messageFormat" , new SimpleMessageFun() );
				try ( StringWriter writer = new StringWriter() ) {
					currentChain.process( data , writer );
					log.info( "ftlx process chain : {}", checkpoint.getFormatTimeDiffMillis() );
					try ( StringReader ftlReader = new StringReader( writer.toString() ) ) {
						this.doHandle(handler, type, sourceType, ftlReader, baos);
						log.info( "ftlx render document : {}", checkpoint.getFormatTimeDiffMillis() );
					}
				}
			}
			configuration.clearTemplateCache();
		} );
	}

	private void handleKts( DocTypeHandler handler, String type, Reader reader, ByteArrayOutputStream baos, String ktsJsonData ) {
		SafeFunction.apply( () -> {
			SimpleCheckpoint checkpoint = new SimpleCheckpoint();
			ScriptEngineManager manager = new ScriptEngineManager();
			log.info( "kts create script manager : {}", checkpoint.getFormatTimeDiffMillis() );
			ScriptEngine engine =  manager.getEngineByExtension( "kts" );
			log.info( "kts create script engine : {}", checkpoint.getFormatTimeDiffMillis() );
			Bindings bindings = engine.createBindings();
			log.info( "kts create script bindings : {}", checkpoint.getFormatTimeDiffMillis() );
			ObjectMapper mapper = new ObjectMapper();
			try ( StringReader jsonReader = new StringReader(ktsJsonData) ) {
				LinkedHashMap<String, Object> data = mapper.readValue( jsonReader, LinkedHashMap.class );
				log.info( "kts read json data : {}", checkpoint.getFormatTimeDiffMillis() );
				bindings.put( "data", data );
				engine.setBindings( bindings, ScriptContext.ENGINE_SCOPE );
				log.info( "kts set bindings : {}", checkpoint.getFormatTimeDiffMillis() );
				Object obj = engine.eval( StreamIO.readString( reader ) );
				log.info( "kts eval script : {}", checkpoint.getFormatTimeDiffMillis() );
				String xml = obj.toString();
				log.info( "kts toXml : {}", checkpoint.getFormatTimeDiffMillis() );
				try ( StringReader xmlReader = new StringReader( xml) ) {
					this.doHandle(handler, type, DocFacadeSource.SOURCE_TYPE_XML, xmlReader, baos);
					log.info( "kts render document : {}", checkpoint.getFormatTimeDiffMillis() );
				}
			}
		} );
	}
	
	public byte[] generateHelper( GenerateInput input, DocTypeHandler handler) throws Exception {
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
				} else if ( InputFacade.FORMAT_KTS.equalsIgnoreCase( input.getInputFormat() ) ) {
					this.handleKts(handler, type, reader, baos, input.getFreemarkerJsonData());
				} else {
					this.doHandle(handler, type, sourceType, reader, baos);
				}
				result =  baos.toByteArray();
			}
		}
		return result;
	}

	public DocTypeHandler findHandler( BasicInput input ) {
		DocTypeHandler handler = null;
		if ( StringUtils.isNotEmpty( input.getOutputFormat() ) ) {
			String outputFormat = input.getOutputFormat().toLowerCase();
			handler = InitPlayground.findHandler( outputFormat );
		}
		return handler;
	}
	
	public DocParser findParser( BasicInput input ) {
		int sourceType = DocFacadeSource.SOURCE_TYPE_XML;
		if ( InputFacade.FORMAT_JSON.equalsIgnoreCase( input.getInputFormat() ) ) {
			sourceType = DocFacadeSource.SOURCE_TYPE_JSON;
		} else if ( InputFacade.FORMAT_YAML.equalsIgnoreCase( input.getInputFormat() ) ) {
			sourceType = DocFacadeSource.SOURCE_TYPE_YAML;
		}
		return DocFacadeSource.getInstance().getParserForSource( sourceType );
	}

}
