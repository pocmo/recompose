package recompose.parser.xml.transformer.attributes

import com.jds.recompose.values.Orientation
import org.xmlpull.v1.XmlPullParser
import recompose.parser.ParserImpl

class OrientationAttributeTransformer : AttributeTransformer<XmlPullParser, Orientation?> {
    override fun toAttributes(input: XmlPullParser, name: String?): Orientation? {
        return when (val value = input.getAttributeValue(null, "android:orientation")) {
            "vertical" -> Orientation.Vertical
            "horizontal" -> Orientation.Horizontal
            null -> null
            else -> throw ParserImpl.ParserException("Unknown orientation: $value")
        }
    }
}
