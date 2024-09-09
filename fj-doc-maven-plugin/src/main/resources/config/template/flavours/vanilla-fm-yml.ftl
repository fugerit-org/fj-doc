<#import 'flavour-macro.ftl' as fhm>
---
flavour: vanilla
process:
  - from: flavours/quarkus-3/pom.ftl
    to: ${context.projectFolder}/pom.xml
  - from: flavours/quarkus-3/README.ftl
    to: ${context.projectFolder}/README.md
  - from: flavours/quarkus-3/gitignore.ftl
    to: ${context.projectFolder}/.gitignore