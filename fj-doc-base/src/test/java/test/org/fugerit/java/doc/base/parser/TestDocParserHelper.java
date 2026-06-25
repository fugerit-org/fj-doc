package test.org.fugerit.java.doc.base.parser;

import org.fugerit.java.doc.base.model.DocBackground;
import org.fugerit.java.doc.base.model.DocBookmarkTree;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocContainer;
import org.fugerit.java.doc.base.model.DocFooter;
import org.fugerit.java.doc.base.model.DocHeader;
import org.fugerit.java.doc.base.model.DocLi;
import org.fugerit.java.doc.base.model.DocList;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.model.DocRow;
import org.fugerit.java.doc.base.model.DocTable;
import org.fugerit.java.doc.base.parser.DocParserHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestDocParserHelper {

    private static final DocParserHelper HELPER = DocParserHelper.getInstance();

    @Test
    void testGetInstanceReturnsSameInstance() {
        Assertions.assertSame( DocParserHelper.getInstance(), DocParserHelper.getInstance() );
    }

    @Test
    void testContainerTagsAreRecognized() {
        Assertions.assertTrue( HELPER.isContainerElement( DocTable.TAG_NAME ) );
        Assertions.assertTrue( HELPER.isContainerElement( DocRow.TAG_NAME ) );
        Assertions.assertTrue( HELPER.isContainerElement( DocCell.TAG_NAME ) );
        Assertions.assertTrue( HELPER.isContainerElement( DocList.TAG_NAME ) );
        Assertions.assertTrue( HELPER.isContainerElement( DocLi.TAG_NAME ) );
        Assertions.assertTrue( HELPER.isContainerElement( DocContainer.TAG_NAME_PL ) );
        Assertions.assertTrue( HELPER.isContainerElement( DocContainer.TAG_NAME_BODY ) );
        Assertions.assertTrue( HELPER.isContainerElement( DocContainer.TAG_NAME_META ) );
        Assertions.assertTrue( HELPER.isContainerElement( DocContainer.TAG_NAME_METADATA ) );
        Assertions.assertTrue( HELPER.isContainerElement( DocHeader.TAG_NAME ) );
        Assertions.assertTrue( HELPER.isContainerElement( DocFooter.TAG_NAME ) );
        Assertions.assertTrue( HELPER.isContainerElement( DocPara.TAG_NAME ) );
        Assertions.assertTrue( HELPER.isContainerElement( DocBackground.TAG_NAME ) );
        Assertions.assertTrue( HELPER.isContainerElement( DocBookmarkTree.TAG_NAME ) );
    }

    @Test
    void testNonContainerTagIsNotRecognized() {
        Assertions.assertFalse( HELPER.isContainerElement( "phrase" ) );
        Assertions.assertFalse( HELPER.isContainerElement( "not-a-container-tag" ) );
        Assertions.assertFalse( HELPER.isContainerElement( "" ) );
    }

    @Test
    void testGetContainerNamesIsNotEmpty() {
        Assertions.assertFalse( HELPER.getContainerNames().isEmpty() );
    }
}
