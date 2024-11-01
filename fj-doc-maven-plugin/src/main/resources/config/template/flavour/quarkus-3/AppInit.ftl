<#import '../flavour-macro.ftl' as fhm>
package <@fhm.toProjectPackage context=context/>;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;
import io.quarkus.runtime.annotations.RegisterForReflection;

@Slf4j
@ApplicationScoped
@RegisterForReflection( targets = { DocHelper.class, People.class } )
public class AppInit {

    void onStart(@Observes StartupEvent ev) {
        log.info("The application is starting...");
    }

}
