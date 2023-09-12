package org.fugerit.java.doc.mod.poi;

import java.io.Serializable;
import java.util.Collection;

import org.apache.poi.ss.usermodel.CellStyle;
import org.fugerit.java.doc.base.model.DocBorders;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocPara;

public class PoiCellStyleModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5403077043309984961L;

	private CellStyle style;
	
	private DocPara docPara;
	
	private DocCell docCell;

	public CellStyle getStyle() {
		return style;
	}

	public void setStyle(CellStyle style) {
		this.style = style;
	}

	public DocPara getDocPara() {
		return docPara;
	}

	public void setDocPara(DocPara docPara) {
		this.docPara = docPara;
	}

	public DocCell getDocCell() {
		return docCell;
	}

	public void setDocCell(DocCell docCell) {
		this.docCell = docCell;
	}

	public PoiCellStyleModel(CellStyle style, DocPara docPara, DocCell docCell) {
		super();
		this.style = style;
		this.docPara = docPara;
		this.docCell = docCell;
	}

	private static boolean eq( Object o1, Object o2 ) {
		return (o1 == null && o2 == null) || ( o1 != null && o1.equals( o2 ));
	}
	
	public static CellStyle find( Collection<PoiCellStyleModel> styles, DocPara p, DocCell c ) {
		CellStyle result = null;
		DocBorders b = c.getDocBorders();
		if ( p!= null ) {
			for ( PoiCellStyleModel style : styles ) {
				DocPara cp = style.getDocPara();
				DocCell cc = style.getDocCell();
				DocBorders cb = cc.getDocBorders();
				boolean equal = eq( p.getBackColor(), cp.getBackColor() )
						&& eq( p.getForeColor(), cp.getForeColor() )
						&& eq( p.getFontName(), cp.getFontName() )
						&& eq( p.getSize(), cp.getSize() )
						&& eq( p.getFormat(), cp.getFormat() )
						&& eq( p.getStyle(), cp.getStyle() )
						&& eq( p.getType(), cp.getType() )
						&& eq( p.getAlign(), cp.getAlign() );
				equal = equal 
						&& eq( c.getBackColor(), cc.getBackColor() )
						&& eq( c.getForeColor(), cc.getForeColor() )
						&& eq( c.getType(), cc.getType() )
						&& eq( c.getAlign(), cc.getAlign() );
				equal = equal 
						&& eq( b.getBorderWidthBottom(), cb.getBorderWidthBottom() )
						&& eq( b.getBorderWidthTop(), cb.getBorderWidthTop() )
						&& eq( b.getBorderWidthLeft(), cb.getBorderWidthLeft() )
						&& eq( b.getBorderWidthRight(), cb.getBorderWidthRight() ) 
						&& eq( b.getBorderColorBottom(), cb.getBorderColorBottom() )
						&& eq( b.getBorderColorTop(), cb.getBorderColorTop() )
						&& eq( b.getBorderColorLeft(), cb.getBorderColorLeft() )
						&& eq( b.getBorderColorRight(), cb.getBorderColorRight() );
				if ( equal ) {
					result = style.getStyle();
					break;
				}
			}
		}
		return result;
	}
	
}
