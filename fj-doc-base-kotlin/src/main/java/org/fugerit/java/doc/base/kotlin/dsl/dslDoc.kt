@file:JvmName("dslDocKt")

package org.fugerit.java.doc.base.kotlin.dsl

fun dslDoc(block: Doc.() -> Unit): Doc =
    Doc().apply(block)