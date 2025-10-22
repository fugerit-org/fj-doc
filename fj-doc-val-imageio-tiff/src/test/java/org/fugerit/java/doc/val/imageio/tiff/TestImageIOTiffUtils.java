package org.fugerit.java.doc.val.imageio.tiff;

import org.fugerit.java.core.xml.dom.DOMIO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class TestImageIOTiffUtils {

    @Test
    void testNodeWithoutAttributes() throws Exception {
        try (InputStream stream = new ByteArrayInputStream( "<test/>".getBytes() )) {
            Document doc = DOMIO.newSafeDocumentBuilderFactory().newDocumentBuilder().parse( stream );
            ImageIOTiffUtils.dumpNode( doc, 1 );
            Assertions.assertNotNull( doc );
        }

    }

}
