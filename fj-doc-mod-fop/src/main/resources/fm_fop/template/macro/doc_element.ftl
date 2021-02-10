<#global defaultBorderColor='black'>
<#global defaultFontSize='10'>

<#macro handleElementList elements>
	<#list elements as item>
		<@doc_element.handleElement current=item/>			    
	</#list>	
</#macro>

<#macro handleRole element><#if element.headLevel &gt; 0>role="H${element.headLevel}" </#if></#macro>

<#macro handleElement current>
	<#assign elementType="${current.class.simpleName}"/>
	<#if elementType = 'DocPhrase'>
		<fo:inline <@handleStyle styleValue=current.style/> font-size="${defaultFontSize}pt">${current.text}</fo:inline>
	<#elseif elementType = 'DocPara'>
		<fo:block <@handleRole element=current/><@handleStyle styleValue=current.style/> font-size="${defaultFontSize}pt">${current.text?replace(r"${currentPage}","<fo:page-number/>")}</fo:block>
	<#elseif elementType = 'DocTable'>
		<@handleTable docTable=current/>
	<#elseif elementType = 'DocImage'>
		<@handleImage docImage=current/>								
	<#else>
		<fo:block space-after="5mm" font-weight="bold" font-size="${defaultFontSize}pt">Element type non implemented yet : ${elementType}</fo:block>
	</#if>
</#macro>

<#macro handleImage docImage>
	<#if (docImage.scaling)??>
		<#assign imageScaling="height='${docImage.scaling}%' content-height='${docImage.scaling}%' content-width='scale-to-fit' scaling='uniform' width='${docImage.scaling}%'"/>
	<#else>
		<#assign imageScaling=""/>
	</#if>
	<fo:block>
		<fo:external-graphic  ${imageScaling} xmlns:fo="http://www.w3.org/1999/XSL/Format" 
			src="data:image;base64,${docImage.resolvedBase64}"/>
	</fo:block>
</#macro>

<#macro handleTable docTable>
		<fo:table border-collapse="separate" width="${docTable.width}%" table-layout="fixed">
			<#list docTable.colWithds as currentColWidth>
			<fo:table-column column-width="${currentColWidth}%" />	
			</#list>
			<fo:table-body>
				<#list docTable.elementList as row>	
				<fo:table-row>
					<#list row.elementList as cell>
						<fo:table-cell padding="${docTable.padding}mm" <@handleAlign alignValue=cell.align/> <@handleBorders docBorders=cell.docBorders/> number-columns-spanned="${cell.columnSpan}">
							<#if (cell.elementList?size > 0)>
								<#list cell.elementList as cellElement>
								<@handleElement current=cellElement/>
								</#list>
							<#else>
								<fo:block></fo:block>
							</#if>
						</fo:table-cell>
					</#list>
				</fo:table-row>				
				</#list>
			</fo:table-body>
		</fo:table>
</#macro>

<#macro handleBorder mode size color><#if size != 0><#if size = -1><#assign calcSize="1"/><#else><#assign calcSize="${size}"/></#if>${mode}='${calcSize}pt solid ${color}' </#if></#macro>

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

<#macro handleAlign alignValue>
	<#if alignValue = 1>text-align="left"
	<#elseif alignValue = 2>text-align="center"
	</#if>
</#macro>

<#macro handleStyle styleValue>
	<#if styleValue = 2>font-weight="bold"
	<#elseif styleValue = 3>font-weight="underline"
	</#if>
</#macro>