package test.org.fugerit.java.doc.mod.fop;

import org.apache.fop.apps.io.ResourceResolverFactory;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.doc.mod.fop.config.FopConfigClassLoaderWrapper;
import org.fugerit.java.doc.mod.fop.config.ResourceResolverWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import test.org.fugerit.java.BasicTest;

class TestFopConfig extends BasicTest {

	@Test
	void testResolver1() {
		Assertions.assertNotNull( SafeFunction.get( () -> {
			FopConfigClassLoaderWrapper wrapper = new FopConfigClassLoaderWrapper( "test", ResourceResolverFactory.createDefaultResourceResolver() );
			return this.fullSerializationTest( wrapper );
		} ) );
	}

	@Test
	void testResolver2() {
		Assertions.assertNotNull( SafeFunction.get( () -> {
			FopConfigClassLoaderWrapper wrapper = new FopConfigClassLoaderWrapper( "test" );
			return this.fullSerializationTest( wrapper );
		} ) );
	}
	
	@Test
	void testResolver3() {
		Assertions.assertNotNull( SafeFunction.get( () -> {
			ResourceResolverWrapper rrw = new ResourceResolverWrapper( ResourceResolverFactory.createDefaultResourceResolver() );
			FopConfigClassLoaderWrapper wrapper = new FopConfigClassLoaderWrapper( "test", rrw );
			return this.fullSerializationTest( wrapper );
		} ) );
	}
	
}
