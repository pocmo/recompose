package recompose.parser.xml.transformer.attributes

import com.jds.recompose.values.Drawable
import org.xmlpull.v1.XmlPullParser
import recompose.parser.ParserImpl

class DrawableAttributeTransformer : AttributeTransformer<XmlPullParser, Drawable?> {

    private val color = ColorAttributeTransformer()

    override fun toAttributes(input: XmlPullParser, name: String?): Drawable? {
        val value = input.getAttributeValue(null, name)

        return when {
            value == null -> null

            value.startsWith("#") -> color.toAttributes(input, name)?.let { Drawable.ColorValue(it) }

            value.startsWith("@drawable/") -> Drawable.Resource(name = value.substring(10))

            value.startsWith("@android:drawable/") -> Drawable.AndroidResource(name = value.substring(18))

            value.startsWith("?") -> Drawable.StyleAttribute(name = value.substring(1))

            else -> throw ParserImpl.ParserException("Unknown drawable format: $value")
        }
    }
}
