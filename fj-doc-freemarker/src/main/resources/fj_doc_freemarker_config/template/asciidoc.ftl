<#import "macro/ascidoc-element.ftl" as doc_element>
<#if (docBase.infoDocTitle)??>= ${docBase.infoDocTitle}</#if>
<#if (docBase.infoDocAuthor)??>${docBase.infoDocAuthor}</#if>
<#if (docBase.infoDocSubject)??>:description: ${docBase.infoDocSubject}</#if>
<#if (docBase.infoDocSubject)??>:lang: ${docBase.infoDocLanguage}</#if>

<#list docBase.docBody.elementList as item>
	<@doc_element.handleElement current=item/>
</#list>