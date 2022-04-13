package recompose.parser.xml.transformer.attributes

import com.jds.recompose.values.Size
import org.xmlpull.v1.XmlPullParser
import recompose.parser.ParserImpl

class SizeAttributeTransformer : AttributeTransformer<XmlPullParser, Size?> {

    override fun toAttributes(input: XmlPullParser, name: String?): Size? {
        val value = input.getAttributeValue(null, name)

        try {
            return when {
                value == null -> null
                value.endsWith("dp") -> Size.Dp(Integer.parseInt(value.substring(0, value.length - 2)))
                value.endsWith("sp") -> Size.Sp(Integer.parseInt(value.substring(0, value.length - 2)))
                else -> throw ParserImpl.ParserException("Unknown size value: $value")
            }
        } catch (e: java.lang.NumberFormatException) {
            throw ParserImpl.ParserException("Cannot parse layout size: $value")
        }
    }
}
