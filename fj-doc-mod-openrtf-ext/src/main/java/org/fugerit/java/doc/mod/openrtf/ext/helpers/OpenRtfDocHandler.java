package org.fugerit.java.doc.mod.openrtf.ext.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocFooter;
import org.fugerit.java.doc.base.model.DocHeader;
import org.fugerit.java.doc.base.model.DocHeaderFooter;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPdfHelper;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPpfDocHandler;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.PdfHelper;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.headerfooter.RtfHeaderFooter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenRtfDocHandler extends OpenPpfDocHandler {

	public OpenRtfDocHandler( Document document, RtfWriter2 rtfWriter2 ) {
		super( document, DOC_OUTPUT_RTF );
		log.trace( "currently unused parameter rtfWriter2 {}", rtfWriter2 );
	}	
	
	@Override
	protected void handleHeaderExt( DocHeader docHeader, PdfHelper pdfHelper, OpenPdfHelper docHelper ) throws DocumentException {
		this.document.setHeader( new RtfHeaderFooter( createRtfHeaderFooter( docHeader , this.document, true, docHelper ) ) );
	}
	
	@Override
	protected void handleFooterExt( DocFooter docFooter, PdfHelper pdfHelper, OpenPdfHelper docHelper ) throws DocumentException {
		this.document.setFooter( new RtfHeaderFooter( createRtfHeaderFooter( docFooter , this.document, false, docHelper ) ) );
	}
	
	private static RtfHeaderFooter createRtfHeaderFooter( DocHeaderFooter docHeaderFooter, Document document, boolean header, OpenPdfHelper docHelper ) throws DocumentException {
		List<DocElement> list = new ArrayList<>();
		Iterator<DocElement> itDoc = docHeaderFooter.docElements();
		while ( itDoc.hasNext() ) {
			list.add( itDoc.next() );
		}
		Element[] e = new Element[ list.size() ];
		SafeFunction.apply( () -> {
			for ( int k=0; k<list.size(); k++ ) {
				e[k] = getElement( document, list.get( k ) , false, docHelper );
			}	
		});
		RtfHeaderFooter rtfHeaderFooter = new RtfHeaderFooter( e );
		rtfHeaderFooter.setDisplayAt( RtfHeaderFooter.DISPLAY_ALL_PAGES );
		if ( header ) {
			rtfHeaderFooter.setType( RtfHeaderFooter.TYPE_HEADER );	
		} else {
			rtfHeaderFooter.setType( RtfHeaderFooter.TYPE_FOOTER );
		}
		return rtfHeaderFooter;
	}
	
}
