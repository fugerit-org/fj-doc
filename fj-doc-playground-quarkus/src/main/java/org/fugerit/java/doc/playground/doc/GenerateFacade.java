package org.fugerit.java.doc.playground.doc;

import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerXMLUTF8;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandler;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;
import org.fugerit.java.doc.freemarker.fun.SimpleMessageFun;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlFragmentTypeHandlerEscapeUTF8;
import org.fugerit.java.doc.mod.fop.FreeMarkerFopTypeHandler;
import org.fugerit.java.doc.mod.opencsv.OpenCSVTypeHandler;
import org.fugerit.java.doc.mod.poi.XlsxPoiTypeHandler;
import org.fugerit.java.doc.playground.config.InitPlayground;
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

@Slf4j
@ApplicationScoped
public class GenerateFacade {


	private static final String FTL_DIRECTIVE = "<#ftl";
	
	private void doHandle( DocTypeHandler handler, String type, int sourceType, Reader reader, ByteArrayOutputStream baos ) {
		SafeFunction.apply( () -> {
			DocInput docInput = DocInput.newInput( type, reader , sourceType );
			DocOutput docOutput = DocOutput.newOutput( baos );
			handler.handle(docInput, docOutput);
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
			// volatile FreeMarker Template configuration
			String chainId = "current_"+System.currentTimeMillis();
			Configuration configuration = new Configuration( new Version( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION_LATEST ) );
			ObjectMapper mapper = new ObjectMapper();
			try ( StringReader jsonReader = new StringReader(freemarkerJsonData) ) {
				JsonNode node = mapper.readTree( jsonReader ); // parse json node to sanitize input
				this.handleConfiguration(configuration, node, StreamIO.readString( reader ), chainId );
				Template currentChain = configuration.getTemplate( chainId );
				Map<Object, Object> data = new HashMap<>();
				data.put( "messageFormat" , new SimpleMessageFun() );
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
			switch (outputFormat) {
			case DocConfig.TYPE_XLSX:
				handler = XlsxPoiTypeHandler.HANDLER;
				break;
			case DocConfig.TYPE_HTML:
				handler = FreeMarkerHtmlFragmentTypeHandlerEscapeUTF8.HANDLER;
				break;
			case DocConfig.TYPE_XML:
				handler = DocTypeHandlerXMLUTF8.HANDLER;
				break;
			case DocConfig.TYPE_CSV:
				handler = OpenCSVTypeHandler.HANDLER_UTF8;
				break;
			case DocConfig.TYPE_MD:
				handler = SimpleMarkdownExtTypeHandler.HANDLER_NOCOMMENTS_UTF8;
				break;
			case DocConfig.TYPE_FO:
				handler = FreeMarkerFopTypeHandler.HANDLER_UTF8;
				break;
			case InitPlayground.OUTPUT_FORMAT_PDF_A:
				handler = InitPlayground.PDFA_FOP_TYPE_HANDLER;
				break;
			default:
				handler = InitPlayground.PDF_FOP_TYPE_HANDLER;
				break;
			}
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
