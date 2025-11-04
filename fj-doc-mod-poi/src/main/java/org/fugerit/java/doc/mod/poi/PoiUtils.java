package org.fugerit.java.doc.mod.poi;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.function.Consumer;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.VenusVersion;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.xml.DocModelUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PoiUtils {

	private PoiUtils() {}
	
    public static void resizeSheet( Sheet s ) {
        Row row = s.getRow( 0 );
        Iterator<Cell> cells = row.cellIterator();
        while ( cells.hasNext() ) {
                Cell c = cells.next();
                s.autoSizeColumn( c.getColumnIndex() );
        }
    }

	public static void autoSizeColumns(Workbook workbook) {
	    int numberOfSheets = workbook.getNumberOfSheets();
	    for (int i = 0; i < numberOfSheets; i++) {
	        Sheet sheet = workbook.getSheetAt(i);
	        resizeSheet( sheet );
	    }
	}
	
	public static void xlsxFormatStyle( WorkbookHelper helper, CellStyle style, DocCell cell ) {
		if ( style instanceof XSSFCellStyle ) {
			XSSFCellStyle realStyle = (XSSFCellStyle) style;
			if ( cell != null && StringUtils.isNotEmpty( cell.getBackColor() ) ) {
				Color c = DocModelUtils.parseHtmlColor( cell.getBackColor() );
				realStyle.setFillForegroundColor( new XSSFColor( c , helper.getIndexedColorMap() ) );
				realStyle.setFillPattern( FillPatternType.SOLID_FOREGROUND );
			}
		}
	}
	
	public static void xlsxFontStyle( WorkbookHelper helper, Font font, DocCell cell ) {
		if ( font instanceof XSSFFont ) {
			XSSFFont realFont = (XSSFFont) font;
			if ( StringUtils.isNotEmpty( cell.getForeColor() ) ) {
				Color c = DocModelUtils.parseHtmlColor( cell.getForeColor() );
				realFont.setColor( new XSSFColor( c, helper.getIndexedColorMap() ) );
			}
		}
	}
	
	public static short findClosestColorIndex( HSSFWorkbook workbook, String color ) {
		Color c = DocModelUtils.parseHtmlColor( color );
		HSSFPalette palette = workbook.getCustomPalette();
		HSSFColor current = palette.findSimilarColor( c.getRed() , c.getGreen(), c.getBlue() );
		return current.getIndex();
	}
	
	public static void xlsFormatStyle( WorkbookHelper helper, CellStyle style, DocCell cell ) {
		Workbook workbook = helper.getWorkbook();
		if ( style instanceof HSSFCellStyle && workbook instanceof HSSFWorkbook ) {
			HSSFCellStyle realStyle = (HSSFCellStyle) style;
			if ( cell != null && StringUtils.isNotEmpty( cell.getBackColor() ) ) {
				short index = findClosestColorIndex( (HSSFWorkbook) workbook , cell.getBackColor() );
				realStyle.setFillForegroundColor( index );
				realStyle.setFillPattern( FillPatternType.SOLID_FOREGROUND );
			}
		}
	}
	
	public static void xlsFontStyle( WorkbookHelper helper, Font font, DocCell cell ) {
		Workbook workbook = helper.getWorkbook();
		if ( workbook instanceof HSSFWorkbook && StringUtils.isNotEmpty( cell.getForeColor() ) ) {
			font.setColor( findClosestColorIndex( (HSSFWorkbook)workbook , cell.getForeColor() ) );	
		}
	}
	
	public static void closeWorkbook( Workbook workbook, DocOutput docOutput ) throws IOException {
		workbook.write( docOutput.getOs() );
		workbook.close();
	}

    private static final String PRODUCER_OVER = "Apache POI";

    private static final String PRODUCER_DEFAULT = String.format( VenusVersion.VENUS_PRODUCER_FORMAT_SH1, DocConfig.FUGERIT_VENUS_DOC , PRODUCER_OVER );

    private static void propertySetup(DocBase docBase, POIXMLProperties props) {
        SafeFunction.applySilent( () -> {
            POIXMLProperties.CoreProperties coreProps = props.getCoreProperties();
            POIXMLProperties.CustomProperties customProps = props.getCustomProperties();
            SafeFunction.applyIfNotNull( docBase.getInfoDocTitle(), () -> coreProps.setTitle( docBase.getInfoDocTitle() ) );
            SafeFunction.applyIfNotNull( docBase.getInfoDocSubject(), () -> coreProps.setSubjectProperty( docBase.getInfoDocSubject() ) );
            SafeFunction.applyIfNotNull( docBase.getInfoDocVersion(), () -> coreProps.setVersion( docBase.getInfoDocVersion() ) );
            SafeFunction.applyIfNotNull( docBase.getInfoDocAuthor(), () -> customProps.addProperty( "Author", docBase.getInfoDocAuthor() ) );
            if ( docBase.getInfoDocProducer() != null ) {
                customProps.addProperty( "Creator" , docBase.getInfoDocCreator() );
            } else {
                customProps.addProperty( "Creator" , VenusVersion.VENUS_CREATOR );
            }
            if ( docBase.getInfoDocProducer() != null ) {
                customProps.addProperty( "Producer" , docBase.getInfoDocProducer() );
            } else {
                customProps.addProperty( "Producer" , PRODUCER_DEFAULT );
            }
            SafeFunction.applyIfNotNull( docBase.getInfoDocLanguage(), () -> customProps.addProperty( "ContentLanguage" , docBase.getInfoDocLanguage() ) );
        } );
    }

    public static WorkbookHelper newHelper(boolean xlsx, InputStream is, DocBase docBase) throws IOException {
		Workbook workbook = null;
		if ( xlsx ) {
            XSSFWorkbook xssfWorkbook = null;
			if ( is == null ) {
                xssfWorkbook = new XSSFWorkbook();
			} else {
                xssfWorkbook = new XSSFWorkbook( is );
			}
            POIXMLProperties props = xssfWorkbook.getProperties();
            propertySetup( docBase, props );
            workbook = xssfWorkbook;
		} else {
			if ( is == null ) {
				workbook = new HSSFWorkbook();
			} else {
				workbook = new HSSFWorkbook( is );
			}
		}
		return new WorkbookHelper( workbook, new DefaultIndexedColorMap() );
	}
	
	public static Consumer<Exception> autoresizeFailHandler( boolean failOnAutoResizeError ) {
		return e -> {
			String message = "Exception on excel autoresize "+e;
			if ( failOnAutoResizeError ) {
				throw new ConfigRuntimeException( message, e );
			} else {
				log.warn( message , e );
			}
		};
	}
	
}
