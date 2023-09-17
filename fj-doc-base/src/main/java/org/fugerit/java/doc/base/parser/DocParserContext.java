package org.fugerit.java.doc.base.parser;

import java.util.LinkedList;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocParserContext {

	private static final boolean FAIL_WHEN_ELEMENT_NOT_FOUND = false;
	
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
	
	private Properties infos;
	
	private LinkedList<DocContainer> parents;

	public DocBase getDocBase() {
		return docBase;
	}
	
	public void startDocument() {
		this.parents = new LinkedList<>();
		this.currentContainer = null;
		this.currentElement = null;
		this.infos = new Properties();
	}
	
	public void endDocument() {
		if ( this.infos.size() != this.docBase.getInfo().size() ) {
			throw new ConfigRuntimeException( "Parsing error, wrong info size : "+this.infos.size() );
		} else {
			this.docBase.setStableInfo( this.infos );
			log.debug( "info {}", this.infos );
		}
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
		}
	}
	
	public void handleEndElement( String qName ) {
		if ( DocInfo.TAG_NAME.equals( qName ) ) {
			DocInfo docInfo = (DocInfo) this.currentElement;
			this.infos.setProperty( docInfo.getName(), docInfo.getContent().toString() );
		}
		if ( this.parserHelper.isContainerElement( qName ) ) {
			if ( !this.parents.isEmpty() ) {
				this.currentContainer = this.parents.remove( this.parents.size()-1 );	
			} else {
				this.currentContainer = null;
			}
		}
	}
	
	// start tag handlers - BEGIN
	
	private void handleStartDoc(Properties props ) {
		this.docBase = new DocBase();
		String xsdVersion = findXsdVersion(props);
		this.docBase.setXsdVersion( xsdVersion );
	}

	private void handleStartMeta() {
		DocContainer docMeta = this.docBase.getDocMeta();
		this.currentElement = docMeta;
	}
	
	private void handleStartInfo(Properties props ) {
		DocInfo docInfo = new DocInfo();
		docInfo.setName( props.getProperty( "name" ) );
		this.currentElement = docInfo;
	}
	
	private void handleStartHeader( String qName, Properties props ) {
		DocHeader docHeader = this.docBase.getDocHeader();
		handleHeaderFooter( docHeader , props );
		docHeader.setUseHeader( true );
		docHeader.setBasic( DocHeader.TAG_NAME.equalsIgnoreCase( qName ) );
		this.currentElement = docHeader;	
	}
	
	private void handleStartFooter( String qName, Properties props ) {
		DocFooter docFooter = this.docBase.getDocFooter();
		handleHeaderFooter( docFooter , props );
		docFooter.setUseFooter( true );
		docFooter.setBasic( DocFooter.TAG_NAME.equalsIgnoreCase( qName ) );
		this.currentElement = docFooter;
	}
	
	private void handleStartBackground() {
		DocBackground docBackground = new DocBackground();
		this.docBase.setDocBackground( docBackground );
		this.currentElement = docBackground;	
	}
	
	private void handleStartBody() {
		DocContainer docBody = this.docBase.getDocBody();
		this.currentElement = docBody;
	}
	
	private void handleStartImage( Properties props ) {
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
		String align = props.getProperty( DocStyleAlignHelper.ATTRIBUTE_NAME_ALIGN );
		docImage.setAlign( DocStyleAlignHelper.getAlign( align ) );
		this.currentElement = docImage;	
	}
	
	private void handleStartTable( Properties props ) {
		DocTable docTable = new DocTable();
		docTable.setColumns( Integer.parseInt( props.getProperty( "columns" ) )  );
		docTable.setWidth( Integer.parseInt( props.getProperty( "width", "-1" ) )  );
		docTable.setBackColor( props.getProperty( DocStyleAlignHelper.ATTRIBUTE_NAME_BACK_COLOR ) );
		docTable.setForeColor( props.getProperty( DocStyleAlignHelper.ATTRIBUTE_NAME_FORE_COLOR ) );
		docTable.setAlt( props.getProperty( DocTable.ATTRIBUTE_NAME_ALT ) );
		docTable.setSpacing( Integer.parseInt( props.getProperty( "spacing", this.infos.getProperty( GenericConsts.INFO_KEY_DEFAULT_TABLE_SPACING, GenericConsts.INFO_VALUE_DEFAULT_TABLE_SPACING ) ) ) );
		docTable.setPadding( Integer.parseInt( props.getProperty( "padding", this.infos.getProperty( GenericConsts.INFO_KEY_DEFAULT_TABLE_PADDING, GenericConsts.INFO_VALUE_DEFAULT_TABLE_PADDING ) ) ) );
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
	}
	
	private void handleStartRow( Properties props ) {
		DocRow docRow = new DocRow();
		docRow.setHeader( BooleanUtils.isTrue( props.getProperty( DocRow.ATT_HEADER ) ) );
		this.currentElement = docRow;
	}
	
	private void handleStartCell( Properties props ) {
		DocCell docCell = new DocCell();
		docCell.setCSpan( Integer.parseInt( props.getProperty( DocCell.ATTRIBUTE_NAME_COLSPAN, DocElement.STRING_1 ) ) );
		docCell.setRSpan( Integer.parseInt( props.getProperty( DocCell.ATTRIBUTE_NAME_ROWSPAN, DocElement.STRING_1 ) ) );
		docCell.setBackColor( props.getProperty( DocStyleAlignHelper.ATTRIBUTE_NAME_BACK_COLOR ) );
		docCell.setForeColor( props.getProperty( DocStyleAlignHelper.ATTRIBUTE_NAME_FORE_COLOR ) );
		docCell.setType( props.getProperty( "type" ) );
		docCell.setHeader( "true".equalsIgnoreCase( props.getProperty( "header" ) ) );
		// h align
		String align = props.getProperty( DocStyleAlignHelper.ATTRIBUTE_NAME_ALIGN );		
		docCell.setAlign( DocStyleAlignHelper.getAlign( align ) );
		// v align
		String valign = props.getProperty( "valign" );
		docCell.setValign( DocStyleAlignHelper.getValign( valign ) );
		docCell.setDocBorders( DocBorders.createBorders( props, this.infos.getProperty( GenericConsts.INFO_KEY_DEFAULT_CELL_BORDER_WIDTH, GenericConsts.INFO_VALUE_DEFAULT_CELL_BORDER_WIDTH ) ) );
		this.currentElement = docCell;
	}
	
	private void handleStartPl( ) {
		DocContainer container = new DocContainer();
		this.currentElement = container;
	}
	
	private void handleStartPara(Properties props ) {
		DocPara docPara = new DocPara();
		valuePara(docPara, props, false);
		this.currentElement = docPara;
	}

	private void handleStartH(Properties props ) {
		DocPara docPara = new DocPara();
		valuePara(docPara, props, true);
		this.currentElement = docPara;	
	}
	
	private void handleStartBr( Properties props ) {
		DocBr docBr = new DocBr();
		valuePhrase(docBr, props);
		docBr.setText( "\n" );
		this.currentElement = docBr;	
	}
	
	private void handleStartNbsp( Properties props ) {
		DocNbsp docNbsp = new DocNbsp();
		valuePhrase(docNbsp, props);
		int length = Integer.parseInt( props.getProperty( "length", "2" ) );
		docNbsp.setLength( length );
		this.currentElement = docNbsp;		
	}
	
	private void handleStartPhrase( Properties props ) {
		DocPhrase docPhrase = new DocPhrase();
		valuePhrase(docPhrase, props);
		this.currentElement = docPhrase;	
	}
	
	private void handleStartBarcode( Properties props ) {
		DocBarcode barcode = new DocBarcode();
		barcode.setSize( Integer.parseInt( props.getProperty( "size", "-1" ) ) );
		barcode.setType( props.getProperty( "type", "EAN" ) );
		barcode.setText( props.getProperty( "text" ) );
		this.currentElement = barcode;
	}
	
	private void handleStartList( Properties props ) {
		DocList docList = new DocList();
		String listType = props.getProperty( "list-type", DocList.LIST_TYPE_OL );
		docList.setListType( listType );
		this.currentElement = docList;
	}
	
	private void handleStartLi( ) {
		DocLi docLi = new DocLi();
		this.currentElement = docLi;	
	}

	private void handleStartPageBreak() {
		this.currentElement = new DocPageBreak();
	}

	private void handleStartBookmarkTree( ) {
		DocBookmarkTree docBookmarkTree = new DocBookmarkTree();
		this.docBase.setDocBookmarkTree(docBookmarkTree);
		this.currentElement = docBookmarkTree;
	}
	
	private void handleStartBookmark(Properties props ) {
		DocBookmark docBookmark = new DocBookmark();
		String ref = props.getProperty( DocBookmark.ATT_REF );
		docBookmark.setRef( ref );
		this.currentElement = docBookmark;
	}
	
	private void handleStartFinalJob( String qName, Properties props ) {
		// finishing touch
		if ( this.currentContainer != null && this.currentContainer != this.currentElement ) {
			this.currentContainer.addElement( this.currentElement );
		}
		if ( this.parserHelper.isContainerElement( qName ) ) {
				this.parents.add( this.currentContainer );
			this.currentContainer = (DocContainer)this.currentElement;
		}
		// setting id
		String id = props.getProperty( "id" );
		if ( id != null ) {
			this.docBase.setId(id, this.currentElement );
		}
	}
	
	// start tag handlers - END
	
	private boolean handleStartElementPriorityOne( String qName, Properties props ) {
		boolean ok = false;
		if ( DocPara.TAG_NAME.equalsIgnoreCase( qName ) ) {
			this.handleStartPara(props);
			ok = true;
		} else if ( DocPara.TAG_NAME_H.equalsIgnoreCase( qName ) ) {
			this.handleStartH(props);
			ok = true;
		} else if ( DocPhrase.TAG_NAME.equalsIgnoreCase( qName ) ) {
			this.handleStartPhrase(props);
			ok = true;
		} else if ( DocList.TAG_NAME.equalsIgnoreCase( qName ) ) {
			this.handleStartList(props);
			ok = true;
		} else if ( DocLi.TAG_NAME.equalsIgnoreCase( qName ) ) {
			this.handleStartLi();
			ok = true;
		} else if ( DocTable.TAG_NAME.equalsIgnoreCase( qName ) ) {
			this.handleStartTable(props);
			ok = true;
		} else if ( DocRow.TAG_NAME.equalsIgnoreCase( qName ) ) {
			this.handleStartRow(props);
			ok = true;
		} else if ( DocCell.TAG_NAME.equalsIgnoreCase( qName ) ) {
			this.handleStartCell(props);
			ok = true;
		} else if ( DocPageBreak.TAG_NAME.equalsIgnoreCase( qName ) ) {
			this.handleStartPageBreak();
			ok = true;
		}
		return ok;
	}
	
	private boolean handleStartElementPriorityTwo( String qName, Properties props ) {
		boolean ok = false;
		if ( this.parserNames.isTypeDoc( qName ) ) {
			this.handleStartDoc(props);
			ok = true;
		} else if ( DocContainer.TAG_NAME_META.equalsIgnoreCase( qName ) || DocContainer.TAG_NAME_METADATA.equalsIgnoreCase( qName ) ) {
			this.handleStartMeta();
			ok = true;
		} else if ( DocContainer.TAG_NAME_BODY.equalsIgnoreCase( qName ) ) {
			this.handleStartBody();
			ok = true;
		} else if ( DocInfo.TAG_NAME.equalsIgnoreCase( qName ) ) {
			this.handleStartInfo( props );
			ok = true;
		} else if ( DocHeader.TAG_NAME.equalsIgnoreCase( qName ) || DocHeader.TAG_NAME_EXT.equalsIgnoreCase( qName )  ) {
			this.handleStartHeader( qName, props);
			ok = true;
		} else if ( DocFooter.TAG_NAME.equalsIgnoreCase( qName ) || DocFooter.TAG_NAME_EXT.equalsIgnoreCase( qName ) ) {
			this.handleStartFooter(qName, props);
			ok = true;
		} else if ( DocBr.TAG_NAME.equalsIgnoreCase( qName ) ) {
			this.handleStartBr( props);
			ok = true;
		} else if ( DocNbsp.TAG_NAME.equalsIgnoreCase( qName ) ) {
			this.handleStartNbsp( props);
			ok = true;
		}
		return ok;
	}
	
	private boolean handleStartElementPriorityThree( String qName, Properties props ) {
		boolean ok = false;
		if ( DocBackground.TAG_NAME.equalsIgnoreCase( qName ) ) {
			this.handleStartBackground();
			ok = true;
		} else if ( DocImage.TAG_NAME.equalsIgnoreCase( qName ) ) {
			this.handleStartImage(props);
			ok = true;
		} else if (  DocContainer.TAG_NAME_PL.equalsIgnoreCase( qName ) ) {
			this.handleStartPl();
			ok = true;
		}  else if ( DocBarcode.TAG_NAME.equalsIgnoreCase( qName ) ) {
			this.handleStartBarcode( props);
			ok = true;
		} else if ( DocBookmarkTree.TAG_NAME.equalsIgnoreCase( qName ) ) {
			this.handleStartBookmarkTree();
			ok = true;
		} else if ( DocBookmark.TAG_NAME.equalsIgnoreCase( qName ) ) {
			this.handleStartBookmark(props);
			ok = true;
		}
		return ok;
	}
	
	public void handleStartElement( String qName, Properties props ) {
		boolean ok = this.handleStartElementPriorityOne(qName, props);
		if ( !ok ) {
			ok = this.handleStartElementPriorityTwo(qName, props);
			if ( !ok ) {
				ok = this.handleStartElementPriorityThree(qName, props);
				if ( !ok ) {
					String message = "Element not found : "+qName;
					if ( FAIL_WHEN_ELEMENT_NOT_FOUND ) {
						throw new ConfigRuntimeException( message );
					} else {
						log.warn( message );
					}
				}
			}
		}
		this.handleStartFinalJob(qName, props);
	}

	private static void handleHeaderFooter( DocHeaderFooter headerFooter, Properties atts ) {
		String align = atts.getProperty( DocStyleAlignHelper.ATTRIBUTE_NAME_ALIGN );
		headerFooter.setAlign( DocStyleAlignHelper.getAlign( align ) );
		String numbered = atts.getProperty( "numbered" );
		headerFooter.setNumbered( BooleanUtils.isTrue( numbered ) );
		String borderWidth = atts.getProperty( "border-width", "0" );
		headerFooter.setBorderWidth( Integer.parseInt( borderWidth ) );
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
		String align = props.getProperty( DocStyleAlignHelper.ATTRIBUTE_NAME_ALIGN );
		docPara.setAlign( DocStyleAlignHelper.getAlign( align ) );
		String fontName = props.getProperty(  "font-name" );
		docPara.setFontName( fontName );
		String leading = props.getProperty( "leading" );
		docPara.setBackColor( props.getProperty( DocStyleAlignHelper.ATTRIBUTE_NAME_BACK_COLOR ) );
		docPara.setForeColor( props.getProperty( DocStyleAlignHelper.ATTRIBUTE_NAME_FORE_COLOR ) );
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
