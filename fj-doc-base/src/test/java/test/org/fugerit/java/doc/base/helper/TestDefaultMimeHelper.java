package test.org.fugerit.java.doc.base.helper;

import org.fugerit.java.doc.base.helper.DefaultMimeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestDefaultMimeHelper {

    @Test
    void testGetDefaultMimePdf() {
        String mime = DefaultMimeHelper.getDefaultMime( "pdf" );
        Assertions.assertEquals( "application/pdf", mime );
    }

    @Test
    void testGetDefaultMimeXml() {
        String mime = DefaultMimeHelper.getDefaultMime( "xml" );
        Assertions.assertEquals( "text/xml", mime );
    }

    @Test
    void testGetDefaultMimeHtml() {
        String mime = DefaultMimeHelper.getDefaultMime( "html" );
        Assertions.assertEquals( "text/html", mime );
    }

    @Test
    void testGetDefaultMimeXls() {
        String mime = DefaultMimeHelper.getDefaultMime( "xls" );
        Assertions.assertEquals( "application/vnd.ms-excel", mime );
    }

    @Test
    void testGetDefaultMimeUnknownReturnsNull() {
        String mime = DefaultMimeHelper.getDefaultMime( "unknown-type-xyz" );
        Assertions.assertNull( mime );
    }
}
