<#import 'flavour-macro.ftl' as fhm>
---
flavour: openliberty
process:
  - from: flavour/openliberty/pom.ftl
    to: ${context.projectFolder}/pom.xml
  - from: flavour/openliberty/README.ftl
    to: ${context.projectFolder}/README.md
  - from: flavour/openliberty/gitignore.ftl
    to: ${context.projectFolder}/.gitignore
  - from: flavour/openliberty/server.ftl
    to: ${context.projectFolder}/src/main/liberty/config/server.xml
  - from: flavour/openliberty/web.ftl
    to: ${context.projectFolder}/src/main/webapp/WEB-INF/web.xml
  - from: flavour/openliberty/RestApplication.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/RestApplication.java
  - from: flavour/openliberty/GreetingResource.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/GreetingResource.java
  - from: flavour/openliberty/DocResource.ftl
    to: ${context.projectFolder}/src/main/java/<@fhm.toProjectPackageFolder context=context/>/DocResource.java