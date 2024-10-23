package org.fugerit.java.doc.base.kotlin.dsl

class Barcode : HelperDSL.TagWithText( "barcode" ) {

    /**
     * Function handling size attribute of the Barcode with specific check on type.
     * @return the value for the size attribute.
     */
   fun size( value: Int ): Barcode = fontSizeType( this, "size", value )
    /**
     * Function handling type attribute of the Barcode with generic check on type.
     * @return the value for the type attribute.
     */
   fun type( value: String ): Barcode = setAtt( this, "type", value )
    /**
     * Function handling text attribute of the Barcode with specific check on type.
     * @return the value for the text attribute.
     */
   fun text( value: String ): Barcode = altType( this, "text", value )

}
