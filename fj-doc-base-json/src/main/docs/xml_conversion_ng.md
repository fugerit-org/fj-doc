# XML conversion conventions Next Generation

As of version fj-doc-base-json 3.1.0 the json ng format is under development.

It add some customization to standard [xml_conversion on fj-xml-to-json](https://github.com/fugerit-org/fj-xml-to-json/blob/main/src/main/docs/xml_conversion.md) project.

## Version 1.0.0-rc.1

Here is a summary of the differences : 

### medata info

info elements that were usually written in this verbose way :

{
    "_t" : "metadata",
    "_e" : [ {
      "name" : "margins",
      "_t" : "info",
      "_v" : "10;10;10;30"
    }, {
      "name" : "excel-table-id",
      "_t" : "info",
      "_v" : "excel-table=print"
    }, {
      "name" : "excel-width-multiplier",
      "_t" : "info",
      "_v" : "450"
    } ]
}


can now be summarized with a single json object node : 

{
    "_t" : "metadata",
    "info" : {
      "margins" : "10;10;10;30",
      "excel-table-id" : "excel-table=print",
      "excel-width-multiplier" : "450"
    }
}
