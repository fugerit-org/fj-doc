package org.fugerit.java.doc.playground;

import static io.restassured.RestAssured.given;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.Handler;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

@QuarkusTest
class SpaRouteFilterTest {

    private RoutingContext createMockRoutingContext(String path, AtomicBoolean nextCalled, AtomicReference<String> reroutedPath) {
        return (RoutingContext) Proxy.newProxyInstance(
                RoutingContext.class.getClassLoader(),
                new Class<?>[]{RoutingContext.class},
                (proxy, method, args) -> {
                    if ("normalizedPath".equals(method.getName())) {
                        return path;
                    } else if ("next".equals(method.getName())) {
                        nextCalled.set(true);
                        return null;
                    } else if ("reroute".equals(method.getName())) {
                        reroutedPath.set((String) args[0]);
                        return null;
                    }
                    return null;
                });
    }

    @Test
    void testHandleRouteNonHome() {
        SpaRouteFilter filter = new SpaRouteFilter();
        AtomicBoolean nextCalled = new AtomicBoolean(false);
        AtomicReference<String> reroutedPath = new AtomicReference<>();
        RoutingContext rc = createMockRoutingContext("/fj-doc-playground/api/catalog/list", nextCalled, reroutedPath);

        filter.handleRoute(rc);

        Assertions.assertTrue(nextCalled.get());
        Assertions.assertNull(reroutedPath.get());
    }

    @Test
    void testHandleRouteIndexPage() {
        SpaRouteFilter filter = new SpaRouteFilter();
        AtomicBoolean nextCalled = new AtomicBoolean(false);
        AtomicReference<String> reroutedPath = new AtomicReference<>();
        RoutingContext rc = createMockRoutingContext(SpaRouteFilter.INDEX_PAGE, nextCalled, reroutedPath);

        filter.handleRoute(rc);

        Assertions.assertTrue(nextCalled.get());
        Assertions.assertNull(reroutedPath.get());
    }

    @Test
    void testHandleRouteStaticAsset() {
        SpaRouteFilter filter = new SpaRouteFilter();
        AtomicBoolean nextCalled = new AtomicBoolean(false);
        AtomicReference<String> reroutedPath = new AtomicReference<>();
        RoutingContext rc = createMockRoutingContext("/fj-doc-playground/home/assets/index.js", nextCalled, reroutedPath);

        filter.handleRoute(rc);

        Assertions.assertTrue(nextCalled.get());
        Assertions.assertNull(reroutedPath.get());
    }

    @Test
    void testHandleRouteSpaSubroute() {
        SpaRouteFilter filter = new SpaRouteFilter();
        AtomicBoolean nextCalled = new AtomicBoolean(false);
        AtomicReference<String> reroutedPath = new AtomicReference<>();
        RoutingContext rc = createMockRoutingContext("/fj-doc-playground/home/doc_fun/doc_xml_editor", nextCalled, reroutedPath);

        filter.handleRoute(rc);

        Assertions.assertFalse(nextCalled.get());
        Assertions.assertEquals(SpaRouteFilter.INDEX_PAGE, reroutedPath.get());
    }

    @Test
    void testHandleRouteSpaHome() {
        SpaRouteFilter filter = new SpaRouteFilter();
        AtomicBoolean nextCalled = new AtomicBoolean(false);
        AtomicReference<String> reroutedPath = new AtomicReference<>();
        RoutingContext rc = createMockRoutingContext("/fj-doc-playground/home", nextCalled, reroutedPath);

        filter.handleRoute(rc);

        Assertions.assertFalse(nextCalled.get());
        Assertions.assertEquals(SpaRouteFilter.INDEX_PAGE, reroutedPath.get());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testOnStart() {
        SpaRouteFilter filter = new SpaRouteFilter();
        AtomicReference<Handler<RoutingContext>> handlerRef = new AtomicReference<>();
        AtomicReference<Integer> orderRef = new AtomicReference<>();

        Route mockRoute = (Route) Proxy.newProxyInstance(
                Route.class.getClassLoader(),
                new Class<?>[]{Route.class},
                (proxy, method, args) -> {
                    if ("order".equals(method.getName())) {
                        orderRef.set((Integer) args[0]);
                        return proxy;
                    } else if ("handler".equals(method.getName())) {
                        handlerRef.set((Handler<RoutingContext>) args[0]);
                        return proxy;
                    }
                    return null;
                });

        Router mockRouter = (Router) Proxy.newProxyInstance(
                Router.class.getClassLoader(),
                new Class<?>[]{Router.class},
                (proxy, method, args) -> {
                    if ("route".equals(method.getName())) {
                        return mockRoute;
                    }
                    return null;
                });

        filter.onStart(new StartupEvent(), mockRouter);

        Assertions.assertEquals(10, orderRef.get());
        Assertions.assertNotNull(handlerRef.get());

        // Test the handler registered in onStart
        AtomicBoolean nextCalled = new AtomicBoolean(false);
        AtomicReference<String> reroutedPath = new AtomicReference<>();
        RoutingContext rc = createMockRoutingContext("/other/path", nextCalled, reroutedPath);
        handlerRef.get().handle(rc);
        Assertions.assertTrue(nextCalled.get());
    }

    @Test
    void testHttpSpaDirectRoute() {
        given()
                .when()
                .get("/fj-doc-playground/home/doc_fun/doc_xml_editor")
                .then()
                .statusCode(200);
    }

    @Test
    void testHttpHome() {
        given()
                .when()
                .get("/fj-doc-playground/home")
                .then()
                .statusCode(200);
    }

    @Test
    void testHttpHomeSlash() {
        given()
                .when()
                .get("/fj-doc-playground/home/")
                .then()
                .statusCode(200);
    }

    @Test
    void testHttpIndexHtml() {
        given()
                .when()
                .get(SpaRouteFilter.INDEX_PAGE)
                .then()
                .statusCode(200);
    }

    @Test
    void testHttpStaticMissing() {
        given()
                .when()
                .get("/fj-doc-playground/home/missing.js")
                .then()
                .statusCode(404);
    }

    @Test
    void testHttpNonHome() {
        given()
                .when()
                .get("/nonexistent-path")
                .then()
                .statusCode(404);
    }

}
