package org.fugerit.java.doc.mod.poi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import lombok.Setter;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.model.*;
import org.fugerit.java.doc.base.typehelper.excel.ExcelHelperConsts;
import org.fugerit.java.doc.base.typehelper.excel.ExcelHelperUtils;
import org.fugerit.java.doc.base.typehelper.excel.TableMatrix;
import org.fugerit.java.doc.base.typehelper.generic.FormatTypeConsts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BasicPoiTypeHandler extends DocTypeHandlerDefault {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1175953200917290547L;

	public static final String MODULE = "poi";
	
	protected BasicPoiTypeHandler(String type) {
		super(type, MODULE);
	}

	protected abstract WorkbookHelper newWorkbook( DocInput docInput, InputStream is ) throws IOException;
	
	protected abstract void closeWorkbook( Workbook workbook, DocOutput docOutput ) throws IOException;
	
	protected abstract  void setFormatStyle( WorkbookHelper helper, Font font, CellStyle style, DocCell cell, DocPara para );
	
	protected abstract void setFontStyle( WorkbookHelper helper, Font font, CellStyle style, DocCell cell, DocPara para );
	
	public static void handleDoc( DocBase docBase, OutputStream os, Workbook templateXls ) throws DocException {
		
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
			log.warn( "Format conversion errot for text '{}', type '{}', format '{}'", text, type, format, e );
			currentCell.setCellValue( text );
		}

	}
	
	private void setFontCommonStyle( CellStyle cellStyle, DocPara currentePara, Font font ) {
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
	}
	
	private void setCommonBorders( CellStyle cellStyle, DocBorders borders ) {
		cellStyle.setBorderBottom( getBorderWidth( borders.getBorderWidthBottom() ) );
		cellStyle.setBorderTop( getBorderWidth( borders.getBorderWidthTop() ) );
		cellStyle.setBorderRight( getBorderWidth( borders.getBorderWidthRight() ) );
		cellStyle.setBorderLeft( getBorderWidth( borders.getBorderWidthLeft() ) );
	}
	
	private void setCommonAlign( DocCell cell, CellStyle cellStyle ) {
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
	}
	
	private void checkFormat( WorkbookDataWrapper wrapper, Collection<PoiCellStyleModel> styleSet, DocPara currentePara, DocCell cell, int rn, int cn, Cell currentCell  ) throws Exception {
		TableMatrix matrix = wrapper.getTableMatrix();
		Workbook workbook = wrapper.getWorkbook();
		WorkbookHelper helper = wrapper.getWorkbookHelper();
		CellStyle cellStyle = PoiCellStyleModel.find( styleSet , currentePara, cell );
		if ( cellStyle == null ) {
			cellStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
			this.setFontStyle( helper, font, cellStyle, cell, currentePara);
			this.setFormatStyle( helper, font, cellStyle, cell, currentePara);
			// common style
			this.setFontCommonStyle(cellStyle, currentePara, font);
			//borders
			DocBorders borders = matrix.getBorders( rn, cn );
			this.setCommonBorders(cellStyle, borders);
			//allignment (vertical and horizzontal)
			this.setCommonAlign(cell, cellStyle);
			// final setup
			cellStyle.setFont( font );
			styleSet.add( new PoiCellStyleModel( cellStyle , currentePara, cell ) );
		}
		currentCell.setCellStyle( cellStyle );
	}
	
	private void iterateCellMatrix( WorkbookDataWrapper wrapper , boolean ignoreFormat, HashSet<PoiCellStyleModel> styleSet , int rn, int cn, Row currentRow ) throws Exception {
		TableMatrix matrix = wrapper.getTableMatrix();
		Workbook workbook = wrapper.getWorkbook();
		DocCell cell = matrix.getCell( rn, cn );
		DocCell parent = matrix.getParent( rn, cn );
		CellHolder holder = new CellHolder();
		if ( cell != null ) {
			this.handleElement( holder, cell );
		}
		Cell currentCell = currentRow.getCell( cn );
		if ( currentCell == null ) {
			currentCell = currentRow.createCell( cn );
		}
		this.handleHyperLink( holder.getLink(), workbook, currentCell );
		if ( cell != null && parent != null && !ignoreFormat ) {
			this.checkFormat( wrapper, styleSet, holder.getCurrentePara(), cell, rn, cn, currentCell );
		} 
		this.setCellValue( workbook, currentCell, holder.getType(), holder.getFormat(), holder.getText());
	}

	private void handleElement( CellHolder holder, DocCell cell ) throws Exception {
		Iterator<DocElement> it1 = cell.docElements();
		DocElement current = it1.next();
		if ( current instanceof DocPara ) {
			holder.setCurrentePara((DocPara) current);
			holder.setText(holder.getCurrentePara().getText());
			holder.setType(holder.getCurrentePara().getType());
			holder.setFormat(holder.getCurrentePara().getFormat());
		} else if ( current instanceof DocPhrase) {
			DocPhrase phrase = (DocPhrase) current;
			holder.setText(phrase.getText());
			holder.setLink(phrase.getLink());
			holder.setCurrentePara(null);
		} else {
			holder.setText( String.valueOf( current ) );
			holder.setCurrentePara(null);
		}
	}

	private class CellHolder {
		@Getter @Setter
		private String text = "";
		@Getter @Setter
		private String link;
		@Getter @Setter
		private String type;
		@Getter @Setter
		private String format;
		@Getter @Setter
		private DocPara currentePara;
	}

	private void handleHyperLink( String link, Workbook workbook, Cell currentCell ) {
		if ( StringUtils.isNotEmpty(link) ) {
			Hyperlink hyperlink = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
			hyperlink.setAddress( link );
			currentCell.setHyperlink( hyperlink );
		}
	}
	
	private void handleSubmatrix( TableMatrix matrix, boolean ignoreFormat, Sheet sheet, WorkbookHelper helper, HashSet<PoiCellStyleModel> styleSet ) throws Exception {
		for ( int rn=0; rn<matrix.getRowCount(); rn++ ) {
			Row currentRow = sheet.getRow( rn );
			if ( currentRow == null ) {
				currentRow = sheet.createRow( rn );
			}
			WorkbookDataWrapper wrapper = new WorkbookDataWrapper( matrix, helper );
			for ( int cn=0; cn<matrix.getColumnCount(); cn++ ) {
				this.iterateCellMatrix(wrapper, ignoreFormat, styleSet, rn, cn, currentRow);
			}
		}
	}
	
	private TableMatrix handleMatrix( DocTable table, boolean ignoreFormat, Sheet sheet, WorkbookHelper helper ) throws Exception {
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
		this.handleSubmatrix(matrix, ignoreFormat, sheet, helper, styleSet);
		log.debug( "TOTAL STYLES : {}", styleSet.size() );
		return matrix;
	}
	
	private void handleMerge( DocTable table, boolean ignoreFormat, Sheet sheet, WorkbookHelper helper ) throws Exception {
		TableMatrix matrix = handleMatrix( table, ignoreFormat, sheet, helper );
		for ( int rn=0; rn<matrix.getRowCount(); rn++ ) {
			for ( int cn=0; cn<matrix.getColumnCount(); cn++ ) {
				DocCell cell = matrix.getCell( rn, cn );
				if ( cell != null ) {
					int rs = cell.getRSpan()-1;
					int cs = cell.getCSpan()-1;
					if ( rs != 0 || cs != 0 ) {
						sheet.addMergedRegion( new CellRangeAddress( rn, rn+rs, cn, cn+cs ) );  
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
		String[] excelTableSheet = excelTableId.split( ";" );		
		boolean ignoreFormat = "true".equalsIgnoreCase( docBase.getInfo().getProperty( ExcelHelperConsts.PROP_XLS_IGNORE_FORMAT ) );
		for ( int k=0; k<excelTableSheet.length; k++ ) {
			String[] currentSheetData = excelTableSheet[k].split( "=" );
			String sheetId = currentSheetData[0];
			String sheetName = currentSheetData[1];
			DocTable table = (DocTable)docBase.getElementById( sheetId );
			
			Sheet sheet = null;
			if ( noTemplate) {
				sheet = outputXls.createSheet( sheetName );
			} else {
				sheet = outputXls.getSheet( sheetName );
			}			
			handleMerge(table, ignoreFormat, sheet, helper);
		}

		boolean tryAutoResize = BooleanUtils.isTrue(  docBase.getInfo().getProperty( ExcelHelperConsts.PROP_XLS_TRY_AUTORESIZE, ExcelHelperConsts.PROP_XLS_TRY_AUTORESIZE_DEFAULT ) );
		if ( tryAutoResize ) {
			boolean failOnAutoResizeError = BooleanUtils.isTrue(  docBase.getInfo().getProperty( ExcelHelperConsts.PROP_XLS_FAIL_ON_AUTORESIZE_ERROR, ExcelHelperConsts.PROP_XLS_FAIL_ON_AUTORESIZE_ERROR_DEFAULT ) );
			SafeFunction.apply( () -> {
				log.info( "try autoresize : {} -> {}", ExcelHelperConsts.PROP_XLS_FAIL_ON_AUTORESIZE_ERROR, failOnAutoResizeError );
				PoiUtils.autoSizeColumns( outputXls );
			}, e -> PoiUtils.autoresizeFailHandler(failOnAutoResizeError) );
		}
		this.closeWorkbook( outputXls , docOutput );
	}

}

@AllArgsConstructor
class WorkbookDataWrapper {
	
	@Getter private TableMatrix tableMatrix;
	
	@Getter private WorkbookHelper workbookHelper;
	
	public Workbook getWorkbook() {
		return this.getWorkbookHelper().getWorkbook();
	}
	
}
