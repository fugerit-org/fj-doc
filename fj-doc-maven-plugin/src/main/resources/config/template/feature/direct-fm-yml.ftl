---
flavour: ${featureId}
process:
  - from: feature/${featureId}/venus-direct-config.ftl
    to: ${context.projectDir}/src/main/resources/venus-direct-config/venus-direct-config.yaml
