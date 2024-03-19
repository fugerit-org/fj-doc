<#assign comp=docBase.stableInfo['doc-version-compatibility']!'2-x' ><#if (comp = '1-x') ><#import "/macro/html_element_1-x.ftl" as doc_element><#else><#import "/macro/html_element.ftl" as doc_element></#if>
<#if docType=='fhtml'>
<div>
	<#list docBase.docBody.elementList as item>
		<@doc_element.handleElement current=item/>			    
	</#list>
</div>
<#else>
<!doctype html>	
<html lang="${docBase.infoDocLanguage!'en'}">
	<head>
		<title>${docBase.infoDocTitle!'Generated document'}</title>
		<#if (docBase.stableInfo['html-charset'])??><meta charset="${docBase.stableInfo['html-charset']}"></#if>
		<#if (docBase.infoDocAuthor)??><meta name="author" content="${docBase.infoDocAuthor}"/></#if>
		<#if (docBase.infoDocSubject)??><meta name="description" content="${docBase.infoDocSubject}"/></#if>
		<#if (docBase.infoDocLanguage)??><meta http-equiv="content-language" content="${docBase.infoDocLanguage}"/></#if>		
		<meta name="doc-version-compatibility" content="${comp}"/>
		${docBase.stableInfo['html-add-to-head']!''}
		<#if (docBase.stableInfo['html-css-style'])??>
		<style type="text/css">
			${docBase.stableInfo['html-css-style']}
		</style>
		</#if>
		<#if (docBase.stableInfo['html-css-link'])??>
		<link rel="stylesheet" href="${docBase.stableInfo['html-css-link']}"/>
		</#if>
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