<#import 'flavour-macro.ftl' as fhm>
---
flavour: quarkus-2
process:
  - from: flavour/quarkus-2/pom.ftl
    to: ${context.projectFolder}/pom.xml
  - from: flavour/quarkus-2/README.ftl
    to: ${context.projectFolder}/README.md
  - from: flavour/quarkus-2/gitignore.ftl
    to: ${context.projectFolder}/.gitignore
  - from: flavour/quarkus-2/GreetingConfig.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/GreetingConfig.java
  - from: flavour/quarkus-2/GreetingResource.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/GreetingResource.java
  - from: flavour/quarkus-2/GreetingResourceTest.ftl
    to: ${context.projectFolder}/src/test/java/test/<@fhm.toProjectPackageFolder context=context/>/GreetingResourceTest.java
  - from: flavour/quarkus-2/GreetingResourceIT.ftl
    to: ${context.projectFolder}/src/test/java/test/<@fhm.toProjectPackageFolder context=context/>/GreetingResourceIT.java
  - from: flavour/quarkus-2/DocResource.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/DocResource.java
  - from: flavour/quarkus-2/DocResourceTest.ftl
    to: ${context.projectFolder}/src/test/java/test/<@fhm.toProjectPackageFolder context=context/>/DocResourceTest.java