<#import "/macro/html_element.ftl" as doc_element>
<html>
	<head><title>Generated document</title></head>
	<body>
		<div>
			<#list docBase.docBody.elementList as item>
				<@doc_element.handleElement current=item/>			    
			</#list>
		</div>
	</body>	
</html>