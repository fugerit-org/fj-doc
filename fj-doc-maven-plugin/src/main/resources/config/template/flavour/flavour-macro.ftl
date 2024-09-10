<#macro toProjectPackageFolder context>${context.groupId?replace(".","/")}/${context.artifactId?replace("-","")}</#macro>

<#macro toProjectPackage context>${context.groupId}.${context.artifactId?replace("-","")}</#macro>

<#macro createDocumentProcess context exceptionType>
    byte[] processDocument(String handlerId) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // creates the doc helper
            DocHelper docHelper = new DocHelper();
            // create custom data for the fremarker template 'document.ftl'
            List<People> listPeople = Arrays.asList(new People("Luthien", "Tinuviel", "Queen"), new People("Thorin", "Oakshield", "King"));
            // output generation
            docHelper.getDocProcessConfig().fullProcess("document", DocProcessContext.newContext("listPeople", listPeople), handlerId, baos);
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

<#macro createQuarkusPath context outputMime outputExtension outputDescription>
    @APIResponse(responseCode = "200", description = "The ${outputDescription} document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tags( { @Tag( name = "document" ), @Tag( name = "${outputDescription?lower_case}" ) } )
    @Operation( operationId = "${outputDescription}Example", summary =  "Generated an example ${outputDescription} document using Fugerit Venus Doc handler" )
    @GET
    @Produces("${outputMime}")
    @Path("/example.${outputExtension}")
    <@createPathMethod context=context outputMime=outputMime outputExtension=outputExtension outputDescription=outputDescription/>
</#macro>

<#macro createMicronautPath context outputMime outputExtension outputDescription>
    @Get(uri="/example.${outputExtension}", produces="${outputMime}")
    <@createPathMethod context=context outputMime=outputMime outputExtension=outputExtension outputDescription=outputDescription/>
</#macro>

<#macro createSpringBootPath context outputMime outputExtension outputDescription>
    @GetMapping(value = "/example.${outputExtension}", produces = "${outputMime}" )
    <@createPathMethod context=context outputMime=outputMime outputExtension=outputExtension outputDescription=outputDescription/>
</#macro>