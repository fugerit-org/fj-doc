package org.fugerit.java.doc.mod.pdfbox.utils;

import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.fugerit.java.core.log.LogFacade;

public class PdfCheckUtils {

	public static boolean isPdf( InputStream pdfStream ) {
		boolean ok = false;
		try {
			PDDocument.load( pdfStream );
			ok = true;
		} catch (Exception e) {
			LogFacade.getLog().warn( "stream is not pdf : "+e, e );
		}
		return ok;
	}
	
}
