package org.fugerit.java.doc.mod.openpdf.ext.helpers;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.fugerit.java.doc.base.model.DocBarcode;
import org.fugerit.java.doc.base.model.DocBorders;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocContainer;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocImage;
import org.fugerit.java.doc.base.model.DocList;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.model.DocPhrase;
import org.fugerit.java.doc.base.model.DocRow;
import org.fugerit.java.doc.base.model.DocTable;
import org.fugerit.java.doc.base.xml.DocModelUtils;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.alignment.VerticalAlignment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenPdfDocTableHelper {

	private OpenPdfDocTableHelper() {}
	
	private static void handleBorderColor( DocBorders docBorders, Cell cell ) {
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
	}
	
	private static void handleBorderWidth( DocBorders docBorders, Cell cell ) {
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
	
	private static void handleBolders( DocCell docCell, Cell cell ) {
		DocBorders docBorders = docCell.getDocBorders();
		if ( docBorders != null ) {
			handleBorderColor(docBorders, cell);
			handleBorderWidth(docBorders, cell);
		}
	}
	
	private static void handleAligns( DocCell docCell, Cell cell ) {
		if ( docCell.getAlign() != DocPara.ALIGN_UNSET ) {
			Optional<HorizontalAlignment> ha = HorizontalAlignment.of( docCell.getAlign() );
			if ( ha.isPresent() ) {
				cell.setHorizontalAlignment( ha.get() );
			}
		}
		if ( docCell.getValign() != DocPara.ALIGN_UNSET ) {
			Optional<VerticalAlignment> va = VerticalAlignment.of( docCell.getValign() );
			if ( va.isPresent() ) {
				cell.setVerticalAlignment( va.get() );
			}
		}
	}
	
	private static List<Font> handleContent( Table table, CellParent cellParent, DocCell docCell, Cell cell, OpenPdfHelper docHelper ) throws DocumentException, IOException {
		List<Font> fontList = new ArrayList<>();
		Iterator<DocElement> itCurrent = docCell.docElements();
		while ( itCurrent.hasNext() ) {
			DocElement docElement = itCurrent.next();
			if ( docElement instanceof DocPara ) {
				DocPara docPara = (DocPara)docElement;
				OpenPpfDocHandler.setStyle( docCell , docPara );
				Paragraph paragraph = OpenPpfDocHandler.createPara( docPara, docHelper, fontList );
				cellParent.add( paragraph );
			} else if ( docElement instanceof DocPhrase ) {
				DocPhrase docPhrase = (DocPhrase)docElement;
				OpenPpfDocHandler.setStyle( docCell, docPhrase );
				log.trace( "docCell -> {}, docPara : {}", docCell, cell );
				cellParent.add( OpenPpfDocHandler.createPhrase( docPhrase, docHelper, fontList ) );						
			} else if ( docElement instanceof DocTable ) {
				DocTable innerTable = (DocTable) docElement;
				for ( DocElement rElem : innerTable.getElementList() ) {
					if ( rElem instanceof DocRow ) {
						DocRow r = (DocRow) rElem;
						Paragraph rowPara = new Paragraph();
						for ( DocElement cElem : r.getElementList() ) {
							if ( cElem instanceof DocCell ) {
								DocCell c = (DocCell) cElem;
								for ( DocElement pElem : c.getElementList() ) {
									if ( pElem instanceof DocPara ) {
										rowPara.add( OpenPpfDocHandler.createPara( (DocPara) pElem, docHelper, fontList ) );
									} else if ( pElem instanceof DocPhrase ) {
										rowPara.add( OpenPpfDocHandler.createPhrase( (DocPhrase) pElem, docHelper, fontList ) );
									}
								}
								rowPara.add( new Chunk("  ") );
							}
						}
						cellParent.add( rowPara );
					}
				}
			} else if ( docElement instanceof DocImage ) {
				Image img = OpenPpfDocHandler.createImage( (DocImage)docElement );
				if ( img != null ) {
					cellParent.add( img );
				}
			} else if ( docElement instanceof DocBarcode ) {
				cellParent.add( OpenPpfDocHandler.createBarcode( (DocBarcode)docElement, docHelper ) );
			} else if ( docElement instanceof DocList ) {
				cellParent.add( OpenPpfDocHandler.createList( (DocList)docElement, docHelper ) );
			} else if ( docElement instanceof DocContainer ) {
				cellParent.add( OpenPpfDocHandler.createPl( (DocContainer)docElement, docHelper ) );
			}
		}
		return fontList;
	}
	
	private static boolean handleCell( Table table, DocRow docRow, DocCell docCell, boolean isHeaderCell, DocTable docTable, OpenPdfHelper docHelper ) throws DocumentException, IOException {
		OpenPpfDocHandler.setStyle( docTable, docCell );
		Cell cell = new Cell();
		if ( isHeaderCell ) {
			cell.setHeader( true );
		}
		cell.setColspan( docCell.getColumnSpan() );
		cell.setRowspan( docCell.getRowSpan() );
		
		handleBolders(docCell, cell);
		
		if ( docCell.getBackColor() != null ) {
			cell.setBackgroundColor( DocModelUtils.parseHtmlColor( docCell.getBackColor() ) );
		}

		handleAligns(docCell, cell);
		
		CellParent cellParent = new CellParent( cell );
		
		List<Font> fontList = handleContent(table, cellParent, docCell, cell, docHelper);
		OpenPpfDocHandler.registerLocalDestination( cell, docCell.getId() );
		
		table.addCell( cell );
		List<Element> listChunk = cell.getChunks();
		if ( listChunk.size() == fontList.size() ) {
			for ( int k=0; k<listChunk.size(); k++ ) {
				Chunk c = (Chunk)listChunk.get( k );
				Font f = fontList.get( k );
				c.setFont( f );
			}
		}
		if ( docHelper.getPdfWriter() != null ) {
			docHelper.getPdfWriter().flush();
		}
		return isHeaderCell;
	}
	
	public static Table createTable( DocTable docTable, OpenPdfHelper docHelper ) throws DocumentException, IOException {
		
		Table table = new Table( docTable.getColumns() );
		table.setBorderWidth(0);	
		table.setWidth( docTable.getWidth() );
		table.setBorderColor( Color.black );
		table.setPadding( docTable.getPadding() );
		table.setSpacing( docTable.getSpacing() );
		table.setCellsFitPage( true );
		if ( docTable.getBackColor() != null ) {
			table.setBackgroundColor( DocModelUtils.parseHtmlColor( docTable.getBackColor() ) );
		}
		
		if ( docTable.getSpaceBefore() != null ) {
			table.setOffset( docTable.getSpaceBefore().floatValue() );
		}
		
		int[] cw = docTable.getColWithds();
		if (  cw != null ) {
			float[] w = new float[ cw.length ];
			for ( int k=0; k<w.length; k++ ) {
				w[k] = ((float)cw[k]/(float)100);
			}
			table.setWidths( w );
		}
		org.fugerit.java.doc.base.model.util.DocTableUtil tableUtil = docTable.getUtil();
		if ( tableUtil.isStrictHeader() ) {
			for ( DocElement hRow : tableUtil.getHeaderRows() ) {
				DocRow docRow = (DocRow) hRow;
				for ( DocElement c : docRow.getElementList() ) {
					handleCell( table, docRow, (DocCell) c, true, docTable, docHelper );
				}
			}
			table.endHeaders();
			for ( DocElement dRow : tableUtil.getDataRows() ) {
				DocRow docRow = (DocRow) dRow;
				for ( DocElement c : docRow.getElementList() ) {
					handleCell( table, docRow, (DocCell) c, false, docTable, docHelper );
				}
			}
		} else {
			for ( DocElement rowElem : docTable.getElementList() ) {
				if ( rowElem instanceof DocRow ) {
					DocRow docRow = (DocRow) rowElem;
					for ( DocElement c : docRow.getElementList() ) {
						handleCell( table, docRow, (DocCell) c, false, docTable, docHelper );
					}
				}
			}
		}
		OpenPpfDocHandler.registerLocalDestination( table, docTable.getId() );

		return table;
	}

	
}
