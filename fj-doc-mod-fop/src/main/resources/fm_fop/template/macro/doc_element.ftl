<#global defaultBorderColor='black'>

<#macro handleElementList elements>
	<#list elements as item>
		<@doc_element.handleElement current=item/>			    
	</#list>	
</#macro>

<#macro handleRole role element><#if role != ''>role="${role}" <#elseif element.headLevel &gt; 0>role="H${element.headLevel}" </#if></#macro>

<#macro handleElement current>
	<#assign elementType="${current.class.simpleName}"/>
	<#if elementType = 'DocPhrase'>
		<@handlePhrase current=current/>
	<#elseif elementType = 'DocPara'>
		<@handlePara current=current/>
	<#elseif elementType = 'DocBr'>
		<fo:block white-space-treatment="preserve" <@handleFont element=current/>>  </fo:block>
	<#elseif elementType = 'DocTable'>
		<@handleTable docTable=current/>
	<#elseif elementType = 'DocImage'>
		<@handleImage docImage=current/>
	<#elseif elementType = 'DocList'>
		<@handleList docList=current/>																				
	<#else>
		<fo:block space-after="5mm">Element type non implemented yet : ${elementType}</fo:block>
	</#if>
</#macro>

<#macro handlePhrase current>
	<#if (current.link)??>
		
		<fo:basic-link internal-destination="${current.internalLink}">${current.text}</fo:basic-link>
	<#elseif (current.anchor)??>
		<fo:block id="${current.anchor}"><@handleWhiteSpace element=current/><@handleStyle styleValue=current.originalStyle/> <@handleFont element=current/>${current.text}</fo:block>
	<#else>
		<fo:inline <@handleWhiteSpace element=current/><@handleStyle styleValue=current.originalStyle/> <@handleFont element=current/>>${current.text}</fo:inline>
	</#if>
</#macro>

<#macro handleParaRole current role>
	<fo:block <@handleWhiteSpace element=current/><@handleRole role=role element=current/><@handleStyle styleValue=current.originalStyle/><@handleParaSpacing spaceBefore=current.spaceBefore!0 spaceAfter=current.spaceAfter!0/><@handleAlign alignValue=current.align/><@handleFont element=current/>>${current.text?replace(r"${currentPage}","<fo:page-number/>")}</fo:block>
</#macro>

<#macro handlePara current>
	<@handleParaRole current=current role=''/>
</#macro>

<#macro handleImage docImage>
	<#if (docImage.scaling)??>
		<#assign imageScaling="height='${docImage.scaling}%' content-height='${docImage.scaling}%' content-width='scale-to-fit' scaling='uniform' width='${docImage.scaling}%'"/>
	<#else>
		<#assign imageScaling=""/>
	</#if>
	<fo:block <@handleAlign alignValue=docImage.align/>>
		<fo:external-graphic <#if (docImage.alt)??> fox:alt-text="${docImage.alt}" </#if> ${imageScaling} xmlns:fo="http://www.w3.org/1999/XSL/Format"
			src="data:image;base64,${docImage.resolvedBase64}"/>
	</fo:block>
</#macro>

<#macro handleList docList>
	<fo:list-block>
		<#list docList.elementList as li>
			<fo:list-item>
				<fo:list-item-label end-indent="label-end()">
					<fo:block><fo:inline font-style="normal" <@handleFont element=li.content/>><#if docList.listType == 'ul'>&#183;<#else>${li?counter}.</#if></fo:inline></fo:block>
				</fo:list-item-label>
				<fo:list-item-body start-indent="body-start()">
					<fo:block><#list li.elementList as element><@handleElement current=element/></#list></fo:block>
				</fo:list-item-body>
			</fo:list-item>			
		</#list>	
	</fo:list-block>
</#macro>

<#macro handleRowList docTable rowList cellType>
			<fo:table-${cellType} <#if cellType == 'header'> font-weight="bold" text-align="center"</#if>>
				<#list rowList as row>	
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
			</fo:table-${cellType}>
</#macro>

<#macro handleRowInline docTable row docTableUtil>
	<#list row.elementList as cell>
		<fo:inline-container vertical-align="top" inline-progression-dimension="${docTable.colWithds[cell?index]}%">
			<#if (cell.elementList?size > 0)>
				<#list cell.elementList as cellElement>
					<@handleElement current=cellElement/>
				</#list>
			<#else>
				<fo:block></fo:block>
			</#if>
		</fo:inline-container>
	</#list>
</#macro>

<#macro handleTable docTable>
	<#assign docTableUtil=docTable.util/>
	<#if docTable.renderMode == 'inline'>
		<#list docTable.elementList as row>
			<@handleRowInline docTable=docTable row=row docTableUtil=docTableUtil/>	
		</#list>
	<#else>
		<fo:table border-collapse="separate" width="${docTable.width}%" table-layout="fixed">
			<#list docTable.colWithds as currentColWidth>
			<fo:table-column column-width="${currentColWidth}%" />	
			</#list>
			<#if (docTableUtil.strictHeader)>
				<@handleRowList docTable=docTable rowList=docTableUtil.headerRows cellType='header'/>
				<@handleRowList docTable=docTable rowList=docTableUtil.dataRows cellType='body'/>
			<#else>
				<@handleRowList docTable=docTable rowList=docTable.elementList cellType='body'/>
			</#if>
		</fo:table>		
	</#if>
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

<#macro handleParaSpacing spaceBefore spaceAfter><#if (spaceBefore > 0)> space-before="${spaceBefore}px"</#if><#if (spaceAfter > 0)> space-after="${spaceAfter}px"</#if></#macro>

<#macro handleWhiteSpace element><#if (element.whiteSpaceCollapse??) && (element.whiteSpaceCollapse != 'true')>white-space="pre" </#if></#macro>

<#macro handleFont element><#if (element.fontName??)> font-family="${element.fontName}" </#if><#if (element.size)?? && (element.size != -1)> font-size="${element.size}pt" </#if></#macro>

<#macro handleAlign alignValue><#if alignValue = 1>text-align="left"<#elseif alignValue = 2>text-align="center"<#elseif alignValue = 3>text-align="right"</#if> </#macro>

<#macro handleStyle styleValue><#if styleValue = 2>font-weight="bold"<#elseif styleValue = 3>font-weight="underline"<#elseif styleValue = 4>font-style="italic"<#elseif styleValue = 5>font-style="italic" font-weight="bold"<#elseif styleValue = 1>font-style="normal" font-weight="normal"</#if> </#macro>
