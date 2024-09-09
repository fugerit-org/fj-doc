<#import '../flavour-macro.ftl' as fhm>
package test.<@fhm.toProjectPackage context=context/>;

import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
class GreetingResourceIT extends GreetingResourceTest {
    // Execute the same tests but in packaged mode.
}
