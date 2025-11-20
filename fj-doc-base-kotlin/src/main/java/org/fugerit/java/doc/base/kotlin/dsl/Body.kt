package org.fugerit.java.doc.base.kotlin.dsl

/**
 * Body represents the body element.
 *
 * This class will provide function to handle all body attributes and kids 
 */
class Body : HelperDSL.TagWithText( "body" ) {
    /**
     * Creates a new default Para instance.
     * @return the new instance.
     */
   fun para( text: String = "", init: Para.() -> Unit = {} ): Para {
       return initTag(Para(text), init);
   }
    /**
     * Creates a new default Table instance.
     * @return the new instance.
     */
   fun table( init: Table.() -> Unit = {} ): Table {
       return initTag(Table(), init);
   }
    /**
     * Creates a new default List instance.
     * @return the new instance.
     */
   fun list( init: List.() -> Unit = {} ): List {
       return initTag(List(), init);
   }
    /**
     * Creates a new default Image instance.
     * @return the new instance.
     */
   fun image( text: String = "", init: Image.() -> Unit = {} ): Image {
       return initTag(Image(text), init);
   }
    /**
     * Creates a new default Phrase instance.
     * @return the new instance.
     */
   fun phrase( text: String = "", init: Phrase.() -> Unit = {} ): Phrase {
       return initTag(Phrase(text), init);
   }
    /**
     * Creates a new default Nbsp instance.
     * @return the new instance.
     */
   fun nbsp( init: Nbsp.() -> Unit = {} ): Nbsp {
       return initTag(Nbsp(), init);
   }
    /**
     * Creates a new default Br instance.
     * @return the new instance.
     */
   fun br( init: Br.() -> Unit = {} ): Br {
       return initTag(Br(), init);
   }
    /**
     * Creates a new default Barcode instance.
     * @return the new instance.
     */
   fun barcode( init: Barcode.() -> Unit = {} ): Barcode {
       return initTag(Barcode(), init);
   }
    /**
     * Creates a new default H instance.
     * @return the new instance.
     */
   fun h( text: String = "", init: H.() -> Unit = {} ): H {
       return initTag(H(text), init);
   }
    /**
     * Creates a new default PageBreak instance.
     * @return the new instance.
     */
   fun pageBreak( init: PageBreak.() -> Unit = {} ): PageBreak {
       return initTag(PageBreak(), init);
   }


}
