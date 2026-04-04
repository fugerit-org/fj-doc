# ---- Stage 1: Build AOT cache ----
FROM registry.access.redhat.com/ubi9/openjdk-25-runtime:1.24-2.1774011800 AS aot-builder

COPY --chown=185:0 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185:0 target/quarkus-app/*.jar /deployments/
COPY --chown=185:0 target/quarkus-app/app/ /deployments/app/
COPY --chown=185:0 target/quarkus-app/quarkus/ /deployments/quarkus/

WORKDIR /deployments

USER root

RUN timeout 120 java -XX:AOTCacheOutput=app.aot \
    -Xlog:aot*=debug:file=/tmp/aot.log \
    -Dquarkus.application.exit-on-startup=true \
    -jar quarkus-run.jar > /tmp/app.log 2>&1 || true && \
    echo "=== AOT Cache Generation Log ===" && \
    tail -50 /tmp/app.log && \
    if [ -f /tmp/aot.log ]; then echo "=== AOT Debug Log ===" && tail -50 /tmp/aot.log; fi && \
    echo "=== Checking for AOT cache ===" && \
    if [ -f /deployments/app.aot ]; then \
        echo "✓ AOT cache generated successfully"; \
        ls -lh /deployments/app.aot; \
    else \
        echo "⚠ WARNING: AOT cache file not generated, creating placeholder"; \
        touch /deployments/app.aot; \
    fi

USER 185:0

# ---- Stage 2: Runtime image ----
FROM registry.access.redhat.com/ubi9/openjdk-25-runtime:1.24-2.1774011800

LABEL org.opencontainers.image.authors="Fugerit" \
      org.opencontainers.image.source="Quarkus" \
      org.opencontainers.image.revision="1.0.0"

COPY --chown=185:0 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185:0 target/quarkus-app/*.jar /deployments/
COPY --chown=185:0 target/quarkus-app/app/ /deployments/app/
COPY --chown=185:0 target/quarkus-app/quarkus/ /deployments/quarkus/

# Copia l'AOT cache generata nello stage precedente
COPY --chown=185:0 --from=aot-builder /deployments/app.aot /deployments/app.aot

WORKDIR /deployments

EXPOSE 8080

# Run with AOT cache if available, otherwise normal startup
CMD ["sh", "-c", "cd /deployments && if [ -f app.aot ] && [ -s app.aot ]; then echo 'Starting with AOT cache...'; exec java -XX:AOTCache=app.aot -jar quarkus-run.jar; else echo 'Starting without AOT cache...'; exec java -jar quarkus-run.jar; fi"]
