package recompose.parser.xml.transformer.nodes

import com.jds.recompose.attributes.ViewGroupAttributes
import com.jds.recompose.nodes.ViewGroupNode
import com.jds.recompose.nodes.ViewNode
import org.xmlpull.v1.XmlPullParser
import recompose.parser.xml.XMLNodeMatcher
import recompose.parser.xml.transformer.attributes.ViewAttributesTransformer

class ViewGroupTransformer : NodeTransformer<ViewGroupNode, XmlPullParser> {
    private val viewAttributesTransformer = ViewAttributesTransformer()

    override fun toAstNode(parser: XmlPullParser): ViewGroupNode {
        val children = mutableListOf<ViewNode>()
        val viewAttributes = viewAttributesTransformer.toAttributes(parser)
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType == XmlPullParser.TEXT) {
                // We do not care about text nodes.
                continue
            }

            XMLNodeMatcher.match(parser)?.let { children.add(it as ViewNode) }
        }

        return ViewGroupNode(viewAttributes, ViewGroupAttributes(children))
    }

    override fun matches(identifier: String) = identifier == "ViewGroup"
}
