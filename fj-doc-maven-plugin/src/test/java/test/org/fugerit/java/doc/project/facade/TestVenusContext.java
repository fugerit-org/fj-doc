package test.org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.project.facade.VenusContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

@Slf4j
class TestVenusContext {

    private static final File TARGET = new File( "target/" );

    @Test
    void testVenusContext() {
        VenusContext context = new VenusContext( TARGET, VenusContext.VERSION_NA_FULL_PROCESS, "fj-doc-base" );
        Assertions.assertNotNull( context.getTestResourcesFolder() );
        Assertions.assertNotNull( context.getMainResourcesFolder() );
        Assertions.assertFalse( context.isAsciidocFreemarkerHandlerAvailable() );
        Assertions.assertTrue( context.isCoreHandlersNotAvailable() );
    }

    @Test
    void testAsciiDocCheck() {
        VenusContext context = new VenusContext( TARGET, "8.10.8", "fj-doc-base" );
        Assertions.assertTrue( context.isAsciidocFreemarkerHandlerAvailable() );
    }

    @Test
    void testVenusContextCoreHandlers() {
        VenusContext context = new VenusContext( TARGET, "8.17.0", "fj-doc-base" );
        Assertions.assertFalse( context.isCoreHandlersNotAvailable() );
    }

}
