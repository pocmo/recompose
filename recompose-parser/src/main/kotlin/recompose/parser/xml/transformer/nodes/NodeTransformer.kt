package recompose.parser.xml.transformer.nodes

import com.jds.recompose.nodes.Node

interface NodeTransformer<OUTPUT : Node, INPUT> {

    fun toAstNode(parser: INPUT): OUTPUT

    fun matches(identifier: String): Boolean
}
