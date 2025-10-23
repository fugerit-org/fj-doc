package org.fugerit.java.doc.val.imageio.tiff;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestImageIOTiffTags {

    @Test
    void testUnknownTag() {
         Assertions.assertEquals( String.valueOf( Integer.MIN_VALUE ), ImageIOTiffTags.tagDescription( Integer.MIN_VALUE ) );
    }

}
