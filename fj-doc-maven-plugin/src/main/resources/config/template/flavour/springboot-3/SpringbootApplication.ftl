<#import '../flavour-macro.ftl' as fhm>
package <@fhm.toProjectPackage context=context/>;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ${context.artifactIdAsClassName}Application {

	public static void main(String[] args) {
		SpringApplication.run(${context.artifactIdAsClassName}Application.class, args);
	}

}
