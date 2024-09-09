micronaut:
  application:
    name: fugerit-demo-micronaut
  router:
    static-resources:
      swagger:
        paths: classpath:META-INF/swagger
        mapping: /swagger/**
      openapi-explorer:
        paths: classpath:META-INF/swagger/views/openapi-explorer
        mapping: /openapi-explorer/**
