<#import "/macro/html_element.ftl" as doc_element>
<div>
	<#list docBase.docBody.elementList as item>
		<@doc_element.handleElement current=item/>			    
	</#list>
</div>