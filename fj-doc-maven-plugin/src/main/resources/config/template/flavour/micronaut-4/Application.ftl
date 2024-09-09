<#import '../flavour-macro.ftl' as fhm>
package <@fhm.toProjectPackage context=context/>;


import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;

@OpenAPIDefinition(
    info = @Info(
            title = "${context.artifactId}",
            version = "${context.projectVersion}"
    )
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}