package org.fugerit.java.doc.playground;

import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class SpaRouteFilter {

    private static final String INDEX_PAGE = "/fj-doc-playground/home/index.html";

    void onStart(@Observes StartupEvent ev, Router router) {
        log.info("SpaRouteFilter onStart router: {}", router);
        router.route().order(10).handler(rc -> {
            String path = rc.normalizedPath();
            log.debug("Incoming request path: {}", path);
            if (path.startsWith("/fj-doc-playground/home")) {
                if (path.equals(INDEX_PAGE) || path.contains(".")) {
                    rc.next();
                } else {
                    log.debug("Rerouting SPA path: {} -> {}", path, INDEX_PAGE);
                    rc.reroute(INDEX_PAGE);
                }
            } else {
                rc.next();
            }
        });
    }

}
