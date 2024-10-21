# Fugerit Document Generation Framework (fj-doc)

## Bundle library : Kotlin utilities (fj-doc-lib-kotlin)

[back to fj-doc index](../README.md)

*Description* :  
This library provides some helper for the kotlin language. The first one is a utility to generate a kotlin DSL mapping an XSD.

*Status* :  
All basic features are implemented.  
  
*Since* : fj-doc 8.10.0
  
*Native support*  :  
Disabled, this is a utility module not usually needed at runtime, support may not be added.
  
*Quickstart* :
This module can be used by adapting this sample code :

```java
GenerateKotlinConfig config = new GenerateKotlinConfig(
        PropsIO.loadFromClassLoader( "generate-kotlin/config.properties" ),
        parseLast());
GenerateKotlinFacade.generate( config );	
```
