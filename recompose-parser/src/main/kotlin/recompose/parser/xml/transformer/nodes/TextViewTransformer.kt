package recompose.parser.xml.transformer.nodes

import com.jds.recompose.nodes.TextViewNode
import org.xmlpull.v1.XmlPullParser
import recompose.parser.util.assertEndTagNext
import recompose.parser.xml.transformer.attributes.ColorAttributeTransformer
import recompose.parser.xml.transformer.attributes.SizeAttributeTransformer
import recompose.parser.xml.transformer.attributes.ViewAttributesTransformer

class TextViewTransformer : NodeTransformer<TextViewNode, XmlPullParser> {

    val viewAttributes = ViewAttributesTransformer()
    val color = ColorAttributeTransformer()
    val size = SizeAttributeTransformer()

    override fun toAstNode(parser: XmlPullParser): TextViewNode {
        val viewAttributes = viewAttributes.toAttributes(parser)
        val text = parser.getAttributeValue(null, "android:text") ?: ""
        val textColor = color.toAttributes(parser, "android:textColor")
        val textSize = size.toAttributes(parser, "android:textSize")
        val maxLines = parser.getAttributeValue(null, "android:maxLines")?.toIntOrNull()

        parser.assertEndTagNext()

        return TextViewNode(
            viewAttributes,
            text,
            textColor,
            textSize,
            maxLines
        )
    }

    override fun matches(identifier: String) = "TextView" == identifier
}
