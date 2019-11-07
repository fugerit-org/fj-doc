package test.org.fugerit.java.doc.sample.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.sample.facade.SampleFacade;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicFacadeTest {

	public static final String TYPE_XML = "xml";
	
	public static final String TYPE_PDF = "pdf";
	
	public static final String TYPE_RTF = "rtf";
	
	public static final String TYPE_HTML = "html";
	
	public static final String TYPE_XLS = "xls";
	
	protected final static Logger logger = LoggerFactory.getLogger( BasicFacadeTest.class );
	
	private static final String BASIC_OUTPUT_PATH = "target/sample_out";
	
	private String nameBase;
	
	private List<String> types;
	
	public BasicFacadeTest() {
		this( "basic", TYPE_PDF, TYPE_XLS, TYPE_RTF, TYPE_HTML );
	}
	
	protected BasicFacadeTest( String nameBase, String ...typeList ) {
		this.nameBase = nameBase;
		this.types = new ArrayList<>();
		for ( String current : typeList ) {
			types.add( current );
		}
	}
	
	public String getNameBase() {
		return this.nameBase;
	}
	
	protected String getXmlPath() {
		return "src/test/resources/sample_docs/"+this.getNameBase()+".xml";
	}
	
	protected DocBase getDocBase() throws Exception {
		DocBase docBase = null;
		try ( FileInputStream is = new FileInputStream( this.getXmlPath() ) ) {
			 docBase = DocFacade.parse( is );
		}
		return docBase;
	}
	
	@Test
	public void produce() throws Exception {
		File baseFile = new File(BASIC_OUTPUT_PATH);
		if (!baseFile.exists()) {
			logger.info("Create base path : {} ({})", baseFile.mkdirs(), baseFile.getCanonicalPath());
		}
		DocBase docBase = this.getDocBase();
		for (String type : this.types) {
			File file = new File(baseFile, this.getNameBase() + "." + type);
			logger.info("Create file {}", file.getCanonicalPath());
			try (FileOutputStream fos = new FileOutputStream(file)) {
				if (TYPE_PDF.equalsIgnoreCase(type)) {
					SampleFacade.createPDF(docBase, fos);
				} else if (TYPE_HTML.equalsIgnoreCase(type)) {
					SampleFacade.createHTML(docBase, fos);
				} else if (TYPE_RTF.equalsIgnoreCase(type)) {
					SampleFacade.createRTF(docBase, fos);
				} else if (TYPE_XLS.equalsIgnoreCase(type)) {
					SampleFacade.createXLS(docBase, fos);
				} else {
					throw new Exception("Unsupported type : " + type);
				}
			}
		}
	}
	
}
