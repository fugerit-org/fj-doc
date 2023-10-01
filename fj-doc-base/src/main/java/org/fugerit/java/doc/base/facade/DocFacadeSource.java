package org.fugerit.java.doc.base.facade;

import java.io.Reader;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.base.xml.DocXmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

public class DocFacadeSource {

	@Getter private DocFacadeSourceConfig docFacadeSourceConfig;
	
	public DocFacadeSource(DocFacadeSourceConfig docFacadeSourceConfig) {
		super();
		this.docFacadeSourceConfig = docFacadeSourceConfig;
	}

	public DocFacadeSource() {
		this( DocFacadeSourceConfig.DEFAULT_CONFIG );
	}
	
	public static final int SOURCE_TYPE_XML = 1;
	
	public static final int SOURCE_TYPE_JSON = 2;
	
	public static final int SOURCE_TYPE_YAML = 3;
	
	public static final int SOURCE_TYPE_JSON_NG = 4;
	
	public static final int SOURCE_TYPE_DEFAULT = SOURCE_TYPE_XML;
	
	private static final Logger logger = LoggerFactory.getLogger( DocFacadeSource.class );
	
	private static final DocFacadeSource DEFAULT_INSTANCE = new DocFacadeSource();
	
	public static DocFacadeSource getInstance() {
		return DEFAULT_INSTANCE;
	}
	
	private static final String TYPE_SOURCE_XML = DocXmlParser.class.getName();							// xml parser is always supported
	
	private static final String TYPE_SOURCE_JSON = "org.fugerit.java.doc.json.parse.DocJsonParser";		// json parser may not be in class loader
	
	private static final String TYPE_SOURCE_YAML = "org.fugerit.java.doc.yaml.parse.DocYamlParser";		// yaml parser may not be in class loader
	
	private static final String TYPE_SOURCE_JSON_NG = "org.fugerit.java.doc.json.ng.DocJsonParserNG";   // json parser NG may not be in class loader
	
	private static final Properties PARSERS = new Properties();
	static {
		PARSERS.setProperty( String.valueOf( SOURCE_TYPE_XML ) , TYPE_SOURCE_XML );
		PARSERS.setProperty( String.valueOf( SOURCE_TYPE_JSON ) , TYPE_SOURCE_JSON );
		PARSERS.setProperty( String.valueOf( SOURCE_TYPE_YAML ) , TYPE_SOURCE_YAML );
		PARSERS.setProperty( String.valueOf( SOURCE_TYPE_JSON_NG ) , TYPE_SOURCE_JSON_NG );
	}
	
	public DocParser getParserForSource( int sourceType ) {
		DocParser parser = null;
		try {
			String type = PARSERS.getProperty( String.valueOf( sourceType ) );
			if ( type != null ) {
				parser = (DocParser) ClassHelper.newInstance( type );
			}
		} catch (Exception e) {
			if ( this.docFacadeSourceConfig.isFailOnSourceModuleNotFound() ) {
				throw new ConfigRuntimeException( "Exception getting parser for sourceType : "+sourceType+" - "+e, e );
			} else {
				logger.warn( "Failed to load parser for source type : {} ({})", sourceType, e.toString() );
			}
		}
		return parser;
	}
	
	public boolean isSourceSupported( int sourceType ) {
		return ( getParserForSource(sourceType) != null );
	}
	
	public DocBase parseRE( Reader reader, int sourceType ) {
		DocBase docBase = null;
		try {
			docBase = this.parse(reader, sourceType);
		} catch (DocException e) {
			throw new ConfigRuntimeException( e );
		}
		return docBase;
	}
	
	public DocBase parse( Reader reader, int sourceType ) throws DocException {
		DocBase docBase = null;
		DocParser parser = this.getParserForSource(sourceType);
		if ( parser == null ) {
			throw new DocException( "No parser found for source type : "+sourceType );
		} else {
			docBase = parser.parse(reader);
		}
		return docBase;
	}
	
}
