# Fugerit Document Generation Framework (fj-doc)

## Fugerit Venus Doc Maven Plugin

[back to fj-doc index](../README.md)

*Description* :  
Ability to add fj-doc configuration to an existing project

*Status* :  
All basic features are implemented.  
  
*Since* : fj-doc 8.6.0

*Quickstart* :

Default configuration : 

```shell
mvn org.fugerit.java:fj-doc-maven-plugin:add
```

Custom configuration :

```shell
mvn org.fugerit.java:fj-doc-maven-plugin:add \
-Dextensions=base,freemarker,mod-fop \
-Dversion=8.6.0
```

*Parameters*

| parameter     | required | default         | description                                                                                       |
|---------------|----------|-----------------|---------------------------------------------------------------------------------------------------|
| version       | true     | latest stable   | fj-doc version to add to the project (i.e. '8.6.0')                                               |
| extensions    | true     | base,freemarker | List of fj-doc core modules to add (*)                                                            |
| projectFolder | true     | .               | Maven project base folder                                                                         |
| addDocFacade  | true     | true            | If true, a stub doc configuration helper will be created                                          |
| force         | false    | true            | Will force project setup even if fj-doc already configured (warning: can overwrite configuration) |

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
