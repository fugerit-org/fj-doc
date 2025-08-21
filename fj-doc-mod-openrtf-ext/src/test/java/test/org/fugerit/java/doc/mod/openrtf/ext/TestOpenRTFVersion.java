package test.org.fugerit.java.doc.mod.openrtf.ext;

import lombok.extern.slf4j.Slf4j;import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.mvn.MavenProps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledForJreRange;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@Slf4j
class TestOpenRTFVersion {

	private static final String SYS_PROP = "openrtf.jdk8.dependency.check";

	private String getVersion() {
		String version = MavenProps.getProperty( "com.github.librepdf", "openrtf", "version" );
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
