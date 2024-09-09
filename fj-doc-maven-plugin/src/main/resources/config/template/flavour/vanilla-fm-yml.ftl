<#import 'flavour-macro.ftl' as fhm>
---
flavour: vanilla
process:
  - from: flavour/quarkus-3/pom.ftl
    to: ${context.projectFolder}/pom.xml
  - from: flavour/quarkus-3/README.ftl
    to: ${context.projectFolder}/README.md
  - from: flavour/quarkus-3/gitignore.ftl
    to: ${context.projectFolder}/.gitignore