package org.fugerit.java.doc.base.typehelper.excel;

import java.io.Serializable;

import org.fugerit.java.doc.base.model.DocBorders;
import org.fugerit.java.doc.base.model.DocCell;

public class MatrixCell implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5551096860606022216L;

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
