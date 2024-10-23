package org.fugerit.java.doc.base.kotlin.dsl

/**
 * Bookmark represents the bookmark element.
 *
 * This class will provide function to handle all bookmark attributes and kids 
 */
class Bookmark( text: String = "" ) : HelperDSL.TagWithText( "bookmark" ) {

   init { setText(text) }
    /**
     * Function to set text content for this element.
     */

   fun setText( value: String ) { addKid( HelperDSL.TextElement( value ) ) }


    /**
     * Function handling ref attribute of the Bookmark with specific check on type.
     * @return the value for the ref attribute.
     */
   fun ref( value: String ): Bookmark = idType( this, "ref", value )

}
