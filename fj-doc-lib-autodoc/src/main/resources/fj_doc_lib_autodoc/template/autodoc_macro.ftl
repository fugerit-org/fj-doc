<#macro handleMultiElementOccurs multiElement>
	<#if multiElement.content.minOccurs?? || multiElement.content.maxOccurs??>[${multiElement.content.minOccurs}-${multiElement.content.maxOccurs}]</#if>
</#macro>

<#macro handleMultiElement multiElement separator>
	<#if multiElement.xsdElements??><phrase>(</phrase><#list multiElement.xsdElements as element><#if element?index != 0><phrase>${separator}</phrase></#if><phrase link="#${element.rawName}">${element.rawName}</phrase></#list><phrase>)<@handleMultiElementOccurs multiElement=multiElement/></phrase></#if>
	<#if multiElement.autodocChoices??><#list multiElement.autodocChoices as current><@handleMultiElement multiElement=current separator=' | '/></#list></#if>
	<#if multiElement.autodocSequence??><#list multiElement.autodocSequence as current><@handleMultiElement multiElement=current separator=' , '/></#list></#if>
</#macro>

<#macro handleTypeChildren autodocType>
	<#if autodocType.choice??><@handleMultiElement multiElement=autodocType.choice separator=' | '/></#if>
	<#if autodocType.sequence??><@handleMultiElement multiElement=autodocType.sequence separator=' , '/></#if>
</#macro>