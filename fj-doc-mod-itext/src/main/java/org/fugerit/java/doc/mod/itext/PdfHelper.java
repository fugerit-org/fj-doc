package org.fugerit.java.doc.mod.itext;

import java.io.IOException;
import java.util.Iterator;

import org.fugerit.java.core.log.LogFacade;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocFooter;
import org.fugerit.java.doc.base.model.DocHeader;
import org.fugerit.java.doc.base.model.DocPara;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Questo � l'handler da usare!
 * 
 * @author mttfranci
 *
 */
public class PdfHelper  extends PdfPageEventHelper {
	
	public PdfHelper( ITextHelper docHelper ) {
		this.docHelper = docHelper;
		this.docHeader = null;
		this.docFooter = null;
	}
	
	protected BaseFont baseFont;
    private PdfTemplate totalPages;
    private float footerTextSize = 8f;
    private int pageNumberAlignment = Element.ALIGN_CENTER;
 
    private ITextHelper docHelper;
    
    private int currentPageNumber;

	private DocHeader docHeader;
	
	private DocFooter docFooter;
    
    public void onStartPage(PdfWriter writer, Document document) {
    	this.currentPageNumber = writer.getPageNumber();
    	this.docHelper.getParams().setProperty( ITextDocHandler.PARAM_PAGE_CURRENT , String.valueOf( writer.getPageNumber() ) );
		if ( this.getDocHeader() != null ) {
			try {
				ITextDocHandler.handleElements( document, this.getDocHeader().docElements(), docHelper );
			} catch (Exception e) {
				LogFacade.getLog().error( "ITextDocHandler - PdfHelper.onStartPage : "+e );
				throw new RuntimeException( e );
			}
		}
	}

	public void onOpenDocument(PdfWriter writer, Document document) {
        totalPages = writer.getDirectContent().createTemplate(100, 100);
        totalPages.setBoundingBox( new Rectangle(-20, -20, 100, 100) );
		//this.baseFont = ITextDocHandler.findFont( this.docHelper.getDefFontName() );
		//System.out.println( "BASE FONT "+this.docHelper.getDefFontName()+" - "+this.baseFont );
        try {
			this.baseFont = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
 
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
				DocElement current = (DocElement)itElements.next();
				if ( current instanceof DocPara ) {
					DocPara para = (DocPara) current;;
					String text = ITextDocHandler.createText( docHelper.getParams(), para.getText() );
					float textSize = baseFont.getWidthPoint(text, footerTextSize);
					float textBase = document.bottom() - totalOffset;
					int rowOffset = 10;
					if( para.getAlign() == DocPara.ALIGN_CENTER ) {
						cb.setTextMatrix((document.right() / 2), textBase);
						cb.showText(text);
						//	cb.addTemplate(totalPages, (document.right() / 2) + textSize, textBase);	
					} else if( para.getAlign() == DocPara.ALIGN_LEFT ) {
						cb.setTextMatrix(document.left(), textBase);
						cb.showText(text);
						//	cb.addTemplate(totalPages, document.left() + textSize, textBase);
					} else {
						float adjust = baseFont.getWidthPoint("0", footerTextSize);
						cb.setTextMatrix(document.right() - textSize - adjust, textBase);
						cb.showText(text);
						//	cb.addTemplate(totalPages, document.right() - adjust, textBase);
					}
					
					totalOffset+= rowOffset;
				} else {
	    			throw new RuntimeException( "Element not allowed in footer (accepted only DocPara) : "+current );
	    		}
				
    		}
            cb.endText();            
            
//    		while ( itElements.hasNext() ) {
//    			DocElement current = (DocElement)itElements.next();
//    			if ( current instanceof DocPara ) {
//    				DocPara para = (DocPara) current;
//    		        String originalText = para.getText();
//    		        String text = ITextDocHandler.createText( docHelper.getParams(), originalText );
//    		        System.out.println( "TEXT "+text+ " OFFSET "+totalOffset );
//        		    float textBase = document.bottom() - totalOffset;
//    		        float textSize = baseFont.getWidthPoint(text, footerTextSize);
//    		        cb.beginText();
//    		        cb.setFontAndSize(baseFont, footerTextSize);
//    		        if( para.getAlign() == DocPara.ALIGN_CENTER ) {
//    		            cb.setTextMatrix((document.right() / 2), textBase);
//    		            cb.showText(text);
//    		            cb.endText();
//    		            //cb.addTemplate(totalPages, (document.right() / 2) + textSize, textBase);	
//    		        } else if( para.getAlign() == DocPara.ALIGN_LEFT ) {
//    		            cb.setTextMatrix(document.left(), textBase);
//    		            cb.showText(text);
//    		            cb.endText();
//    		            //cb.addTemplate(totalPages, document.left() + textSize, textBase);
//    		        } else {
//    		            float adjust = baseFont.getWidthPoint("0", footerTextSize);
//    		            cb.setTextMatrix(document.right() - textSize - adjust, textBase);
//    		            cb.showText(text);
//    		            cb.endText();
//    		            //cb.addTemplate(totalPages, document.right() - adjust, textBase);
//    		        }
//    		        
//    			} else {
//    				throw new RuntimeException( "Element not allowed in footer (accepted only DocPara) : "+current );
//    			}
//    		}
    		
 
    		// restore writer state
    		cb.restoreState();
    	}

    }
 
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