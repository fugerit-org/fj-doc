<?xml version="1.0" encoding="UTF-8"?>
<#import "/macro/doc_element.ftl" as doc_element>
<#import "/macro/doc_info.ftl" as doc_info>
<#assign docInfo=docBase.info/>
<fo:root <#if (docBase.infoDocLanguage)??>xml:lang="${docBase.infoDocLanguage}"</#if>
	<@doc_info.checkDefaultFont info=docInfo/> 
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:fox="http://xmlgraphics.apache.org/fop/extensions">
	<fo:layout-master-set>
		<fo:simple-page-master
			page-width="${docBase.infoPageWidth!'21cm'}" 
			page-height="${docBase.infoPageHeight!'29.7cm'}" 
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
	
	<#if (docBase.infoDocTitle)?? || (docBase.infoDocSubject)?? || (docBase.infoDocAuthor)?? || (docBase.infoDocLanguage)??>
		<fo:declarations>
		  <x:xmpmeta xmlns:x="adobe:ns:meta/">
		    <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
		      <rdf:Description rdf:about=""
		          xmlns:dc="http://purl.org/dc/elements/1.1/">
		        <#if (docBase.infoDocTitle)??><dc:title>${docBase.infoDocTitle}</dc:title></#if>
		        <#if (docBase.infoDocAuthor)??><dc:creator>${docBase.infoDocAuthor}</dc:creator></#if>
		        <#if (docBase.infoDocSubject)??><dc:description>${docBase.infoDocSubject}</dc:description></#if>
		        <#if (docBase.infoDocLanguage)??><dc:language><rdf:Bag><rdf:li>${docBase.infoDocLanguage}</rdf:li></rdf:Bag></dc:language></#if>
		      </rdf:Description>
		      <rdf:Description rdf:about="" xmlns:xmp="http://ns.adobe.com/xap/1.0/">
		      	<xmp:CreatorTool><#if (docBase.infoDocCreator)??>${docBase.infoDocCreator}<#else>Apache FOP over Fugerit DOC</#if></xmp:CreatorTool>
		      </rdf:Description>
		    </rdf:RDF>
		  </x:xmpmeta>
		</fo:declarations>
	</#if>
	
	<#if (docBase.docBookmarkTree)??>
		<fo:bookmark-tree>
			<#list docBase.docBookmarkTree.elementList as docBookmark>	
		    <fo:bookmark internal-destination="${docBookmark.ref}">
		    	<fo:bookmark-title>${docBookmark.title}</fo:bookmark-title>
		    </fo:bookmark>
		    </#list>
		</fo:bookmark-tree>
	</#if>
	
	<fo:page-sequence master-reference="simpleA4" initial-page-number="1">
		<#if (docBase.useHeader)>
			<fo:static-content flow-name="xsl-region-before">
				<fo:block>
				<@doc_element.handleElementList elements=docBase.docHeader.elementList/>
				</fo:block>	
	        </fo:static-content>
        </#if>
        <#if (docBase.useFooter)>
	        <fo:static-content flow-name="xsl-region-after">
	        	<fo:block>
	            <@doc_element.handleElementList elements=docBase.docFooter.elementList/>
	            </fo:block>
	        </fo:static-content>
        </#if>			
		<fo:flow flow-name="xsl-region-body" >
		
	        <#if ( (docBase.docBackground)?? && docBase.docBackground.elementList?size > 0 )>
				<fo:block-container absolute-position="absolute" top="0cm" left="0cm" width="21cm" height="29.7cm">
		            <@doc_element.handleElementList elements=docBase.docBackground.elementList/>
				</fo:block-container>	        
	        </#if>		
		
			<fo:block>
				<@doc_element.handleElementList elements=docBase.docBody.elementList/>	
			</fo:block>	
		</fo:flow>
	</fo:page-sequence>
</fo:root>