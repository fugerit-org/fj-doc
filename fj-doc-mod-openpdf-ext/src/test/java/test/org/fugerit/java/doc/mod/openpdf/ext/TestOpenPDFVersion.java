package test.org.fugerit.java.doc.mod.openpdf.ext;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.util.mvn.MavenProps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@Slf4j
class TestOpenPDFVersion {

    private static final String SYS_PROP = "openpdf.jdk8.dependency.check";

    private String getVersion() {
        String version = MavenProps.getProperty( "com.github.librepdf", "openpdf", "version" );
        log.info( "using version {}, check:{}={}", version, SYS_PROP, System.getProperty( SYS_PROP ) );
        return version;
    }

    @Test
    @EnabledIfSystemProperty( named = SYS_PROP, matches = "true" )
    void testJdk8on() {
        Assertions.assertTrue( this.getVersion().startsWith( "1." ) );
    }

    @Test
    @DisabledIfSystemProperty( named = SYS_PROP, matches = "true" )
    void testJdk21on() {
        Assertions.assertTrue( this.getVersion().startsWith( "2." ) );
    }

}
