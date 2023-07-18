# Fugerit Document Generation Framework (fj-doc)

## Doc tool - utilities (fj-doc-tool)

[back to fj-doc index](../README.md)  

*Description* :  
This modules contains useful tools for the Venus Doc Generation Framework

*Status* :  
generate-stub tool implemented (allows generation of stub configuration of the new freemarker doc process config)

*[ChangeLog](CHANGELOG.md)*  
  
*Quickstart* :

Build :

```
mvn clean install -P singlepackage
```

Run (sample) :

```
java -jar target/dist-fj-doc-tool-1.3.1-rc.002.jar \
	--tool generate-stub \
	--input src/test/resources/convert-config-test/doc-process-sample.xml \
	--output target/new-config.xml
```

## reference parameters for tool : generate-stub

1. output=[required] path to the stub configuration to be generated
2. input=[optional] path to input config file in old model
3. stub-handler=[default='1'] '1' to add the default handlers configuration to the stub
4. stub-chain=[default='0'] '1' to add the default chain to the stub
5. enable-fop-base=[default='0'] '1' will enable basic fop type handlers for .fo and .pdf formats
6. enable-fop-full=[default='0'] '1' will enable full fop type handlers for custon and PDF/A .pdf format (Additional configuration may be required)
7. enable-poi=[default='0'] '1' will enable xls and xlsx formats
8. enable-opencsv=[default='0'] '1' will enable xls and xlsx formats
9. config-id=[default='FJ_DOC_STUB'] The freemarker configuration id to use
10. fm-version=[default='2.3.29']  
11. fm-template-path=[default='/free_marker/']  

