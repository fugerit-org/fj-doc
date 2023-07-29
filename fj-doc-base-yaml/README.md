# Fugerit Document Generation Framework (fj-doc)

## Core library : yaml extension (fj-doc-base-json)

[back to fj-doc index](../README.md)  

*Description* :  
Add the option to use a yank as document generator instead of standard xml source provided by default.

*Status* :  
All basic features are implemented (yaml parsing, conversion from and to xml)

*Since* : fj-doc 0.7

*Native support*  :  
Disabled, native support will be added in a future release.

*Doc YAML format*  
The xml and yaml format have inherent differences. this is why is important to read the [conversion conventions used](../fj-doc-base-json/xml_conversion.md) [note : the conventions refers to the json format, but the same considerations has been used for YAML format).

src/test/resources/sample/doc_test_01.yaml

Examples : 
* [sample yaml doc](src/test/resources/sample/doc_test_01.yaml)
* [sample xml doc](src/test/resources/sample/doc_test_01.xml)