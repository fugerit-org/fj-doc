package org.fugerit.java.doc.base.kotlin.dsl

class Cell : HelperDSL.TagWithText( "cell" ) {
    /**
     * Creates a new default Para instance.
     * @return the new instance.
     */
   fun para( text: String = "", init: Para.() -> Unit = {} ): Para {
       return initTag(Para(text), init);
   }
    /**
     * Creates a new default Table instance.
     * @return the new instance.
     */
   fun table( init: Table.() -> Unit = {} ): Table {
       return initTag(Table(), init);
   }
    /**
     * Creates a new default Image instance.
     * @return the new instance.
     */
   fun image( init: Image.() -> Unit = {} ): Image {
       return initTag(Image(), init);
   }
    /**
     * Creates a new default Phrase instance.
     * @return the new instance.
     */
   fun phrase( text: String = "", init: Phrase.() -> Unit = {} ): Phrase {
       return initTag(Phrase(text), init);
   }
    /**
     * Creates a new default Nbsp instance.
     * @return the new instance.
     */
   fun nbsp( init: Nbsp.() -> Unit = {} ): Nbsp {
       return initTag(Nbsp(), init);
   }
    /**
     * Creates a new default Br instance.
     * @return the new instance.
     */
   fun br( init: Br.() -> Unit = {} ): Br {
       return initTag(Br(), init);
   }
    /**
     * Creates a new default Barcode instance.
     * @return the new instance.
     */
   fun barcode( init: Barcode.() -> Unit = {} ): Barcode {
       return initTag(Barcode(), init);
   }
    /**
     * Creates a new default H instance.
     * @return the new instance.
     */
   fun h( text: String = "", init: H.() -> Unit = {} ): H {
       return initTag(H(text), init);
   }

   fun id( value: String ): Cell = idType( this, "id", value )
   fun colspan( value: Int ): Cell = spanType( this, "colspan", value )
   fun rowspan( value: Int ): Cell = spanType( this, "rowspan", value )
   fun align( value: String ): Cell = alignType( this, "align", value )
   fun valign( value: String ): Cell = valignType( this, "valign", value )
   fun header( value: Boolean ): Cell = setAtt( this, "header", value )
   fun borderColor( value: String ): Cell = colorType( this, "border-color", value )
   fun borderColorTop( value: String ): Cell = colorType( this, "border-color-top", value )
   fun borderColorBottom( value: String ): Cell = colorType( this, "border-color-bottom", value )
   fun borderColorLeft( value: String ): Cell = colorType( this, "border-color-left", value )
   fun borderColorRight( value: String ): Cell = colorType( this, "border-color-right", value )
   fun borderWidth( value: Int ): Cell = borderWidthType( this, "border-width", value )
   fun borderWidthTop( value: Int ): Cell = borderWidthType( this, "border-width-top", value )
   fun borderWidthBottom( value: Int ): Cell = borderWidthType( this, "border-width-bottom", value )
   fun borderWidthLeft( value: Int ): Cell = borderWidthType( this, "border-width-left", value )
   fun borderWidthRight( value: Int ): Cell = borderWidthType( this, "border-width-right", value )
   fun backColor( value: String ): Cell = colorType( this, "back-color", value )
   fun foreColor( value: String ): Cell = colorType( this, "fore-color", value )
   fun type( value: String ): Cell = dataType( this, "type", value )

}
