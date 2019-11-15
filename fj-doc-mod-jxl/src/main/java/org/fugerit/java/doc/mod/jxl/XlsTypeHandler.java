package org.fugerit.java.doc.mod.jxl;

import java.awt.Color;
import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Iterator;

import org.fugerit.java.doc.base.config.DocConfig;
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
import org.fugerit.java.doc.base.typehelper.excel.ExcelHelperConsts;
import org.fugerit.java.doc.base.typehelper.excel.TableMatrix;
import org.fugerit.java.doc.base.typehelper.generic.FormatTypeConsts;
import org.fugerit.java.doc.base.xml.DocModelUtils;

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
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8856614360877809158L;

	public static DocTypeHandler HANDLER = new XlsTypeHandler();
	
	public XlsTypeHandler() {
		super( DocConfig.TYPE_XLS );
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		DocBase docBase = docInput.getDoc();
		OutputStream outputStream = docOutput.getOs();
		String excelTemplate = docBase.getInfo().getProperty( ExcelHelperConsts.PROP_XLS_TEMPLATE );
		Workbook templateXls = null;
		if ( excelTemplate != null ) {
			templateXls = Workbook.getWorkbook( new File( excelTemplate ) );
		}			
		XlsTypeHandler.handleDoc( docBase , outputStream, templateXls );
	}

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
				if ( format != null && FormatTypeConsts.TYPE_NUMBER.equalsIgnoreCase( type ) ) {
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
						wf.setColour( ( closestColor( DocModelUtils.parseHtmlColor( parent.getForeColor() ) ) ) );
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
						cf.setBackground( closestColor( DocModelUtils.parseHtmlColor( parent.getBackColor() ) ) );
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
				if ( FormatTypeConsts.TYPE_NUMBER.equalsIgnoreCase( type ) ) {
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
		String excelTableId = docBase.getInfo().getProperty( ExcelHelperConsts.PROP_XLS_TABLE_ID );
		String excelTableSheet[] = excelTableId.split( ";" );
		WritableWorkbook outputXls = null;
		if ( templateXls == null ) {
			outputXls = Workbook.createWorkbook( os );
		} else {
			outputXls = Workbook.createWorkbook( os, templateXls );
		}
		
		boolean ignoreFormat = "true".equalsIgnoreCase( docBase.getInfo().getProperty( ExcelHelperConsts.PROP_XLS_IGNORE_FORMAT ) );
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
				int mul = Integer.parseInt( docBase.getInfo().getProperty( ExcelHelperConsts.PROP_XLS_WIDTH_MULTIPLIER, ExcelHelperConsts.PROP_XLS_WIDTH_MULTIPLIER_DEFAULT ) );
				cw.setSize( colW[i]* mul );
				dati.setColumnView( i , cw );
			}
			
			handleMerge(table, ignoreFormat, dati);
			
		}
		outputXls.write();
		outputXls.close();
	}

}