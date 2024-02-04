package org.fugerit.java.doc.mod.openpdf.ext.helpers;

import java.util.Iterator;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocFooter;
import org.fugerit.java.doc.base.model.DocHeader;
import org.fugerit.java.doc.base.model.DocPara;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This is the handler to use
 * 
 * @author fugerit79
 *
 */
public class PdfHelper  extends PdfPageEventHelper {
	
	public PdfHelper( OpenPdfHelper docHelper ) {
		this.docHelper = docHelper;
		this.docHeader = null;
		this.docFooter = null;
	}
	
	protected BaseFont baseFont;
    private PdfTemplate totalPages;
    private float footerTextSize = 8f;
    private int pageNumberAlignment = Element.ALIGN_CENTER;
 
    private OpenPdfHelper docHelper;
    
    private int currentPageNumber;

	private DocHeader docHeader;
	
	private DocFooter docFooter;
    
	@Override
    public void onStartPage(PdfWriter writer, Document document) {
    	this.currentPageNumber = writer.getPageNumber();
    	this.docHelper.getParams().setProperty( OpenPpfDocHandler.PARAM_PAGE_CURRENT , String.valueOf( writer.getPageNumber() ) );
		if ( this.getDocHeader() != null ) {
			OpenPpfDocHandler.handleElementsSafe( document, this.getDocHeader().docElements(), docHelper );
		}
	}

	@Override
	public void onOpenDocument(PdfWriter writer, Document document) {
        totalPages = writer.getDirectContent().createTemplate(100, 100);
        totalPages.setBoundingBox( new Rectangle(-20, -20, 100, 100) );
		this.baseFont = OpenPdfFontHelper.createBaseFontSafe(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
    }
 
	@Override
    public void onEndPage(PdfWriter writer, Document document) {
    	if ( this.getDocFooter() != null && !this.getDocFooter().isBasic() ) {
    		// allocate direct writer
            PdfContentByte cb = writer.getDirectContent();
            // save writer state
            cb.saveState();
    		Iterator<DocElement> itElements = this.getDocFooter().docElements();
    		int totalOffset = 20;
	        cb.beginText();
	        cb.setFontAndSize(baseFont, footerTextSize);
    		while ( itElements.hasNext() ) {
				DocElement current = itElements.next();
				if ( current instanceof DocPara ) {
					DocPara para = (DocPara) current;
					String text = OpenPpfDocHandler.createText( docHelper.getParams(), para.getText() );
					float textSize = baseFont.getWidthPoint(text, footerTextSize);
					float textBase = document.bottom() - totalOffset;
					int rowOffset = 10;
					if( para.getAlign() == DocPara.ALIGN_CENTER ) {
						cb.setTextMatrix((document.right() / 2), textBase);
						cb.showText(text);	
					} else if( para.getAlign() == DocPara.ALIGN_LEFT ) {
						cb.setTextMatrix(document.left(), textBase);
						cb.showText(text);
					} else {
						float adjust = baseFont.getWidthPoint("0", footerTextSize);
						cb.setTextMatrix(document.right() - textSize - adjust, textBase);
						cb.showText(text);
					}
					
					totalOffset+= rowOffset;
				} else {
	    			throw new ConfigRuntimeException( "Element not allowed in footer (accepted only DocPara) : "+current );
	    		}
				
    		}
            cb.endText();             
    		// restore writer state
    		cb.restoreState();
    	}
    }
 
	@Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        totalPages.beginText();
        totalPages.setFontAndSize(baseFont, footerTextSize);
        totalPages.setTextMatrix(0, 0);
        totalPages.showText(String.valueOf( writer.getPageNumber() - 1) );
        totalPages.endText();
    }
 
    public void setPageNumberAlignment(int pageNumberAlignment) {
        this.pageNumberAlignment = pageNumberAlignment;
    }

	public DocHeader getDocHeader() {
		return docHeader;
	}

	public void setDocHeader(DocHeader docHeader) {
		this.docHeader = docHeader;
	}

	public DocFooter getDocFooter() {
		return docFooter;
	}

	public void setDocFooter(DocFooter docFooter) {
		this.docFooter = docFooter;
	}

	public int getPageNumberAlignment() {
		return pageNumberAlignment;
	}

	public int getCurrentPageNumber() {
		return currentPageNumber;
	}
	
}