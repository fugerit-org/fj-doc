<#macro toProjectPackageFolder context>${context.groupId?replace(".","/")}/${context.artifactId?replace("-","")}</#macro>

<#macro toProjectPackage context>${context.groupId}.${context.artifactId?replace("-","")}</#macro>
