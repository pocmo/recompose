package recompose.parser.xml.transformer.nodes

import com.jds.recompose.nodes.LinearLayoutNode
import com.jds.recompose.values.Orientation
import org.xmlpull.v1.XmlPullParser
import recompose.parser.xml.transformer.attributes.OrientationAttributeTransformer
import recompose.parser.xml.transformer.attributes.ViewAttributesTransformer

class LinearLayoutTransformer : NodeTransformer<LinearLayoutNode, XmlPullParser> {

    private val viewAttributes = ViewAttributesTransformer()
    private val viewGroupAttributes = ViewGroupTransformer()
    private val orientation = OrientationAttributeTransformer()

    override fun toAstNode(parser: XmlPullParser): LinearLayoutNode {
        return LinearLayoutNode(
            view = viewAttributes.toAttributes(parser),
            orientation = orientation.toAttributes(parser) ?: Orientation.Vertical,
            viewGroup = viewGroupAttributes.toAstNode(parser)
        )
    }

    override fun matches(identifier: String) = "LinearLayout" == identifier
}
