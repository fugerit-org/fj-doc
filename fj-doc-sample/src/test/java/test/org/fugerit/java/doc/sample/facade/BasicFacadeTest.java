package test.org.fugerit.java.doc.sample.facade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.util.checkpoint.CheckpointFormatHelper;
import org.fugerit.java.core.util.checkpoint.Checkpoints;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.facade.DocHandlerFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.json.parse.DocJsonFacade;
import org.fugerit.java.doc.sample.facade.SampleFacade;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicFacadeTest {

	protected final static Logger logger = LoggerFactory.getLogger( BasicFacadeTest.class );
	
	public static final String BASIC_OUTPUT_PATH = "target/sample_out";
	
	private String nameBase;
	
	private String facadeId;
	
	private List<String> types;
	
	protected Checkpoints checkpoints;
	
	public BasicFacadeTest() {
		this( "basic", DocConfig.TYPE_PDF, DocConfig.TYPE_XLS, DocConfig.TYPE_HTML );
	}
	
	protected BasicFacadeTest( String nameBase, String ...typeList ) {
		this.checkpoints = Checkpoints.newInstance( CheckpointFormatHelper.DEFAULT_DECORATION );
		this.nameBase = nameBase;
		this.types = new ArrayList<>();
		for ( String current : typeList ) {
			types.add( current );
		}
		this.setFacadeId( SampleFacade.MAIN_FACTORY );
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
	
	private boolean isJson() {
		return this.getNameBase().endsWith( "json" );
	}
	
	private String getXmlPath() {
		String baseName = this.getNameBase();
		if ( !this.isJson() ) {
			baseName+= ".xml";
		}
		return "src/test/resources/sample_docs/"+baseName;
	}
	
	protected Reader getXmlReader() throws Exception {
		return new FileReader( this.getXmlPath() );
	}
	
	
	protected DocBase getDocBase() throws Exception {
		// required : parsing the XML for model to be passed to DocFacade
		DocBase docBase = null;
		try ( Reader reader = this.getXmlReader() ) {
			long start = System.currentTimeMillis();
			docBase = null;
			if ( this.isJson() ) {
				docBase = DocJsonFacade.parse( reader );
			} else {
				docBase = DocFacade.parse( reader );
			}
			this.checkpoints.addCheckpointFromStartTime( "PARSE", start );
		}
		return docBase;
	}
	
	public void produce( File outputFolder, String facadeId, DocBase doc, Reader reader, String baseName, String type ) throws Exception {
		DocHandlerFacade facade = SampleFacade.getFacade( facadeId );
		DocTypeHandler handler = facade.findHandler( type );
		StringBuilder append = new StringBuilder();
		if ( handler == null ) {
			throw new ConfigException( "No handler with id : "+type );
		} else if ( !handler.getType().equalsIgnoreCase( type ) ) {
			append.append( "_" );
			append.append( handler.getModule() );
		}
		append.append( "." );
		append.append( handler.getType() );
		File file = new File( outputFolder, baseName + append.toString() );
		logger.info("Create file {}", file.getCanonicalPath());
		try (FileOutputStream fos = new FileOutputStream(file)) {
			long start = System.currentTimeMillis();
			DocInput input = null;
			if ( this.isJson() ) {
				input = DocInput.newInput( type , DocJsonFacade.parse( reader ) );
			} else { 
				DocInput.newInput( type , reader );
			}
			DocOutput output = DocOutput.newOutput( fos );
			facade.handle( input , output );
			this.checkpoints.addCheckpointFromStartTime( "PRODUCE-"+type, start );
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
