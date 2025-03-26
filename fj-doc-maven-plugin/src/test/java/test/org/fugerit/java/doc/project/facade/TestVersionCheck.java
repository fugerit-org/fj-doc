package test.org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.project.facade.VenusContext;
import org.fugerit.java.doc.project.facade.VersionCheck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

@Slf4j
class TestVersionCheck {

    private static final String V_8_6_2 = "8.6.2";

    @Test
    void testVersionCheck() {
        Assertions.assertTrue(VersionCheck.isMajorThan( V_8_6_2, "8.6.1-SNAPSHOT" ));
        Assertions.assertFalse(VersionCheck.isMajorThan( "8.5.1", "8.5.2" ));
        VenusContext context = new VenusContext( new File( "target" ), V_8_6_2, "base" );
        Assertions.assertFalse( context.isPreVersion862() );
        log.info( "version from null to : {}", VersionCheck.findVersion( null ) );
        log.info( "version from null to : {}", VersionCheck.findVersion( VersionCheck.LATEST ) );
        Assertions.assertEquals( V_8_6_2, VersionCheck.findFromPropsFile( "maven/pom.properties" ) );
    }

}
