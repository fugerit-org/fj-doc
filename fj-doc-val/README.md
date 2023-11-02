# Fugerit Document Generation Framework (fj-doc)

## Doc type validation utilities (fj-doc-val)

[back to fj-doc index](../README.md)  

[![Maven Central](https://img.shields.io/maven-central/v/org.fugerit.java/fj-doc-val.svg)](https://mvnrepository.com/artifact/org.fugerit.java/fj-doc-val) 
[![javadoc](https://javadoc.io/badge2/org.fugerit.java/fj-doc-val/javadoc.svg)](https://javadoc.io/doc/org.fugerit.java/fj-doc-val)

*Description* :  
This module offer validation utilities to check content of files and streams based on type.

*Status* :  
Offer basic support for validating jpg, png, tiff, pdf and p7m
  
*Since* : fj-doc 0.8
  
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