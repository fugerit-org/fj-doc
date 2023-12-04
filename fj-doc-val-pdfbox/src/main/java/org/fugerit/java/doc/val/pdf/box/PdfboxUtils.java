package org.fugerit.java.doc.val.pdf.box;

import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfboxUtils {
	
	private PdfboxUtils() {}

	public static PDDocument parse( InputStream is, boolean lenient ) throws IOException {
		try( RandomAccessBufferedFileInputStream raFile = new RandomAccessBufferedFileInputStream( is ) ) {
			PDFParser parser = new PDFParser(raFile);
			parser.setLenient( lenient );
			parser.parse();
			return parser.getPDDocument();
		}
	}
	
}
