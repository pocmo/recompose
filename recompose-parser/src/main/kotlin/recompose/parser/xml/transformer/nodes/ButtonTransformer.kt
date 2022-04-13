package recompose.parser.xml.transformer.nodes

import com.jds.recompose.nodes.ButtonNode
import org.xmlpull.v1.XmlPullParser
import recompose.parser.util.assertEndTagNext
import recompose.parser.xml.transformer.attributes.ViewAttributesTransformer

class ButtonTransformer : NodeTransformer<ButtonNode, XmlPullParser> {

    private val viewAttributes = ViewAttributesTransformer()

    override fun toAstNode(parser: XmlPullParser): ButtonNode {
        val node =
            ButtonNode(
                view = viewAttributes.toAttributes(parser),
                text = parser.getAttributeValue(null, "android:text")
            )
        parser.assertEndTagNext()
        return node
    }

    override fun matches(identifier: String) = identifier == "Button"
}
