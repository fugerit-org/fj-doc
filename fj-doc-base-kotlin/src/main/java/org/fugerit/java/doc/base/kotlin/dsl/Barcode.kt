package org.fugerit.java.doc.base.kotlin.dsl

class Barcode : HelperDSL.TagWithText( "barcode" ) {

   fun size( value: Int ): Barcode = fontSizeType( this, "size", value )
   fun type( value: String ): Barcode = setAtt( this, "type", value )
   fun text( value: String ): Barcode = altType( this, "text", value )

}
