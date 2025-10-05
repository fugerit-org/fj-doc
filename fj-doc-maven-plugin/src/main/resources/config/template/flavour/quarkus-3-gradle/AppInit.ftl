<#import '../flavour-macro.ftl' as fhm>
package <@fhm.toProjectPackage context=context/>;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.fugerit.java.doc.base.config.InitHandler;

@Slf4j
@ApplicationScoped
@RegisterForReflection( targets = { DocHelper.class, People.class } )
public class AppInit {

    DocHelper docHelper;

    public AppInit(DocHelper docHelper) {
        this.docHelper = docHelper;
    }

    void onStart(@Observes StartupEvent ev) {
        log.info("The application is starting...");
        /*
         * This will initialize all the doc handlers using async mode.
         * (use method InitHandler.initDocAll() for synced startup)
         */
        InitHandler.initDocAllAsync(
        docHelper.getDocProcessConfig().getFacade().handlers() );
    }

}
