package test.org.fugerit.java.doc.freemarker.process;

import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestFreeMarkerConstants {

    @Test
    void testFreeMarkerConstants() {
        Assertions.assertNotNull(FreeMarkerConstants.getFreeMarkerMap(DocProcessContext.newContext()));
    }

}
