<?xml version="1.0" encoding="UTF-8"?>
<#import "/macro/doc_element.ftl" as doc_element>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<fo:layout-master-set>
		<fo:simple-page-master 
			page-width="21cm" page-height="29.7cm" 
			master-name="simpleA4" 
			margin-left="${docBase.marginLeft!'10'}pt"
			margin-right="${docBase.marginRight!'10'}pt"
			margin-top="${docBase.marginTop!'10'}pt"
			margin-bottom="${docBase.marginBottom!'10'}pt">
			<#assign bodyMarginTop="">
			<#assign bodyMarginBottom="">
			<#if (docBase.useHeader)>
				<#assign headerSize="${docBase.docHeader.expectedSize}mm">
				<#assign bodyMarginTop=" margin-top='${headerSize}' ">
			</#if>
			<#if (docBase.useFooter)>
				<#assign footerSize="${docBase.docFooter.expectedSize}mm">
				<#assign bodyMarginBottom=" margin-bottom='${footerSize}' ">
			</#if>									
				<fo:region-body region-name="xsl-region-body" ${bodyMarginTop} ${bodyMarginBottom}/>
			<#if (docBase.useHeader)>
            	<fo:region-before region-name="xsl-region-before" extent="${headerSize}"/>
            </#if>
            <#if (docBase.useFooter)>
            	<fo:region-after region-name="xsl-region-after" extent="${footerSize}"/>
            </#if>
		</fo:simple-page-master>
	</fo:layout-master-set>
	<fo:page-sequence master-reference="simpleA4" initial-page-number="1">
		<#if (docBase.useHeader)>
			<fo:static-content flow-name="xsl-region-before">
				<@doc_element.handleElementList elements=docBase.docHeader.elementList/>	
	        </fo:static-content>
        </#if>
        <#if (docBase.useFooter)>
	        <fo:static-content flow-name="xsl-region-after">
	            <@doc_element.handleElementList elements=docBase.docFooter.elementList/>
	        </fo:static-content>
        </#if>			
		<fo:flow flow-name="xsl-region-body" >
			<fo:block>
				<@doc_element.handleElementList elements=docBase.docBody.elementList/>	
			</fo:block>	
		</fo:flow>
	</fo:page-sequence>
</fo:root>