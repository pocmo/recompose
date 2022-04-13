package recompose.parser.xml.transformer.nodes

import com.jds.recompose.nodes.UnknownNode
import org.xmlpull.v1.XmlPullParser
import recompose.parser.xml.transformer.attributes.ViewAttributesTransformer

class UnknownNodeTransformer : NodeTransformer<UnknownNode, XmlPullParser> {
    private val viewAttributes = ViewAttributesTransformer()
    private val viewGroupAttributes = ViewGroupTransformer()

    override fun toAstNode(parser: XmlPullParser): UnknownNode {
        return UnknownNode(
            unknownNodeName = parser.name,
            view = viewAttributes.toAttributes(parser),
            viewGroup = viewGroupAttributes.toAstNode(parser)
        )
    }

    override fun matches(identifier: String) = "Unknown" == identifier
}
