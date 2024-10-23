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

    /**
     * Function handling id attribute of the Cell with specific check on type.
     * @return the value for the id attribute.
     */
   fun id( value: String ): Cell = idType( this, "id", value )
    /**
     * Function handling colspan attribute of the Cell with specific check on type.
     * @return the value for the colspan attribute.
     */
   fun colspan( value: Int ): Cell = spanType( this, "colspan", value )
    /**
     * Function handling rowspan attribute of the Cell with specific check on type.
     * @return the value for the rowspan attribute.
     */
   fun rowspan( value: Int ): Cell = spanType( this, "rowspan", value )
    /**
     * Function handling align attribute of the Cell with specific check on type.
     * @return the value for the align attribute.
     */
   fun align( value: String ): Cell = alignType( this, "align", value )
    /**
     * Function handling valign attribute of the Cell with specific check on type.
     * @return the value for the valign attribute.
     */
   fun valign( value: String ): Cell = valignType( this, "valign", value )
    /**
     * Function handling header attribute of the Cell with generic check on type.
     * @return the value for the header attribute.
     */
   fun header( value: Boolean ): Cell = setAtt( this, "header", value )
    /**
     * Function handling borderColor attribute of the Cell with specific check on type.
     * @return the value for the borderColor attribute.
     */
   fun borderColor( value: String ): Cell = colorType( this, "border-color", value )
    /**
     * Function handling borderColorTop attribute of the Cell with specific check on type.
     * @return the value for the borderColorTop attribute.
     */
   fun borderColorTop( value: String ): Cell = colorType( this, "border-color-top", value )
    /**
     * Function handling borderColorBottom attribute of the Cell with specific check on type.
     * @return the value for the borderColorBottom attribute.
     */
   fun borderColorBottom( value: String ): Cell = colorType( this, "border-color-bottom", value )
    /**
     * Function handling borderColorLeft attribute of the Cell with specific check on type.
     * @return the value for the borderColorLeft attribute.
     */
   fun borderColorLeft( value: String ): Cell = colorType( this, "border-color-left", value )
    /**
     * Function handling borderColorRight attribute of the Cell with specific check on type.
     * @return the value for the borderColorRight attribute.
     */
   fun borderColorRight( value: String ): Cell = colorType( this, "border-color-right", value )
    /**
     * Function handling borderWidth attribute of the Cell with specific check on type.
     * @return the value for the borderWidth attribute.
     */
   fun borderWidth( value: Int ): Cell = borderWidthType( this, "border-width", value )
    /**
     * Function handling borderWidthTop attribute of the Cell with specific check on type.
     * @return the value for the borderWidthTop attribute.
     */
   fun borderWidthTop( value: Int ): Cell = borderWidthType( this, "border-width-top", value )
    /**
     * Function handling borderWidthBottom attribute of the Cell with specific check on type.
     * @return the value for the borderWidthBottom attribute.
     */
   fun borderWidthBottom( value: Int ): Cell = borderWidthType( this, "border-width-bottom", value )
    /**
     * Function handling borderWidthLeft attribute of the Cell with specific check on type.
     * @return the value for the borderWidthLeft attribute.
     */
   fun borderWidthLeft( value: Int ): Cell = borderWidthType( this, "border-width-left", value )
    /**
     * Function handling borderWidthRight attribute of the Cell with specific check on type.
     * @return the value for the borderWidthRight attribute.
     */
   fun borderWidthRight( value: Int ): Cell = borderWidthType( this, "border-width-right", value )
    /**
     * Function handling backColor attribute of the Cell with specific check on type.
     * @return the value for the backColor attribute.
     */
   fun backColor( value: String ): Cell = colorType( this, "back-color", value )
    /**
     * Function handling foreColor attribute of the Cell with specific check on type.
     * @return the value for the foreColor attribute.
     */
   fun foreColor( value: String ): Cell = colorType( this, "fore-color", value )
    /**
     * Function handling type attribute of the Cell with specific check on type.
     * @return the value for the type attribute.
     */
   fun type( value: String ): Cell = dataType( this, "type", value )

}
