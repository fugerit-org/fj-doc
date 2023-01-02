# XML conversion conventions

When converting to json (or yaml) format, some conventions has been assumed.

A) Three special properties are used to handle some conditions:  
1. "_t" : property is used for storing the original element name (for instance if we are storing a `<para>` element, we will have a `"_t":"para"` property)  
2. "_v" : property is used for storing text context (for instance if we are storing a `<para>my text</para>` element, we will have a `"_v":"my text"` property  
3. "_e" : property is used for storing child elements as an array. (for instance if we are storing a `<row><cell></cell><cell></cell></row>` we will have a `"_e":[{"_t":"cell"}.{"_t":"cell"}]` property.

B) All other xml attributes are stored using the corresponding property name. (for instance `<para style="bold">my text</para>` will convert to `{"_t":"para","_v":"my text","style":"bold"}`

C) The property `xsd-version` may be used in the root of the three to store the reference to xsd version to use `{"xsd-version":"1-10","_t":"doc","_e":[...]}` (alternatively the standard properties `"xmlns:xsi"`, `"xsi:schemaLocation"`, `"xmlns"` can be used)
