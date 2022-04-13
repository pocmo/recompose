package recompose.parser.xml.transformer.nodes

import com.jds.recompose.nodes.ViewNode
import org.xmlpull.v1.XmlPullParser
import recompose.parser.util.assertEndTagNext
import recompose.parser.xml.transformer.attributes.ViewAttributesTransformer

class ViewTransformer : NodeTransformer<ViewNode, XmlPullParser> {
    val viewAttributes = ViewAttributesTransformer()

    override fun toAstNode(parser: XmlPullParser): ViewNode {
        val viewAttributes = viewAttributes.toAttributes(parser)
        parser.assertEndTagNext()
        return ViewNode(viewAttributes)
    }

    override fun matches(identifier: String) = "View" == identifier
}
