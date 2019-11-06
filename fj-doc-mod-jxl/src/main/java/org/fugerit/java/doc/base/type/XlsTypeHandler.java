package org.fugerit.java.doc.base.type;

import java.awt.Color;
import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocBorders;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.model.DocRow;
import org.fugerit.java.doc.base.model.DocTable;
import org.fugerit.java.doc.mod.itext.ITextDocHandler;

import jxl.CellView;
import jxl.Workbook;
import jxl.biff.DisplayFormat;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Font;
import jxl.format.RGB;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.NumberFormats;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class XlsTypeHandler extends DocTypeHandlerDefault {
	
	public static DocTypeHandler HANDLER = new XlsTypeHandler();
	
	public XlsTypeHandler() {
		super( DOC_OUTPUT_XLS );
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		DocBase docBase = docInput.getDoc();
		OutputStream outputStream = docOutput.getOs();
		String excelTemplate = docBase.getInfo().getProperty( XlsTypeHandler.PROP_XLS_TEMPLATE );
		Workbook templateXls = null;
		if ( excelTemplate != null ) {
			templateXls = Workbook.getWorkbook( new File( excelTemplate ) );
		}			
		XlsTypeHandler.handleDoc( docBase , outputStream, templateXls );
	}

	public final static String PROP_XLS_WIDTH_MULTIPLIER = "excel-width-multiplier";
	
	public final static String PROP_XLS_IGNORE_FORMAT = "excel-ignore-format";
	
	public final static String PROP_XLS_TEMPLATE = "excel-template";
	
	public final static String PROP_XLS_TABLE_ID = "excel-table-id";

	public final static String PROP_XLS_WIDTH_MULTIPLIER_DEFAULT = "256";
	
	public final static String DOC_OUTPUT_XLS = "xls";
	

	private static int rgbDiff( RGB rgb1, RGB rgb2 ) {
		int result = 0;
		result+= ( Math.abs( rgb1.getRed() - rgb2.getRed() ) );
		result+= ( Math.abs( rgb1.getBlue() - rgb2.getBlue() ) );
		result+= ( Math.abs( rgb1.getGreen() - rgb2.getGreen() ) );
		return result;
	}
	
	// find the closest jxl colour to a give awt colour
	public static Colour closestColor( Color awtColor ) {
		Colour cc = null;
		Colour[] c = Colour.getAllColours();
		RGB rgbBase = new RGB( awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue() );
		int rgbDiff = 10000000;
		for ( int a=0; a<c.length; a++ ) {
			Colour t = c[a];
			RGB rgb = t.getDefaultRGB();
			int currDiff = rgbDiff( rgb, rgbBase );
			if ( rgbDiff > currDiff  ) {
				rgbDiff = currDiff;
				cc = t;
			}
		}
		return cc;
	}
	
	private static BorderLineStyle getBorderStyle( int borderWidth ) {
		BorderLineStyle bls = BorderLineStyle.THIN;
		if ( borderWidth == 0 ) {
			bls = BorderLineStyle.NONE;
		} else if ( borderWidth > 1 ) {
			bls = BorderLineStyle.MEDIUM;
		} else if ( borderWidth > 3 ) {
			bls = BorderLineStyle.THICK;			
		}		
		return bls;
	}
	
	private static TableMatrix handleMatrix( DocTable table, boolean ignoreFormat, WritableSheet dati ) throws Exception {
		TableMatrix matrix = new TableMatrix( table.containerSize() , table.getColumns() );
		Iterator<DocElement> rows = table.docElements();
		while ( rows.hasNext() ) {
			DocRow row = (DocRow)rows.next();
			Iterator<DocElement> cells = row.docElements();
			while ( cells.hasNext() ) {
				DocCell cell = (DocCell)cells.next();
				matrix.setNext( cell, cell.getRSpan() , cell.getCSpan() );
			}
		}
		
		for ( int rn=0; rn<matrix.getRowCount(); rn++ ) {
			for ( int cn=0; cn<matrix.getColumnCount(); cn++ ) {
				String type = null;
				String format = null;
				DocCell cell = matrix.getCell( rn, cn );
				DocCell parent = matrix.getParent( rn, cn );
				String text = "";
				DocPara currentePara = null;
				if ( cell != null ) {
					Iterator<DocElement> it1 = cell.docElements();
					DocElement current = (DocElement)it1.next();
					if ( current instanceof DocPara ) {
						currentePara = ((DocPara)current);
						text = currentePara.getText();
						type = currentePara.getType();
						format = currentePara.getFormat();
					} else {
						text = String.valueOf( current );
						currentePara = null;
					}
				} else {
					currentePara = null;
				}
				WritableCellFormat cf = new WritableCellFormat();
				DisplayFormat df = null;
				// in case of number check for format string
				if ( format != null && "number".equalsIgnoreCase( type ) ) {
					System.out.println( ">>>>>>>>>>>>>> FORMAT - "+format );
					if ( "float".equalsIgnoreCase( format ) ) {
						df = NumberFormats.FLOAT;
					} else {
						df = new NumberFormat( format, NumberFormat.COMPLEX_FORMAT);
					}
					cf = new WritableCellFormat( df );
				}
				if ( parent != null && !ignoreFormat ) {
					
					// must go first as it has the chance to change the cell format
					if ( parent.getForeColor() != null ) {
						Font f = cf.getFont();
						WritableFont wf = new WritableFont( f );
						wf.setColour( ( closestColor( ITextDocHandler.parseHtmlColor( parent.getForeColor() ) ) ) );
						if ( df != null ) {
							cf = new WritableCellFormat( wf, df );	
						} else {
							cf = new WritableCellFormat( wf );
						}
						
					}	
					// style
					if ( currentePara != null ) {
						Font f = cf.getFont();
						WritableFont wf = new WritableFont( f );
						if ( currentePara.getStyle() == DocPara.STYLE_BOLD ) {
							wf.setBoldStyle( WritableFont.BOLD );
						} else if ( currentePara.getStyle() == DocPara.STYLE_ITALIC ) {
							wf.setItalic( true );
						} else if ( currentePara.getStyle() == DocPara.STYLE_BOLDITALIC ) {	
							wf.setBoldStyle( WritableFont.BOLD );
							wf.setItalic( true );
						} else if ( currentePara.getStyle() == DocPara.STYLE_UNDERLINE ) {
							wf.setUnderlineStyle( UnderlineStyle.SINGLE );
						}
						if ( df != null ) {
							cf = new WritableCellFormat( wf, df );	
						} else {
							cf = new WritableCellFormat( wf );
						}
					}
					// back color
					if ( parent.getBackColor() != null) {
						cf.setBackground( closestColor( ITextDocHandler.parseHtmlColor( parent.getBackColor() ) ) );
					}
					//bordi
					DocBorders borders = matrix.getBorders( rn, cn );
					cf.setBorder( Border.LEFT,  getBorderStyle( borders.getBorderWidthLeft() ) );
					cf.setBorder( Border.RIGHT,  getBorderStyle( borders.getBorderWidthRight() ) );
					cf.setBorder( Border.BOTTOM,  getBorderStyle( borders.getBorderWidthBottom() ) );
					cf.setBorder( Border.TOP,  getBorderStyle( borders.getBorderWidthTop() ) );
					if ( cell != null ) {
						// alignment
						if ( cell.getAlign() == DocPara.ALIGN_CENTER ) {
							cf.setAlignment( Alignment.CENTRE );
						} else if ( cell.getAlign() == DocPara.ALIGN_RIGHT ) {
							cf.setAlignment( Alignment.RIGHT );
						} else if ( cell.getAlign() == DocPara.ALIGN_LEFT ) {
							cf.setAlignment( Alignment.LEFT );
						}
						// vertical alignment
						if ( cell.getValign() == DocPara.ALIGN_MIDDLE ) {
							cf.setVerticalAlignment( VerticalAlignment.CENTRE );
						} else if ( cell.getValign() == DocPara.ALIGN_BOTTOM ) {
							cf.setVerticalAlignment( VerticalAlignment.BOTTOM );
						} else if ( cell.getValign() == DocPara.ALIGN_TOP ) {
							cf.setVerticalAlignment( VerticalAlignment.TOP );
						} 
					}
				}
				WritableCell current = null;
				if ( "number".equalsIgnoreCase( type ) ) {
					BigDecimal bd = new BigDecimal( text );
					current = new Number( cn, rn,  bd.doubleValue(), cf );
				} else {
					current = new Label( cn, rn, text, cf );
				}
				dati.addCell( current );	
			}
		}
		return matrix;
	}

	public static String convertComma( String s ) {
		int index = s.indexOf( ',' );
		if ( index!=-1 ) {
			s = s.substring( 0, index )+"."+s.substring( index+1 );
		}
		return s; 	
	}	
	
	public static String removeDots( String s ) {
		StringBuffer r = new StringBuffer();
		StringTokenizer st = new StringTokenizer( s, "." );
		while (st.hasMoreTokens()) {
			r.append( st.nextToken() );
		}
		return r.toString(); 	
	}
	
	public static String prepareNumber( String s ) {
		s = removeDots( s );
		s = convertComma( s );
		return s;
	}
	
	private static void handleMerge( DocTable table, boolean ignoreFormat, WritableSheet dati ) throws Exception {
		TableMatrix matrix = handleMatrix(table, ignoreFormat, dati);
		for ( int rn=0; rn<matrix.getRowCount(); rn++ ) {
			for ( int cn=0; cn<matrix.getColumnCount(); cn++ ) {
				DocCell cell = matrix.getCell( rn, cn );
				if ( cell != null ) {
					int rs = cell.getRSpan()-1;
					int cs = cell.getCSpan()-1;
					if ( rs != 0 || cs != 0 ) {
						dati.mergeCells( cn, rn, cn+cs, rn+rs );	
					}
				}
			}
		}
	}

	
	public static void handleDoc( DocBase docBase, OutputStream os, Workbook templateXls ) throws Exception {
		String excelTableId = docBase.getInfo().getProperty( PROP_XLS_TABLE_ID );
		String excelTableSheet[] = excelTableId.split( ";" );
		WritableWorkbook outputXls = null;
		if ( templateXls == null ) {
			outputXls = Workbook.createWorkbook( os );
		} else {
			outputXls = Workbook.createWorkbook( os, templateXls );
		}
		
		boolean ignoreFormat = "true".equalsIgnoreCase( docBase.getInfo().getProperty( PROP_XLS_IGNORE_FORMAT ) );
		for ( int k=0; k<excelTableSheet.length; k++ ) {
			String currentSheetData[] = excelTableSheet[k].split( "=" );
			String sheetId = currentSheetData[0];
			String sheetName = currentSheetData[1];
			DocTable table = (DocTable)docBase.getElementById( sheetId );
			
			WritableSheet dati = null;
			if ( templateXls == null ) {
				dati = outputXls.createSheet( sheetName , k );
			} else {
				dati = outputXls.getSheet( k );
				dati.setName( sheetName );
			}
			
			int[] colW = table.getColWithds();
			for ( int i=0; i<colW.length; i++ ) {
				CellView cw = new CellView( dati.getColumnView( i ) );
				int mul = Integer.parseInt( docBase.getInfo().getProperty( PROP_XLS_WIDTH_MULTIPLIER, PROP_XLS_WIDTH_MULTIPLIER_DEFAULT ) );
				cw.setSize( colW[i]* mul );
				dati.setColumnView( i , cw );
			}
			
			handleMerge(table, ignoreFormat, dati);
			
		}
		outputXls.write();
		outputXls.close();
	}

}

class TableMatrix {
	
	private MatrixCell[][] text;
	
	private int cn, rn;
	
	public TableMatrix( int rows, int columns ) {
		this.text = new MatrixCell[ rows ][ columns ];
		cn = -1;
		rn = 0;
	}
	
	public int getRowCount() {
		return this.text.length;
	}
	
	public int getColumnCount() {
		return this.text[0].length;
	}	
	
	public DocCell getCell( int r, int c ) {
		return this.text[ r ][ c ].getCell(); 
	}
	
	public DocCell getParent( int r, int c ) {
		return this.text[ r ][ c ].getParent(); 
	}
	
	public DocBorders getBorders( int r, int c ) throws CloneNotSupportedException {
		return this.text[ r ][ c ].getBorders(); 
	}
	
	private MatrixCell getCellMatrix( int r, int c ) {
		return this.text[ r ][ c ]; 
	}
	
	private void setCell( DocCell s, DocCell p, int r, int c ) {
		this.text[ r ][ c ] = new MatrixCell( s, p ); 
	}
	
	public void setNext( DocCell s, final int rs, final int cs ) {
		if ( 1+this.cn == this.getColumnCount() ) {
			this.cn = 0;
			this.rn++;
		} else {
			this.cn++;	
		}
		DocCell p = s;
		if ( this.getCellMatrix( this.rn , this.cn ) == null ) {
			int counterRS = 0;
			while ( counterRS < rs ) {
				int counterCS = 0;
				while ( counterCS < cs ) {
					this.setCell( s , p, this.rn+counterRS, this.cn+counterCS );
					s = null;
					counterCS++;
				}
				counterRS ++;
			}
		} else {
			this.setNext( s, rs, cs );
		}
	}
	
	public boolean isCellEmpty( int r, int c ) {
		return this.getCell(r, c)==null;
	}

}

class MatrixCell {
	
	private DocCell cell;

	private DocCell parent;
	
	public MatrixCell( DocCell cell, DocCell parent ) {
		super();
		this.cell = cell;
		this.parent = parent;
	}

	public DocCell getParent() {
		return parent;
	}

	public DocCell getCell() {
		return cell;
	}

	public void setCell(DocCell cell) {
		this.cell = cell;
	}
	
	public DocBorders getBorders() throws CloneNotSupportedException {
		DocBorders borders = new DocBorders();
		if ( this.getParent() == this.getCell() ) {
			borders = (DocBorders)this.cell.getDocBorders().clone();
			if ( this.getCell().getRSpan() > 1 ) {
				borders.setBorderWidthBottom( 0 );
			}
			if ( this.getCell().getCSpan() > 1 ) {
				borders.setBorderWidthRight( 0 );
			}
		} else {
			borders = (DocBorders)this.parent.getDocBorders().clone();
			if ( this.parent.getRSpan() > 1 ) {
				borders.setBorderWidthTop( 0 );
				borders.setBorderWidthBottom( 0 );
			}
			if ( this.parent.getCSpan() > 1 ) {
				borders.setBorderWidthLeft( 0 );
				borders.setBorderWidthRight( 0 );
			}			
		}
		return borders;
	}

}



//public class XlsTypeHandler extends BasicTypeHandler {
//	
//	public final static String PROP_XLS_WIDTH_MULTIPLIER = "excel-width-multiplier";
//	
//	public final static String PROP_XLS_IGNORE_FORMAT = "excel-ignore-format";
//	
//	public final static String PROP_XLS_TEMPLATE = "excel-template";
//	
//	public final static String PROP_XLS_TABLE_ID = "excel-table-id";
//
//	public final static String PROP_XLS_WIDTH_MULTIPLIER_DEFAULT = "256";
//	
//	public XlsTypeHandler() {
//		super( "xls", "xls" );
//	}
//	
//	private static int rgbDiff( RGB rgb1, RGB rgb2 ) {
//		int result = 0;
//		result+= ( Math.abs( rgb1.getRed() - rgb2.getRed() ) );
//		result+= ( Math.abs( rgb1.getBlue() - rgb2.getBlue() ) );
//		result+= ( Math.abs( rgb1.getGreen() - rgb2.getGreen() ) );
//		return result;
//	}
//	
//	// find the closest jxl colour to a give awt colour
//	public static Colour closestColor( Color awtColor ) {
//		Colour cc = null;
//		Colour[] c = Colour.getAllColours();
//		RGB rgbBase = new RGB( awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue() );
//		int rgbDiff = 10000000;
//		for ( int a=0; a<c.length; a++ ) {
//			Colour t = c[a];
//			RGB rgb = t.getDefaultRGB();
//			int currDiff = rgbDiff( rgb, rgbBase );
//			if ( rgbDiff > currDiff  ) {
//				rgbDiff = currDiff;
//				cc = t;
//			}
//		}
//		return cc;
//	}
//	
//	private static BorderLineStyle getBorderStyle( int borderWidth ) {
//		BorderLineStyle bls = BorderLineStyle.THIN;
//		if ( borderWidth == 0 ) {
//			bls = BorderLineStyle.NONE;
//		} else if ( borderWidth > 1 ) {
//			bls = BorderLineStyle.MEDIUM;
//		} else if ( borderWidth > 3 ) {
//			bls = BorderLineStyle.THICK;			
//		}		
//		return bls;
//	}
//	
//	private static TableMatrix handleMatrix( DocTable table, boolean ignoreFormat, WritableSheet dati ) throws Exception {
//		TableMatrix matrix = new TableMatrix( table.containerSize() , table.getColumns() );
//		Iterator<DocElement> rows = table.docElements();
//		while ( rows.hasNext() ) {
//			DocRow row = (DocRow)rows.next();
//			Iterator<DocElement> cells = row.docElements();
//			while ( cells.hasNext() ) {
//				DocCell cell = (DocCell)cells.next();
//				matrix.setNext( cell, cell.getRSpan() , cell.getCSpan() );
//			}
//		}
//		
//		for ( int rn=0; rn<matrix.getRowCount(); rn++ ) {
//			for ( int cn=0; cn<matrix.getColumnCount(); cn++ ) {
//				String type = null;
//				String format = null;
//				DocCell cell = matrix.getCell( rn, cn );
//				DocCell parent = matrix.getParent( rn, cn );
//				String text = "";
//				DocPara currentePara = null;
//				if ( cell != null ) {
//					Iterator<DocElement> it1 = cell.docElements();
//					DocElement current = (DocElement)it1.next();
//					if ( current instanceof DocPara ) {
//						currentePara = ((DocPara)current);
//						text = currentePara.getText();
//						type = currentePara.getType();
//						format = currentePara.getFormat();
//					} else {
//						text = String.valueOf( current );
//						currentePara = null;
//					}
//				} else {
//					currentePara = null;
//				}
//				WritableCellFormat cf = new WritableCellFormat();
//				DisplayFormat df = null;
//				// in case of number check for format string
//				if ( format != null && "number".equalsIgnoreCase( type ) ) {
//					System.out.println( ">>>>>>>>>>>>>> FORMAT - "+format );
//					if ( "float".equalsIgnoreCase( format ) ) {
//						df = NumberFormats.FLOAT;
//					} else {
//						df = new NumberFormat( format, NumberFormat.COMPLEX_FORMAT);
//					}
//					cf = new WritableCellFormat( df );
//				}
//				if ( parent != null && !ignoreFormat ) {
//					
//					// must go first as it has the chance to change the cell format
//					if ( parent.getForeColor() != null ) {
//						Font f = cf.getFont();
//						WritableFont wf = new WritableFont( f );
//						wf.setColour( ( closestColor( ITextDocHandler.parseHtmlColor( parent.getForeColor() ) ) ) );
//						if ( df != null ) {
//							cf = new WritableCellFormat( wf, df );	
//						} else {
//							cf = new WritableCellFormat( wf );
//						}
//						
//					}	
//					// style
//					if ( currentePara != null ) {
//						Font f = cf.getFont();
//						WritableFont wf = new WritableFont( f );
//						if ( currentePara.getStyle() == DocPara.STYLE_BOLD ) {
//							wf.setBoldStyle( WritableFont.BOLD );
//						} else if ( currentePara.getStyle() == DocPara.STYLE_ITALIC ) {
//							wf.setItalic( true );
//						} else if ( currentePara.getStyle() == DocPara.STYLE_BOLDITALIC ) {	
//							wf.setBoldStyle( WritableFont.BOLD );
//							wf.setItalic( true );
//						} else if ( currentePara.getStyle() == DocPara.STYLE_UNDERLINE ) {
//							wf.setUnderlineStyle( UnderlineStyle.SINGLE );
//						}
//						if ( df != null ) {
//							cf = new WritableCellFormat( wf, df );	
//						} else {
//							cf = new WritableCellFormat( wf );
//						}
//					}
//					// back color
//					if ( parent.getBackColor() != null) {
//						cf.setBackground( closestColor( ITextDocHandler.parseHtmlColor( parent.getBackColor() ) ) );
//					}
//					//bordi
//					DocBorders borders = matrix.getBorders( rn, cn );
//					cf.setBorder( Border.LEFT,  getBorderStyle( borders.getBorderWidthLeft() ) );
//					cf.setBorder( Border.RIGHT,  getBorderStyle( borders.getBorderWidthRight() ) );
//					cf.setBorder( Border.BOTTOM,  getBorderStyle( borders.getBorderWidthBottom() ) );
//					cf.setBorder( Border.TOP,  getBorderStyle( borders.getBorderWidthTop() ) );
//					if ( cell != null ) {
//						// alignment
//						if ( cell.getAlign() == DocPara.ALIGN_CENTER ) {
//							cf.setAlignment( Alignment.CENTRE );
//						} else if ( cell.getAlign() == DocPara.ALIGN_RIGHT ) {
//							cf.setAlignment( Alignment.RIGHT );
//						} else if ( cell.getAlign() == DocPara.ALIGN_LEFT ) {
//							cf.setAlignment( Alignment.LEFT );
//						}
//						// vertical alignment
//						if ( cell.getValign() == DocPara.ALIGN_MIDDLE ) {
//							cf.setVerticalAlignment( VerticalAlignment.CENTRE );
//						} else if ( cell.getValign() == DocPara.ALIGN_BOTTOM ) {
//							cf.setVerticalAlignment( VerticalAlignment.BOTTOM );
//						} else if ( cell.getValign() == DocPara.ALIGN_TOP ) {
//							cf.setVerticalAlignment( VerticalAlignment.TOP );
//						} 
//					}
//				}
//				WritableCell current = null;
//				if ( "number".equalsIgnoreCase( type ) ) {
//					BigDecimal bd = new BigDecimal( text );
//					current = new Number( cn, rn,  bd.doubleValue(), cf );
//				} else {
//					current = new Label( cn, rn, text, cf );
//				}
//				dati.addCell( current );	
//			}
//		}
//		return matrix;
//	}
//
//	public static String convertComma( String s ) {
//		int index = s.indexOf( ',' );
//		if ( index!=-1 ) {
//			s = s.substring( 0, index )+"."+s.substring( index+1 );
//		}
//		return s; 	
//	}	
//	
//	public static String removeDots( String s ) {
//		StringBuffer r = new StringBuffer();
//		StringTokenizer st = new StringTokenizer( s, "." );
//		while (st.hasMoreTokens()) {
//			r.append( st.nextToken() );
//		}
//		return r.toString(); 	
//	}
//	
//	public static String prepareNumber( String s ) {
//		s = removeDots( s );
//		s = convertComma( s );
//		return s;
//	}
//	
//	private static void handleMerge( DocTable table, boolean ignoreFormat, WritableSheet dati ) throws Exception {
//		TableMatrix matrix = handleMatrix(table, ignoreFormat, dati);
//		for ( int rn=0; rn<matrix.getRowCount(); rn++ ) {
//			for ( int cn=0; cn<matrix.getColumnCount(); cn++ ) {
//				DocCell cell = matrix.getCell( rn, cn );
//				if ( cell != null ) {
//					int rs = cell.getRSpan()-1;
//					int cs = cell.getCSpan()-1;
//					if ( rs != 0 || cs != 0 ) {
//						dati.mergeCells( cn, rn, cn+cs, rn+rs );	
//					}
//				}
//			}
//		}
//	}
//
//	
//	public static void handleDoc( DocBase docBase, OutputStream os, Workbook templateXls ) throws Exception {
//		String excelTableId = docBase.getInfo().getProperty( PROP_XLS_TABLE_ID );
//		String excelTableSheet[] = excelTableId.split( ";" );
//		WritableWorkbook outputXls = null;
//		if ( templateXls == null ) {
//			outputXls = Workbook.createWorkbook( os );
//		} else {
//			outputXls = Workbook.createWorkbook( os, templateXls );
//		}
//		
//		boolean ignoreFormat = "true".equalsIgnoreCase( docBase.getInfo().getProperty( PROP_XLS_IGNORE_FORMAT ) );
//		for ( int k=0; k<excelTableSheet.length; k++ ) {
//			String currentSheetData[] = excelTableSheet[k].split( "=" );
//			String sheetId = currentSheetData[0];
//			String sheetName = currentSheetData[1];
//			DocTable table = (DocTable)docBase.getElementById( sheetId );
//			
//			WritableSheet dati = null;
//			if ( templateXls == null ) {
//				dati = outputXls.createSheet( sheetName , k );
//			} else {
//				dati = outputXls.getSheet( k );
//				dati.setName( sheetName );
//			}
//			
//			int[] colW = table.getColWithds();
//			for ( int i=0; i<colW.length; i++ ) {
//				CellView cw = new CellView( dati.getColumnView( i ) );
//				int mul = Integer.parseInt( docBase.getInfo().getProperty( PROP_XLS_WIDTH_MULTIPLIER, PROP_XLS_WIDTH_MULTIPLIER_DEFAULT ) );
//				cw.setSize( colW[i]* mul );
//				dati.setColumnView( i , cw );
//			}
//			
//			handleMerge(table, ignoreFormat, dati);
//			
//		}
//		outputXls.write();
//		outputXls.close();
//	}
//	
//	public void handleDocType(HttpServletRequest request, HttpServletResponse response, DocContext docContext ) throws Exception {
//		this.getLogger().info( "XlsTypeHandler start" );
//		try {
//			DocBase docBase = docContext.getDocBase( request );
//			String excelTemplate = docBase.getInfo().getProperty( PROP_XLS_TEMPLATE );
//			Workbook templateXls = null;
//			if ( excelTemplate != null ) {
//				templateXls = Workbook.getWorkbook( new File( request.getRealPath( "/" ), excelTemplate ) );
//			}			
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			handleDoc(docBase, baos, templateXls);
//			docContext.getBufferStream().write( baos.toByteArray() );
//		} catch (Exception e) {
//			this.getLogger().error( "XlsTypeHandler handler file ERROR", e );
//		}
//		this.getLogger().info( "XlsTypeHandler end" );
//	}
//
//	public void init(Element config) throws ConfigException {
//		
//	}	
//	
//	public void handleDocTypePost(HttpServletRequest arg0, HttpServletResponse arg1, DocContext arg2) throws Exception {
//
//	}
//
//}
//
//class TableMatrix {
//	
//	private MatrixCell[][] text;
//	
//	private int cn, rn;
//	
//	public TableMatrix( int rows, int columns ) {
//		this.text = new MatrixCell[ rows ][ columns ];
//		cn = -1;
//		rn = 0;
//	}
//	
//	public int getRowCount() {
//		return this.text.length;
//	}
//	
//	public int getColumnCount() {
//		return this.text[0].length;
//	}	
//	
//	public DocCell getCell( int r, int c ) {
//		return this.text[ r ][ c ].getCell(); 
//	}
//	
//	public DocCell getParent( int r, int c ) {
//		return this.text[ r ][ c ].getParent(); 
//	}
//	
//	public DocBorders getBorders( int r, int c ) throws CloneNotSupportedException {
//		return this.text[ r ][ c ].getBorders(); 
//	}
//	
//	private MatrixCell getCellMatrix( int r, int c ) {
//		return this.text[ r ][ c ]; 
//	}
//	
//	private void setCell( DocCell s, DocCell p, int r, int c ) {
//		this.text[ r ][ c ] = new MatrixCell( s, p ); 
//	}
//	
//	public void setNext( DocCell s, final int rs, final int cs ) {
//		if ( 1+this.cn == this.getColumnCount() ) {
//			this.cn = 0;
//			this.rn++;
//		} else {
//			this.cn++;	
//		}
//		DocCell p = s;
//		if ( this.getCellMatrix( this.rn , this.cn ) == null ) {
//			int counterRS = 0;
//			while ( counterRS < rs ) {
//				int counterCS = 0;
//				while ( counterCS < cs ) {
//					this.setCell( s , p, this.rn+counterRS, this.cn+counterCS );
//					s = null;
//					counterCS++;
//				}
//				counterRS ++;
//			}
//		} else {
//			this.setNext( s, rs, cs );
//		}
//	}
//	
//	public boolean isCellEmpty( int r, int c ) {
//		return this.getCell(r, c)==null;
//	}
//
//}
//
//class MatrixCell {
//	
//	private DocCell cell;
//
//	private DocCell parent;
//	
//	public MatrixCell( DocCell cell, DocCell parent ) {
//		super();
//		this.cell = cell;
//		this.parent = parent;
//	}
//
//	public DocCell getParent() {
//		return parent;
//	}
//
//	public DocCell getCell() {
//		return cell;
//	}
//
//	public void setCell(DocCell cell) {
//		this.cell = cell;
//	}
//	
//	public DocBorders getBorders() throws CloneNotSupportedException {
//		DocBorders borders = new DocBorders();
//		if ( this.getParent() == this.getCell() ) {
//			borders = (DocBorders)this.cell.getDocBorders().clone();
//			if ( this.getCell().getRSpan() > 1 ) {
//				borders.setBorderWidthBottom( 0 );
//			}
//			if ( this.getCell().getCSpan() > 1 ) {
//				borders.setBorderWidthRight( 0 );
//			}
//		} else {
//			borders = (DocBorders)this.parent.getDocBorders().clone();
//			if ( this.parent.getRSpan() > 1 ) {
//				borders.setBorderWidthTop( 0 );
//				borders.setBorderWidthBottom( 0 );
//			}
//			if ( this.parent.getCSpan() > 1 ) {
//				borders.setBorderWidthLeft( 0 );
//				borders.setBorderWidthRight( 0 );
//			}			
//		}
//		return borders;
//	}
//
//}
//
