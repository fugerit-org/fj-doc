package test.org.fugerit.java.doc.sample.facade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.facade.DocHandlerFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.sample.facade.SampleFacade;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicFacadeTest {

	protected final static Logger logger = LoggerFactory.getLogger( BasicFacadeTest.class );
	
	private static final String BASIC_OUTPUT_PATH = "target/sample_out";
	
	private String nameBase;
	
	private String facadeId;
	
	private List<String> types;
	
	public BasicFacadeTest() {
		this( "basic", DocConfig.TYPE_PDF, DocConfig.TYPE_XLS, DocConfig.TYPE_RTF, DocConfig.TYPE_HTML );
	}
	
	protected BasicFacadeTest( String nameBase, String ...typeList ) {
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
	
	private String getXmlPath() {
		return "src/test/resources/sample_docs/"+this.getNameBase()+".xml";
	}
	
	protected Reader getXmlReader() throws Exception {
		return new FileReader( this.getXmlPath() );
	}
	
	protected DocBase getDocBase() throws Exception {
		// required : parsing the XML for model to be passed to DocFacade
		DocBase docBase = null;
		try ( Reader reader = this.getXmlReader() ) {
			 docBase = DocFacade.parse( reader );
		}
		return docBase;
	}
	
	public static void produce( File outputFolder, String facadeId, DocBase doc, Reader reader, String baseName, String type ) throws Exception {
		File file = new File( outputFolder, baseName + "." + type);
		logger.info("Create file {}", file.getCanonicalPath());
		try (FileOutputStream fos = new FileOutputStream(file)) {
			DocInput input = DocInput.newInput( type , reader );
			DocOutput output = DocOutput.newOutput( fos );
			DocHandlerFacade facade = SampleFacade.getFacade( facadeId );
			facade.handle( input , output );
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
	}
	
}
