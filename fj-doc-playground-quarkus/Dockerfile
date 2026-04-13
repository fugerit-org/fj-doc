# ---- Stage 1: Build AOT cache ----
FROM registry.access.redhat.com/ubi10/openjdk-25-runtime:1.24 AS aot-builder

COPY --chown=185:0 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185:0 target/quarkus-app/*.jar /deployments/
COPY --chown=185:0 target/quarkus-app/app/ /deployments/app/
COPY --chown=185:0 target/quarkus-app/quarkus/ /deployments/quarkus/

# the cache is created with the following parameters.
#java -XX:AOTCacheOutput=app.aot \
#     -Xlog:aot*=debug:file=/tmp/aot.log \
#     -XX:+UseCompactObjectHeaders \
#     -jar quarkus-run.jar

# Warmup script: starts the app in the background, fires representative
# HTTP requests to exercise all major hot paths, then sends SIGTERM so
# the JVM flushes a richer AOT cache before exiting.
COPY --chown=185:0 aot-warmup.sh /deployments/aot-warmup.sh

WORKDIR /deployments

USER root

RUN chmod +x /deployments/aot-warmup.sh && bash /deployments/aot-warmup.sh

USER 185:0

# ---- Stage 2: Runtime image ----
#
# sample build :
#
# docker build -t playground-test .
#
# sample run :
#
# docker run --rm -p 8080:8080 --name playground -e JAVA_OPTS_APPEND="-XX:AOTCache=app.aot -Xlog:aot -XX:+UseCompactObjectHeaders" playground-test
#
FROM registry.access.redhat.com/ubi10/openjdk-25-runtime:1.24

LABEL org.opencontainers.image.authors="Fugerit" \
      org.opencontainers.image.source="Quarkus" \
      org.opencontainers.image.revision="1.0.0"

COPY --chown=185:0 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185:0 target/quarkus-app/*.jar /deployments/
COPY --chown=185:0 target/quarkus-app/app/ /deployments/app/
COPY --chown=185:0 target/quarkus-app/quarkus/ /deployments/quarkus/

# Copy AOT cache generated in the previous stage
COPY --chown=185:0 --from=aot-builder /deployments/app.aot /deployments/app.aot

WORKDIR /deployments

EXPOSE 8080
