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
  - from: flavour/${context.flavour}/server.ftl
    to: ${context.projectFolder}/src/main/liberty/config/server.xml
  - from: flavour/${context.flavour}/web.ftl
    to: ${context.projectFolder}/src/main/webapp/WEB-INF/web.xml
  - from: flavour/${context.flavour}/RestApplication.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/RestApplication.java
  - from: flavour/${context.flavour}/GreetingResource.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/GreetingResource.java
  - from: flavour/${context.flavour}/DocResource.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/DocResource.java