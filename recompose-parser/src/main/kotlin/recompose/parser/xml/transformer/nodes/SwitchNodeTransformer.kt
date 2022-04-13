package recompose.parser.xml.transformer.nodes

import com.jds.recompose.nodes.SwitchNode
import org.xmlpull.v1.XmlPullParser
import recompose.parser.xml.transformer.attributes.ViewAttributesTransformer

class SwitchNodeTransformer : NodeTransformer<SwitchNode, XmlPullParser> {
    val viewAttributes = ViewAttributesTransformer()

    override fun toAstNode(parser: XmlPullParser): SwitchNode {
        val viewAttributes = viewAttributes.toAttributes(parser)
        val checked = parser.getAttributeValue(null, "android:checked").toBoolean()

        return SwitchNode(
            viewAttributes,
            checked
        )
    }

    override fun matches(identifier: String) = "Switch" == identifier
}
