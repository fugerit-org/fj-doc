<#macro createExampleJavadoc context junit>
/**
 * This is a basic example of Fugerit Venus Doc usage,
<#if junit > * running this junit will :<#else> * running this main program will :</#if>
 * - creates data to be used in document model
 * - renders the 'document.ftl' template
 * - print the result in markdown format
 *
 * For further documentation :
 * https://github.com/fugerit-org/fj-doc
 *
 * NOTE: This is a 'Hello World' style example, adapt it to your scenario, especially :<#if !context.addLombok >
 *  - remove system out and system err with your logging system</#if>
 *  - change the doc handler and the output mode (here a ByteArrayOutputStream buffer is used)
 */
</#macro>

<#macro createExampleBody context junit className>
        try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
            // creates the doc helper
            DocHelper docHelper = new DocHelper();
            // create custom data for the fremarker template 'document.ftl'
            List<People> listPeople = Arrays.asList( new People( "Luthien", "Tinuviel", "Queen" ), new People( "Thorin", "Oakshield", "King" ) );
            <#if context.modules?seq_contains("fj-doc-base-json")>// json source supported, if you want to try it, use the chainId "document-json"</#if>
            <#if context.modules?seq_contains("fj-doc-base-yaml")>// yaml source supported, if you want to try it, use the chainId "document-yaml"</#if>
            String chainId = "document";
            <#if context.preVersion862 >
            // find the handler for the output :
            DocTypeHandler docTypeHandler = docHelper.getDocProcessConfig().getFacade().findHandler(DocConfig.TYPE_MD);
            // output generation
            docHelper.getDocProcessConfig().fullProcess( chainId, DocProcessContext.newContext( "listPeople", listPeople ), docTypeHandler, DocOutput.newOutput( baos ) );
            <#else>
            // handler id
            String handlerId = DocConfig.TYPE_MD;
            // output generation
            docHelper.getDocProcessConfig().fullProcess( chainId, DocProcessContext.newContext( "listPeople", listPeople ), handlerId, baos );
            </#if>
            // print the output
            <#if context.addLombok >log.info( "{} output : \n{}", handlerId, new String( baos.toByteArray(), StandardCharsets.UTF_8 ) );<#else>System.out.println( handlerId+" output : \n"+ new String( baos.toByteArray(), StandardCharsets.UTF_8 ) );</#if>
            <#if junit >Assertions.assertNotEquals( 0, baos.size() );</#if>
        }
</#macro>