package com.jds.recompose.visitor

interface Visitable {
    fun accept(visitor: Visitor)
}
