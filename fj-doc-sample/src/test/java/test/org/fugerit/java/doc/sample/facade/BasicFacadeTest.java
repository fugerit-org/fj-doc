package test.org.fugerit.java.doc.sample.facade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.checkpoint.CheckpointFormatHelper;
import org.fugerit.java.core.util.checkpoint.Checkpoints;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.facade.DocHandlerFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

public class BasicFacadeTest {

	protected final static Logger logger = LoggerFactory.getLogger( BasicFacadeTest.class );
	
	public static final String BASIC_OUTPUT_PATH = "target/sample_out";
	
	private static final boolean VALIDATE_DEFAULT = true;
	
	private String nameBase;
	
	private String facadeId;
	
	private List<String> types;
	
	protected Checkpoints checkpoints;
	
	private boolean validate;
	
	public BasicFacadeTest() {
		this( "basic", DocConfig.TYPE_PDF, DocConfig.TYPE_XLS, DocConfig.TYPE_HTML );
		this.validate = VALIDATE_DEFAULT;
	}
	
	protected BasicFacadeTest( String nameBase, String ...typeList ) {
		this.checkpoints = Checkpoints.newInstance( CheckpointFormatHelper.DEFAULT_DECORATION );
		this.nameBase = nameBase;
		this.types = new ArrayList<>();
		for ( String current : typeList ) {
			types.add( current );
		}
		this.validate = VALIDATE_DEFAULT;
	}
	
	protected void setValidate(boolean validate) {
		this.validate = validate;
	}

	protected void setFacadeId(String facadeId) {
		this.facadeId = facadeId;
	}
	
	protected String getFacadeId() {
		return facadeId;
	}

	public String getNameBase() {
		return this.nameBase;
	}
	
	private static FreemarkerDocProcessConfig init() {
		FreemarkerDocProcessConfig config = null;
		try ( InputStreamReader xmlReader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "config/freemarker-doc-process.xml" ) ) ) {
			config = FreemarkerDocProcessConfigFacade.loadConfig( xmlReader );
		} catch (Exception e) {
			throw new ConfigRuntimeException( "Exception on init : "+e, e );
		}
		return config;
	}
	
	@Getter protected static FreemarkerDocProcessConfig PROCESSCONFIG = init();
	
	private int getSourceType() {
		int sourceType = DocFacadeSource.SOURCE_TYPE_DEFAULT;
		if ( this.getNameBase().endsWith( "json" ) ) {
			sourceType = DocFacadeSource.SOURCE_TYPE_JSON;
		} else if ( this.getNameBase().endsWith( "yaml" ) ) {
			sourceType = DocFacadeSource.SOURCE_TYPE_YAML;
		}
		return sourceType;
	}
	
	private String getXmlPath() {
		String baseName = this.getNameBase();
		if ( this.getSourceType() == DocFacadeSource.SOURCE_TYPE_XML ) {
			baseName+= ".xml";
		}
		return "src/main/resources/sample_docs/junit_base/"+baseName;
	}
	
	protected Reader getXmlReader() throws Exception {
		return new FileReader( this.getXmlPath() );
	}
	
	
	protected DocBase getDocBase() throws Exception {
		// required : parsing the XML for model to be passed to DocFacade
		DocBase docBase = null;
		if ( this.validate ) {
			try ( Reader reader = this.getXmlReader() ) {
				DocParser docParser = DocFacadeSource.getInstance().getParserForSource( this.getSourceType() );
				DocValidationResult result = docParser.validateVersionResult( reader );
				for ( String message : result.getInfoList() ) {
					logger.info( "Validation info {}", message );
				}
				if ( result.isResultOk() ) {
					logger.info( "Validation ok {}", result );
				} else {
					logger.info( "Validation ko {}", result );
					for ( String message : result.getErrorList() ) {
						logger.info( "Validation error {}", message );
					}
					throw new DocException( "Validation KO : "+result.getResultCode() );
				}
			}
		}
		try ( Reader reader = this.getXmlReader() ) {
			long start = System.currentTimeMillis();
			docBase = DocFacadeSource.getInstance().parse( reader, getSourceType() );
			this.checkpoints.addCheckpointFromStartTime( "PARSE", start );
		}
		return docBase;
	}
	
	public void produce( File outputFolder, String facadeId, DocBase doc, Reader reader, String baseName, String format ) throws Exception {
		DocHandlerFacade facade = PROCESSCONFIG.getFacade();
		DocTypeHandler handler = facade.findHandler( format );
		StringBuilder append = new StringBuilder();
		if ( handler == null ) {
			throw new ConfigException( "No handler with id : "+format );
		} else if ( !handler.getType().equalsIgnoreCase( format ) ) {
			append.append( "_" );
			append.append( handler.getModule() );
		}
		append.append( "." );
		append.append( handler.getType() );
		if ( !handler.getType().equalsIgnoreCase( handler.getFormat() ) ) {
			baseName = baseName+"_"+handler.getFormat().replaceAll( "/" , "_");
		}
		File file = new File( outputFolder, baseName + append.toString() );
		logger.info("Create file {}", file.getCanonicalPath());
		try (FileOutputStream fos = new FileOutputStream(file)) {
			long start = System.currentTimeMillis();
			DocInput input = DocInput.newInput( format , reader, this.getSourceType() );
			DocOutput output = DocOutput.newOutput( fos );
			facade.handle( input , output );
			this.checkpoints.addCheckpointFromStartTime( "PRODUCE-"+format, start );
		}
	}
	
	@Test
	public void produce() throws Exception {
		File baseFile = new File(BASIC_OUTPUT_PATH);
		if (!baseFile.exists()) {
			logger.info("Create base path : {} ({})", baseFile.mkdirs(), baseFile.getCanonicalPath());
		}
		DocBase doc = this.getDocBase();
		for (String type : this.types) {
			produce( baseFile, this.getFacadeId(), doc, this.getXmlReader(), this.getNameBase(), type);
		}
		this.checkpoints.printInfo();
	}
	
}
