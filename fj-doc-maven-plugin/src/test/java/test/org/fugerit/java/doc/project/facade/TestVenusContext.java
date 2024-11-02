package test.org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.project.facade.VenusContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

@Slf4j
public class TestVenusContext {

    private static final File TARGET = new File( "target/" );

    @Test
    public void testVenusContext() {
        VenusContext context = new VenusContext( TARGET, VenusContext.VERSION_NA_FULL_PROCESS, "fj-doc-base" );
        Assert.assertNotNull( context.getTestResourcesFolder() );
        Assert.assertNotNull( context.getMainResourcesFolder() );
        Assert.assertFalse( context.isAsciidocFreemarkerHandlerAvailable() );
    }

    @Test
    public void testAsciiDocCheck() {
        VenusContext context = new VenusContext( TARGET, "8.10.8", "fj-doc-base" );
        Assert.assertTrue( context.isAsciidocFreemarkerHandlerAvailable() );
    }

}
