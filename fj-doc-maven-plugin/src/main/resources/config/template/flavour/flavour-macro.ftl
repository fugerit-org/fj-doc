<#macro toProjectPackageFolder context>${context.evaluateBasePackage?replace(".","/")}</#macro>

<#macro toProjectPackage context>${context.evaluateBasePackage}</#macro>

<#macro createDocumentProcess context exceptionType>
    // creates the doc helper
    DocHelper docHelper = new DocHelper();
    <@createDocumentProcessNoHelper context=context exceptionType=exceptionType/>
</#macro>

<#macro createDocumentProcessNoHelper context exceptionType>
    byte[] processDocument(String handlerId) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // create custom data for the fremarker template 'document.ftl'
            List<People> listPeople = Arrays.asList(new People("Luthien", "Tinuviel", "Queen"), new People("Thorin", "Oakshield", "King"));
            <#if context.modules?seq_contains("fj-doc-base-json")>// json source supported, if you want to try it, use the chainId "document-json"</#if>
            <#if context.modules?seq_contains("fj-doc-base-yaml")>// yaml source supported, if you want to try it, use the chainId "document-yaml"</#if>
            <#if context.modules?seq_contains("fj-doc-base-kotlin")>// kotlin source supported, if you want to try it,
            // use the chainId "document-kotlin" for FreeMarker + Kotlin
            // or "document-kotlin-pure" for pure Kotlin</#if>
            <#if context.addLombok >
            log.info( "processDocument handlerId : {}", handlerId );
            </#if>
            String chainId = "document";
            // output generation
            this.docHelper.getDocProcessConfig().fullProcess(chainId, DocProcessContext.newContext("listPeople", listPeople), handlerId, baos);
            // return the output
            return baos.toByteArray();
        } catch (Exception e) {
            String message = String.format("Error processing %s, error:%s", handlerId, e);
            log.error(message, e);
            throw new ${exceptionType}(message, e);
        }
    }
</#macro>

<#macro createPathMethod context outputMime outputExtension outputDescription>
    public byte[] ${outputDescription?lower_case}Example() {
        return processDocument(DocConfig.TYPE_${outputExtension?upper_case});
    }
</#macro>

<#macro createQuarkusPathPrefix context outputMime outputExtension outputDescription pathPrefix>
    @APIResponse(responseCode = "200", description = "The ${outputDescription} document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tag( name = "document" )
    @Tag( name = "${outputDescription?lower_case}" )
    @Operation( operationId = "${outputDescription}Example", summary = "Example ${outputDescription} generation",
        description =  "Generates an example ${outputDescription} document using Fugerit Venus Doc handler" )
    @GET
    @Produces("${outputMime}")
    @Path("${pathPrefix}/example.${outputExtension}")
    <@createPathMethod context=context outputMime=outputMime outputExtension=outputExtension outputDescription=outputDescription/>
</#macro>

<#macro createQuarkusPath context outputMime outputExtension outputDescription><@createQuarkusPathPrefix context=context outputMime=outputMime outputExtension=outputExtension outputDescription=outputDescription pathPrefix=''/></#macro>

<#-- using https://javadoc.io/doc/io.swagger.core.v3/swagger-annotations/latest/index.html -->
<#macro createOpenAPIDoc context outputMime outputExtension outputDescription>
    @Operation( method = "GET", operationId = "${outputDescription}Example", tags = { "document", "${outputDescription?lower_case}" },
        summary = "Example ${outputDescription} generation",
        description = "Generates an example ${outputDescription} document using Fugerit Venus Doc handler" )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The ${outputDescription} document content", content = { @Content(mediaType = "${outputMime}") } ),
        @ApiResponse(responseCode = "500", description = "In case of an unexpected error" )
    })
</#macro>

<#macro createMicronautPathPrefix context outputMime outputExtension outputDescription pathPrefix>
    <@createOpenAPIDoc context=context outputMime=outputMime outputExtension=outputExtension outputDescription=outputDescription/>
    @Get(uri="${pathPrefix}/example.${outputExtension}", produces="${outputMime}")
    <@createPathMethod context=context outputMime=outputMime outputExtension=outputExtension outputDescription=outputDescription/>
</#macro>

<#macro createMicronautPath context outputMime outputExtension outputDescription><@createMicronautPathPrefix context=context outputMime=outputMime outputExtension=outputExtension outputDescription=outputDescription pathPrefix=''/></#macro>

<#macro createSpringBootPathPrefix context outputMime outputExtension outputDescription pathPrefix>
    <@createOpenAPIDoc context=context outputMime=outputMime outputExtension=outputExtension outputDescription=outputDescription/>
    @GetMapping(value = "${pathPrefix}/example.${outputExtension}", produces = "${outputMime}" )
    <@createPathMethod context=context outputMime=outputMime outputExtension=outputExtension outputDescription=outputDescription/>
</#macro>

<#macro createSpringBootPath context outputMime outputExtension outputDescription><@createSpringBootPathPrefix context=context outputMime=outputMime outputExtension=outputExtension outputDescription=outputDescription pathPrefix=''/></#macro>