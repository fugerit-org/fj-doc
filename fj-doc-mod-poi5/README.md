# Fugerit Document Generation Framework (fj-doc)

## Apache POI 5+ Renderer (XLS/XLSX)(fj-doc-mod-poi5)

[back to fj-doc index](../README.md)  

*Status* :  
Most basic features implemented. (proper color and font handling missing).  
For the intrinsic limitations of the XLS/XLSX format, it is possible to choose a some tables in the document and output it as excel sheets.  
  
*Quickstart* :  
Basically this is only a type handler, see core library [fj-doc-base](../fj-doc-base/README.md).  

*Compatibility*  
This module is based on Apache POI 5.X, which is compatible with java 11+. If you are using java 8 you can use the [fj-doc-mod-poi](../fj-doc-mod-poi/README.md)   module. 

NOTE: If you have any special need you can open a pull request or create your own handler based on this.