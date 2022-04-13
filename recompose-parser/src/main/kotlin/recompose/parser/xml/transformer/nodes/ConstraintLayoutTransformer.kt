package recompose.parser.xml.transformer.nodes

import com.jds.recompose.nodes.ConstraintLayoutNode
import org.xmlpull.v1.XmlPullParser
import recompose.parser.xml.transformer.attributes.ViewAttributesTransformer

class ConstraintLayoutTransformer : NodeTransformer<ConstraintLayoutNode, XmlPullParser> {

    private val viewAttributes = ViewAttributesTransformer()
    private val viewGroupAttributes = ViewGroupTransformer()

    override fun toAstNode(parser: XmlPullParser): ConstraintLayoutNode {
        return ConstraintLayoutNode(
            view = viewAttributes.toAttributes(parser),
            viewGroup = viewGroupAttributes.toAstNode(parser)
        )
    }

    override fun matches(identifier: String) = "androidx.constraintlayout.widget.ConstraintLayout" == identifier
}
