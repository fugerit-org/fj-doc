package org.fugerit.java.doc.mod.poi5;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocBorders;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.model.DocRow;
import org.fugerit.java.doc.base.model.DocTable;
import org.fugerit.java.doc.base.typehelper.excel.ExcelHelperConsts;
import org.fugerit.java.doc.base.typehelper.excel.ExcelHelperUtils;
import org.fugerit.java.doc.base.typehelper.excel.TableMatrix;
import org.fugerit.java.doc.base.typehelper.generic.FormatTypeConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasicPoiTypeHandler extends DocTypeHandlerDefault {

	private final static Logger logger = LoggerFactory.getLogger( BasicPoiTypeHandler.class );  
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1175953200917290547L;

	public static final String MODULE = "poi5";
	
	public BasicPoiTypeHandler(String type) {
		super(type, MODULE);
	}

	protected abstract WorkbookHelper newWorkbook( DocInput docInput, InputStream is ) throws Exception;
	
	protected abstract void closeWorkbook( Workbook workbook, DocOutput docOutput ) throws Exception;
	
	protected abstract  void setFormatStyle( WorkbookHelper helper, Font font, CellStyle style, DocCell cell, DocPara para ) throws Exception;
	
	protected abstract void setFontStyle( WorkbookHelper helper, Font font, CellStyle style, DocCell cell, DocPara para ) throws Exception;
	
	public static void handleDoc( DocBase docBase, OutputStream os, Workbook templateXls ) throws Exception {
		
	}
	
	protected static BorderStyle getBorderWidth( int width ) {
		BorderStyle b = BorderStyle.NONE;
		if ( width > 5 ) {
			b = BorderStyle.THICK;
		} else if ( width > 3 ) {
			b = BorderStyle.MEDIUM;
		} else if ( width > 0 || width == -1 ) {
			b = BorderStyle.THIN;
		}
		return b;
	}
	
	protected void setCellValue( Workbook workbook, Cell currentCell, String type, String format, String text ) throws Exception {
		try {
  			if ( StringUtils.isEmpty( type ) ) {
				currentCell.setCellValue( text );
			} else if ( FormatTypeConsts.TYPE_NUMBER.equalsIgnoreCase( type ) ) {
				BigDecimal bd = new BigDecimal( text );
				currentCell.setCellValue( bd.doubleValue() );
			} else if ( FormatTypeConsts.TYPE_DATE.equalsIgnoreCase( type ) ) {
				String formatString = StringUtils.valueWithDefault( format , FormatTypeConsts.FORMAT_DATE_DEFAULT );
				if ( "iso".equalsIgnoreCase( format ) ) {
					formatString = FormatTypeConsts.FORMAT_DATE_ISO;
				}
				SimpleDateFormat sdf = new SimpleDateFormat( formatString );
				Date d = sdf.parse( text );
				currentCell.setCellValue( d );
				CellStyle style = currentCell.getCellStyle();
				CreationHelper helper = workbook.getCreationHelper();
				if ( FormatTypeConsts.FORMAT_DATE_ISO.equals( formatString ) ) {
					style.setDataFormat( helper.createDataFormat().getFormat( "d/m/yy h:mm:ss" ) );	
				} else if ( FormatTypeConsts.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS.equals( formatString ) ) {
					style.setDataFormat( helper.createDataFormat().getFormat( "d/m/yy h:mm:ss" ) );	
				} else {
					style.setDataFormat( helper.createDataFormat().getFormat( "d/m/yy" ) );	
				}
			} else {
				currentCell.setCellValue( text );
			}
		} catch (Exception e) {
			logger.warn( "Format conversion errot for text '{}', type '{}', format '{}'", text, type, format, e );
			currentCell.setCellValue( text );
		}

	}
	
	private void checkFormat( WorkbookHelper helper, Collection<PoiCellStyleModel> styleSet, DocPara currentePara,
			 DocCell cell, TableMatrix matrix, int rn, int cn, Cell currentCell  ) throws Exception {
		Workbook workbook = helper.getWorkbook();
		CellStyle cellStyle = PoiCellStyleModel.find( styleSet , currentePara, cell );
		if ( cellStyle == null ) {
			
			cellStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
			
			this.setFontStyle( helper, font, cellStyle, cell, currentePara);
			this.setFormatStyle( helper, font, cellStyle, cell, currentePara);

			// style
			if ( currentePara != null ) {
				if ( currentePara.getStyle() == DocPara.STYLE_BOLD ) {
					font.setBold( true );
				} else if ( currentePara.getStyle() == DocPara.STYLE_ITALIC ) {
					font.setItalic( true );
				} else if ( currentePara.getStyle() == DocPara.STYLE_BOLDITALIC ) {	
					font.setBold( true );
					font.setItalic( true );
				} else if ( currentePara.getStyle() == DocPara.STYLE_UNDERLINE ) {
					font.setUnderline( Font.U_SINGLE );
				}
				
			}
			cellStyle.setFont( font );

			//bordi
			DocBorders borders = matrix.getBorders( rn, cn );
			
			cellStyle.setBorderBottom( getBorderWidth( borders.getBorderWidthBottom() ) );
			cellStyle.setBorderTop( getBorderWidth( borders.getBorderWidthTop() ) );
			cellStyle.setBorderRight( getBorderWidth( borders.getBorderWidthRight() ) );
			cellStyle.setBorderLeft( getBorderWidth( borders.getBorderWidthLeft() ) );
			
			if ( cell != null ) {
				// alignment
				if ( cell.getAlign() == DocPara.ALIGN_CENTER ) {
					cellStyle.setAlignment( HorizontalAlignment.CENTER );
				} else if ( cell.getAlign() == DocPara.ALIGN_RIGHT ) {
					cellStyle.setAlignment( HorizontalAlignment.RIGHT );
				} else if ( cell.getAlign() == DocPara.ALIGN_LEFT ) {
					cellStyle.setAlignment( HorizontalAlignment.LEFT );
				}
				// vertical alignment
				if ( cell.getValign() == DocPara.ALIGN_MIDDLE ) {
					cellStyle.setVerticalAlignment( VerticalAlignment.CENTER );
				} else if ( cell.getValign() == DocPara.ALIGN_BOTTOM ) {
					cellStyle.setVerticalAlignment( VerticalAlignment.BOTTOM );
				} else if ( cell.getValign() == DocPara.ALIGN_TOP ) {
					cellStyle.setVerticalAlignment( VerticalAlignment.TOP );
				} 
			}
			cellStyle.setFont( font );
			styleSet.add( new PoiCellStyleModel( cellStyle , currentePara, cell ) );
		}
		currentCell.setCellStyle( cellStyle );
	}
	
	private TableMatrix handleMatrix( DocTable table, boolean ignoreFormat, Sheet dati, WorkbookHelper helper ) throws Exception {
		Workbook workbook = helper.getWorkbook();
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
		
		HashSet<PoiCellStyleModel> styleSet = new HashSet<>();
		
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
				
				Cell currentCell = currentRow.getCell( cn );
				if ( currentCell == null ) {
					currentCell = currentRow.createCell( cn );
				}
				if ( cell != null && parent != null && !ignoreFormat ) {
					this.checkFormat( helper, styleSet, currentePara, cell, matrix, rn, cn, currentCell );
				} 
				this.setCellValue( workbook, currentCell, type, format, text);
			}
			 
		}
		
		logger.info( "TOTAL STYLES : {}", styleSet.size() );
		
		return matrix;
	}
	
	private void handleMerge( DocTable table, boolean ignoreFormat, Sheet dati, WorkbookHelper helper ) throws Exception {
		TableMatrix matrix = handleMatrix( table, ignoreFormat, dati, helper );
		for ( int rn=0; rn<matrix.getRowCount(); rn++ ) {
			for ( int cn=0; cn<matrix.getColumnCount(); cn++ ) {
				DocCell cell = matrix.getCell( rn, cn );
				if ( cell != null ) {
					int rs = cell.getRSpan()-1;
					int cs = cell.getCSpan()-1;
					if ( rs != 0 || cs != 0 ) {
						dati.addMergedRegion( new CellRangeAddress( rn, rn+rs, cn, cn+cs ) );  
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
		WorkbookHelper helper = this.newWorkbook( docInput, is ); 
		Workbook outputXls = helper.getWorkbook();
		
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
			
			handleMerge(table, ignoreFormat, dati, helper);
			
		}

		boolean tryAutoResize = BooleanUtils.isTrue(  docBase.getInfo().getProperty( ExcelHelperConsts.PROP_XLS_TRY_AUTORESIZE, ExcelHelperConsts.PROP_XLS_TRY_AUTORESIZE_DEFAULT ) );
		if ( tryAutoResize ) {
			boolean failOnAutoResizeError = BooleanUtils.isTrue(  docBase.getInfo().getProperty( ExcelHelperConsts.PROP_XLS_FAIL_ON_AUTORESIZE_ERROR, ExcelHelperConsts.PROP_XLS_FAIL_ON_AUTORESIZE_ERROR_DEFAULT ) );
			try {
				logger.info( "try autoresize : {} -> {}", ExcelHelperConsts.PROP_XLS_FAIL_ON_AUTORESIZE_ERROR, failOnAutoResizeError );
				PoiUtils.autoSizeColumns( outputXls );
			} catch (Exception e) {
				if ( failOnAutoResizeError ) {
					throw e;
				} else {
					logger.warn( "Excel autoresize failed "+e , e );
				}
			}
		}
		
		this.closeWorkbook( outputXls , docOutput );
	}

}
