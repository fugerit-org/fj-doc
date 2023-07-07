# Fugerit Document Generation Framework (fj-doc)

## Bundle library : simple table (fj-doc-lib-simpletable-import)

[back to fj-doc index](../README.md)

*Description* :  
This is a simple library to convert xlsx or csv document to SimpleTable

*Status* :  
All basic features are implemented.  
  
  
*Quickstart* :

For CSV : 

```

		try ( FileInputStream is = new FileInputStream( inputFile );
				FileOutputStream os = new FileOutputStream( outputFile )) {
			ConvertCsvToSimpleTableFacade.getInstance().processCsv(is, os, XlsxPoiTypeHandler.HANDLER, new Properties());
		} catch (Exception e) {
			...
		}
		
```

For XLSX :

```

		try ( FileInputStream is = new FileInputStream( inputFile );
				FileOutputStream os = new FileOutputStream( outputFile )) {
			ConvertXlsxToSimpleTableFacade.getInstance().processXlsx(is, os, XlsxPoiTypeHandler.HANDLER, new Properties());
		} catch (Exception e) {
			...
		}
		
```