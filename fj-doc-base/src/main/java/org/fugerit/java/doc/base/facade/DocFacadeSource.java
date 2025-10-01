package org.fugerit.java.doc.base.facade;

import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.feature.DocFeatureRuntimeException;
import org.fugerit.java.doc.base.feature.FeatureConfig;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.DocConvert;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.base.xml.DocXMLUtils;
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

	public static final int SOURCE_TYPE_KOTLIN = 9;

	public static final int SOURCE_TYPE_DEFAULT = SOURCE_TYPE_XML;
	
	private static final Logger logger = LoggerFactory.getLogger( DocFacadeSource.class );
	
	private static final DocFacadeSource DEFAULT_INSTANCE = new DocFacadeSource();
	
	public static DocFacadeSource getInstance() {
		return DEFAULT_INSTANCE;
	}
	
	private static final String TYPE_SOURCE_XML = "org.fugerit.java.doc.base.xml.DocXmlParser";			// xml parser is always supported
	
	private static final String TYPE_SOURCE_JSON = "org.fugerit.java.doc.json.parse.DocJsonParser";		// json parser may not be in class loader
	
	private static final String TYPE_SOURCE_YAML = "org.fugerit.java.doc.yaml.parse.DocYamlParser";		// yaml parser may not be in class loader
	
	private static final String TYPE_SOURCE_JSON_NG = "org.fugerit.java.doc.json.ng.DocJsonParserNG";   // json parser NG may not be in class loader

	private static final String TYPE_SOURCE_KOTLIN = "org.fugerit.java.doc.base.kotlin.parse.DocKotlinParser";	// kotlin parser may not be in class loader

	private static final Properties PARSERS = new Properties();
	static {
		PARSERS.setProperty( String.valueOf( SOURCE_TYPE_XML ) , TYPE_SOURCE_XML );
		PARSERS.setProperty( String.valueOf( SOURCE_TYPE_JSON ) , TYPE_SOURCE_JSON );
		PARSERS.setProperty( String.valueOf( SOURCE_TYPE_YAML ) , TYPE_SOURCE_YAML );
		PARSERS.setProperty( String.valueOf( SOURCE_TYPE_JSON_NG ) , TYPE_SOURCE_JSON_NG );
		PARSERS.setProperty( String.valueOf( SOURCE_TYPE_KOTLIN ) , TYPE_SOURCE_KOTLIN );
	}

	private static String toConvertStringKey( int fromSourceType, int toSourceType ) {
		return String.format( "%s_to_%s", fromSourceType, toSourceType );
	}

	private static final Properties CONVERTERS = new Properties();
	static {
		CONVERTERS.setProperty( String.format( toConvertStringKey( DocFacadeSource.SOURCE_TYPE_XML, DocFacadeSource.SOURCE_TYPE_JSON ) ) , "org.fugerit.java.doc.json.parse.DocXmlToJson" );
		CONVERTERS.setProperty( String.format( toConvertStringKey( DocFacadeSource.SOURCE_TYPE_XML, DocFacadeSource.SOURCE_TYPE_YAML ) ) , "org.fugerit.java.doc.yaml.parse.DocXmlToYaml" );
		CONVERTERS.setProperty( String.format( toConvertStringKey( DocFacadeSource.SOURCE_TYPE_JSON, DocFacadeSource.SOURCE_TYPE_XML ) ) , "org.fugerit.java.doc.json.parse.DocJsonToXml" );
		CONVERTERS.setProperty( String.format( toConvertStringKey( DocFacadeSource.SOURCE_TYPE_JSON, DocFacadeSource.SOURCE_TYPE_YAML ) ) , "org.fugerit.java.doc.yaml.parse.DocJsonToYaml" );
		CONVERTERS.setProperty( String.format( toConvertStringKey( DocFacadeSource.SOURCE_TYPE_YAML, DocFacadeSource.SOURCE_TYPE_XML ) ) , "org.fugerit.java.doc.yaml.parse.DocYamlToXml" );
		CONVERTERS.setProperty( String.format( toConvertStringKey( DocFacadeSource.SOURCE_TYPE_YAML, DocFacadeSource.SOURCE_TYPE_JSON ) ) , "org.fugerit.java.doc.yaml.parse.DocYamlToJson" );
		CONVERTERS.setProperty( String.format( toConvertStringKey( DocFacadeSource.SOURCE_TYPE_XML, DocFacadeSource.SOURCE_TYPE_XML ) ) , ReflectiveDocConvert.class.getName() );
		CONVERTERS.setProperty( String.format( toConvertStringKey( DocFacadeSource.SOURCE_TYPE_JSON, DocFacadeSource.SOURCE_TYPE_JSON ) ) , ReflectiveDocConvert.class.getName() );
		CONVERTERS.setProperty( String.format( toConvertStringKey( DocFacadeSource.SOURCE_TYPE_YAML, DocFacadeSource.SOURCE_TYPE_YAML ) ) , ReflectiveDocConvert.class.getName() );
	}
	
	public DocParser getParserForSource( int sourceType ) {
		DocParser parser = null;
		try {
			String type = PARSERS.getProperty( String.valueOf( sourceType ) );
			if ( type != null ) {
				parser = (DocParser) ClassHelper.newInstance( type );
			}
		} catch (Exception e) {
			if ( this.getDocFacadeSourceConfig().isFailOnSourceModuleNotFound() ) {
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

	public DocBase parseRE(Reader reader, int sourceType, FeatureConfig featureConfig) {
		DocBase docBase = null;
		try {
			docBase = this.parse(reader, sourceType, featureConfig);
		} catch (DocFeatureRuntimeException e) {
			throw e;
		} catch (DocException e) {
			throw new ConfigRuntimeException( e );
		}
		return docBase;
	}

	public DocBase parseRE( Reader reader, int sourceType ) {
		return this.parseRE( reader, sourceType, FeatureConfig.DEFAULT );
	}
	
	public DocBase parse( Reader reader, int sourceType ) throws DocException {
		return this.parse( reader, sourceType, FeatureConfig.DEFAULT );
	}

	public DocBase parse( Reader reader, int sourceType, FeatureConfig featureConfig ) throws DocException {
		DocBase docBase = null;
		DocParser parser = this.getParserForSource(sourceType);
		if ( parser == null ) {
			throw new DocException( "No parser found for source type : "+sourceType );
		} else {
			parser.setFeatureConfig( featureConfig );
			docBase = parser.parse(reader);
		}
		return docBase;
	}

	public static DocConvert findDocConvert( int fromSourceType, int toSourceType ) {
		String docConvertType = CONVERTERS.getProperty( toConvertStringKey( fromSourceType, toSourceType ) );
		if ( docConvertType == null ) {
			throw new ConfigRuntimeException( String.format( "Converter from %s to %s not found.", fromSourceType, toSourceType ) );
		} else {
			logger.debug( "doc convert found : {}", docConvertType );
			return SafeFunction.get( () -> (DocConvert)ClassHelper.newInstance( docConvertType ) );
		}
	}

	public void convert(Reader from, int fromSourceType, Writer to, int toSourceType ) {
		if ( isSourceSupported( fromSourceType ) && isSourceSupported( toSourceType ) ) {
			SafeFunction.apply( () -> findDocConvert( fromSourceType, toSourceType ).convert( from, to ) );
		} else if ( isSourceSupported( fromSourceType ) ) {
			throw new ConfigRuntimeException( String.format( "Unsupported from source type: %s", toSourceType ) );
		} else {
			throw new ConfigRuntimeException( String.format( "Unsupported to source type: %s", fromSourceType ) );
		}
	}

	public static Reader cleanSource( Reader source, int sourceType ) {
		if ( sourceType == DocFacadeSource.SOURCE_TYPE_XML ) {
			return new StringReader(DocXMLUtils.cleanXML(SafeFunction.get(()->StreamIO.readString(source))));
		} else {
			return source;
		}
	}

	public static String cleanSource( String source, int sourceType ) {
		try ( StringReader reader = new StringReader( source) ) {
			return SafeFunction.get(()->StreamIO.readString(cleanSource( reader, sourceType )));
		}
	}

}
