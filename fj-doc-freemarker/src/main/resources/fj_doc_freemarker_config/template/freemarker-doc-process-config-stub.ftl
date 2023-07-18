<?xml version="1.0" encoding="utf-8"?>
<freemarker-doc-process-config
	xmlns="https://freemarkerdocprocess.fugerit.org"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="https://freemarkerdocprocess.fugerit.org https://www.fugerit.org/data/java/doc/xsd/freemarker-doc-process-1-0.xsd" > 	

	<!--
		Configuration stub version : 002 (2023-07-18)
	-->

	<#assign stubHandler=stubParams['stub-handler']!'1'>
	<#if stubHandler == '1'>
	
	<docHandlerConfig>

		<!-- Type handler for markdown format -->
		<docHandler id="md-ext" info="md" type="org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandler" />
		<!-- Type henalder for xml format, generates the source xml:doc -->
		<docHandler id="xml-doc" info="xml" type="org.fugerit.java.doc.base.config.DocTypeHandlerXMLUTF8" />
	
		<!-- Type handlers for html using freemarker --> 
		<docHandler id="html-fm" info="html" type="org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandlerUTF8" />	
		<!-- Type handlers for html using freemarker (fragment version, only generates body content no html or head part --> 
		<docHandler id="html-fragment-fm" info="fhtml" type="org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlFragmentTypeHandlerUTF8" />
		
		<#assign enableFopBase=stubParams['enable-fop-base']!'0'>
		<#assign enableFopFull=stubParams['enable-fop-full']!'0'>
		<!--
			Handlers dependent on :
			<dependency>
				<groupId>org.fugerit.java</groupId>
				<artifactId>fj-doc-mod-fop</artifactId>
			</dependency>	
		-->
		<!-- Apache Fop Xml type handler <#if enableFopBase == '1'> --> </#if>
		<docHandler id="fo-fop" info="fo" type="org.fugerit.java.doc.mod.fop.FreeMarkerFopTypeHandlerUTF8" />
		<#if enableFopBase != '1'> --> </#if>
		<!-- Simple default PDF FOP Type handler with no added configuration  <#if enableFopBase == '1'> --> </#if>
		<docHandler id="pdf-fop" info="pdf" type="org.fugerit.java.doc.mod.fop.PdfFopTypeHandler"/>
		<#if enableFopBase != '1'> --> </#if>
		<!-- PDF FOP Type handler, set charset to UTF-8, needs a fop configuration file in a specific class loader path  <#if enableFopFull == '1'> --> </#if>
		<docHandler id="pdf-fop" info="pdf" type="org.fugerit.java.doc.mod.fop.PdfFopTypeHandler">
			<docHandlerCustomConfig charset="UTF-8" fop-config-mode="classloader" fop-config-classloader-path="fop-config.xml"/>
		</docHandler>
		<#if enableFopFull != '1'> --> </#if>
		<!-- PDF FOP Type handler, set charset to UTF-8, needs a fop configuration file in a specific class loader path, set the PDF/A-1a profile  <#if enableFopFull == '1'> --> </#if>
		<docHandler id="PDF/A-1a" info="pdf" type="org.fugerit.java.doc.mod.fop.PdfFopTypeHandler">
			<docHandlerCustomConfig charset="UTF-8" fop-config-mode="classloader" fop-config-classloader-path="fop-config-pdfa.xml" pdf-a-mode="PDF/A-1a"/>
		</docHandler>
		<#if enableFopFull != '1'> --> </#if>
	
		<#assign enablePoi=stubParams['enable-poi']!'0'>
        <!--
			Handlers dependent on :
			<dependency>
				<groupId>org.fugerit.java</groupId>
				<artifactId>fj-doc-mod-poi</artifactId>
			</dependency>	
		-->
		<!-- XLS type hanlder <#if enablePoi == '1'> --> </#if>
		<docHandler id="xls-poi" info="xls" type="org.fugerit.java.doc.mod.poi.XlsPoiTypeHandler" />
		<#if enablePoi != '1'>--> </#if>
		<!-- XLSX type hanlder <#if enablePoi == '1'> --> </#if>
		<docHandler id="xlsx-poi" info="xlsx" type="org.fugerit.java.doc.mod.poi.XlsxPoiTypeHandler" />			
		<#if enablePoi != '1'>--> </#if>
				
		<#assign enableOpencsv=stubParams['enable-opencsv']!'0'>
		<!--
			Handlers dependent on :
			<dependency>
				<groupId>org.fugerit.java</groupId>
				<artifactId>fj-doc-mod-opencsv</artifactId>
			</dependency>	
		-->
		<!-- CSV type hanlder <#if enableOpencsv == '1'> --> </#if>
		<docHandler id="csv-opencsv" info="csv" type="org.fugerit.java.doc.mod.opencsv.OpenCSVTypeHandler"/>
		<#if enableOpencsv != '1'>--> </#if>
				
	</docHandlerConfig>
	</#if>

	<#assign stubChain=stubParams['stub-chain']!'0'>
	<#if stubChain == '1'>
	<#assign configId=stubParams['config-id']!'FJ_DOC_STUB'>
	<#assign fmVersion=stubParams['fm-version']!'2.3.29'>
	<#assign fmTemplatePath=stubParams['fm-template-path']!'/free_marker/'>
	<docChain id="shared">
		<!-- configurations -->
		<chainStep stepType="config">
			<config id="${configId}" version="${fmVersion}" path="${fmTemplatePath}"/>
		</chainStep>
		<!-- functions configuration -->
		<chainStep stepType="function">
			<function name="messageFormat" value="org.fugerit.java.doc.freemarker.fun.SimpleMessageFun"/>
			<function name="sumLong" value="org.fugerit.java.doc.freemarker.fun.SimpleSumLongFun"/>
		</chainStep>
	</docChain>

	<docChain id="sample-chain" parent="shared">
		<!-- attribute mapper from DocProcessContext to FreeMarker attributes Map
		<chainStep stepType="map">
			<map name="name1" value="value1"/>
			<map name="name2" value="value2"/>
		</chainStep>
		-->
		<chainStep stepType="complex" template-path="sample-chain.ftl"/>
	</docChain>
	</#if>

	<#if (configModel)??>
		<#list configModel.chainList as chainModel>
	<docChain id="${chainModel.id}">
			<#list chainModel.stepList as stepModel>
		<chainStep stepType="${stepModel.type}"<#if stepModel.type == 'complex'><#list stepModel.attNames as currentAttName> ${currentAttName}="${stepModel.atts[currentAttName]}"</#list></#if>>
			<#if stepModel.type == 'map'>
			<#list stepModel.attNames as currentAttName>
			<map name="${currentAttName}" value="${stepModel.atts[currentAttName]}"/>
			</#list>
			<#elseif stepModel.type == 'config'>
			<config
				<#list stepModel.attNames as currentAttName>
				${currentAttName}="${stepModel.atts[currentAttName]}"
				</#list>
			/>
			<#elseif stepModel.type == 'complex'>
			<#else>
			<!-- custom step, additional configuration may be needed -->
			</#if>
		</chainStep>
			</#list> 
	</docChain>

		</#list>   
	</#if>
		
</freemarker-doc-process-config>
