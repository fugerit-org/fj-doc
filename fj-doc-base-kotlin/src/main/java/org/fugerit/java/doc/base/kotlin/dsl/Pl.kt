package org.fugerit.java.doc.base.kotlin.dsl

/**
 * Pl represents the pl element.
 *
 * This class will provide function to handle all pl attributes and kids 
 */
class Pl : HelperDSL.TagWithText( "pl" ) {
    /**
     * Creates a new default Phrase instance.
     * @return the new instance.
     */
   fun phrase( text: String = "", init: Phrase.() -> Unit = {} ): Phrase {
       return initTag(Phrase(text), init);
   }


}
