package recompose.parser.xml.transformer.attributes

import com.jds.recompose.values.LayoutSize
import org.xmlpull.v1.XmlPullParser

class LayoutSizeAttributeTransformer : AttributeTransformer<XmlPullParser, LayoutSize?> {

    private val size: SizeAttributeTransformer = SizeAttributeTransformer()

    override fun toAttributes(input: XmlPullParser, name: String?): LayoutSize? {
        return when (input.getAttributeValue(null, name)) {
            null -> null
            "match_parent" -> LayoutSize.MatchParent
            "wrap_content" -> LayoutSize.WrapContent
            else -> size.toAttributes(input, name)?.let { size -> LayoutSize.Absolute(size) }
        }
    }
}
