# Fugerit Document Generation Framework (fj-doc)

## Fugerit Venus Doc Maven Plugin

[back to fj-doc index](../README.md)

*Description* :  
Ability to add fj-doc configuration to an existing project

*Status* :  
All basic features are implemented.  
  
*Since* : fj-doc 8.6.0

## Available goals

| goal   | since | description                                                                                                                                                                         |
|--------|-------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| add    | 8.6.0 | add Venus Doc Configuration to an existing project                                                                                                                                  |
| verify | 8.7.0 | verify the templates in a FreeMarker configuration (folder), note: it can be used on any Apache FreeMarker configuration, not only Fugerit Venus Doc.                               |
| init   | 8.7.2 | create a new project project already configured (actually an extension to 'add' goal); an UI is available on the [playground](https://docs.fugerit.org/fj-doc-playground/home) too. |

NOTE: Even though possible, it is not a recommended to run the 'add' or 'init' plugin with setting a 'version' parmeter lower than '8.7.4' (see issue [#180](https://github.com/fugerit-org/fj-doc/issues/180))

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

### Add Fugerit Venus, parameters

| parameter       | required | default         | description                                                                                                                       |
|-----------------|----------|-----------------|-----------------------------------------------------------------------------------------------------------------------------------|
| version         | true     | latest stable   | fj-doc version to add to the project (i.e. '8.7.5'), *advice*: **keep the default** unless a specific version is striclty needed. |
| extensions      | true     | base,freemarker | List of fj-doc core modules to add (*)                                                                                            |
| projectFolder   | true     | .               | Maven project base folder                                                                                                         |
| addDocFacade    | true     | true            | If true, a stub doc configuration helper will be created                                                                          |
| force           | false    | false           | Will force project setup even if fj-doc already configured (warning: can overwrite configuration)                                 |
| excludeXmlApis  | false    | false           | It will exclude dependency xml-apis:xml-apis                                                                                      |
| addExclusions   | false    |                 | Add comma separated exclusion, for instance : xml-apis:xml-apis,${groupId}:${artificatId}                                         |
| addVerifyPlugin | true     | true            | If set to true, it will configure the 'verify' goal on the project                                                                |
| addJunit5       | false    | false           | If set to true, it will add junit5 and basic test                                                                                 |

### Add Fugerit Venus, available extensions

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


## Goal : init

Create a new project and add Venus Doc Configuration to it.

*Quickstart* :

```shell
mvn org.fugerit.java:fj-doc-maven-plugin:init \
-DgroupId=org.example.doc \
-DartifactId=fugerit-demo
```

Project folder will be `./${artifactId}/`.

### Init Fugerit Venus, parameters

| parameter      | required | default        | description          |
|----------------|----------|----------------|----------------------|
| groupId        | true     |                | new project group id |
| artifactId     | true     |                | new project group id |
| projectVersion | true     | 1.0.0-SNAPSHOT | new project version  |
| javaRelease    | true     | 21             | java release version |

NOTE: it is possible to set any property from 'add' goal, except 'projectFolder' which is set to `./${artifactId}`.

## Goal : verify

verify the templates in a FreeMarker configuration (folder), note: it can be used on any Apache FreeMarker configuration, not only Fugerit Venus Doc.

*Quickstart* :

### Verify existing FreeMarker configuration

```shell
mvn org.fugerit.java:fj-doc-maven-plugin:verify -DtemplateBasePath=./src/test/resources/fj_doc_test/template-fail
```

### Verify FreeMarker configuration at maven build time

```xml
  <plugin>
    <groupId>org.fugerit.java</groupId>
    <artifactId>fj-doc-maven-plugin</artifactId>
    <version>${fj-doc-version}</version>
    <executions>
      <execution>
        <id>freemarker-verify</id>
        <phase>compile</phase>
        <goals>
          <goal>verify</goal>
        </goals>
      </execution>
    </executions>
    <configuration>
      <!-- Where the FreeMarker templates are located -->
      <templateBasePath>${project.basedir}/src/main/resources/fugerit-blank/template</templateBasePath>
      <!-- WARNING: if set to 'true', build will fail when at least one syntax error is found -->
      <failOnErrors>true</failOnErrors>
      <!-- If 'true' a report will be generated (when 'true', param reportOutputFolder is required) -->
      <generateReport>true</generateReport>
      <!-- Template syntax verify report output folder -->
      <reportOutputFolder>${project.build.directory}/freemarker-syntax-verify-report</reportOutputFolder>
    </configuration>
  </plugin>
```

### Verify FreeMarker parameters

| parameter           | required | default       | description                                                                                                        |
|---------------------|----------|---------------|--------------------------------------------------------------------------------------------------------------------|
| templateBasePath    | true     |               | Path to base folder containing FreeMarker templates                                                                |
| freemarkerVersion   | false    | latest stable | FreeMarker configuration ( i.e. 2.3.33)                                                                            |
| templateFilePattern | false    |               | Filter on templates to be checked, regex on filename, i.e. ".{0,}[.]ftl"                                           |
| failOnErrors        | true     | true          | If set to true the build will fail when template syntax errors will be found, otherwise errors will be only logged |
| generateReport      | false    | false         | If set to true a report will be generated (and property 'reportOutputFolder' will be olso required).               |
| reportOutputFolder  | false    |               | Output folder for the generated report.                                                                            |
| reportOutputFormat  | false    | 'html'        | Output format for the generated report, supported : html (default), pdf, csv, xlsx, md.                            |

