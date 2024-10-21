package org.fugerit.java.doc.base.kotlin.dsl

class Body : HelperDSL.TagWithText( "body" ) {
   fun para( text: String = "", init: Para.() -> Unit = {} ): Para {
       return initTag(Para(text), init);
   }
   fun table( init: Table.() -> Unit = {} ): Table {
       return initTag(Table(), init);
   }
   fun list( init: List.() -> Unit = {} ): List {
       return initTag(List(), init);
   }
   fun image( init: Image.() -> Unit = {} ): Image {
       return initTag(Image(), init);
   }
   fun phrase( text: String = "", init: Phrase.() -> Unit = {} ): Phrase {
       return initTag(Phrase(text), init);
   }
   fun nbsp( init: Nbsp.() -> Unit = {} ): Nbsp {
       return initTag(Nbsp(), init);
   }
   fun br( init: Br.() -> Unit = {} ): Br {
       return initTag(Br(), init);
   }
   fun barcode( init: Barcode.() -> Unit = {} ): Barcode {
       return initTag(Barcode(), init);
   }
   fun h( text: String = "", init: H.() -> Unit = {} ): H {
       return initTag(H(text), init);
   }
   fun pageBreak( init: PageBreak.() -> Unit = {} ): PageBreak {
       return initTag(PageBreak(), init);
   }


}
