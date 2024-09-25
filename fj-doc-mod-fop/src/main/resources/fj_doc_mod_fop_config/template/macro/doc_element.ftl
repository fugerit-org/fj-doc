<#global defaultBorderColor='black'>

<#macro handleElementList elements>
	<#list elements as item>
		<@doc_element.handleElement current=item/>			    
	</#list>	
</#macro>

<#macro handleRole role element><#if role != ''> role="${role}"<#elseif element.headLevel &gt; 0> role="H${element.headLevel}"</#if></#macro>

<#macro handleElement current>
	<#assign elementType="${current.class.simpleName}"/>
	<#if elementType = 'DocPhrase'>
		<@handlePhrase current=current/>
	<#elseif elementType = 'DocPara'>
		<@handlePara current=current/>
	<#elseif elementType = 'DocBr'>
		<fo:block white-space-treatment="preserve"<@handleFont element=current/>>  </fo:block>
	<#elseif elementType = 'DocTable'>
		<@handleTable docTable=current/>
	<#elseif elementType = 'DocImage'>
		<@handleImage docImage=current/>
	<#elseif elementType = 'DocList'>
		<@handleList docList=current/>
	<#elseif elementType = 'DocContainer'>
		<#list current.elementList as currentChild><@handleElement current=currentChild/></#list>		
	<#elseif elementType = 'DocPageBreak'>
		<fo:block page-break-before="always"></fo:block>																			
	<#else>
		<fo:block space-after="5mm">Element type non implemented yet : ${elementType}</fo:block>
	</#if>
</#macro>

<#macro handlePhrase current>
	<#if (current.link)??>
		<#if (current.internal)>
			<fo:basic-link <@handleStyle styleValue=current.originalStyle/> <@handleFont element=current/> internal-destination="${current.internalLink}"><![CDATA[${current.text}]]></fo:basic-link>
		<#else>
			<fo:basic-link <@handleStyle styleValue=current.originalStyle/> <@handleFont element=current/> external-destination="url('${current.link}')" color="blue" text-decoration="underline"><![CDATA[${current.text}]]></fo:basic-link>
		</#if>
	<#elseif (current.anchor)??>
		<fo:block id="${current.anchor}"><@handleWhiteSpace element=current/><@handleStyle styleValue=current.originalStyle/><@handleFont element=current/><![CDATA[${current.text}]]></fo:block>
	<#else>
		<fo:inline <@handleWhiteSpace element=current/><@handleStyle styleValue=current.originalStyle/> <@handleFont element=current/>><![CDATA[${current.text}]]></fo:inline>
	</#if>
</#macro>

<#macro handleParaRole current role>
	<fo:block<#if (current.id)??> id="${current.id}"</#if><@handleFormat formatValue=current.format!''/><@handleWhiteSpace element=current/><@handleRole role=role element=current/><@handleStyle styleValue=current.originalStyle/><@handleSpacing dc=current/><@handleAlign alignValue=current.align/><@handleFont element=current/>><@handleTextSubstitution text=current.text /><#list current.elementList as currentChild><@handleElement current=currentChild/></#list></fo:block>
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
	<#if docList.elementList?size gt 0>
	<fo:list-block>
		<#list docList.elementList as li>
			<#assign current=li.content/>
			<fo:list-item>
				<fo:list-item-label end-indent="label-end()">
					<fo:block<@handleSpacing dc=current/> ><fo:inline font-style="normal" <@handleFont element=li.content/>><#if li.contentList><#elseif docList.clt == 'uld'>&#183;<#elseif docList.clt == 'ulm'>-<#elseif docList.clt == 'oll'>${li?counter?lower_abc}.<#else>${li?counter}.</#if></fo:inline></fo:block>
				</fo:list-item-label>
				<fo:list-item-body start-indent="body-start()">
					<fo:block><@handleElement current=li.content/><#if li.secondList><@handleElement current=li.secondContent/></#if></fo:block>
				</fo:list-item-body>
			</fo:list-item>			
		</#list>	
	</fo:list-block>
	</#if>
</#macro>

<#macro handleRowList docTable rowList cellType>
			<fo:table-${cellType} <#if cellType == 'header'> font-weight="bold" text-align="center"</#if>>
				<#list rowList as row>	
				<fo:table-row>
					<#list row.elementList as cell>
						<fo:table-cell<#if (cell.backColor??)> background-color="${cell.backColor}" </#if><@addCssValue name='padding' value=docTable.padding def=0 unit='mm'/><@addCssValue name='margin' value=docTable.padding def=0 unit='mm'/><@handleAlign alignValue=cell.align/><@handleVerticalAlign valignValue=cell.valign/><@handleBorders docBorders=cell.docBorders/><@handleCellSpan cell=cell/>>
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
		<fo:table <@handleSpacing dc=docTable/> border-collapse="${docBase.stableInfo['table-border-collapse']!'separate'}" width="${docTable.width}%" table-layout="fixed"  <#if (docTable.spacing)??>border-separation="${docTable.spacing}px"</#if>>
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

<#macro handleBorder mode size color><#if size != 0><#if size = -1><#assign calcSize="1"/><#else><#assign calcSize="${size}"/></#if> ${mode}='${calcSize}pt solid ${color}'</#if></#macro>

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

<#macro handleFormat formatValue><#if formatValue = 'preserve-line'> linefeed-treatment="preserve"</#if></#macro>

<#-- 
padding-top : space-before
padding-bottom : 10 * leading + space-bottom
padding-left : 10 * space-left
padding-right : space-right
text-indent : text-indent
-->
<#macro handlePadding amt dir><#if (amt > 0)> margin-${dir}="${amt}px"</#if></#macro>
<#macro handleIndent amt><#if (amt > 0)> text-indent="${amt}px"</#if></#macro>
<#macro handleSpacing dc><@handleIndent amt=dc.textIndent!0/><@handlePadding amt=dc.spaceBefore!0 dir='top'/><@handlePadding amt=((dc.leading!0)*10 + dc.spaceAfter!0) dir='bottom'/><@handlePadding amt=dc.spaceLeft!0 dir='left'/><@handlePadding amt=dc.spaceRight!0 dir='right'/></#macro>

<#macro handleWhiteSpace element><#if (element.whiteSpaceCollapse??) && (element.whiteSpaceCollapse != 'true')> white-space="pre"</#if></#macro>

<#macro handleFont element><#if (element.fontName??)> font-family="${element.fontName}"</#if><#if (element.size)?? && (element.size != -1)> font-size="${element.size}pt"</#if><#if (element.backColor??)> background-color="${element.backColor}"</#if><#if (element.foreColor??)> color="${element.foreColor}"</#if></#macro>

<#macro handleAlign alignValue><#if alignValue = 1> text-align="left"<#elseif alignValue = 2> text-align="center"<#elseif alignValue = 3> text-align="right"<#elseif alignValue = 8 || alignValue = 9> text-align="justify"</#if></#macro>

<#macro handleCellSpan cell><#if cell.columnSpan != 1> number-columns-spanned="${cell.columnSpan}"</#if><#if cell.rowSpan != 1> number-rows-spanned="${cell.rowSpan}"</#if></#macro>

<#macro handleVerticalAlign valignValue><#if valignValue = 5> vertical-align="middle" display-align="center"<#elseif valignValue = 4> vertical-align="top"<#elseif valignValue = 6> vertical-align="bottom" display-align="after"</#if></#macro>

<#macro handleStyle styleValue><#if styleValue = 2> font-weight="bold"<#elseif styleValue = 3> text-decoration="underline"<#elseif styleValue = 4> font-style="italic"<#elseif styleValue = 5> font-style="italic" font-weight="bold"<#elseif styleValue = 1> font-style="normal" font-weight="normal"</#if></#macro>

<#macro handleTextSubstitution text><![CDATA[${text?replace(r"${currentPage}","]]><fo:page-number/><![CDATA[")?replace(r"${pageCount}","]]><fo:page-number-citation ref-id='EndOfDocument'/><![CDATA[")}]]></#macro>

<#--
	macro : addCssValue
	name : name of the css property to add
	value : value of the css property to add
	def : comparisong value (if the value equals the default, the property will be skipped)
	unit : to add to the value (if any)
-->
<#macro addCssValue name value def unit><#if (value != def)> ${name}="${value}${unit}"</#if></#macro>
