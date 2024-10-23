@file:JvmName("dslDocKt")

package org.fugerit.java.doc.base.kotlin.dsl

/**
 * The base function for this domain specific language.
 */
fun dslDoc(block: Doc.() -> Unit): Doc =
    Doc().apply(block)