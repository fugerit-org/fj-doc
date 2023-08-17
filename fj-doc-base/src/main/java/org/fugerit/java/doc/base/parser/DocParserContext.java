package org.fugerit.java.doc.base.parser;

import java.util.LinkedList;
import java.util.Properties;

import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.model.DocBackground;
import org.fugerit.java.doc.base.model.DocBarcode;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocBookmark;
import org.fugerit.java.doc.base.model.DocBookmarkTree;
import org.fugerit.java.doc.base.model.DocBorders;
import org.fugerit.java.doc.base.model.DocBr;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocContainer;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocFooter;
import org.fugerit.java.doc.base.model.DocHeader;
import org.fugerit.java.doc.base.model.DocHeaderFooter;
import org.fugerit.java.doc.base.model.DocImage;
import org.fugerit.java.doc.base.model.DocInfo;
import org.fugerit.java.doc.base.model.DocLi;
import org.fugerit.java.doc.base.model.DocList;
import org.fugerit.java.doc.base.model.DocNbsp;
import org.fugerit.java.doc.base.model.DocPageBreak;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.model.DocPhrase;
import org.fugerit.java.doc.base.model.DocRow;
import org.fugerit.java.doc.base.model.DocTable;
import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;
import org.fugerit.java.doc.base.xml.DocStyleAlignHelper;

public class DocParserContext {

	public static String findXsdVersion( Properties props ) {
		String xsdVersion = null;
		for ( Object key : props.keySet() ) {
			String k = String.valueOf( key );
			if ( k.contains( "schemaLocation" ) ) {
				String value = props.getProperty( k );
				int xsdBaseIndex = value.indexOf( XSD_URL_BASE );
				if ( xsdBaseIndex != -1 ) {
					xsdVersion = value.substring( xsdBaseIndex+XSD_URL_BASE.length(), value.length()-4 );
				}
			}
		}
		return xsdVersion;
	}
	
	private static final String XSD_URL_BASE = "www.fugerit.org/data/java/doc/xsd/doc-";
	
	private static final String XSD_BASE = "http://javacoredoc.fugerit.org https://"+XSD_URL_BASE;
	
	public static String createXsdVersionXmlns( String xsdVersion ) {
		return XSD_BASE+xsdVersion+".xsd";
	}
	
	private DocParserHelper parserHelper = DocParserHelper.getInstance();
	
	private DocParserNameCheck parserNames = DocParserNameCheck.getInstance();
	
	private DocBase docBase;
	
	private DocElement currentElement;
	
	private DocContainer currentContainer;
	
	private LinkedList<DocContainer> parents;

	private String defaultTablePadding = GenericConsts.INFO_DEFAULT_TABLE_PADDING_DEF;
	private String defaultTableSpacing = GenericConsts.INFO_DEFAULT_TABLE_SPACING_DEF;
	private String defaultCellBorderWidth = GenericConsts.INFO_VALUE_DEFAULT_CELL_BORDER_WIDTH;
	
	public DocBase getDocBase() {
		return docBase;
	}
	
	public void startDocument() {
		this.parents = new LinkedList<>();
		this.currentContainer = null;
		this.currentElement = null;
	}
	
	public void endDocument() {
		this.docBase.setStableInfo( this.docBase.getInfo() );
	}
	
	public void handleText( String text ) {
		if ( text.trim().length() > 0 && this.currentElement instanceof DocPhrase ) {	
			DocPhrase docPhrase = (DocPhrase)this.currentElement;
			docPhrase.setText( docPhrase.getText()+text );
		} else if ( text.trim().length() > 0 && this.currentElement instanceof DocPara ) {
			DocPara docPara = (DocPara)this.currentElement;
			docPara.setText( docPara.getText()+text );
		} else if ( text.trim().length() > 0 && this.currentElement instanceof DocBookmark ) {
			DocBookmark docBookmarkTitle = (DocBookmark)this.currentElement;
			docBookmarkTitle.setTitle( docBookmarkTitle.getTitle()+text );
		} else if ( text.trim().length() > 0 && this.currentElement instanceof DocInfo ) {
			DocInfo docInfo = (DocInfo)this.currentElement;
			docInfo.getContent().append( text );
			if ( GenericConsts.INFO_DEFAULT_TABLE_PADDING.equalsIgnoreCase( docInfo.getName() ) ) {
				this.defaultTablePadding = text;
			} else if ( GenericConsts.INFO_DEFAULT_TABLE_SPACING.equalsIgnoreCase( docInfo.getName() ) ) {
				this.defaultTableSpacing = text;
			} else if ( GenericConsts.INFO_KEY_DEFAULT_CELL_BORDER_WIDTH.equalsIgnoreCase( docInfo.getName() ) ) {
				this.defaultCellBorderWidth = text;
			}
		}
	}
	
	public void handleEndElement( String qName ) {
		if ( this.parserHelper.isContainerElement( qName ) ) {
			if ( !this.parents.isEmpty() ) {
				this.currentContainer = (DocContainer)this.parents.remove( this.parents.size()-1 );	
			} else {
				this.currentContainer = null;
			}
		}
	}
	
	public void handleStartElement( String qName, Properties props ) {
		if ( this.parserNames.isTypeDoc( qName ) ) {
			this.docBase = new DocBase();
			String xsdVersion = findXsdVersion(props);
			this.docBase.setXsdVersion( xsdVersion );
		} else if ( DocContainer.TAG_NAME_META.equalsIgnoreCase( qName ) || DocContainer.TAG_NAME_METADATA.equalsIgnoreCase( qName ) ) {
			DocContainer docMeta = this.docBase.getDocMeta();
			this.currentElement = docMeta;
		} else if ( DocInfo.TAG_NAME.equalsIgnoreCase( qName ) ) {
			DocInfo docInfo = new DocInfo();
			docInfo.setName( props.getProperty( "name" ) );
			this.currentElement = docInfo;
		} else if ( DocHeader.TAG_NAME.equalsIgnoreCase( qName ) || DocHeader.TAG_NAME_EXT.equalsIgnoreCase( qName )  ) {
			DocHeader docHeader = this.docBase.getDocHeader();
			handleHeaderFooter( docHeader , props );
			docHeader.setUseHeader( true );
			if ( DocHeader.TAG_NAME_EXT.equalsIgnoreCase( qName ) ) {
				docHeader.setBasic( false );
			} else {
				docHeader.setBasic( true );
			}
			this.currentElement = docHeader;		
		} else if ( DocFooter.TAG_NAME.equalsIgnoreCase( qName ) || DocFooter.TAG_NAME_EXT.equalsIgnoreCase( qName ) ) {
			DocFooter docFooter = this.docBase.getDocFooter();
			handleHeaderFooter( docFooter , props );
			docFooter.setUseFooter( true );
			if ( DocFooter.TAG_NAME_EXT.equalsIgnoreCase( qName ) ) {
				docFooter.setBasic( false );
			} else {
				docFooter.setBasic( true );
			}
			this.currentElement = docFooter;
		} else if ( DocBackground.TAG_NAME.equalsIgnoreCase( qName ) ) {
			DocBackground docBackground = new DocBackground();
			this.docBase.setDocBackground( docBackground );
			this.currentElement = docBackground;
		} else if ( "body".equalsIgnoreCase( qName ) ) {
			DocContainer docBody = this.docBase.getDocBody();
			this.currentElement = docBody;
			//Properties info = this.docBase.getInfo();
		} else if ( DocImage.TAG_NAME.equalsIgnoreCase( qName ) ) {
			DocImage docImage = new DocImage();
			// setting paragraph style
			String url = props.getProperty( "url" );
			docImage.setUrl( url );
			String scaling = props.getProperty( "scaling" );
			if ( scaling != null ) {
				docImage.setScaling( Integer.valueOf( scaling ) );	
			} else {
				docImage.setScaling( null );
			}
			String base64 = props.getProperty( "base64" );
			if ( StringUtils.isNotEmpty( base64 ) ) {
				docImage.setBase64( base64 );
			}
			String type = props.getProperty( "type" );
			if ( StringUtils.isNotEmpty( type ) ) {
				docImage.setType( type );
			}
			String alt = props.getProperty( "alt" );
			if ( StringUtils.isNotEmpty( alt ) ) {
				docImage.setAlt( alt );
			}
			String align = props.getProperty( "align" );
			docImage.setAlign( DocStyleAlignHelper.getAlign( align ) );
			this.currentElement = docImage;		
		} else if ( "pl".equalsIgnoreCase( qName ) ) {
			DocContainer container = new DocContainer();
			this.currentElement = container;
		} else if ( DocPara.TAG_NAME.equalsIgnoreCase( qName ) ) {
			DocPara docPara = new DocPara();
			valuePara(docPara, props, false);
			this.currentElement = docPara;
		} else if ( DocPara.TAG_NAME_H.equalsIgnoreCase( qName ) ) {
			DocPara docPara = new DocPara();
			valuePara(docPara, props, true);
			this.currentElement = docPara;			
		} else if ( "br".equalsIgnoreCase( qName ) ) {
			DocBr docBr = new DocBr();
			valuePhrase(docBr, props);
			docBr.setText( "\n" );
			this.currentElement = docBr;			
		} else if ( "nbsp".equalsIgnoreCase( qName ) ) {
			DocNbsp docNbsp = new DocNbsp();
			valuePhrase(docNbsp, props);
			int length = Integer.parseInt( props.getProperty( "length", "2" ) );
			docNbsp.setLength( length );
			this.currentElement = docNbsp;					
		} else if ( DocPhrase.TAG_NAME.equalsIgnoreCase( qName ) ) {
			DocPhrase docPhrase = new DocPhrase();
			valuePhrase(docPhrase, props);
			this.currentElement = docPhrase;			
		} else if ( "barcode".equalsIgnoreCase( qName ) ) {
			DocBarcode barcode = new DocBarcode();
			barcode.setSize( Integer.parseInt( props.getProperty( "size", "-1" ) ) );
			barcode.setType( props.getProperty( "type", "EAN" ) );
			barcode.setText( props.getProperty( "text" ) );
			this.currentElement = barcode;
		} else if ( "list".equalsIgnoreCase( qName ) ) {
			DocList docList = new DocList();
			String listType = props.getProperty( "list-type", DocList.LIST_TYPE_OL );
			docList.setListType( listType );
			this.currentElement = docList;
		} else if ( "li".equalsIgnoreCase( qName ) ) {
			DocLi docLi = new DocLi();
			this.currentElement = docLi;			
		} else if ( DocTable.TAG_NAME.equalsIgnoreCase( qName ) ) {
			DocTable docTable = new DocTable();
			docTable.setColumns( Integer.parseInt( props.getProperty( "columns" ) )  );
			docTable.setWidth( Integer.parseInt( props.getProperty( "width", "-1" ) )  );
			docTable.setBackColor( props.getProperty( "back-color" ) );
			docTable.setForeColor( props.getProperty( "fore-color" ) );
			docTable.setSpacing( Integer.parseInt( props.getProperty( "spacing", this.defaultTableSpacing ) ) );
			docTable.setPadding( Integer.parseInt( props.getProperty( "padding", this.defaultTablePadding ) ) );
			docTable.setRenderMode( props.getProperty( "render-mode", DocTable.RENDER_MODE_NORMAL ) );
			String cols = props.getProperty( "colwidths" );
			if ( cols != null ) {
				String[] colsParsed = cols.split( ";" );
				int[] withds = new int[colsParsed.length];
				for ( int k=0; k<withds.length; k++ ) {
					withds[k] = Integer.parseInt( colsParsed[k] );
				}
				docTable.setColWithds( withds );
			}
			String spaceBefore = props.getProperty( "space-before" );
			String spaceAfter = props.getProperty( "space-after" );
			if ( spaceBefore != null ) {
				docTable.setSpaceBefore( Float.valueOf( spaceBefore ) );
			}
			if ( spaceAfter != null ) {
				docTable.setSpaceAfter( Float.valueOf( spaceAfter ) );
			}			
			this.currentElement = docTable;
		} else if ( DocRow.TAG_NAME.equalsIgnoreCase( qName ) ) {
			DocRow docRow = new DocRow();
			docRow.setHeader( BooleanUtils.isTrue( props.getProperty( DocRow.ATT_HEADER ) ) );
			this.currentElement = docRow;
		} else if ( DocCell.TAG_NAME.equalsIgnoreCase( qName ) ) {
			DocCell docCell = new DocCell();
			docCell.setCSpan( Integer.parseInt( props.getProperty( DocCell.ATTRIBUTE_NAME_COLSPAN, DocElement.STRING_1 ) ) );
			docCell.setRSpan( Integer.parseInt( props.getProperty( DocCell.ATTRIBUTE_NAME_ROWSPAN, DocElement.STRING_1 ) ) );
			docCell.setBackColor( props.getProperty( "back-color" ) );
			docCell.setForeColor( props.getProperty( "fore-color" ) );
			docCell.setType( props.getProperty( "type" ) );
			docCell.setHeader( "true".equalsIgnoreCase( props.getProperty( "header" ) ) );
			// h align
			String align = props.getProperty( "align" );		
			docCell.setAlign( DocStyleAlignHelper.getAlign( align ) );
			// v align
			String valign = props.getProperty( "valign" );
			docCell.setValign( DocStyleAlignHelper.getValign( valign ) );
			docCell.setDocBorders( DocBorders.createBorders( props, this.defaultCellBorderWidth ) );
			this.currentElement = docCell;
		} else if ( "page-break".equalsIgnoreCase( qName ) ) {
			this.currentElement = new DocPageBreak();
		} else if ( DocBookmarkTree.TAG_NAME.equalsIgnoreCase( qName ) ) {
			DocBookmarkTree docBookmarkTree = new DocBookmarkTree();
			this.docBase.setDocBookmarkTree(docBookmarkTree);
			this.currentElement = docBookmarkTree;
		} else if ( DocBookmark.TAG_NAME.equalsIgnoreCase( qName ) ) {
			DocBookmark docBookmark = new DocBookmark();
			String ref = props.getProperty( DocBookmark.ATT_REF );
			docBookmark.setRef( ref );
			this.currentElement = docBookmark;
		}
		// processamenti finali
		if ( this.currentContainer != null && this.currentContainer != this.currentElement ) {
			this.currentContainer.addElement( this.currentElement );
		}
		if ( this.parserHelper.isContainerElement( qName ) ) {
			//if ( !qName.equals( "header-ext" ) && !qName.equals( "footer-ext" ) ) {
				this.parents.add( this.currentContainer );
			//}
			this.currentContainer = (DocContainer)this.currentElement;
		}
		// setting id
		String id = props.getProperty( "id" );
		if ( id != null ) {
			this.docBase.setId(id, this.currentElement );
		}
		
	}

	private static void handleHeaderFooter( DocHeaderFooter headerFooter, Properties atts ) {
		String align = atts.getProperty( "align" );
		headerFooter.setAlign( DocStyleAlignHelper.getAlign( align ) );
		String numbered = atts.getProperty( "numbered" );
		headerFooter.setNumbered( Boolean.valueOf( numbered ).booleanValue() );
		String borderWidth = atts.getProperty( "border-width", "0" );
		headerFooter.setBorderWidth( Integer.valueOf( borderWidth ).intValue() );
		String exepectedSize = atts.getProperty( "expected-size", "15" );
		headerFooter.setExpectedSize( Integer.parseInt( exepectedSize ) );
	}
	
	private static void valuePhrase( DocPhrase docPhrase, Properties props ) {
		// setting phrase size
		docPhrase.setSize( Integer.parseInt( props.getProperty( "size", "-1" ) ) );
		// setting phrase style
		String style = props.getProperty( "style" );
		docPhrase.setStyle( DocPara.parseStyle( style ) );
		docPhrase.setOriginalStyle( DocPara.parseStyle( style, DocPara.STYLE_UNSET ) );
		// font name
		String fontName = props.getProperty(  "font-name" );
		docPhrase.setFontName( fontName );
		//leading
		String leading = props.getProperty( "leading" );
		if ( leading != null ) {
			docPhrase.setLeading( Float.valueOf( leading ) );
		}
		String link = props.getProperty( "link" );
		if ( link != null ) {
			docPhrase.setLink( link  );
		}
		String anchor = props.getProperty( "anchor" );
		if ( anchor != null ) {
			docPhrase.setAnchor( anchor );
		}
		// white space collpase
		String whiteSpaceCollapse = props.getProperty( "white-space-collapse" );
		if ( StringUtils.isNotEmpty( whiteSpaceCollapse ) ) {
			docPhrase.setWhiteSpaceCollapse(whiteSpaceCollapse);
		}
	}
	
	private static void valuePara( DocPara docPara, Properties props, boolean headings ) {
		// setting paragraph style
		String style = props.getProperty( "style" );
		int defaultStyle = DocPara.STYLE_NORMAL;
		if ( headings ) {
			defaultStyle = DocPara.STYLE_BOLD;
		}
		docPara.setStyle( DocPara.parseStyle( style, defaultStyle ) );
		docPara.setOriginalStyle( DocPara.parseStyle( style, DocPara.STYLE_UNSET ) );
		String id = props.getProperty( "id" );
		docPara.setId( id );
		// setting paragraph align
		String align = props.getProperty( "align" );
		docPara.setAlign( DocStyleAlignHelper.getAlign( align ) );
		String fontName = props.getProperty(  "font-name" );
		docPara.setFontName( fontName );
		String leading = props.getProperty( "leading" );
		docPara.setBackColor( props.getProperty( "back-color" ) );
		docPara.setForeColor( props.getProperty( "fore-color" ) );
		docPara.setFormat( props.getProperty( "format" ) );
		docPara.setType( props.getProperty( "type" ) );
		if ( leading != null ) {
			docPara.setLeading( Float.valueOf( leading ) );
		}
		// setting paragraph size
		docPara.setSize( Integer.parseInt( props.getProperty( "size", "-1" ) ) );
		String textIndent = props.getProperty( "text-indent" );
		String spaceBefore = props.getProperty( "space-before" );
		String spaceAfter = props.getProperty( "space-after" );
		String spaceLeft = props.getProperty( "space-left" );
		String spaceRight = props.getProperty( "space-right" );
		if ( textIndent != null ) {
			docPara.setTextIndent( Float.valueOf( textIndent ) );
		}
		if ( spaceBefore != null ) {
			docPara.setSpaceBefore( Float.valueOf( spaceBefore ) );
		}
		if ( spaceAfter != null ) {
			docPara.setSpaceAfter( Float.valueOf( spaceAfter ) );
		}
		if ( spaceLeft != null ) {
			docPara.setSpaceLeft( Float.valueOf( spaceLeft ) );
		}
		if ( spaceRight != null ) {
			docPara.setSpaceRight( Float.valueOf( spaceRight ) );
		}
		// setting head level
		docPara.setHeadLevel( Integer.parseInt( props.getProperty( "head-level", String.valueOf( DocPara.DEFAULT_HEAD_LEVEL ) ) ) );
		// white space collpase
		String whiteSpaceCollapse = props.getProperty( "white-space-collapse" );
		if ( StringUtils.isNotEmpty( whiteSpaceCollapse ) ) {
			docPara.setWhiteSpaceCollapse(whiteSpaceCollapse);
		}
	}

	
}
