package test.org.fugerit.java.doc.lib.autodoc.meta;

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.lib.autodoc.AutodocDocConfig;
import org.fugerit.java.doc.lib.autodoc.facade.AutodocMetaFacade;
import org.fugerit.java.doc.lib.autodoc.meta.AutodocMetaModel;
import org.fugerit.java.doc.lib.autodoc.meta.model.AutodocMeta;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenerateDocHelper {

	protected boolean docWorker( String sourcePath, String destPath, DocTypeHandler handler ) {
		boolean ok = false;
		File destFile = new File( destPath );
		log.info( "handler {}, sourcePath : {}, destFile : {}", handler, sourcePath, destFile );
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( sourcePath );
				OutputStream os = new FileOutputStream( destFile );
				ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
			AutodocMeta autodocMeta = AutodocMetaFacade.unmarshal( is );
			AutodocMetaModel autodocMetaModel = new AutodocMetaModel(autodocMeta);
			AutodocDocConfig.getInstance().processAutodocMeta( autodocMetaModel , handler, os);
			// test marashall
			AutodocMetaFacade.marshal(autodocMeta, baos);
			ok = baos.toByteArray().length > 0;
		} catch (Exception e) {
			String message = "Error : "+e;
			log.error( message, e );
			fail( message );
		}
		return ok;
	}
	
}
