/*****************************************************************
<copyright>
	Fugerit Java Library org.fugerit.java.doc.base 

	Copyright (c) 2019 Fugerit

	All rights reserved. This program and the accompanying materials
	are made available under the terms of the Apache License v2.0
	which accompanies this distribution, and is available at
	http://www.apache.org/licenses/
	(txt version : http://www.apache.org/licenses/LICENSE-2.0.txt
	html version : http://www.apache.org/licenses/LICENSE-2.0.html)

   This product includes software developed at
   The Apache Software Foundation (http://www.apache.org/).
</copyright>
*****************************************************************/
/*
 * @(#)ITextDocHandler.java
 *
 * @project    : org.fugerit.java.doc.base
 * @package    : org.fugerit.java.doc.mod.itext
 * @creation   : 06/set/06
 * @license	   : META-INF/LICENSE.TXT
 */
package org.fugerit.java.doc.mod.itext;


import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.fugerit.java.core.log.LogFacade;
import org.fugerit.java.core.util.regex.ParamFinder;
import org.fugerit.java.doc.base.model.DocBarcode;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocBorders;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocFooter;
import org.fugerit.java.doc.base.model.DocHeader;
import org.fugerit.java.doc.base.model.DocHeaderFooter;
import org.fugerit.java.doc.base.model.DocImage;
import org.fugerit.java.doc.base.model.DocInfo;
import org.fugerit.java.doc.base.model.DocPageBreak;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.model.DocPhrase;
import org.fugerit.java.doc.base.model.DocRow;
import org.fugerit.java.doc.base.model.DocStyle;
import org.fugerit.java.doc.base.model.DocTable;
import org.fugerit.java.doc.base.xml.DocModelUtils;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Header;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
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
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.headerfooter.RtfHeaderFooter;

/**
 * 
 *
 * @author mfranci
 *
 */
public class ITextDocHandler {
	

	private static final ParamFinder PARAM_FINDER = ParamFinder.newFinder();
	
	public static final String PARAM_PAGE_CURRENT = "currentPage";
	
	public static final String PARAM_PAGE_TOTAL = "totalPage";
	
	public static final String PARAM_PAGE_TOTAL_FINDER = ParamFinder.DEFAULT_PRE+"totalPage"+ParamFinder.DEFAULT_POST;
	
	private static HashMap<String, BaseFont> fonts = new HashMap<>();

	public static void registerFont( String name, String path ) throws Exception {
		BaseFont font = BaseFont.createFont( path, BaseFont.CP1252, true );
		registerFont( name, font );
	}
	
	public static void registerFont( String name, BaseFont font ) {
		fonts.put( name , font );
	}
	
	public static BaseFont findFont( String name ) {
		BaseFont res = (BaseFont)fonts.get( name );
		return res;
	}
	
	private static void setStyle( DocStyle parent, DocStyle current ) {
		if ( current.getBackColor() == null ) {
			current.setBackColor( parent.getBackColor() );
		}
		if ( current.getForeColor() == null ) {
			current.setForeColor( parent.getForeColor() );
		}
	}
		
	private PdfWriter pdfWriter;
	
	//private RtfWriter2 rtfWriter2;
	
	private Document document;
	
	private String docType;
	
	public final static String DOC_OUTPUT_HTML = "html";
	
	public final static String DOC_OUTPUT_PDF = "pdf";
	
	public final static String DOC_OUTPUT_RTF = "rtf";
	
	public final static String DOC_DEFAULT_FONT_NAME = "default-font-name";
	public final static String DOC_DEFAULT_FONT_SIZE = "default-font-size";
	public final static String DOC_DEFAULT_FONT_STYLE = "default-font-style";

	private int totalPageCount; 
	
	public ITextDocHandler( Document document, RtfWriter2 rtfWriter2 ) {
		this( document, DOC_OUTPUT_RTF );
		//this.rtfWriter2 = rtfWriter2;
	}	
	
	public ITextDocHandler( Document document, PdfWriter pdfWriter ) {
		this(document, pdfWriter, -1);
	}	
	
	public ITextDocHandler( Document document, PdfWriter pdfWriter, int totalPageCount ) {
		this( document, DOC_OUTPUT_PDF );
		this.pdfWriter = pdfWriter;
		this.totalPageCount = totalPageCount;
	}	
	
	public ITextDocHandler( Document document, String docType ) {
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
	
	private static int getValign( int align ) {
		int r = Element.ALIGN_TOP;
		if ( align == DocPara.ALIGN_BOTTOM ) {
			r = Element.ALIGN_BOTTOM;
		} else if ( align == DocPara.ALIGN_MIDDLE ) {
			r = Element.ALIGN_MIDDLE;	
		}
		return r;
	}	
	
	protected static Image createImage( DocImage docImage ) throws Exception {
		Image image = null;
		String url = docImage.getUrl();
		try {
			image = Image.getInstance( new URL( url )  );
			if ( docImage.getScaling() != null ) {
				image.scalePercent( docImage.getScaling().floatValue() );
			}
		} catch (Exception e) {
			LogFacade.getLog().error( "ITextDocHandler.createImage() Error loading image url : "+url, e );
			throw e;
		}
		return image;
	}	
	
	
	public static String createText( Properties params, String text ) {
		return PARAM_FINDER.substitute( text , params );
	}
	
	protected static Chunk createChunk( DocPhrase docPhrase, ITextHelper docHelper ) throws Exception {
		String text = createText( docHelper.getParams(), docPhrase.getText() );
		int style = docPhrase.getStyle();
		String fontName = docPhrase.getFontName();
		Font f = createFont(fontName, docPhrase.getSize(), style, docHelper, docPhrase.getForeColor() );
		Chunk p = new Chunk( text, f );
		return p;
	}	
	
	protected static Phrase createPhrase( DocPhrase docPhrase, ITextHelper docHelper, List<Font> fontMap ) throws Exception {
		String text = createText( docHelper.getParams(), docPhrase.getText() );
		int style = docPhrase.getStyle();
		String fontName = docPhrase.getFontName();
		Font f = createFont(fontName, docPhrase.getSize(), style, docHelper, docPhrase.getForeColor() );
		Phrase p = new Phrase( text, f );
		if (fontMap != null) {
			fontMap.add( f );
		}
		return p;
	}
	
	protected static Phrase createPhrase( DocPhrase docPhrase, ITextHelper docHelper ) throws Exception {
		return createPhrase(docPhrase, docHelper, null);
	}	

	protected static Paragraph createPara( DocPara docPara, ITextHelper docHelper ) throws Exception {
		return createPara(docPara, docHelper, null);
	}
	
	protected static Paragraph createPara( DocPara docPara, ITextHelper docHelper, List<Font> fontMap ) throws Exception {
		int style = docPara.getStyle();
		String text = createText( docHelper.getParams(), docPara.getText() );
//		if ( DOC_OUTPUT_HTML.equals( this.docType ) ) {
//			int count = 0;
//			StringBuffer buffer = new StringBuffer();
//			while ( count < text.length() && text.indexOf( " " )==count ) {
//				count++;
//			}
//			buffer.append( text.substring( count ) );
//			text = buffer.toString();
//		}
		String fontName = docPara.getFontName();
		Font f = createFont(fontName, docPara.getSize(), style, docHelper, docPara.getForeColor() );
		Phrase phrase = new Phrase( text, f );
		Paragraph p = new Paragraph( new Phrase( text, f ) );
		if ( docPara.getForeColor() != null ) {
			Color c = DocModelUtils.parseHtmlColor( docPara.getForeColor() );
			Font f1 = new Font( f.getFamily(), f.getSize(), f.getStyle(), c );
			p = new Paragraph( new Phrase( text, f1 ) );
			//f = f1;
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
	
	protected static Table createTable( DocTable docTable, ITextHelper docHelper ) throws Exception {
		
		boolean startHeader = false;
		Table table = new Table( docTable.getColumns() );
		table.setBorderWidth(0);	
		table.setWidth( docTable.getWidth() );
		table.setBorderColor( Color.black );
		table.setPadding( docTable.getPadding() );
		table.setSpacing( docTable.getSpacing() );
		table.setCellsFitPage( true );
		
		
		if ( docTable.getSpaceBefore() != null ) {
			table.setSpacing( docTable.getSpaceBefore().floatValue() );
		}
		if ( docTable.getSpaceAfter() != null ) {
			table.setSpacing( docTable.getSpaceAfter().floatValue() );
		}
		
		int[] cw = docTable.getColWithds();
		if (  cw != null ) {
			float[] w = new float[ cw.length ];
			for ( int k=0; k<w.length; k++ ) {
				w[k] = (float)((float)cw[k]/(float)100);
			}
			table.setWidths( w );
		}
		Iterator<DocElement> itRow = docTable.docElements();
		while ( itRow.hasNext() ) {
			DocRow docRow = (DocRow)itRow.next();
			Iterator<DocElement> itCell = docRow.docElements();
			while ( itCell.hasNext() ) {
				DocCell docCell = (DocCell)itCell.next();
				setStyle( docTable, docCell );
				Cell cell = new Cell();
				if ( docCell.isHeader() ) {
					cell.setHeader( true );
					startHeader = true;
				} else {
					if ( startHeader ) {
						startHeader = false;
						table.endHeaders();
					}
				}
				cell.setColspan( docCell.getCSpan() );
				cell.setRowspan( docCell.getRSpan() );
				DocBorders docBorders = docCell.getDocBorders();
				if ( docBorders != null ) {
					if ( docBorders.getBorderColorBottom() != null ) {
						cell.setBorderColorBottom( DocModelUtils.parseHtmlColor( docBorders.getBorderColorBottom() ) );
					}
					if ( docBorders.getBorderColorTop() != null ) {
						cell.setBorderColorTop(  DocModelUtils.parseHtmlColor( docBorders.getBorderColorTop() ) );
					}
					if ( docBorders.getBorderColorLeft() != null ) {
						cell.setBorderColorLeft(  DocModelUtils.parseHtmlColor( docBorders.getBorderColorLeft() ) );
					}
					if ( docBorders.getBorderColorRight() != null ) {
						cell.setBorderColorRight(  DocModelUtils.parseHtmlColor( docBorders.getBorderColorRight() ) );
					}
					if ( docBorders.getBorderWidthBottom() != -1 ) {
						cell.setBorderWidthBottom( docBorders.getBorderWidthBottom() );
					}
					if ( docBorders.getBorderWidthTop() != -1 ) {
						cell.setBorderWidthTop( docBorders.getBorderWidthTop() );
					}
					if ( docBorders.getBorderWidthLeft() != -1 ) {
						cell.setBorderWidthLeft( docBorders.getBorderWidthLeft() );
					}
					if ( docBorders.getBorderWidthRight() != -1 ) {
						cell.setBorderWidthRight( docBorders.getBorderWidthRight() );
					}
				}
				if ( docCell.getBackColor() != null ) {
					cell.setBackgroundColor( DocModelUtils.parseHtmlColor( docCell.getBackColor() ) );
				}
				if ( docCell.getAlign() != DocPara.ALIGN_UNSET ) {
					cell.setHorizontalAlignment( getAlign( docCell.getAlign() ) );
				}
				if ( docCell.getValign() != DocPara.ALIGN_UNSET ) {
					cell.setVerticalAlignment( getValign( docCell.getValign() ) );
				}				
				CellParent cellParent = new CellParent( cell );
				Iterator<DocElement> itCurrent = docCell.docElements();
				List<Font> fontList = new ArrayList<>();
				while ( itCurrent.hasNext() ) {
					DocElement docElement = (DocElement) itCurrent.next();
					if ( docElement instanceof DocPara ) {
						DocPara docPara = (DocPara)docElement;
						setStyle( docCell , docPara );
						Paragraph paragraph = createPara( docPara, docHelper, fontList );
						cellParent.add( paragraph );
					} else if ( docElement instanceof DocPhrase ) {
						DocPhrase docPhrase = (DocPhrase)docElement;
						//setStyle( docCell , docPara );
						cellParent.add( createPhrase( docPhrase, docHelper, fontList ) );						
					} else if ( docElement instanceof DocTable ) {
						LogFacade.getLog().debug( "nested table" );
						table.insertTable( createTable( (DocTable)docElement, docHelper ) );
					} else if ( docElement instanceof DocImage ) {
						LogFacade.getLog().debug( "cell DocImage : "+docElement );
						cellParent.add( createImage( (DocImage)docElement ) );
					} else if ( docElement instanceof DocBarcode ) {
						LogFacade.getLog().info( "cell DocBarcode : "+docElement );
						cellParent.add( createBarcode( (DocBarcode)docElement, docHelper ) );
					}
				}
				table.addCell( cell );
				@SuppressWarnings("unchecked")
				List<Chunk> listChunk = cell.getChunks();
				if ( listChunk.size() == fontList.size() ) {
					for ( int k=0; k<listChunk.size(); k++ ) {
						Chunk c = (Chunk)listChunk.get( k );
						Font f = (Font) fontList.get( k );
						c.setFont( f );
					}
				}
				if ( docHelper.getPdfWriter() != null ) {
					docHelper.getPdfWriter().flush();
				}
			}
		}

		return table;
	}
	
	
	private static Image createBarcode( DocBarcode docBarcode, ITextHelper helper ) throws Exception {
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
		Image img = Image.getInstance( awtImage, null );
		return img;
	}
	
	private static RtfHeaderFooter createRtfHeaderFooter( DocHeaderFooter docHeaderFooter, Document document, boolean header, ITextHelper docHelper ) throws Exception {
		List<DocElement> list = new ArrayList<>();
		Iterator<DocElement> itDoc = docHeaderFooter.docElements();
		while ( itDoc.hasNext() ) {
			list.add( itDoc.next() );
		}
		Element[] e = new Element[ list.size() ];
		for ( int k=0; k<list.size(); k++ ) {
			e[k] = (Element)getElement( document, (DocElement)list.get( k ) , false, docHelper );
		}
		RtfHeaderFooter rtfHeaderFooter = new RtfHeaderFooter( e );
		rtfHeaderFooter.setDisplayAt( RtfHeaderFooter.DISPLAY_ALL_PAGES );
		if ( header ) {
			rtfHeaderFooter.setType( RtfHeaderFooter.TYPE_HEADER );	
		} else {
			rtfHeaderFooter.setType( RtfHeaderFooter.TYPE_FOOTER );
		}
		return rtfHeaderFooter;
	}
	
	public static Font createFont( String fontName, int fontSize, int fontStyle, ITextHelper docHelper, String color ) throws Exception {
		return createFont(fontName, fontName, fontSize, fontStyle, docHelper, color);
	}
	
	private static Font createFont( String fontName, String fontPath, int fontSize, int fontStyle, ITextHelper docHelper, String color ) throws Exception {
		Font font = null;
		int size = fontSize;
		int style = Font.NORMAL;
		BaseFont bf = null;
		int bfV = -1;
		if ( size == -1 ) {
			size = Integer.parseInt( docHelper.getDefFontSize() );
		}
		if ( fontStyle == DocPara.STYLE_BOLD ) {
			style = Font.BOLD;
		} else if ( fontStyle == DocPara.STYLE_UNDERLINE ) {
			style = Font.UNDERLINE;
		} else if ( fontStyle == DocPara.STYLE_ITALIC ) {
			style = Font.ITALIC;
		} else if ( fontStyle == DocPara.STYLE_BOLDITALIC ) {
			style = Font.BOLDITALIC;
		}
		if ( fontName == null ) {
			fontName = docHelper.getDefFontName();
		}
		if ( "helvetica".equalsIgnoreCase( fontName ) ) {
			bfV = Font.HELVETICA;
		} else if ( "courier".equalsIgnoreCase( fontName ) ) {
			bfV = Font.COURIER;
		} else if ( "times-roman".equalsIgnoreCase( fontName ) ) {
			bfV = Font.TIMES_ROMAN;
		} else if ( "symbol".equalsIgnoreCase( fontName ) ) {
			bfV = Font.SYMBOL;
		} else {
			bf = findFont( fontName );
			if ( bf == null) {
				bf = BaseFont.createFont( fontPath, BaseFont.CP1252, true );
				registerFont( fontName, bf );
			}
		}
		Color c = Color.BLACK;
		if ( color != null ) {
			c = DocModelUtils.parseHtmlColor( color );
		}
		if ( bfV == -1 ) {
			font = new Font( bf, size, style, c );	
		} else {
			font = new Font( bfV, size, style, c );
		}
		return font;
	}
	
	/* (non-Javadoc)
	 * @see org.fugerit.java.doc.base.DocHandler#handleDoc(org.fugerit.java.doc.base.DocBase)
	 */
	public void handleDoc(DocBase docBase) throws Exception {
		Properties info = docBase.getInfo();
		
		String defaultFontName = info.getProperty( DOC_DEFAULT_FONT_NAME, "helvetica" );
		String defaultFontSize = info.getProperty( DOC_DEFAULT_FONT_SIZE, "10" );
		String defaultFontStyle = info.getProperty( DOC_DEFAULT_FONT_STYLE, "normal" );
		ITextHelper docHelper = new ITextHelper();
		
		if ( this.pdfWriter != null ) {
			docHelper.setPdfWriter( this.pdfWriter );
		}
		
		docHelper.setDefFontName( defaultFontName );
		docHelper.setDefFontStyle( defaultFontStyle );
		docHelper.setDefFontSize( defaultFontSize );

		if ( this.totalPageCount != -1 ) {
			docHelper.getParams().setProperty( PARAM_PAGE_TOTAL , String.valueOf( this.totalPageCount ) );
		}
		
		// per documenti tipo HTML
		if ( DOC_OUTPUT_HTML.equalsIgnoreCase( this.docType ) ) {
			String cssLink = info.getProperty( DocInfo.INFO_NAME_CSS_LINK );
			if ( cssLink != null ) {
				this.document.add( new Header( HtmlTags.STYLESHEET, cssLink ) );
			}
		}
		
		// per documenti tipo word o pdf
		if ( DOC_OUTPUT_PDF.equalsIgnoreCase( this.docType ) || DOC_OUTPUT_RTF.equalsIgnoreCase( this.docType ) ) {
			Rectangle size = this.document.getPageSize();
			String pageOrient = info.getProperty( DocInfo.INFO_NAME_PAGE_ORIENT );
			if ( pageOrient != null ) {
				if ( "horizontal".equalsIgnoreCase( pageOrient ) ) {
					size = new Rectangle( size.getHeight(), size.getWidth() );
					this.document.setPageSize( size );
				}
			}
			
			if ( DOC_OUTPUT_PDF.equalsIgnoreCase( this.docType ) ) {
				
				
				String pdfFormat = info.getProperty( DocInfo.INFO_NAME_PDF_FORMAT );
				if ( "pdf-a".equalsIgnoreCase( pdfFormat ) ) {
					this.pdfWriter.setPDFXConformance(PdfWriter.PDFA1B);
					PdfDictionary outi = new PdfDictionary( PdfName.OUTPUTINTENT );
					outi.put( PdfName.OUTPUTCONDITIONIDENTIFIER, new PdfString("sRGB IEC61966-2.1") );
					outi.put( PdfName.INFO, new PdfString("sRGB IEC61966-2.1") );
					outi.put( PdfName.S, PdfName.GTS_PDFA1 );
//					FontFactory.
//					BaseFont bf =  BaseFont.createFont( Font.HELVETICA, BaseFont.WINANSI, true );
					this.pdfWriter.getExtraCatalog().put( PdfName.OUTPUTINTENTS, new PdfArray( outi ) );
				}
				
			}
			
			

			
		}		
		
		// header / footer section
		PdfHelper pdfHelper = new PdfHelper( docHelper );
		DocHeader docHeader = docBase.getDocHeader();
		if ( docHeader != null && docHeader.isUseHeader() ) {
			if ( docHeader.isBasic() ) {
				HeaderFooter header = this.createHeaderFoter( docHeader, docHeader.getAlign(), docHelper );
				this.document.setHeader( header );	
			} else {
				if ( DOC_OUTPUT_PDF.equals( this.docType ) ) {
					pdfHelper.setDocHeader( docHeader ); 
				} else if ( DOC_OUTPUT_RTF.equals( this.docType ) ) {
					this.document.setHeader( new RtfHeaderFooter( createRtfHeaderFooter( docHeader , this.document, true, docHelper ) ) );
				}
			}
		}
		DocFooter docFooter = docBase.getDocFooter();
		if ( docFooter != null && docFooter.isUseFooter() ) {
			if ( docFooter.isBasic() ) {
				HeaderFooter footer = this.createHeaderFoter( docFooter, docFooter.getAlign(), docHelper );
				this.document.setFooter( footer );	
			} else {
				if ( DOC_OUTPUT_PDF.equals( this.docType ) ) {
					pdfHelper.setDocFooter( docFooter ); 
				} else if ( DOC_OUTPUT_RTF.equals( this.docType ) ) {
					this.document.setFooter( new RtfHeaderFooter( createRtfHeaderFooter( docFooter , this.document, false, docHelper ) ) );
				}
			}
			
		}		
		if ( DOC_OUTPUT_PDF.equals( this.docType ) ) {
			this.pdfWriter.setPageEvent( pdfHelper );
		}
		
		this.document.open();
		
		Iterator<DocElement> itDoc = docBase.getDocBody().docElements();
		handleElements(document, itDoc, docHelper);
		
		this.document.close();
	}
	
	public static void handleElements( Document document, Iterator<DocElement> itDoc, ITextHelper docHelper ) throws Exception {
		while ( itDoc.hasNext() ) {
			DocElement docElement = (DocElement)itDoc.next();
			getElement(document, docElement, true, docHelper );
		}
	}
	
	public static Element getElement( Document document, DocElement docElement, boolean addElement, ITextHelper docHelper ) throws Exception {
		Element result = null;
		DocumentParent documentParent = new DocumentParent( document );
		if ( docElement instanceof DocPhrase ) {
			result = createPhrase( (DocPhrase)docElement, docHelper );
			if ( addElement ) {
				documentParent.add( result );	
			}
		} else if ( docElement instanceof DocPara ) {
			result = createPara( (DocPara)docElement, docHelper );
			if ( addElement ) {
				documentParent.add( result );	
			}
		} else if ( docElement instanceof DocTable ) {
			result = createTable( (DocTable)docElement, docHelper );
			if ( addElement ) {
				document.add( result );	
			}
		} else if ( docElement instanceof DocImage ) {
			result = createImage( (DocImage)docElement );
			if ( addElement ) {
				documentParent.add( result );	
			}
		} else if ( docElement instanceof DocPageBreak ) {
			document.newPage();
		}
		return result;
	}
	
	private HeaderFooter createHeaderFoter( DocHeaderFooter container, int align, ITextHelper docHelper ) throws Exception {
		Iterator<DocElement> it = container.docElements(); 
		Phrase phrase = new Phrase();
		float leading = (float)-1.0;
		while ( it.hasNext() ) {
			DocElement docElement = (DocElement)it.next();
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
					try {
						f.setColor( DocModelUtils.parseHtmlColor( docPara.getForeColor() ) );	
					} catch (Exception fe) {
						LogFacade.getLog().warn( "Error setting fore color on footer : "+docPara.getForeColor(), fe );
					}
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
		HeaderFooter headerFooter  = new HeaderFooter( phrase, container.isNumbered() );
		
		if ( align == DocPara.ALIGN_UNSET ) {
			align = DocPara.ALIGN_CENTER;
		}
		headerFooter.setAlignment( getAlign( align ) );
		headerFooter.setBorder( container.getBorderWidth() );
		//headerFooter.setUseVariableBorders( true );
		return headerFooter;
	}
	
}


interface ParentElement {
	
	public void add( Element element ) throws Exception;
	
}

class PhraseParent implements ParentElement {

	private Phrase phrase;
	
	public PhraseParent( Phrase phrase ) {
		this.phrase = phrase;
	}
	
	/* (non-Javadoc)
	 * @see org.fugerit.java.doc.mod.itext.ParentElement#add(com.lowagie.text.Element)
	 */
	public void add(Element element) throws Exception {
		this.phrase.add( element );
	}
	
}

class DocumentParent implements ParentElement {

	private Document document;
	
	public DocumentParent( Document document) {
		this.document = document;
	}
	
	/* (non-Javadoc)
	 * @see org.fugerit.java.doc.mod.itext.ParentElement#add(com.lowagie.text.Element)
	 */
	public void add(Element element) throws Exception {
		this.document.add( element );
	}
	
}

class CellParent implements ParentElement {
	
	private Cell cell;
	
	public CellParent( Cell cell ) {
		this.cell = cell;
	}

	/* (non-Javadoc)
	 * @see org.fugerit.java.doc.mod.itext.ParentElement#add(com.lowagie.text.Element)
	 */
	public void add(Element element) throws Exception {
		this.cell.addElement( element );
	}
	
}