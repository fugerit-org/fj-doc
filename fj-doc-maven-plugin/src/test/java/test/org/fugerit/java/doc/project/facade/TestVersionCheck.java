package test.org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.project.facade.VenusContext;
import org.fugerit.java.doc.project.facade.VersionCheck;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

@Slf4j
public class TestVersionCheck {

    private static String V_8_6_2 = "8.6.2";

    @Test
    public void testVersionCheck() {
        Assert.assertTrue(VersionCheck.isMajorThan( V_8_6_2, "8.6.1" ));
        Assert.assertFalse(VersionCheck.isMajorThan( "8.5.1", "8.5.2" ));
        VenusContext context = new VenusContext( new File( "target" ), V_8_6_2, "base" );
        Assert.assertFalse( context.isPreVersion862() );
        log.info( "version from null to : {}", VersionCheck.findVersion( null ) );
        Assert.assertEquals( V_8_6_2, VersionCheck.findFromPropsFile( "maven/pom.properties" ) );
    }

}
