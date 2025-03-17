greeting.message = "hello"

# if needed add -H:+UnlockExperimentalVMOptions
quarkus.native.additional-build-args = -H:IncludeResources=${context.artifactId}/fm-doc-process-config.xml,\
-H:IncludeResources=${context.artifactId}/template/document.ftl
