<#import 'flavour-macro.ftl' as fhm>
---
flavour: springboot-3
process:
  - from: flavour/springboot-3/pom.ftl
    to: ${context.projectFolder}/pom.xml
  - from: flavour/springboot-3/README.ftl
    to: ${context.projectFolder}/README.md
  - from: flavour/springboot-3/gitignore.ftl
    to: ${context.projectFolder}/.gitignore
  - from: flavour/springboot-3/SpringbootApplication.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/${context.artifactIdAsClassName}Application.java
  - from: flavour/springboot-3/DocController.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/DocController.java
  - from: flavour/springboot-3/SpringbootApplication.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/${context.artifactIdAsClassName}Application.java
  - from: flavour/springboot-3/SpringbootApplicationTests.ftl
    to: ${context.projectFolder}/src/test/java/test/<@fhm.toProjectPackageFolder context=context/>/${context.artifactIdAsClassName}ApplicationTests.java
  - from: flavour/springboot-3/application.ftl
    to: ${context.projectFolder}/src/main/resources/application.yaml