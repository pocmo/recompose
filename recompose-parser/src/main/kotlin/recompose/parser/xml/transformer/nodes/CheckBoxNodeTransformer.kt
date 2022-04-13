package recompose.parser.xml.transformer.nodes

import com.jds.recompose.nodes.CheckBoxNode
import org.xmlpull.v1.XmlPullParser
import recompose.parser.xml.transformer.attributes.ViewAttributesTransformer

class CheckBoxNodeTransformer : NodeTransformer<CheckBoxNode, XmlPullParser> {
    val viewAttributes = ViewAttributesTransformer()

    override fun toAstNode(parser: XmlPullParser): CheckBoxNode {
        val viewAttributes = viewAttributes.toAttributes(parser)
        val text = parser.getAttributeValue(null, "android:text")
        val checked = parser.getAttributeValue(null, "android:checked").toBoolean()

        return CheckBoxNode(
            viewAttributes,
            text,
            checked
        )
    }

    override fun matches(identifier: String) = identifier == "CheckBox"
}
