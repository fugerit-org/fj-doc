# Fugerit Document Generation Framework (fj-doc)

## Doc type validation utilities (fj-doc-val)

[back to fj-doc index](../README.md)  

*Description* :  
This module offer validation utilities to check content of files and streams based on type.

*Status* :  
Offer basic support for validating jpg, png, tiff and pdf
  
*[ChangeLog](ChangeLog.md)*  
  
*Quickstart* :
Created the facade and use it : 

```
	String extension = ...;
	InputStream stream = ...;
	// add the validators you eant, included custom validators : 
	DocValidatorFacade facade = DocValidatorFacade.newFacadeStrict( 
			PdfboxValidator.DEFAULT,
			ImageValidator.JPG_VALIDATOR,
			ImageValidator.PNG_VALIDATOR
	);
	boolean isValidType = facade.checkByExtension(extension, stream);
```