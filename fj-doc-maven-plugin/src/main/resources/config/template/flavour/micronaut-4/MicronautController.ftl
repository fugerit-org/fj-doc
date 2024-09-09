<#import '../flavour-macro.ftl' as fhm>
package <@fhm.toProjectPackage context=context/>;

import io.micronaut.http.annotation.*;

@Controller("${context.artifactId}")
public class ${context.artifactIdAsClassName}Controller {

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }
}