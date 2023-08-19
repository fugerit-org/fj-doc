package org.fugerit.java.doc.base.typehelper.excel;

import java.io.Serializable;

import org.fugerit.java.doc.base.model.DocBorders;
import org.fugerit.java.doc.base.model.DocCell;

public class TableMatrix implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4486272508479249602L;

	private MatrixCell[][] text;
	
	private int cn;
	
	private int rn;
	
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