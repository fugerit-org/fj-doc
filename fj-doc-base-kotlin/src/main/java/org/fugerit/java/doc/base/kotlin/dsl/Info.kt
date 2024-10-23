package org.fugerit.java.doc.base.kotlin.dsl

class Info( text: String = "" ) : HelperDSL.TagWithText( "info" ) {

   init { setText(text) }

   fun setText( value: String ) { addKid( HelperDSL.TextElement( value ) ) }


    /**
     * Function handling name attribute of the Info with generic check on type.
     * @return the value for the name attribute.
     */
   fun name( value: String ): Info = setAtt( this, "name", value )

}
