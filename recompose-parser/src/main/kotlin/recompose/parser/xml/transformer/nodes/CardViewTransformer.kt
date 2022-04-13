package recompose.parser.xml.transformer.nodes

import com.jds.recompose.nodes.CardViewNode
import org.xmlpull.v1.XmlPullParser
import recompose.parser.xml.transformer.attributes.ViewAttributesTransformer

class CardViewTransformer : NodeTransformer<CardViewNode, XmlPullParser> {

    private val viewAttributes = ViewAttributesTransformer()
    private val viewGroupAttributes = ViewGroupTransformer()

    override fun toAstNode(parser: XmlPullParser): CardViewNode {
        return CardViewNode(
            view = viewAttributes.toAttributes(parser),
            viewGroup = viewGroupAttributes.toAstNode(parser)
        )
    }

    override fun matches(identifier: String) =
        "androidx.cardview.widget.CardView" == identifier ||
            "com.google.android.material.card.MaterialCardView" == identifier
}
