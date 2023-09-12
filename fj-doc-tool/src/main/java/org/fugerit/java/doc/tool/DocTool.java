package org.fugerit.java.doc.tool;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cli.ArgUtils;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.PropsIO;
import org.fugerit.java.doc.tool.handler.GenerateStubHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocTool {
	
	private DocTool() {}

	private static final String LINE = "----------------------------------------------------------";
	
	public static final String ARG_TOOL = "tool";
	
	public static final String ARG_HELP = "help";
	
	public static final String ARG_TOOL_GENERATE_STUB = "generate-stub";
	
	public static void handle( Properties params ) throws Exception {
		String toolHandler = params.getProperty( ARG_TOOL );
		if ( StringUtils.isEmpty( toolHandler ) ) {
			throw new ConfigException( "Required parameter : "+ARG_TOOL );
		}
		Consumer<Properties> handler = null;
		if ( ARG_TOOL_GENERATE_STUB.equalsIgnoreCase( toolHandler ) ) {
			handler = new GenerateStubHandler();
		} else {
			throw new ConfigException( "Unknown tool : "+toolHandler );
		}
		String helpParam = params.getProperty( ARG_HELP );
		if ( helpParam != null ) {
			String path = "help/"+toolHandler+".properties";
			Properties helpProps = PropsIO.loadFromClassLoader( path );
			List<String> keySet = helpProps.keySet().stream().map( m -> String.valueOf( m ) ).collect( Collectors.toList() );
			Collections.sort( keySet );
			log.info( LINE );
			log.info( "help params for tool : {}", toolHandler );
			log.info( LINE );
			for ( String k : keySet ) {
				log.info( "param : {} -> {}", k, helpProps.getProperty( k ) );
			}
			log.info( LINE );
		} else {
			handler.accept(params);	
		}
	}
	
	public static void main( String[] args ) {
		log.info( "START" );
		try {
			Properties params = ArgUtils.getArgs( args );
			handle(params);
		} catch (Exception e) {
			log.error( "Error : "+e, e );
		}
		log.info( "END" );
	}
	
}
