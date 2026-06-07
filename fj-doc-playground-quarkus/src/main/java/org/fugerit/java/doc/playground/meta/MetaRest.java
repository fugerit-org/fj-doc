package org.fugerit.java.doc.playground.meta;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.PropsIO;
import org.fugerit.java.doc.playground.RestHelper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
@Path("/meta")
public class MetaRest {

    @ConfigProperty(name = "quarkus.platform.version", defaultValue = "unset")
    String quarkusPlatformVersion;

    /**
     * Comma-separated list of available Venus versions.
     * Can be overridden in application.properties via:
     * playground.available-versions=8.18.5-SNAPSHOT,8.18.4,...
     */
    @ConfigProperty(name = "playground.available-versions", defaultValue = "8.18.4,8.18.0,8.17.9,8.17.0,8.16.9,8.16.0,8.15.1,8.15.0,8.14.1,8.14.0,8.13.14,8.13.0,8.12.8,8.12.0,8.11.9,8.10.9,8.10.0,8.9.7,8.9.0,8.8.9,8.8.0,8.7.6")
    Optional<String> availableVersions;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/version")
    public Response getVersion() {
        return RestHelper.defaultHandle(() -> {
            Properties buildProps = PropsIO.loadFromClassLoader("build.properties");
            log.info("buildProps : {}", buildProps);

            return Response.ok().entity(buildProps).build();
        });
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/available-versions")
    public Response getAvailableVersions() {
        return RestHelper.defaultHandle(() -> {
            List<String> versions = availableVersions
                    .filter(s -> !s.isBlank())
                    .map(s -> Arrays.asList(s.split(",")))
                    .orElse(List.of());
            return Response.ok().entity(versions).build();
        });
    }

    private static final String[] ADD_PROPS = { "java.version", "java.vendor", "os.name", "os.version", "os.arch" };

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/info")
    public Response getInfo() {
        return RestHelper.defaultHandle(() -> {
            Map<String, String> info = new LinkedHashMap<>();
            info.put("quarkus.version", io.quarkus.builder.Version.getVersion());
            SafeFunction.applyIfNotNull(this.quarkusPlatformVersion,
                    () -> info.put("quarkus.platform.version", this.quarkusPlatformVersion));
            for (String key : ADD_PROPS) {
                info.put(key, System.getProperty(key));
            }
            return Response.ok().entity(info).build();
        });
    }

}