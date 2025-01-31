<#import 'flavour-macro.ftl' as fhm>
---
flavour: ${context.flavour}
process:
  - from: flavour/${context.flavour}/settings.gradle.ftl
    to: ${context.projectFolder}/settings.gradle.kts
  - from: flavour/${context.flavour}/gradle.ftl
    to: ${context.projectFolder}/gradle.properties
  - from: flavour/${context.flavour}/build.gradle.ftl
    to: ${context.projectFolder}/build.gradle.kts
  - from: flavour/${context.flavour}/README.ftl
    to: ${context.projectFolder}/README.md
  - from: flavour/${context.flavour}/gitignore.ftl
    to: ${context.projectFolder}/.gitignore
  - from: flavour/${context.flavour}/GreetingConfig.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/GreetingConfig.java
  - from: flavour/${context.flavour}/GreetingResource.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/GreetingResource.java
  - from: flavour/${context.flavour}/GreetingResourceTest.ftl
    to: ${context.projectFolder}/src/test/java/test/<@fhm.toProjectPackageFolder context=context/>/GreetingResourceTest.java
  - from: flavour/${context.flavour}/GreetingResourceIT.ftl
    to: ${context.projectFolder}/src/test/java/test/<@fhm.toProjectPackageFolder context=context/>/GreetingResourceIT.java
  - from: flavour/${context.flavour}/DocResourceIT.ftl
    to: ${context.projectFolder}/src/test/java/test/<@fhm.toProjectPackageFolder context=context/>/DocResourceIT.java
  - from: flavour/${context.flavour}/DocResource.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/DocResource.java
  - from: flavour/${context.flavour}/application.ftl
    to: ${context.projectFolder}/src/main/resources/application.yml
  - from: flavour/${context.flavour}/AppInit.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/AppInit.java
  - from: flavour/${context.flavour}/DocResourceTest.ftl
    to: ${context.projectFolder}/src/test/java/test/<@fhm.toProjectPackageFolder context=context/>/DocResourceTest.java
  - from: flavour/${context.flavour}/DocHelper.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/DocHelper.java