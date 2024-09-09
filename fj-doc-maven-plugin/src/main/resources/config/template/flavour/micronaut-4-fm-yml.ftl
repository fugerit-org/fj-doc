<#import 'flavour-macro.ftl' as fhm>
---
flavour: micronaut-4
process:
  - from: flavour/micronaut-4/pom.ftl
    to: ${context.projectFolder}/pom.xml
  - from: flavour/micronaut-4/README.ftl
    to: ${context.projectFolder}/README.md
  - from: flavour/micronaut-4/gitignore.ftl
    to: ${context.projectFolder}/.gitignore
  - from: flavour/micronaut-4/MicronautController.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/${context.artifactIdAsClassName}Controller.java
  - from: flavour/micronaut-4/DocController.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/DocController.java
  - from: flavour/micronaut-4/Application.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/Application.java
  - from: flavour/micronaut-4/MicronautTest.ftl
    to: ${context.projectFolder}/src/test/java/test/<@fhm.toProjectPackageFolder context=context/>/${context.artifactIdAsClassName}Test.java
  - from: flavour/micronaut-4/application_yml.ftl
    to: ${context.projectFolder}/src/main/resources/application.yaml