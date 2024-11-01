package org.fugerit.java.fjdocnativequarkus;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Arrays;

@Slf4j
@ApplicationScoped
@RegisterForReflection( targets = { DocHelper.class } )
public class AppInit {

    void onStart(@Observes StartupEvent ev) {
        log.info("The application is starting...");
    }

}
