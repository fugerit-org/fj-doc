<#macro handleElement current><@handleElementWorker current=current isInner=false/></#macro>

<#macro handleElementWorker current isInner>
    <#assign elementType="${current.class.simpleName}"/>
    <#if elementType = 'DocPhrase'>
        <@handlePhrase current=current/>
    <#elseif elementType = 'DocNbsp'>&nbsp;
    <#elseif elementType = 'DocBr'>
    <#elseif elementType = 'DocPara'>
        <#if isInner><@handlePhrase current=current/><#else><@handlePara current=current/></#if>
    <#elseif elementType = 'DocTable'>
        <@handleTable docTable=current/>
    <#elseif elementType = 'DocImage'>
        <@handleImage docImage=current/>
    <#elseif elementType = 'DocList'>
        <@handleList docList=current level=1/>
    <#elseif elementType = 'DocPageBreak'>

<<<

    <#elseif elementType = 'DocContainer'>
        <#list current.elementList as currentChild><@handleElement current=currentChild/></#list>
    <#else>
        <span>Element type non implemented yet : ${elementType}</span>
    </#if>
</#macro>

<#macro handlePara current><@handleHeadings headLevel=current.headLevel/><@handleTextStyle styleValue=current.style text=current.text/> +
</#macro>

<#macro handlePhrase current><#if (current.link)?? >link:${current.link}[<@handleTextStyle styleValue=current.style text=current.text/>]<#else><@handleTextStyle styleValue=current.style text=current.text/></#if></#macro>

<#macro handleRowList docTable rowList>
<#list rowList as row>
<#list row.elementList as cell><#if cell.columnSpan &gt; 1>${cell.columnSpan}+</#if><#if cell.rowSpan &gt; 1>.${cell.rowSpan}+</#if>|<#list cell.elementList as cellElement><@handleElement current=cellElement/></#list></#list>
</#list>
</#macro>

<#macro handleColWidth docTableCols><#list docTableCols as current><#if current?index != 0>,</#if>${current}</#list></#macro>

<#macro handleTable docTable>
<#assign docTableUtil=docTable.util/>
<#if (docTableUtil.strictHeader)>

[%header,cols="<@handleColWidth docTableCols=docTable.colWithds/>"]
<#else>
[cols="<@handleColWidth docTableCols=docTable.colWithds/>"]
</#if>
|===
<@handleRowList docTable=docTable rowList=docTable.elementList/>
|===
</#macro>

<#macro handleImage docImage>
<#if (docImage.scaling)??><#assign imageScaling=",${docImage.scaling}%,${docImage.scaling}%'"/><#else><#assign imageScaling=""/></#if>
<#if (docImage.align)??></#if>
image::data:image/${docImage.resolvedType};base64, ${docImage.resolvedBase64}[${docImage.alt!'NA'}${imageScaling}]
</#macro>

<#macro handleList docList level>
<#list docList.elementList as li>
<#if docList.ordered><#list 1..level as x>.</#list><#else><#list 1..level as x>*</#list></#if><#list li.elementList as element><#if element.class.simpleName='DocList'>
<@handleList docList=element level=level+1/><#else> <@handleElementWorker current=element isInner=true/></#if></#list>
</#list>
</#macro>

<#macro handleTextStyle styleValue text><#if styleValue = 2>*${text}*<#elseif styleValue = 3>__${text}__<#elseif styleValue = 4>_${text}_<#elseif styleValue = 5>*_${text}_*<#else>${text}</#if></#macro>

<#macro handleHeadings headLevel><#if headLevel != 0><#list 0..headLevel as x>=</#list> </#if></#macro>