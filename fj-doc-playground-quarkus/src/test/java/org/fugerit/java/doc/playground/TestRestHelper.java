package org.fugerit.java.doc.playground;

import java.io.IOException;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;

@QuarkusTest
class TestRestHelper {

    @Test
    void test500() {
        Assertions.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                RestHelper.defaultHandle(() -> {
                    throw new IOException("scenario exception");
                }).getStatus());
    }

    @Test
    void test400() {
        Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
                RestHelper.defaultHandle(() -> null).getStatus());
    }

    @Test
    void testFindCause() {
        Assertions.assertEquals(ConfigRuntimeException.class, RestHelper.findCause(new ConfigRuntimeException("a")).getClass());
        Assertions.assertEquals(ConfigRuntimeException.class,
                RestHelper.findCause(new ConfigException(new ConfigRuntimeException("a"))).getClass());
    }

}
