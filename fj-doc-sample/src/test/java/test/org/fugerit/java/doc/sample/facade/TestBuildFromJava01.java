package test.org.fugerit.java.doc.sample.facade;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocBorders;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.model.DocPhrase;
import org.fugerit.java.doc.base.model.DocRow;
import org.fugerit.java.doc.base.model.DocTable;

public class TestBuildFromJava01 extends BasicFacadeTest {

	public TestBuildFromJava01() {
		super( "build_from_java_01", DocConfig.TYPE_PDF, DocConfig.TYPE_RTF, DocConfig.TYPE_HTML );
	}

	@Override
	protected DocBase getDocBase() throws Exception {
		DocBase docBase = new DocBase();
		
		// phrase
		DocPhrase docPhrase = new DocPhrase();
		docPhrase.setSize( -1 );
		docPhrase.setText( "Test first paragraph" );
		docBase.getDocBody().addElement( docPhrase );
		
		// table
		int cw[] = { 70, 30 };
		DocTable docTable = new DocTable();
		
		DocBorders docBorders = new DocBorders();
		docBorders.setBorderColorBottom( "#000000" );
		docBorders.setBorderColorTop( "#000000" );
		docBorders.setBorderColorLeft( "#000000" );
		docBorders.setBorderColorRight( "#000000" );
		docBorders.setBorderWidthBottom( 1 );
		docBorders.setBorderWidthTop( 1 );
		docBorders.setBorderWidthLeft( 1 );
		docBorders.setBorderWidthRight( 1 );
		
		docTable.setWidth( 100 );
		docTable.setColumns( cw.length );
		docTable.setColWithds( cw );
		docTable.setSpaceBefore( 10f );
		DocRow docRow = new DocRow();
		DocCell docCell1 = new DocCell();
		docCell1.setDocBorders( docBorders );
		DocPara docPara1 = new DocPara();
		docPara1.setText( "Cell content test 01" );
		docPara1.setSize( -1 );
		docCell1.addElement( docPara1 );
		docRow.addElement( docCell1 );
		DocCell docCell2 = new DocCell();
		docCell2.setDocBorders( docBorders );
		DocPara docPara2 = new DocPara();
		docPara2.setText( "Cell content test 02" );
		docPara2.setSize( -1 );
		docCell2.addElement( docPara2 );
		docRow.addElement( docCell2 );
		docTable.addElement( docRow );
		docBase.getDocBody().addElement( docTable );

		return docBase;
	}
	
}
