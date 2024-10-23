package org.fugerit.java.doc.base.kotlin.dsl

/**
 * Info represents the info element.
 *
 * This class will provide function to handle all info attributes and kids 
 */
class Info( text: String = "" ) : HelperDSL.TagWithText( "info" ) {

   init { setText(text) }
    /**
     * Function to set text content for this element.
     */

   fun setText( value: String ) { addKid( HelperDSL.TextElement( value ) ) }


    /**
     * Function handling name attribute of the Info with generic check on type.
     * @return the value for the name attribute.
     */
   fun name( value: String ): Info = setAtt( this, "name", value )

}
