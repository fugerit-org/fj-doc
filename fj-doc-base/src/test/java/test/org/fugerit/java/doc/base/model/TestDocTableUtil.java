package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.model.DocRow;
import org.fugerit.java.doc.base.model.DocTable;
import org.fugerit.java.doc.base.model.util.DocTableUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestDocTableUtil {

    private DocTable buildTable( boolean... headerFlags ) {
        DocTable table = new DocTable();
        table.setColumns( 2 );
        for ( boolean header : headerFlags ) {
            DocRow row = new DocRow();
            row.setHeader( header );
            DocCell cell1 = new DocCell();
            DocPara para1 = new DocPara();
            para1.setText( "cell1" );
            cell1.addElement( para1 );
            row.addElement( cell1 );
            DocCell cell2 = new DocCell();
            DocPara para2 = new DocPara();
            para2.setText( "cell2" );
            cell2.addElement( para2 );
            row.addElement( cell2 );
            table.addElement( row );
        }
        return table;
    }

    @Test
    void testStrictHeaderWhenHeaderRowsFirst() {
        DocTable table = buildTable( true, true, false, false );
        DocTableUtil util = table.getUtil();
        Assertions.assertEquals( 2, util.getHeaderRows().size() );
        Assertions.assertEquals( 2, util.getDataRows().size() );
        Assertions.assertTrue( util.isStrictHeader() );
    }

    @Test
    void testNotStrictHeaderWhenHeaderRowsMixed() {
        DocTable table = buildTable( false, true, false );
        DocTableUtil util = table.getUtil();
        Assertions.assertEquals( 1, util.getHeaderRows().size() );
        Assertions.assertEquals( 2, util.getDataRows().size() );
        Assertions.assertFalse( util.isStrictHeader() );
    }

    @Test
    void testNoHeaderRows() {
        DocTable table = buildTable( false, false );
        DocTableUtil util = table.getUtil();
        Assertions.assertTrue( util.getHeaderRows().isEmpty() );
        Assertions.assertEquals( 2, util.getDataRows().size() );
        Assertions.assertFalse( util.isStrictHeader() );
    }

    @Test
    void testAllHeaderRows() {
        DocTable table = buildTable( true, true );
        DocTableUtil util = table.getUtil();
        Assertions.assertEquals( 2, util.getHeaderRows().size() );
        Assertions.assertTrue( util.getDataRows().isEmpty() );
        Assertions.assertTrue( util.isStrictHeader() );
    }

    @Test
    void testEmptyTable() {
        DocTable table = new DocTable();
        DocTableUtil util = table.getUtil();
        Assertions.assertTrue( util.getHeaderRows().isEmpty() );
        Assertions.assertTrue( util.getDataRows().isEmpty() );
        Assertions.assertFalse( util.isStrictHeader() );
    }
}
