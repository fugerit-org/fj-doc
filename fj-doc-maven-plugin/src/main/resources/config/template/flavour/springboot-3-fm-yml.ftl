<#import 'flavour-macro.ftl' as fhm>
---
flavour: ${context.flavour}
process:
  - from: flavour/${context.flavour}/pom.ftl
    to: ${context.projectFolder}/pom.xml
  - from: flavour/${context.flavour}/README.ftl
    to: ${context.projectFolder}/README.md
  - from: flavour/${context.flavour}/gitignore.ftl
    to: ${context.projectFolder}/.gitignore
  - from: flavour/${context.flavour}/SpringbootApplication.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/${context.artifactIdAsClassName}Application.java
  - from: flavour/${context.flavour}/DocController.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/DocController.java
  - from: flavour/${context.flavour}/SpringbootApplication.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/${context.artifactIdAsClassName}Application.java
  - from: flavour/${context.flavour}/SpringbootApplicationTests.ftl
    to: ${context.projectFolder}/src/test/java/test/<@fhm.toProjectPackageFolder context=context/>/${context.artifactIdAsClassName}ApplicationTests.java
  - from: flavour/${context.flavour}/application.ftl
    to: ${context.projectFolder}/src/main/resources/application.yaml