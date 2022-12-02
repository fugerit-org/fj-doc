<#global defaultBorderColor='black'>

<#macro checkDefaultFontFamily info><#if (info['default-font-name'])??>font-family="${info['default-font-name']}" </#if></#macro>
<#macro checkDefaultFontSize info><#if (info['default-font-size'])??>font-size="${info['default-font-size']}pt" </#if></#macro>
<#macro getFontStyle infoValue><#if infoValue=='bold'>font-weight="bold"<#elseif infoValue=='italic'>font-style="italic"<#elseif infoValue=='bolditalic'>font-weight="bold" font-style="italic"</#if> </#macro>
<#macro checkDefaultFontStyle info><#if (info['default-font-style'])??><@getFontStyle infoValue=info['default-font-style']/></#if></#macro>

<#macro checkDefaultFont info><@checkDefaultFontFamily info=info/><@checkDefaultFontSize info=info/><@checkDefaultFontStyle info=info/></#macro>
			

