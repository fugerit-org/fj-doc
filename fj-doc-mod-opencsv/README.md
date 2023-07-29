# Fugerit Document Generation Framework (fj-doc)

## OpenCSV Render (CSV)(fj-doc-mod-opencsv)

[back to fj-doc index](../README.md)  

*Status* :  
Basic features implemented. (Sample JUnit [TestFreeMarker01](../fj-doc-sample/src/test/java/test/org/fugerit/java/doc/sample/freemarker/TestFreeMarker01.java) is now working).  
For the intrinsic limitations of the CSV format, is possible to choose a sinlge table in the document and outputs it as CSV.  
  
*Since* : fj-doc 0.6
  
*Native support*  :  
Disabled, native support will be added in a future release. OpenCSV native support must be verified.
  
*Quickstart* :  
Basically this is only a type handler, see core library [fj-doc-base](../fj-doc-base/README.md).  
NOTE: If you have any special need you can open a pull request or create your own handler based on this.