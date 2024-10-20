package org.fugerit.java.doc.base.kotlin.dsl

class Nbsp : HelperDSL.TagWithText( "nbsp" ) {

   fun length( value: Int ): Nbsp = lengthType( this, "length", value )

}
