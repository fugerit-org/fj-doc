# Fugerit Document Generation Framework (fj-doc)

## Bundle library : XSD Autodoc (fj-doc-lib-autodoc)

[back to fj-doc index](../README.md)

*Description* :  
This library generating documentation automatically from an XSD. It has been tested deeply only on Venus XSD 2.0 and later.

*Status* :  
All basic features are implemented.  
  
  
*Quickstart* :
This module can be used by adapting this sample code :

```
		try ( FileOutputStream fos = new FileOutputStream( new File( "target.html" ) ) )  {
			String pathToXsd =  ...;
			XsdParserFacade xsdParserFacade = new XsdParserFacade();
			AutodocModel autodocModel = xsdParserFacade.parse( pathToXsd );
			autodocModel.setVersion( "version" );
			autodocModel.setTitle( "title" );
			autodocModel.setXsdPrefix( "xsd:" );		// xsd prefix
			autodocModel.setAutodocPrefix( "doc:" );	// my xsd prefix
			AutodocDocConfig docConfig = AutodocDocConfig.newConfig();
			docConfig.processAutodocHtmlDefault(autodocModel, fos);
		} catch (Exception e) {
			... error handling ...
		}
		
```

*Dependencies* :  
This library currently relies on [xmlet/XsdParser](https://github.com/xmlet/XsdParser)