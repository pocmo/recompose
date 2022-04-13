package recompose.parser.xml.transformer.nodes

import com.jds.recompose.nodes.RadioButtonNode
import org.xmlpull.v1.XmlPullParser
import recompose.parser.xml.transformer.attributes.ViewAttributesTransformer

class RadioButtonTransformer : NodeTransformer<RadioButtonNode, XmlPullParser> {

    val viewAttributes = ViewAttributesTransformer()

    override fun toAstNode(parser: XmlPullParser): RadioButtonNode {
        val viewAttribute = viewAttributes.toAttributes(parser)
        val text = parser.getAttributeValue(null, "android:text")
        val checked = parser.getAttributeValue(null, "android:checked").toBoolean()
        return RadioButtonNode(
            viewAttribute,
            text,
            checked
        )
    }

    override fun matches(identifier: String) = identifier == "RadioButton"
}
