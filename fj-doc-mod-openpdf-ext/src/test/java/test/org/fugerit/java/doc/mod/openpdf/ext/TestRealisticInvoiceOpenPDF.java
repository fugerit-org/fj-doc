package test.org.fugerit.java.doc.mod.openpdf.ext;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.doc.base.config.DocConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestRealisticInvoiceOpenPDF extends TestDocBase {

	private static final String REALISTIC_INVOICE = "realistic_invoice";

	@Test
	void testOpenPDFRealisticInvoice() {
		boolean ok = this.testDocWorker( REALISTIC_INVOICE, DocConfig.TYPE_PDF );
		Assertions.assertTrue( ok );
		File pdfFile = new File( "target", REALISTIC_INVOICE + "." + DocConfig.TYPE_PDF );
		Assertions.assertTrue( pdfFile.exists() );
		Assertions.assertTrue( pdfFile.length() > 0 );
		File comparisonFile = new File( "target", REALISTIC_INVOICE + "-openpdf.pdf" );
		SafeFunction.apply( () -> Files.copy( pdfFile.toPath(), comparisonFile.toPath(), StandardCopyOption.REPLACE_EXISTING ) );
		log.info( "Rendered OpenPDF invoice to {}, size: {} bytes", pdfFile.getAbsolutePath(), pdfFile.length() );
		SafeFunction.apply( () -> {
			try ( PDDocument pdfDoc = PDDocument.load( pdfFile ) ) {
				PDFTextStripper stripper = new PDFTextStripper();
				String pdfText = stripper.getText( pdfDoc );
				Assertions.assertTrue( pdfText.contains( "INVOICE #INV-2026-0685" ) );
				Assertions.assertTrue( pdfText.contains( "Acme Cloud Integration Ltd." ) );
				Assertions.assertTrue( pdfText.contains( "Enterprise Corp S.p.A." ) );
				Assertions.assertTrue( pdfText.contains( "11,150.00" ) );
				Assertions.assertTrue( pdfText.contains( "Page 1 / 1" ) );
			}
		} );
	}

}
