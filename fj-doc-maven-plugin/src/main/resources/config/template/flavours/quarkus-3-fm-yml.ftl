<#import 'flavour-macro.ftl' as fhm>
---
flavour: quarkus-3
process:
  - from: flavours/quarkus-3/pom.ftl
    to: ${context.projectFolder}/pom.xml
  - from: flavours/quarkus-3/README.ftl
    to: ${context.projectFolder}/README.md
  - from: flavours/quarkus-3/gitignore.ftl
    to: ${context.projectFolder}/.gitignore
  - from: flavours/quarkus-3/GreetingConfig.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/GreetingConfig.java
  - from: flavours/quarkus-3/GreetingResource.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/GreetingResource.java
  - from: flavours/quarkus-3/GreetingResourceTest.ftl
    to: ${context.projectFolder}/src/test/java/test/<@fhm.toProjectPackageFolder context=context/>/GreetingResourceTest.java
  - from: flavours/quarkus-3/GreetingResourceIT.ftl
    to: ${context.projectFolder}/src/test/java/test/<@fhm.toProjectPackageFolder context=context/>/GreetingResourceIT.java
  - from: flavours/quarkus-3/DocResource.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/DocResource.java
  - from: flavours/quarkus-3/DocResourceTest.ftl
    to: ${context.projectFolder}/src/test/java/test/<@fhm.toProjectPackageFolder context=context/>/DocResourceTest.java