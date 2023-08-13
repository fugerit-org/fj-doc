# Fugerit Document Generation Framework BOM (fj-doc-bom)

## BOM to import dependency management for all modules (included incubator and deprecated)

[back to fj-doc index](../README.md)

*Description* :  
Just a BOM to make easier dependency management.  
It included all the incubator, extra and deprecated modules, currently excluded from the fj-doc parent pom.
This bom is a convenient way to be always up to date with last known version of extra modules.  
Currently supported modules : 
1. [fj-doc-mod-openpdf](https://github.com/fugerit-org/fj-doc-mod-openpdf) PDF/RTF/HTML handlers based on OpenPDF (An IText fork) (extra)
2. [fj-doc-mod-pdfbox](https://github.com/fugerit-org/fj-doc-mod-pdfbox) PDF Handler based on Apache PDFBOX (incubator)
3. [fj-doc-ent](https://github.com/fugerit-org/fj-doc-ent) Once a core module, it has been long deprecated in favor of fj-doc-freemarker (deprecated)
4. [fj-doc-mod-itext](https://github.com/fugerit-org/fj-doc-mod-itext) PDF/RTF/HTML handlers based on IText 2.1.X (deprecated)
5. [fj-doc-mod-jxl](https://github.com/fugerit-org/fj-doc-mod-jxl) XLS handler based on JExcel API (deprecated)


*Status* :  
Implemented
  
*Quickstart* :
Just add to dependencies section in your POM : 

```
		<dependency>
			<groupId>org.fugerit.java</groupId>
			<artifactId>fj-doc-bom</artifactId>
			<version>${fj-doc-version}</version>
			<type>pom</type>
		</dependency>	
		
```
