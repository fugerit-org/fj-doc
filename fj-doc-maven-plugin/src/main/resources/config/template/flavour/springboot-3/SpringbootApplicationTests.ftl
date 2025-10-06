<#import '../flavour-macro.ftl' as fhm>
package test.<@fhm.toProjectPackage context=context/>;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ${context.artifactIdAsClassName}ApplicationTests {

    // add tests

}
