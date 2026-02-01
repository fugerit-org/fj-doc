package org.fugerit.java.doc.val.pdf.box3;

import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;

public class Pdfbox3Utils {
	
	private Pdfbox3Utils() {}

	public static PDDocument parse( InputStream is, boolean lenient ) throws IOException {
		try( RandomAccessReadBuffer raFile = new RandomAccessReadBuffer( is ) ) {
			PDFParser parser = new PDFParser(raFile);
			return parser.parse( lenient );
		}
	}
	
}
