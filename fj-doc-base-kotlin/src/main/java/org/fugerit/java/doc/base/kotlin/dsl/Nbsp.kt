package org.fugerit.java.doc.base.kotlin.dsl

/**
 * Nbsp represents the nbsp element.
 *
 * This class will provide function to handle all nbsp attributes and kids 
 */
class Nbsp : HelperDSL.TagWithText( "nbsp" ) {

    /**
     * Function handling length attribute of the Nbsp with specific check on type.
     * @return the value for the length attribute.
     */
   fun length( value: Int ): Nbsp = lengthType( this, "length", value )

}
