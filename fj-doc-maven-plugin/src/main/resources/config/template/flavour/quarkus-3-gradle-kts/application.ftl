greeting:
  message: "hello"

quarkus:
  native:
    # if needed add -H:+UnlockExperimentalVMOptions
    additional-build-args: -H:IncludeResources=${context.artifactId}/fm-doc-process-config.xml,\
      -H:IncludeResources=${context.artifactId}/template/document.ftl