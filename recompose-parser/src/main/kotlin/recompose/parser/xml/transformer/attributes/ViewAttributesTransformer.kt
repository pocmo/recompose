package recompose.parser.xml.transformer.attributes

import com.jds.recompose.attributes.ViewAttributes
import com.jds.recompose.values.LayoutSize
import org.xmlpull.v1.XmlPullParser

class ViewAttributesTransformer : AttributeTransformer<XmlPullParser, ViewAttributes> {

    private val padding = PaddingAttributeTransformer()
    private val layoutSize = LayoutSizeAttributeTransformer()
    private val id = IdAtrributeTransformer()
    private val constraints = ConstraintsAttributeTransformer()
    private val drawable = DrawableAttributeTransformer()

    override fun toAttributes(input: XmlPullParser, name: String?): ViewAttributes {
        return ViewAttributes(
            id = id.toAttributes(input),
            width = layoutSize.toAttributes(input, "android:layout_width") ?: LayoutSize.WrapContent,
            height = layoutSize.toAttributes(input, "android:layout_height") ?: LayoutSize.WrapContent,
            background = drawable.toAttributes(input, "android:background"),
            padding = padding.toAttributes(input),
            constraints = constraints.toAttributes(input)
        )
    }
}
