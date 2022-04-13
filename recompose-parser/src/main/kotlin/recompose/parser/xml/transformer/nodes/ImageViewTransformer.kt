package recompose.parser.xml.transformer.nodes

import com.jds.recompose.nodes.ImageViewNode
import org.xmlpull.v1.XmlPullParser
import recompose.parser.util.assertEndTagNext
import recompose.parser.xml.transformer.attributes.DrawableAttributeTransformer
import recompose.parser.xml.transformer.attributes.ViewAttributesTransformer

class ImageViewTransformer : NodeTransformer<ImageViewNode, XmlPullParser> {
    private val viewAttributes = ViewAttributesTransformer()
    private val drawable = DrawableAttributeTransformer()

    override fun toAstNode(parser: XmlPullParser): ImageViewNode {
        val viewAttributes = viewAttributes.toAttributes(parser)
        val compatSrc = drawable.toAttributes(parser, "app:srcCompat")
        val src = drawable.toAttributes(parser, "android:src")

        parser.assertEndTagNext()

        return ImageViewNode(
            viewAttributes,
            compatSrc ?: src
        )
    }

    override fun matches(identifier: String) = identifier == "ImageView"
}
