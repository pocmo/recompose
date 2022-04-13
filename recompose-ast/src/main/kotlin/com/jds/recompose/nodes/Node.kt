package com.jds.recompose.nodes

import com.jds.recompose.attributes.ViewAttributes
import com.jds.recompose.attributes.ViewGroupAttributes
import com.jds.recompose.visitor.Visitable
import com.jds.recompose.visitor.Visitor

sealed class Node : Visitable {
    abstract val name: String
}

open class ViewGroupNode(view: ViewAttributes, val viewGroupAttributes: ViewGroupAttributes) : ViewNode(view) {
    override val name: String
        get() = "ViewGroup"

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    companion object {
        val EMPTY = ViewGroupNode(ViewAttributes.EMPTY, ViewGroupAttributes(emptyList()))
    }
}
