package recompose.parser

import com.jds.recompose.nodes.Node

interface NodeMatcher<T> {
    fun match(parser: T): Node?
}
