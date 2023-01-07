package test.org.fugerit.java.doc.lib.autodoc.facade;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.fugerit.java.doc.lib.autodoc.AutodocDocConfig;
import org.fugerit.java.doc.lib.autodoc.detail.AutodocDetailModel;
import org.fugerit.java.doc.lib.autodoc.detail.model.AutodocDetail;
import org.fugerit.java.doc.lib.autodoc.facade.AutodocDetailFacade;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestAutodocDocConfigDetail {

	private static final Logger logger = LoggerFactory.getLogger( TestAutodocDocConfigDetail.class );
	
	private static final String SOURCE = "../fj-doc-freemarker/src/main/resources/autodoc_detail/autodoc-detail-html.xml";
	
	private static final String TARGET = "target/autodoc_detail.html";
	
	@Test
	public void testGenerateAutodocDetail() {
		try ( FileInputStream fis = new FileInputStream( new File( SOURCE ) );
				FileOutputStream fos = new FileOutputStream( new File( TARGET ) ) )  {
			AutodocDetailFacade facade = AutodocDetailFacade.getInstance();
			AutodocDetail autodocDetail = facade.unmarshal( fis );
			AutodocDetailModel autodocDetailModel = new AutodocDetailModel(autodocDetail);
			AutodocDocConfig docConfig = AutodocDocConfig.newConfig();
			docConfig.processAutodocDetailHtmlDefault(autodocDetailModel, fos);
		} catch (Exception e) {
			String message = "Error : "+e;
			logger.error( message, e );
			fail( message );
		}
	}
	
}
