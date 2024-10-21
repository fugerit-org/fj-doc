package org.fugerit.java.doc.base.kotlin.dsl

import org.fugerit.java.core.cfg.ConfigRuntimeException
import org.fugerit.java.core.log.LogFacade
import org.fugerit.java.core.util.checkpoint.CheckpointUtils
import org.fugerit.java.core.xml.dom.DOMIO
import java.io.StringWriter

/*
 * Inspired by :
 * https://kotlinlang.org/docs/type-safe-builders.html#scope-control-dslmarker
 */

class HelperDSL {

    fun interface Element {
        fun render(xmlParent: org.w3c.dom.Element, xmlDocument: org.w3c.dom.Document)
    }

    class TextElement(val text: String) : Element {
        override fun render(xmlParent: org.w3c.dom.Element, xmlDocument: org.w3c.dom.Document) {
            xmlParent.appendChild( xmlDocument.createTextNode( text ) )
        }
    }

    @DslMarker
    annotation class DocTagMarker

    @DocTagMarker
    abstract class Tag(val name: String) : Element {
        val children = arrayListOf<Element>()
        val attributes = hashMapOf<String, String>()

        protected fun <T : Element> initTag(tag: T, init: T.() -> Unit): T {
            tag.init()
            addKid( tag )
            return tag
        }

        override fun render(xmlParent: org.w3c.dom.Element, xmlDocument: org.w3c.dom.Document) {
            val xmlElement = xmlDocument.createElement( name )
            xmlParent.appendChild( xmlElement )
            helper(xmlElement, xmlDocument)
        }

        private fun helper(xmlParent: org.w3c.dom.Element, xmlDocument: org.w3c.dom.Document) {
            attributes.forEach { a -> xmlParent.setAttribute( a.key, a.value ) }
            children.forEach { e -> e.render(xmlParent, xmlDocument) }
        }

        protected fun <T : Element, V> setAtt( tag : T, name : String, value: V, check: (v: V) -> Boolean = {_->true} ) : T {
            if ( check( value ) ) {
                att( name, value.toString() )
            } else {
                throw ConfigRuntimeException( "Check failed for attribute, name : '$name', value : '$value'" )
            }
            return tag
        }

        protected fun att( name : String, value: String ) {
            attributes.put( name, value )
        }

        protected fun addKid( element : Element ) {
            children.add( element )
        }

        fun attList(data: Map<*, *>, key: String): kotlin.collections.List<*> = data[key] as kotlin.collections.List<*>

        @Suppress("UNCHECKED_CAST")
        fun attListMap(data: Map<*, *>, key: String): kotlin.collections.List<Map<*, *>> = attList( data, key ) as kotlin.collections.List<Map<*, *>>

        fun attMap(data: Map<*, *>, key: String): kotlin.collections.Map<*, *> = data[key] as kotlin.collections.Map<*, *>

        fun attStr(data: Map<*, *>, key: String): String = data[key].toString()

		private var checkFun0 : (v: String) -> Boolean =  { v -> setOf( "center", "right", "left", "justify", "justifyall" ).contains( v ) }
		private var checkFun1 : (v: Int) -> Boolean =  { v -> v in 0..32 }
		private var checkFun2 : (v: String) -> Boolean =  { v -> v.length in 1..64 }
		private var checkFun3 : (v: String) -> Boolean =  { v -> setOf( "normal", "bold", "underline", "italic", "bolditalic" ).contains( v ) }
		private var checkFun4 : (v: Int) -> Boolean =  { v -> v in 0..2048 }
		private var checkFun5 : (v: String) -> Boolean =  { v -> v.matches(Regex("#([A-Fa-f0-9]{6})")) }
		private var checkFun6 : (v: String) -> Boolean =  { v -> setOf( "string", "number", "date" ).contains( v ) }
		private var checkFun7 : (v: String) -> Boolean =  { v -> v.length in 1..128 }
		private var checkFun8 : (v: Int) -> Boolean =  { v -> v in 0..256 }
		private var checkFun9 : (v: Int) -> Boolean =  { v -> v in 1..2048 }
		private var checkFun10 : (v: Int) -> Boolean =  { v -> v in 1..100 }
		private var checkFun11 : (v: String) -> Boolean =  { v -> setOf( "normal", "inline" ).contains( v ) }
		private var checkFun12 : (v: String) -> Boolean =  { v -> setOf( "middle", "top", "bottom" ).contains( v ) }
		private var checkFun13 : (v: String) -> Boolean =  { v -> setOf( "ul", "uld", "ulm", "ol", "oln", "oll" ).contains( v ) }
		private var checkFun14 : (v: String) -> Boolean =  { v -> v.length in 0..2048 }
		private var checkFun15 : (v: String) -> Boolean =  { v -> setOf( "png", "jpg", "gif" ).contains( v ) }
		private var checkFun16 : (v: Int) -> Boolean =  { v -> v in 1..7 }
		protected fun <T : Element> alignType( tag : T, name : String, v: String) : T = setAtt( tag, name, v, checkFun0 ) 
		protected fun <T : Element> borderWidthType( tag : T, name : String, v: Int) : T = setAtt( tag, name, v, checkFun1 ) 
		protected fun <T : Element> idType( tag : T, name : String, v: String) : T = setAtt( tag, name, v, checkFun2 ) 
		protected fun <T : Element> styleType( tag : T, name : String, v: String) : T = setAtt( tag, name, v, checkFun3 ) 
		protected fun <T : Element> fontNameType( tag : T, name : String, v: String) : T = setAtt( tag, name, v, checkFun2 ) 
		protected fun <T : Element> leadingType( tag : T, name : String, v: Int) : T = setAtt( tag, name, v, checkFun4 ) 
		protected fun <T : Element> colorType( tag : T, name : String, v: String) : T = setAtt( tag, name, v, checkFun5 ) 
		protected fun <T : Element> dataType( tag : T, name : String, v: String) : T = setAtt( tag, name, v, checkFun6 ) 
		protected fun <T : Element> formatType( tag : T, name : String, v: String) : T = setAtt( tag, name, v, checkFun7 ) 
		protected fun <T : Element> fontSizeType( tag : T, name : String, v: Int) : T = setAtt( tag, name, v, checkFun8 ) 
		protected fun <T : Element> textIndentType( tag : T, name : String, v: Int) : T = setAtt( tag, name, v, checkFun4 ) 
		protected fun <T : Element> spaceType( tag : T, name : String, v: Int) : T = setAtt( tag, name, v, checkFun4 ) 
		protected fun <T : Element> whiteSpaceCollapsType( tag : T, name : String, v: Boolean) : T = setAtt( tag, name, v ) 
		protected fun <T : Element> columnsType( tag : T, name : String, v: Int) : T = setAtt( tag, name, v, checkFun9 ) 
		protected fun <T : Element> percentageType( tag : T, name : String, v: Int) : T = setAtt( tag, name, v, checkFun10 ) 
		protected fun <T : Element> tableRenderModeType( tag : T, name : String, v: String) : T = setAtt( tag, name, v, checkFun11 ) 
		protected fun <T : Element> spanType( tag : T, name : String, v: Int) : T = setAtt( tag, name, v ) 
		protected fun <T : Element> valignType( tag : T, name : String, v: String) : T = setAtt( tag, name, v, checkFun12 ) 
		protected fun <T : Element> listType( tag : T, name : String, v: String) : T = setAtt( tag, name, v, checkFun13 ) 
		protected fun <T : Element> urlType( tag : T, name : String, v: String) : T = setAtt( tag, name, v, checkFun14 ) 
		protected fun <T : Element> imageType( tag : T, name : String, v: String) : T = setAtt( tag, name, v, checkFun15 ) 
		protected fun <T : Element> scalingType( tag : T, name : String, v: Int) : T = setAtt( tag, name, v ) 
		protected fun <T : Element> base64Type( tag : T, name : String, v: String) : T = setAtt( tag, name, v ) 
		protected fun <T : Element> altType( tag : T, name : String, v: String) : T = setAtt( tag, name, v, checkFun14 ) 
		protected fun <T : Element> lengthType( tag : T, name : String, v: Int) : T = setAtt( tag, name, v ) 
		protected fun <T : Element> headLevelType( tag : T, name : String, v: Int) : T = setAtt( tag, name, v, checkFun16 ) 


        fun toXml(): String {
            val startTime = System.currentTimeMillis();
            val xmlDocument = DOMIO.newSafeDocumentBuilderFactory().newDocumentBuilder().newDocument();
            val xmlRoot = xmlDocument.createElement(name)
            helper(xmlRoot, xmlDocument)
            val writer = StringWriter()
            DOMIO.writeDOMIndent( xmlRoot, writer )
            LogFacade.getLog().debug( "toXml time : {}", CheckpointUtils.formatTimeDiffMillis( startTime, System.currentTimeMillis() ) )
            return writer.toString()
        }

        override fun toString(): String = toXml()

    }

    abstract class TagWithText(name: String) : Tag(name) {
        operator fun String.unaryPlus() {
            children.add(TextElement(this))
        }
    }

}