<#import "macro/ascidoc-element.ftl" as doc_element>
<#if (docBase.infoDocTitle)??>= ${docBase.infoDocTitle}</#if>
<#if (docBase.infoDocAuthor)??>${docBase.infoDocAuthor}</#if>
<#if (docBase.infoDocSubject)??>:description: ${docBase.infoDocSubject}</#if>
<#if (docBase.infoDocLanguage)??>:lang: ${docBase.infoDocLanguage}</#if>
<#if (docBase.infoDocProducer)??>:generator: ${docBase.infoDocProducer}<#else>:generator: Venus Fugerit Doc over Apache FreeMarker</#if>
<#if (docBase.infoDocCreator)??>:created-by: ${docBase.infoDocCreator}<#else>:created-by: Venus Fugerit Doc (https://venusdocs.fugerit.org)</#if>

<#list docBase.docBody.elementList as item>
	<@doc_element.handleElement current=item/>
</#list>