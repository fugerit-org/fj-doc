package test.org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.project.facade.VenusContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

@Slf4j
public class TestVenusContext {

    @Test
    public void testVenusContext() {
        File folder = new File( "target/" );
        VenusContext context = new VenusContext( folder, VenusContext.VERSION_NA_FULL_PROCESS, "fj-doc-base" );
        Assert.assertNotNull( context.getTestResourcesFolder() );
        Assert.assertNotNull( context.getMainResourcesFolder() );
    }

}
