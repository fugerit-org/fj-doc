# Fugerit Document Generation Framework (fj-doc)

## Fugerit Venus Doc Maven Plugin

[back to fj-doc index](../README.md)

*Description* :  
Ability to add fj-doc configuration to an existing project

*Status* :  
All basic features are implemented.  
  
*Since* : fj-doc 8.6.0

## Available goals

| goal   | since | description                                                                                                                                           |
|--------|-------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| add    | 8.6.0 | add Venus Doc Configuration to an existing project                                                                                                    |
| verify | 8.7.0 | verify the templates in a FreeMarker configuration (folder), note: it can be used on any Apache FreeMarker configuration, not only Fugerit Venus Doc. |


## Goal : add 

add Venus Doc Configuration to an existing project

*Quickstart* :

Default configuration : 

```shell
mvn org.fugerit.java:fj-doc-maven-plugin:add
```

Custom configuration :

```shell
mvn org.fugerit.java:fj-doc-maven-plugin:add \
-Dextensions=base,freemarker,mod-fop
```

*Parameters*

| parameter      | required | default         | description                                                                                       |
|----------------|----------|-----------------|---------------------------------------------------------------------------------------------------|
| version        | true     | latest stable   | fj-doc version to add to the project (i.e. '8.6.5')                                               |
| extensions     | true     | base,freemarker | List of fj-doc core modules to add (*)                                                            |
| projectFolder  | true     | .               | Maven project base folder                                                                         |
| addDocFacade   | true     | true            | If true, a stub doc configuration helper will be created                                          |
| force          | false    | false           | Will force project setup even if fj-doc already configured (warning: can overwrite configuration) |
| excludeXmlApis | false    | false           | It will exclude dependency xml-apis:xml-apis                                                      |
| addExclusions  | false    |                 | Add comma separated exclusion, for instance : xml-apis:xml-apis,${groupId}:${artificatId}         |



*Available extensions*

| short name      | full name              | type handler | description                                                                                             |
|-----------------|------------------------|--------------|---------------------------------------------------------------------------------------------------------|
| base            | fj-doc-base            | md           | library base, xml as format for document template                                                       |
| freemarker      | fj-doc-freemarker      | html         | Template and configuration functionalities based on [Apache FreeMarker](https://freemarker.apache.org/) |
| mod-fop         | fj-doc-mod-fop         | fo, pdf      | Type handler based on [Apache FOP](https://xmlgraphics.apache.org/fop/)                                 |
| mod-poi         | fj-doc-mod-poi         | xls, xlsx    | Type handler based on [Apache POI](https://poi.apache.org/)                                             |
| mod-opencsv     | fj-doc-mod-opencsv     | opencsv      | Type handler based on [OpenCSV](https://opencsv.sourceforge.net/)                                       |
| mod-openpdf-ext | fj-doc-mod-openpdf-ext | pdf          | Type handler based on [OpenPDF](https://github.com/LibrePDF/OpenPDF)                                    |
| mod-openrtf-ext | fj-doc-mod-openrtf-ext | rtf          | Type handler based on [OpenRTF](https://github.com/LibrePDF/OpenRTF)                                    |
| base-json       | fj-doc-base-json       |              | add support to use json documents as format for document template                                       |
| base-yaml       | fj-doc-base-yaml       |              | add support to use yaml documents as format for document template                                       |


## Goal : verify

add Venus Doc Configuration to an existing project

*Quickstart* :

```shell
mvn org.fugerit.java:fj-doc-maven-plugin:verify -DtemplateBasePath=./src/test/resources/fj_doc_test/template-fail
```

verify the templates in a FreeMarker configuration (folder), note: it can be used on any Apache FreeMarker configuration, not only Fugerit Venus Doc.

| parameter           | required | default       | description                                                                                                        |
|---------------------|----------|---------------|--------------------------------------------------------------------------------------------------------------------|
| templateBasePath    | true     |               | Path to base folder containing FreeMarker templates                                                                |
| freemarkerVersion   | false    | latest stable | FreeMarker configuration ( i.e. 2.3.33)                                                                            |
| templateFilePattern | false    |               | Filter on templates to be checked, regex on filename, i.e. ".{0,}[.]ftl"                                           |
| failOnErrors        | true     | true          | If set to true the build will fail when template syntax errors will be found, otherwise errors will be only logged |

