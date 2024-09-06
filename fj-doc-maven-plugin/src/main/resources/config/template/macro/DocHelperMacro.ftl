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
            List<People> listPeople = Arrays.asList( new ${className}.People( "Luthien", "Tinuviel", "Queen" ), new ${className}.People( "Thorin", "Oakshield", "King" ) );
            <#if context.preVersion862 >
            // find the handler for the output :
            DocTypeHandler docTypeHandler = docHelper.getDocProcessConfig().getFacade().findHandler(DocConfig.TYPE_MD);
            // output generation
            docHelper.getDocProcessConfig().fullProcess( "document", DocProcessContext.newContext( "listPeople", listPeople ), docTypeHandler, DocOutput.newOutput( baos ) );
            <#else>
            // handler id
            String handlerId = DocConfig.TYPE_MD;
            // output generation
            docHelper.getDocProcessConfig().fullProcess( "document", DocProcessContext.newContext( "listPeople", listPeople ), handlerId, baos );
            </#if>
            // print the output
            <#if context.addLombok >log.info( "html output : \n{}", new String( baos.toByteArray(), StandardCharsets.UTF_8 ) );<#else>System.out.println( "html output : \n"+ new String( baos.toByteArray(), StandardCharsets.UTF_8 ) );</#if>
            <#if junit >Assertions.assertNotEquals( 0, baos.size() );</#if>
        } catch (Exception e) {
            <#if context.addLombok >log.error( String.format( "Error : %s", e.toString() ), e );<#else>e.printStackTrace();</#if>
        }
</#macro>

<#macro createExampleModel context>
    /*
     * Class used to wrap data to be rendered in the document template
     */
    <#if context.addLombok >@Getter
    @AllArgsConstructor
    </#if>
    public static class People {

        private String name;

        private String surname;

        private String title;
        <#if !context.addLombok >
        public People(String name, String surname, String title) {
            this.name = name;
            this.surname = surname;
            this.title = title;
        }

        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        public String getTitle() {
            return title;
        }
        </#if>
    }
</#macro>