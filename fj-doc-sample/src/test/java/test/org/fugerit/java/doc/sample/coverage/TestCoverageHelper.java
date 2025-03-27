package test.org.fugerit.java.doc.sample.coverage;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownBasicTypeHandler;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandler;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.fugerit.java.doc.mod.opencsv.OpenCSVTypeHandler;
import org.fugerit.java.doc.mod.poi.XlsxPoiTypeHandler;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestCoverageHelper {

	private static final DocTypeHandler[] HANDLERS = { SimpleMarkdownBasicTypeHandler.HANDLER,
			SimpleMarkdownExtTypeHandler.HANDLER, PdfFopTypeHandler.HANDLER,
			XlsxPoiTypeHandler.HANDLER, OpenCSVTypeHandler.HANDLER_UTF8, };

	private static final String[] TEMPLATES = { 
			"coverage/default_doc_alt.xml", 
			"coverage/default_doc_fail1.xml",
			"coverage/default_doc.xml" };

	@Test
	void testHandlers() {
		Arrays.asList(HANDLERS).stream().forEach(handler -> {
			Arrays.asList(TEMPLATES).stream().forEach(template -> {
				SafeFunction.apply(() -> {
					log.info( "covering {} -> {}", handler, template );
					try (InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader(template) );
							OutputStream os = new ByteArrayOutputStream() ) {
						SafeFunction.apply( 
								() -> handler.handle( DocInput.newInput( handler.getType() , reader) , DocOutput.newOutput( os ) ),
								SafeFunction.EX_CONSUMER_LOG_WARN );
					}
				});

			});
		});
	}

}
