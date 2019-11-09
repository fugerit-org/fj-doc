package org.fugerit.java.doc.mod.poi;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.model.DocRow;
import org.fugerit.java.doc.base.model.DocTable;
import org.fugerit.java.doc.base.typehelper.excel.ExcelHelperConsts;
import org.fugerit.java.doc.base.typehelper.excel.ExcelHelperUtils;
import org.fugerit.java.doc.base.typehelper.excel.TableMatrix;

public abstract class BasicPoiTypeHandler extends DocTypeHandlerDefault {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1175953200917290547L;

	public BasicPoiTypeHandler(String type) {
		super(type);
	}

	protected abstract Workbook newWorkbook( DocInput docInput, InputStream is ) throws Exception;
	
	protected abstract void closeWorkbook( Workbook workbook, DocOutput docOutput ) throws Exception;
	
	
	public static void handleDoc( DocBase docBase, OutputStream os, Workbook templateXls ) throws Exception {
		
	}
	
	private static TableMatrix handleMatrix( DocTable table, boolean ignoreFormat, Sheet dati ) throws Exception {
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
			Row currentRow = dati.getRow( rn );
			if ( currentRow == null ) {
				currentRow = dati.createRow( rn );
			}
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
//				WritableCellFormat cf = new WritableCellFormat();
//				DisplayFormat df = null;
//				// in case of number check for format string
//				if ( format != null && FormatTypeConsts.TYPE_NUMBER.equalsIgnoreCase( type ) ) {
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
				Cell currentCell = currentRow.getCell( cn );
				if ( currentCell == null ) {
					currentCell = currentRow.createCell( cn );
				}
				currentCell.setCellValue( text );
//				Cell current = null;
//				if ( FormatTypeConsts.TYPE_NUMBER.equalsIgnoreCase( type ) ) {
//					BigDecimal bd = new BigDecimal( text );
//					current = new Number( cn, rn,  bd.doubleValue(), cf );
//				} else {
//					current = new Label( cn, rn, text, cf );
//				}
			}
		}
		return matrix;
	}
	
	private static void handleMerge( DocTable table, boolean ignoreFormat, Sheet dati ) throws Exception {
		TableMatrix matrix = handleMatrix(table, ignoreFormat, dati);
		for ( int rn=0; rn<matrix.getRowCount(); rn++ ) {
			for ( int cn=0; cn<matrix.getColumnCount(); cn++ ) {
				DocCell cell = matrix.getCell( rn, cn );
				if ( cell != null ) {
					int rs = cell.getRSpan()-1;
					int cs = cell.getCSpan()-1;
					if ( rs != 0 || cs != 0 ) {
						dati.addMergedRegion( new CellRangeAddress( cn, rn, cn+cs, rn+rs ) );  
					}
				}
			}
		}
	}
	
	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		DocBase docBase = docInput.getDoc();
		InputStream is = ExcelHelperUtils.resoveTemplateStream( docBase );
		boolean noTemplate = (is == null);
		Workbook outputXls = this.newWorkbook( docInput, is );
		
		String excelTableId = docBase.getInfo().getProperty( ExcelHelperConsts.PROP_XLS_TABLE_ID );
		String excelTableSheet[] = excelTableId.split( ";" );		
		boolean ignoreFormat = "true".equalsIgnoreCase( docBase.getInfo().getProperty( ExcelHelperConsts.PROP_XLS_IGNORE_FORMAT ) );
		for ( int k=0; k<excelTableSheet.length; k++ ) {
			String currentSheetData[] = excelTableSheet[k].split( "=" );
			String sheetId = currentSheetData[0];
			String sheetName = currentSheetData[1];
			DocTable table = (DocTable)docBase.getElementById( sheetId );
			
			Sheet dati = null;
			if ( noTemplate) {
				dati = outputXls.createSheet( sheetName );
			} else {
				dati = outputXls.getSheet( sheetName );
			}
			
//			int[] colW = table.getColWithds();
//			for ( int i=0; i<colW.length; i++ ) {
//				CellView cw = new CellView( dati.getColumnView( i ) );
//				int mul = Integer.parseInt( docBase.getInfo().getProperty( ExcelHelperConsts.PROP_XLS_WIDTH_MULTIPLIER, ExcelHelperConsts.PROP_XLS_WIDTH_MULTIPLIER_DEFAULT ) );
//				cw.setSize( colW[i]* mul );
//				dati.setColumnView( i , cw );
//			}
			
			handleMerge(table, ignoreFormat, dati);
			
		}

		
		this.closeWorkbook( outputXls , docOutput );
	}

}
