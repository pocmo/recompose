package com.jds.recompose.visitor

import com.jds.recompose.nodes.Node

interface Visitor {
    fun visit(node: Node)
}
