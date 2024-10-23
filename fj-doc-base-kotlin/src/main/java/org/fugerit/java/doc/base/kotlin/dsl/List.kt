package org.fugerit.java.doc.base.kotlin.dsl

/**
 * List represents the list element.
 *
 * This class will provide function to handle all list attributes and kids 
 */
class List : HelperDSL.TagWithText( "list" ) {
    /**
     * Creates a new default Li instance.
     * @return the new instance.
     */
   fun li( init: Li.() -> Unit = {} ): Li {
       return initTag(Li(), init);
   }

    /**
     * Function handling id attribute of the List with specific check on type.
     * @return the value for the id attribute.
     */
   fun id( value: String ): List = idType( this, "id", value )
    /**
     * Function handling listType attribute of the List with specific check on type.
     * @return the value for the listType attribute.
     */
   fun listType( value: String ): List = listType( this, "list-type", value )

}
