package org.fugerit.java.doc.mod.openpdf.ext.helpers;


import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.lowagie.text.*;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.regex.ParamFinder;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.helper.SourceResolverHelper;
import org.fugerit.java.doc.base.model.*;
import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;
import org.fugerit.java.doc.base.xml.DocModelUtils;

import com.lowagie.text.html.HtmlTags;
import com.lowagie.text.pdf.Barcode;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.BarcodeEAN;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenPpfDocHandler {
	
	private static final ParamFinder PARAM_FINDER = ParamFinder.newFinder();
	
	public static final String PARAM_PAGE_CURRENT = "currentPage";
	
	public static final String PARAM_PAGE_TOTAL = "totalPage";
	
	public static final String PARAM_PAGE_TOTAL_FINDER = ParamFinder.DEFAULT_PRE+PARAM_PAGE_TOTAL+ParamFinder.DEFAULT_POST;
	
	private static HashMap<String, BaseFont> fonts = new HashMap<>();

	public static BaseFont registerFont( String name, String path ) throws DocumentException, IOException {
		BaseFont font = BaseFont.createFont( path, BaseFont.CP1252, true );
		registerFont( name, font );
		return font;
	}
	
	public static void registerFont( String name, BaseFont font ) {
		fonts.put( name , font );
	}
	
	public static BaseFont findFont( String name ) {
		return fonts.get( name );
	}
	
	protected static void setStyle( DocStyle parent, DocStyle current ) {
		if ( current.getBackColor() == null ) {
			current.setBackColor( parent.getBackColor() );
		}
		if ( current.getForeColor() == null ) {
			current.setForeColor( parent.getForeColor() );
		}
	}
		
	private PdfWriter pdfWriter;
	
	protected Document document;
	
	private String docType;
	
	public static final String MODULE = "openpdf-ext";

	private int totalPageCount; 
	
	
	public OpenPpfDocHandler( Document document, PdfWriter pdfWriter ) {
		this(document, pdfWriter, -1);
	}	
	
	public OpenPpfDocHandler( Document document, PdfWriter pdfWriter, int totalPageCount ) {
		this( document, DocConfig.TYPE_PDF );
		this.pdfWriter = pdfWriter;
		this.totalPageCount = totalPageCount;
	}	
	
	public OpenPpfDocHandler( Document document, String docType ) {
		this.document = document;
		this.docType = docType;
	}
	
	private static int getAlign( int align ) {
		int r = Element.ALIGN_LEFT;
		if ( align == DocPara.ALIGN_RIGHT ) {
			r = Element.ALIGN_RIGHT;
		} else if ( align == DocPara.ALIGN_CENTER ) {
			r = Element.ALIGN_CENTER;	
		} else if ( align == DocPara.ALIGN_JUSTIFY ) {
			r = Element.ALIGN_JUSTIFIED;
		} else if ( align == DocPara.ALIGN_JUSTIFY_ALL ) {
			r = Element.ALIGN_JUSTIFIED_ALL;
		}
		return r;
	}
	
	
	protected static Image createImage( DocImage docImage ) {
		String url = docImage.getUrl();
		log.trace( "currently unsupported image param url {}", url );
		return SafeFunction.get( () -> {
			byte[] data = SourceResolverHelper.resolveImage( docImage );
			Image image = Image.getInstance( data );
			if ( docImage.getScaling() != null ) {
				image.scalePercent( docImage.getScaling().floatValue() );
			}
			return image;
		} );
	}	
	
	
	public static String createText( Properties params, String text ) {
		return PARAM_FINDER.substitute( text , params );
	}
	
	protected static Chunk createChunk( DocPhrase docPhrase, OpenPdfHelper docHelper ) throws DocumentException, IOException {
		String text = createText( docHelper.getParams(), docPhrase.getText() );
		int style = docPhrase.getStyle();
		String fontName = docPhrase.getFontName();
		Font f = createFont(fontName, docPhrase.getSize(), style, docHelper, docPhrase.getForeColor() );
		return new Chunk( text, f );
	}	
	
	protected static Phrase createPhrase( DocPhrase docPhrase, OpenPdfHelper docHelper, List<Font> fontMap ) throws DocumentException, IOException {
		String text = createText( docHelper.getParams(), docPhrase.getText() );
		int style = docPhrase.getStyle();
		String fontName = docPhrase.getFontName();
		Font f = createFont(fontName, docPhrase.getSize(), style, docHelper, docPhrase.getForeColor() );
		Phrase p = null;
		if ( StringUtils.isNotEmpty( docPhrase.getLink() ) ) {
			Anchor a = new Anchor( text , f );
			a.setReference( docPhrase.getLink() );
			p = a;
		} else if ( StringUtils.isNotEmpty( docPhrase.getAnchor() ) ) {
			Anchor a = new Anchor( text , f );
			a.setName( docPhrase.getAnchor() );
			p = a;		
		} else {
			p = new Phrase( text, f );
		}
		if (fontMap != null) {
			fontMap.add( f );
		}
		return p;
	}
	
	protected static Phrase createPhrase( DocPhrase docPhrase, OpenPdfHelper docHelper ) throws DocumentException, IOException {
		return createPhrase(docPhrase, docHelper, null);
	}	

	protected static Paragraph createPara( DocPara docPara, OpenPdfHelper docHelper ) throws DocumentException, IOException {
		return createPara(docPara, docHelper, null);
	}
	
	protected static Paragraph createPara( DocPara docPara, OpenPdfHelper docHelper, List<Font> fontMap ) throws DocumentException, IOException {
		int style = docPara.getStyle();
		String text = createText( docHelper.getParams(), docPara.getText() );
		String fontName = docPara.getFontName();
		Font f = createFont(fontName, docPara.getSize(), style, docHelper, docPara.getForeColor() );
		Phrase phrase = new Phrase( text, f );
		Paragraph p = new Paragraph( new Phrase( text, f ) );
		if ( docPara.getForeColor() != null ) {
			Color c = DocModelUtils.parseHtmlColor( docPara.getForeColor() );
			Font f1 = new Font( f.getFamily(), f.getSize(), f.getStyle(), c );
			p = new Paragraph( new Phrase( text, f1 ) );
		}
		if ( docPara.getAlign() != DocPara.ALIGN_UNSET ) {
			p.setAlignment( getAlign( docPara.getAlign() ) );
		}
		if ( docPara.getLeading() != null ) {
			p.setLeading(  docPara.getLeading().floatValue() );
		}
		if ( docPara.getSpaceBefore() != null ) {
			p.setSpacingBefore( docPara.getSpaceBefore().floatValue() );
		}
		if ( docPara.getSpaceAfter() != null ) {
			p.setSpacingAfter( docPara.getSpaceAfter().floatValue() );
		}
		p.setFont( f );
		phrase.setFont( f );
		if ( fontMap != null ) {
			fontMap.add( f );
		}
		return p;
	}
	
	protected static Image createBarcode( DocBarcode docBarcode, OpenPdfHelper helper ) throws BadElementException, IOException  {
		log.trace( "currently unused parameter helper : {}", helper );
		Barcode barcode = null;
		if ( "128".equalsIgnoreCase( docBarcode.getType() ) ) {
			barcode = new Barcode128();
		} else {
			barcode = new BarcodeEAN();	
		}
		if ( docBarcode.getSize() != -1 ) {
			barcode.setBarHeight( docBarcode.getSize() );
		}
		barcode.setCode( docBarcode.getText() );
		barcode.setAltText( docBarcode.getText() );
		java.awt.Image awtImage = barcode.createAwtImage( Color.white, Color.black );
		return Image.getInstance( awtImage, null );
	}
	

 	
	public static Font createFont( String fontName, int fontSize, int fontStyle, OpenPdfHelper docHelper, String color ) throws DocumentException, IOException {
		return OpenPdfFontHelper.createFont(fontName, fontName, fontSize, fontStyle, docHelper, color);
	}
	
	private void handleTypeSpecific( Properties info ) {
		// per documenti tipo HTML
		if ( DocConfig.TYPE_HTML.equalsIgnoreCase( this.docType ) ) {
			String cssLink = info.getProperty( DocInfo.INFO_NAME_CSS_LINK );
			if ( cssLink != null ) {
				this.document.add( new Header( HtmlTags.STYLESHEET, cssLink ) );
			}
		}
		// per documenti tipo word o pdf
		if ( DocConfig.TYPE_PDF.equalsIgnoreCase( this.docType ) || DocConfig.TYPE_RTF.equalsIgnoreCase( this.docType ) ) {
			Rectangle size = this.document.getPageSize();
			String pageOrient = info.getProperty( DocInfo.INFO_NAME_PAGE_ORIENT );
			if ( "horizontal".equalsIgnoreCase( pageOrient ) ) {
				size = new Rectangle( size.getHeight(), size.getWidth() );
				this.document.setPageSize( size );
			}
			if ( DocConfig.TYPE_PDF.equalsIgnoreCase( this.docType ) ) {
				String pdfFormat = info.getProperty( DocInfo.INFO_NAME_PDF_FORMAT );
				if ( DocConfig.FORMAT_PDF_A_1B.equalsIgnoreCase( pdfFormat ) ) {
					this.pdfWriter.setPDFXConformance(PdfWriter.PDFA1B);
					PdfDictionary outi = new PdfDictionary( PdfName.OUTPUTINTENT );
					outi.put( PdfName.OUTPUTCONDITIONIDENTIFIER, new PdfString("sRGB IEC61966-2.1") );
					outi.put( PdfName.INFO, new PdfString("sRGB IEC61966-2.1") );
					outi.put( PdfName.S, PdfName.GTS_PDFA1 );
					this.pdfWriter.getExtraCatalog().put( PdfName.OUTPUTINTENTS, new PdfArray( outi ) );
				}
				
			}
		}		
	}
	
	protected void handleHeaderExt( DocHeader docHeader, PdfHelper pdfHelper, OpenPdfHelper docHelper ) throws DocumentException {
		log.trace( "handleHeaderExt docHelper : {}", docHelper );
		pdfHelper.setDocHeader( docHeader );
	}
	
	protected void handleFooterExt( DocFooter docFooter, PdfHelper pdfHelper, OpenPdfHelper docHelper ) throws DocumentException {
		log.trace( "handleFooterExt docHelper : {}", docHelper );
		pdfHelper.setDocFooter( docFooter ); 
	}
	
	private void handleHeader( DocBase docBase, PdfHelper pdfHelper, OpenPdfHelper docHelper) throws DocumentException, IOException {
		DocHeader docHeader = docBase.getDocHeader();
		if ( docHeader != null && docHeader.isUseHeader() ) {
			if ( docHeader.isBasic() ) {
				HeaderFooter header = this.createHeaderFooter( docHeader, docHeader.getAlign(), docHelper );
				this.document.setHeader( header );	
			} else {
				this.handleHeaderExt(docHeader, pdfHelper, docHelper);
			}
		}
	}
	
	private void handleFooter( DocBase docBase, PdfHelper pdfHelper, OpenPdfHelper docHelper) throws DocumentException, IOException {
		DocFooter docFooter = docBase.getDocFooter();
		if ( docFooter != null && docFooter.isUseFooter() ) {
			if ( docFooter.isBasic() ) {
				HeaderFooter footer = this.createHeaderFooter( docFooter, docFooter.getAlign(), docHelper );
				this.document.setFooter( footer );	
			} else {
				this.handleFooterExt(docFooter, pdfHelper, docHelper);
			}
		}	
	}
	
	/* (non-Javadoc)
	 * @see org.fugerit.java.doc.base.DocHandler#handleDoc(org.fugerit.java.doc.base.DocBase)
	 */
	public void handleDoc(DocBase docBase) throws DocumentException, IOException {
		Properties info = docBase.getInfo();
		
		String defaultFontName = info.getProperty( GenericConsts.DOC_DEFAULT_FONT_NAME, "helvetica" );
		String defaultFontSize = info.getProperty( GenericConsts.DOC_DEFAULT_FONT_SIZE, "10" );
		String defaultFontStyle = info.getProperty( GenericConsts.DOC_DEFAULT_FONT_STYLE, "normal" );
		OpenPdfHelper docHelper = new OpenPdfHelper();
		
		if ( this.pdfWriter != null ) {
			docHelper.setPdfWriter( this.pdfWriter );
		}
		
		docHelper.setDefFontName( defaultFontName );
		docHelper.setDefFontStyle( defaultFontStyle );
		docHelper.setDefFontSize( defaultFontSize );

		if ( this.totalPageCount != -1 ) {
			docHelper.getParams().setProperty( PARAM_PAGE_TOTAL , String.valueOf( this.totalPageCount ) );
		}
		
		this.handleTypeSpecific(info);
		
		// header / footer section
		PdfHelper pdfHelper = new PdfHelper( docHelper );
		this.handleHeader(docBase, pdfHelper, docHelper);
		this.handleFooter(docBase, pdfHelper, docHelper);
		if ( DocConfig.TYPE_PDF.equals( this.docType ) ) {
			this.pdfWriter.setPageEvent( pdfHelper );
		}
		
		this.document.open();
		
		Iterator<DocElement> itDoc = docBase.getDocBody().docElements();
		handleElements(document, itDoc, docHelper);
		
		this.document.close();
	}
	
	public static void handleElementsSafe( Document document, Iterator<DocElement> itDoc, OpenPdfHelper docHelper ) {
		SafeFunction.apply( () -> handleElements(document, itDoc, docHelper) );
	}
	
	public static void handleElements( Document document, Iterator<DocElement> itDoc, OpenPdfHelper docHelper ) throws DocumentException, IOException {
		while ( itDoc.hasNext() ) {
			DocElement docElement = itDoc.next();
			getElement(document, docElement, true, docHelper );
		}
	}

	public static void checkAddElement( DocumentParent documentParent, boolean addElement, Element result ) {
		if ( addElement ) {
			documentParent.add( result );
		}
	}

	public static Element getElement( Document document, DocElement docElement, boolean addElement, OpenPdfHelper docHelper ) throws DocumentException, IOException {
		Element result = null;
		DocumentParent documentParent = new DocumentParent( document );
		if ( docElement instanceof DocPhrase ) {
			result = createPhrase( (DocPhrase)docElement, docHelper );
			checkAddElement( documentParent, addElement, result );
		} else if ( docElement instanceof DocPara ) {
			result = createPara( (DocPara)docElement, docHelper );
			checkAddElement( documentParent, addElement, result );
		} else if ( docElement instanceof DocTable ) {
			result = OpenPdfDocTableHelper.createTable( (DocTable)docElement, docHelper );
			checkAddElement( documentParent, addElement, result );
		} else if ( docElement instanceof DocImage ) {
			result = createImage( (DocImage)docElement );
			checkAddElement( documentParent, addElement, result );
		} else if ( docElement instanceof DocList) {
			result = createList( (DocList)docElement, docHelper );
			checkAddElement( documentParent, addElement, result );
		} else if ( docElement instanceof DocPageBreak ) {
			document.newPage();
		}
		return result;
	}

	private static com.lowagie.text.List createList( DocList docList, OpenPdfHelper docHelper ) throws IOException {
		com.lowagie.text.List list = new com.lowagie.text.List( docList.isOrdered() );
		for (  DocElement element : docList.getElementList() ) {
			DocLi li = (DocLi) element;
			if ( li.getContent() instanceof DocPara ) {
				list.add( new ListItem( createPara( (DocPara)li.getContent(), docHelper ) ) );
			} else {
				throw new ConfigRuntimeException( String.format( "unsupported type %s", element.getClass().getSimpleName() ) );
			}
		}
		return list;
	}
	
	private void handleHeaderFooterElement( DocElement docElement, float inputLeading, OpenPdfHelper docHelper , Phrase phrase ) throws DocumentException, IOException {
		float leading = inputLeading;
		if ( docElement instanceof DocPhrase ) {
			DocPhrase docPhrase = (DocPhrase) docElement;
			Chunk ck = createChunk( docPhrase, docHelper );
			if( docPhrase.getLeading() != null && docPhrase.getLeading().floatValue() != leading ) {
				leading = docPhrase.getLeading().floatValue();
				phrase.setLeading( leading );
			}
			phrase.add( ck );
		} else  if ( docElement instanceof DocPara ) {
			DocPara docPara = (DocPara) docElement;
			if ( docPara.getLeading() != null ) {
				phrase.setLeading( docPara.getLeading().floatValue() );
			}
			Font f = new Font( Font.HELVETICA, docPara.getSize() );
			if ( docPara.getForeColor() != null ) {
				SafeFunction.applySilent( () -> f.setColor( DocModelUtils.parseHtmlColor( docPara.getForeColor() ) ) );
			}
			Chunk ck = new Chunk( docPara.getText(), f );
			phrase.add( ck );
		} else if ( docElement instanceof DocImage ) {
			DocImage docImage = (DocImage)docElement;
			Image img = createImage( docImage );
			Chunk ck = new Chunk( img, 0, 0, true );
			phrase.add( ck );
		}
	}
	
	private HeaderFooter createHeaderFooter( DocHeaderFooter container, int inputAlign, OpenPdfHelper docHelper ) throws DocumentException, IOException {
		int align = inputAlign;
		Iterator<DocElement> it = container.docElements();
		Phrase phrase = new Phrase();
		float leading = (float)-1.0;
		while ( it.hasNext() ) {
			DocElement docElement = it.next();
			this.handleHeaderFooterElement(docElement, leading, docHelper, phrase);
		}
		HeaderFooter headerFooter  = new HeaderFooter( phrase, container.isNumbered() );
		if ( align == DocPara.ALIGN_UNSET ) {
			align = DocPara.ALIGN_CENTER;
		}
		headerFooter.setAlignment( getAlign( align ) );
		headerFooter.setBorder( container.getBorderWidth() );
		return headerFooter;
	}
	
}

