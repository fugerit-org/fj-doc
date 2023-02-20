<#assign comp=docBase.stableInfo['doc-version-compatibility']!DocConfig.DOC_VERSION_COMPATIBILITY_DEFAULT ><#if (comp = DocConfig.DOC_VERSION_COMPATIBILITY_1_X) ><#import "/macro/html_element_1-x.ftl" as doc_element><#else><#import "/macro/html_element.ftl" as doc_element></#if>
<#if docType=='${DocConfig.TYPE_HTML_FRAGMENT}'>
<div>
	<#list docBase.docBody.elementList as item>
		<@doc_element.handleElement current=item/>			    
	</#list>
</div>
<#else>
<html>
	<head>
		<title>${docBase.infoDocTitle!'Generated document'}</title>
		<#if (docBase.infoDocAuthor)??><meta name="author" content="${docBase.infoDocAuthor}"/></#if>
		<#if (docBase.infoDocSubject)??><meta name="description" content="${docBase.infoDocSubject}"/></#if>
		<#if (docBase.infoDocLanguage)??><meta http-equiv="content-language" content="${docBase.infoDocLanguage}"/></#if>		
		<meta name="doc-version-compatibility" content="${comp}"/>
		${docBase.stableInfo['html-add-to-head']!''}
	</head>
	<body>
		<div>
			<#list docBase.docBody.elementList as item>
				<@doc_element.handleElement current=item/>			    
			</#list>
		</div>
	</body>	
</html>
</#if>