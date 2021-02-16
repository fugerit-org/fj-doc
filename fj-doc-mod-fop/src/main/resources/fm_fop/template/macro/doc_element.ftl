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
	<fo:inline <@handleWhiteSpace element=current/><@handleStyle styleValue=current.style/> <@handleFont element=current/>>${current.text}</fo:inline>
</#macro>

<#macro handleParaRole current role>
	<fo:block <@handleWhiteSpace element=current/><@handleRole role=role element=current/><@handleStyle styleValue=current.style/><@handleAlign alignValue=current.align/><@handleFont element=current/>>${current.text?replace(r"${currentPage}","<fo:page-number/>")}</fo:block>
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
	<fo:block>
		<fo:external-graphic  ${imageScaling} xmlns:fo="http://www.w3.org/1999/XSL/Format" 
			src="data:image;base64,${docImage.resolvedBase64}"/>
	</fo:block>
</#macro>

<#macro handleList docList>
	<fo:list-block>
		<#list docList.elementList as li>
			<fo:list-item>
				<fo:list-item-label end-indent="label-end()">
					<fo:block><@handlePhrase current=li.liLabel/></fo:block>
				</fo:list-item-label>
				<fo:list-item-body start-indent="body-start()">
					<@handlePara current=li.liBody/>
				</fo:list-item-body>
			</fo:list-item>			
		</#list>	
	</fo:list-block>
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

white-space-collapse="false"

<#macro handleWhiteSpace element><#if (element.whiteSpaceCollapse??) && (element.whiteSpaceCollapse != 'true')>white-space="pre" </#if></#macro>

<#macro handleFont element><#if (element.fontName??)> font-family="${element.fontName}" </#if><#if (element.size)?? && (element.size != -1)> font-size="${element.size}pt" </#if></#macro>

<#macro handleAlign alignValue><#if alignValue = 1>text-align="left"<#elseif alignValue = 2>text-align="center"<#elseif alignValue = 3>text-align="right"</#if> </#macro>

<#macro handleStyle styleValue><#if styleValue = 2>font-weight="bold"<#elseif styleValue = 3>font-weight="underline"<#elseif styleValue = 4>font-style="italic"<#elseif styleValue = 5>font-style="italic" font-weight="bold"<#else>font-style="normal" font-weight="normal"</#if> </#macro>
