
2.1.0.rc.3 (2025-03-22)
------------------

* Added 'svg' among accepted image types.

2.1.0.rc.2 (2024-10-14)
------------------

* 'h' elemented accepted as child of 'para'.

2.1.0.rc.1 (2023-08-18)
------------------

* Added 'caption' attribute for 'table' element.

2.0.0.rc.3 (2023-08-17)
------------------

* Added documentation for table 'padding', 'spacing' attributes and info element values 

2.0.0.rc.002 (2023-07-29)
------------------
* Added documentation for ${currentPage} and ${pageCount} constant.

2.0.0-rc.001 (2023-01-17)
------------------
* Refactor of type order with documentation for the new [fj-doc-lib-autodoc](https://github.com/fugerit-org/fj-doc/tree/main/fj-doc-lib-autodoc) feature.
* Attributes 'id' are now typed as 'idType'
* Border width attributes are now typed as 'borderWidthType'
* Spacing attributes are now typed as 'spaceType'
* Colspan and rowspan attributes are now typed as 'spanType'
* Percentages have now their own type 'percentageType'
* Attribute 'columns' has now its own type 'columnsType'
* Attribute 'font-name' has now its own type 'fontNameType'
* Attribute 'size' has now its own type 'fontSizeType'
* Attribute 'text-indent' has now its own type 'textIndentType'
* Attribute 'leading' has now its own type 'leadingType'
* Attribute 'white-space-collpse' has now its own type 'whiteSpaceCollapseType'
* Attribute 'scaling' has now its own type 'scalingType'
* Attribute 'alt' has now its own type 'altType'
* Attribute 'base64' has now its own type 'base64Type'
* Attribute 'url' has now its own type 'urlType'
* Element 'br', 'barcode', 'image', 'nbsp' are no longer 'mixed'
* Type styleType as a new possible value : 'normal'

Summary of previous versions : 
-----------------------------
* 1-10 - para can now contains para and phrases
* 1-9-1 - added phrase list option to list item
* 1-9 - deep change to list handling
