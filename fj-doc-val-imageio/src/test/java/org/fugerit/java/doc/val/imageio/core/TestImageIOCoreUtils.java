package org.fugerit.java.doc.val.imageio.core;

import org.fugerit.java.core.xml.dom.DOMIO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class TestImageIOCoreUtils {

    @Test
    void testNodeWithoutAttributes() throws Exception {
        try (InputStream stream = new ByteArrayInputStream( "<test/>".getBytes() )) {
            Document doc = DOMIO.newSafeDocumentBuilderFactory().newDocumentBuilder().parse( stream );
            ImageIOCoreUtils.dumpNode( doc, 1 );
            Assertions.assertNotNull( doc );
        }

    }

}
