package test.org.fugerit.java.doc.mod.fop;

import org.apache.fop.apps.io.ResourceResolverFactory;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.doc.mod.fop.config.FopConfigClassLoaderWrapper;
import org.fugerit.java.doc.mod.fop.config.ResourceResolverWrapper;
import org.junit.Assert;
import org.junit.Test;

import test.org.fugerit.java.BasicTest;

public class TestFopConfig extends BasicTest {

	@Test
	public void testResolver1() {
		Assert.assertNotNull( SafeFunction.get( () -> {
			FopConfigClassLoaderWrapper wrapper = new FopConfigClassLoaderWrapper( "test", ResourceResolverFactory.createDefaultResourceResolver() );
			return this.fullSerializationTest( wrapper );
		} ) );
	}

	@Test
	public void testResolver2() {
		Assert.assertNotNull( SafeFunction.get( () -> {
			FopConfigClassLoaderWrapper wrapper = new FopConfigClassLoaderWrapper( "test" );
			return this.fullSerializationTest( wrapper );
		} ) );
	}
	
	@Test
	public void testResolver3() {
		Assert.assertNotNull( SafeFunction.get( () -> {
			ResourceResolverWrapper rrw = new ResourceResolverWrapper( ResourceResolverFactory.createDefaultResourceResolver() );
			FopConfigClassLoaderWrapper wrapper = new FopConfigClassLoaderWrapper( "test", rrw );
			return this.fullSerializationTest( wrapper );
		} ) );
	}
	
}
