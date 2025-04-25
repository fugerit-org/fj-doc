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