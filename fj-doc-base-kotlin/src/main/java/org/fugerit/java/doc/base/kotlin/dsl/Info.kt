package org.fugerit.java.doc.base.kotlin.dsl

class Info( text: String = "" ) : HelperDSL.TagWithText( "info" ) {

   init { setText(text) }

   fun setText( value: String ) { addKid( HelperDSL.TextElement( value ) ) }


   fun name( value: String ): Info = setAtt( this, "name", value )

}
