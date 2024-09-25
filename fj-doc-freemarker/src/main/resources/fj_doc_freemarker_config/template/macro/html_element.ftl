<#global defaultBorderColor='black'>
<#global defaultFontSize='10'>

<#if escapeTextAsHtml!false>
<#import "feature-escape-html.ftl" as escape>
<#else>
<#import "feature-escape-none.ftl" as escape>
</#if>

<#macro handleElement current>
	<#assign elementType="${current.class.simpleName}"/>
	<#if elementType = 'DocPhrase'>
		<@handlePhrase current=current/>
	<#elseif elementType = 'DocNbsp'>
		&nbsp;
	<#elseif elementType = 'DocBr'>
		<br/>		
	<#elseif elementType = 'DocPara'>
		<@handlePara current=current/>
	<#elseif elementType = 'DocTable'>
		<@handleTable docTable=current/>
	<#elseif elementType = 'DocImage'>
		<@handleImage docImage=current/>					
	<#elseif elementType = 'DocList'>
		<@handleList docList=current/>	
	<#elseif elementType = 'DocPageBreak'>
</div>
<div>
	<#elseif elementType = 'DocContainer'>
		<#list current.elementList as currentChild><@handleElement current=currentChild/></#list>
	<#else>
		<span>Element type non implemented yet : ${elementType}</span>
	</#if>
</#macro>

<#macro handlePhrase current>
	<#if (current.link)??>
		<a <@handleId element=current/><@handleStyleOnly styleValue=current.style/> href="${current.link}"><@escape.printText e=current/></a>
	<#elseif (current.anchor)??>
		<span <@handleId element=current/><@handleStyleOnly styleValue=current.style/>><@escape.printText e=current/></span>
	<#else>
		<span <@handleId element=current/><@handleStyleOnly styleValue=current.style/>><@escape.printText e=current/></span>
	</#if>
</#macro>

<#macro handlePara current>
	<#if current.headLevel == 0>
		<p<@handleId element=current/><@handleStyleComplete element=current styleValue=current.originalStyle alignValue=current.align dc=current/>><@escape.printText e=current/><#list current.elementList as currentChild><@handleElement current=currentChild/></#list></p>
	<#else>
		<h${current.headLevel} <@handleId element=current/><@handleStyleComplete element=current styleValue=current.style alignValue=current.align dc=current/>><@escape.printText e=current/><#list current.elementList as currentChild><@handleElement current=currentChild/></#list></h${current.headLevel}>
	</#if>
</#macro>

<#macro handleImage docImage>
	<#if (docImage.scaling)??>
		<#assign imageScaling="height='${docImage.scaling}%' width='${docImage.scaling}%'"/>
	<#else>
		<#assign imageScaling=""/>
	</#if>
	<#if (docImage.align)??>
		<#if docImage.align = 2>
			<#assign imageAlign="style='display: block; margin-left: auto; margin-right: auto;'"/>
		</#if>
	</#if>	
	<img <@handleId element=docImage/> ${imageAlign!''} <#if (docImage.alt)??> alt="${docImage.alt}" </#if> ${imageScaling} src="data:image/png;base64, ${docImage.resolvedBase64}" />
</#macro>

<#macro handleList docList>
	<#if docList.elementList?size gt 0>
	<${docList.htmlType} <@handleId element=docList/>>
		<#list docList.elementList as li>
			<#if (li?counter > 0)>
				<li <@handleId element=li/><#if li.contentList>style="list-style-type: none;"<#elseif docList.listType = 'oll'>style="list-style-type: lower-alpha;"<#elseif docList.listType = 'ulm'>style="list-style-type: square;"<#elseif docList.listType = 'uld'>style="list-style-type: circle;"</#if>> <#list li.elementList as element><@handleElement current=element/></#list></li>
			</#if>			
			<#assign prevLi=li/>
		</#list>	
	</${docList.htmlType}>
	</#if>
</#macro>

<#macro handleRowList docTable rowList cellType>
	<#list rowList as row>	
		<tr<@handleId element=row/>>
			<#list row.elementList as cell><#assign defCellId>cell_${row?index}_${cell?index}</#assign>
				<${cellType}<@handleIdDef element=cell defId=defCellId/> style="<#if (cell.backColor??)> background: ${cell.backColor};</#if> width: ${docTable.colWithds[cell?index]}%;<@handleAlign alignValue=cell.align/><@handleBorders docBorders=cell.docBorders/><@addCssValue name='padding' value=docTable.padding def=0 unit='px'/><@addCssValue name='margin' value=docTable.spacing def=0 unit='px'/>"<@handleColspan colspanValue=cell.columnSpan/><@handleRowspan rowspanValue=cell.rowSpan/>>
					<#list cell.elementList as cellElement>
					<@handleElement current=cellElement/>
					</#list>
				</${cellType}>
			</#list>
		</tr>				
	</#list>
</#macro>

<#macro handleRowInline docTable row docTableUtil>
	<#list row.elementList as cell>
		<div style="width: ${docTable.colWithds[cell?index]}%; float: left;">
			<#if (cell.elementList?size > 0)>
				<#list cell.elementList as cellElement>
					<@handleElement current=cellElement/>
				</#list>
			</#if>
		</div>
	</#list>
</#macro>

<#macro handleTable docTable>
	<#assign docTableUtil=docTable.util/>
	<#if docTable.renderMode == 'inline'>
		<#list docTable.elementList as row>
			<@handleRowInline docTable=docTable row=row docTableUtil=docTableUtil/>	
		</#list>
	<#else>	
		<table style='<@handleTableSpacing dc=docTable/> width: ${docTable.width}%'<#if (docTable.alt)??> aria-describedby="${docTable.alt}"</#if>>
			<#if (docTableUtil.strictHeader)>
				<thead>
				<@handleRowList docTable=docTable rowList=docTableUtil.headerRows cellType='th'/>
				</thead>
				<tbody>
				<@handleRowList docTable=docTable rowList=docTableUtil.dataRows cellType='td'/>
				</tbody>
			<#else>
				<@handleRowList docTable=docTable rowList=docTable.elementList cellType='td'/>
			</#if>
		</table>
	</#if>
</#macro>

<#macro handleBorder mode size color><#if size != 0><#if size = -1><#assign calcSize="1"/><#else><#assign calcSize="${size}"/></#if> ${mode}: ${calcSize}px solid ${color};</#if></#macro>

<#macro handleBorders docBorders>
	<#if (docBorders.borderColorTop)??><#assign borderColorTop="${docBorders.borderColorTop}"/><#else><#assign borderColorTop="${defaultBorderColor}"/></#if>
	<#if (docBorders.borderColorBottom)??><#assign borderColorBottom="${docBorders.borderColorBottom}"/><#else><#assign borderColorBottom="${defaultBorderColor}"/></#if>
	<#if (docBorders.borderColorLeft)??><#assign borderColorLeft="${docBorders.borderColorLeft}"/><#else><#assign borderColorLeft="${defaultBorderColor}"/></#if>
	<#if (docBorders.borderColorRight)??><#assign borderColorRight="${docBorders.borderColorRight}"/><#else><#assign borderColorRight="${defaultBorderColor}"/></#if>
	<@handleBorder mode='border-top' size=docBorders.borderWidthTop color=borderColorTop/>
	<@handleBorder mode='border-bottom' size=docBorders.borderWidthBottom color=borderColorBottom/>
	<@handleBorder mode='border-left' size=docBorders.borderWidthLeft color=borderColorLeft/>
	<@handleBorder mode='border-right' size=docBorders.borderWidthRight color=borderColorRight/>
</#macro>

<#macro handleIdDef element defId><#if (element.id)??> id="${element.id}" <#else> id="${defId}" </#if></#macro>

<#macro handleId element><#if (element.id)??> id="${element.id}"<#elseif (element.anchor)??> id="${element.anchor}"</#if></#macro>

<#macro handleRowspan rowspanValue><#if (rowspanValue != 1)> rowspan="${rowspanValue}"</#if></#macro>

<#macro handleColspan colspanValue><#if (colspanValue != 1)> colspan="${colspanValue}"</#if></#macro>

<#-- 
padding-top : space-before
padding-bottom : 10 * leading + space-bottom
padding-left : 10 * text-indent + space-left
padding-right : space-right
white-space-collapse -> false : white-space-collapse: preserve; white-space: pre-wrap;
-->
<#macro handlePadding amt dir><#if (amt > 0)> padding-${dir}: ${amt}px;</#if></#macro>
<#macro handleSpacing dc><@handlePadding amt=dc.spaceBefore!0 dir='top'/><@handlePadding amt=((dc.leading!0)*10 + dc.spaceAfter!0) dir='bottom'/><@handlePadding amt=((dc.textIndent!0)*10 + dc.spaceLeft!0) dir='left'/><@handlePadding amt=dc.spaceRight!0 dir='right'/><#if dc.notWhiteSpaceCollapse> white-space-collapse: preserve; white-space: pre-wrap;</#if></#macro>
<#macro handleTableSpacing dc><@handlePadding amt=dc.spaceBefore!0 dir='top'/><@handlePadding amt=((dc.leading!0)*10 + dc.spaceAfter!0) dir='bottom'/></#macro>

<#macro handleAlign alignValue><#if alignValue = 1> text-align: left;<#elseif alignValue = 2> text-align: center;<#elseif alignValue = 3> text-align: right;<#elseif alignValue = 8 || alignValue = 9> text-align: justify;</#if></#macro>

<#macro handleFont element> <#if (element.fontName??)> font-family: '${element.fontName}'; </#if><#if (element.size)?? && (element.size != -1)> font-size: '${element.size}'; </#if><#if (element.backColor??)> background-color: ${element.backColor}; </#if><#if (element.foreColor??)> color: ${element.foreColor}; </#if></#macro>

<#macro handleStyle styleValue><#if styleValue = 2> font-weight: bold;<#elseif styleValue = 3> text-decoration: underline;<#elseif styleValue = 4> font-style: italic;<#elseif styleValue = 5> font-weight: bold; font-style: italic;</#if></#macro>

<#macro handleStyleOnly styleValue><#assign cStyle><@handleStyle styleValue=styleValue/></#assign><@handleStylePrint cStyle=cStyle/></#macro>

<#macro handleStyleComplete element styleValue alignValue dc><#assign cStyle><@handleFont element=element/> <@handleStyle styleValue=styleValue/></#assign><#assign cStyle>${cStyle} <@handleAlign alignValue=alignValue/></#assign><#assign cStyle>${cStyle} <@handleSpacing dc=dc/></#assign><@handleStylePrint cStyle=cStyle/></#macro>

<#macro handleStylePrint cStyle><#assign tStyle=cStyle?trim><#if tStyle?has_content> style="${tStyle}"</#if></#macro>

<#--
	macro : addCssValue
	name : name of the css property to add
	value : value of the css property to add
	def : comparisong value (if the value equals the default, the property will be skipped)
	unit : to add to the value (if any)
-->
<#macro addCssValue name value def unit><#if (value != def)> ${name}: ${value}${unit};</#if></#macro>
