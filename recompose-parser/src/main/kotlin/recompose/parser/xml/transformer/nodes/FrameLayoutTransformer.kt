package recompose.parser.xml.transformer.nodes

import com.jds.recompose.nodes.FrameLayoutNode
import org.xmlpull.v1.XmlPullParser
import recompose.parser.xml.transformer.attributes.ViewAttributesTransformer

class FrameLayoutTransformer : NodeTransformer<FrameLayoutNode, XmlPullParser> {
    private val viewAttributes = ViewAttributesTransformer()
    private val viewGroupAttributes = ViewGroupTransformer()

    override fun toAstNode(parser: XmlPullParser): FrameLayoutNode {
        return FrameLayoutNode(
            view = viewAttributes.toAttributes(parser),
            viewGroup = viewGroupAttributes.toAstNode(parser)
        )
    }

    override fun matches(identifier: String) = "FrameLayout" == identifier
}
