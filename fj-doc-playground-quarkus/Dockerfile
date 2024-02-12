# fj-doc-playground-quarkus image build 1.0.0
#
# Public built image form amd64/arm64 linux can be found on repository : 
# https://hub.docker.com/repository/docker/fugeritorg/fj-doc-playground-quarkus/general
#
# create : docker run -it -p 8080:8080 --name fj-doc-playground-quarkus fugeritorg/fj-doc-playground-quarkus:latest
# start : docker start fj-doc-playground-quarkus
# stop : docker stop fj-doc-playground-quarkus
#
# Image tag : 
# docker image tag [image tag] fugeritorg/fj-doc-playground-quarkus:latest
#
# Change with any base openjdk image is preferred 
FROM eclipse-temurin:21.0.2_13-jre-alpine

LABEL org.opencontainers.image.authors="Fugerit" \
      org.opencontainers.image.source="Quarkus" \
      org.opencontainers.image.revision="1.0.0"


COPY --chown=185:0 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185:0 target/quarkus-app/*.jar /deployments/
COPY --chown=185:0 target/quarkus-app/app/ /deployments/app/
COPY --chown=185:0 target/quarkus-app/quarkus/ /deployments/quarkus/

USER root

# disabled for now
RUN echo "securerandom.source=file:/dev/./urandom" >> /opt/java/openjdk/lib/security/java.security

CMD java -Dfile.encoding="UTF-8"  -jar /deployments/quarkus-run.jar
