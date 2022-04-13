package recompose.parser.xml.transformer.nodes

import com.jds.recompose.nodes.EditTextNode
import org.xmlpull.v1.XmlPullParser
import recompose.parser.util.assertEndTagNext
import recompose.parser.xml.transformer.attributes.ColorAttributeTransformer
import recompose.parser.xml.transformer.attributes.InputTypeAttributeTransformer
import recompose.parser.xml.transformer.attributes.ViewAttributesTransformer

class EditTextTransformer() : NodeTransformer<EditTextNode, XmlPullParser> {

    private val viewAttributesTransformer = ViewAttributesTransformer()
    private val color = ColorAttributeTransformer()
    private val inputType = InputTypeAttributeTransformer()

    override fun toAstNode(parser: XmlPullParser): EditTextNode {
        val node = EditTextNode(
            view = viewAttributesTransformer.toAttributes(parser),
            text = parser.getAttributeValue(null, "android:text") ?: "",
            hint = parser.getAttributeValue(null, "android:hint") ?: "",
            textColorHint = color.toAttributes(parser, "android:textColorHint"),
            inputType = inputType.toAttributes(parser),
        )
        parser.assertEndTagNext()
        return node
    }

    override fun matches(identifier: String): Boolean {
        return identifier == "EditText"
    }
}
