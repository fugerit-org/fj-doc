package test.org.fugerit.java.doc.freemarker.process;

import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConstants;
import org.junit.Assert;
import org.junit.Test;

public class TestFreeMarkerConstants {

    @Test
    public void testFreeMarkerConstants() {
        Assert.assertNotNull(FreeMarkerConstants.getFreeMarkerMap(DocProcessContext.newContext()));
    }

}
