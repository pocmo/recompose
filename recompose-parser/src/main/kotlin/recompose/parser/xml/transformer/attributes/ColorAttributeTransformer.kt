package recompose.parser.xml.transformer.attributes

import com.jds.recompose.values.Color
import org.xmlpull.v1.XmlPullParser
import recompose.parser.ParserImpl

class ColorAttributeTransformer : AttributeTransformer<XmlPullParser, Color?> {
    override fun toAttributes(input: XmlPullParser, name: String?): Color? {
        val raw = input.getAttributeValue(null, name)

        return when {
            raw == null -> null

            raw.startsWith("#") ->
                try {
                    val colorAsString = raw.substring(1)
                    val colorToParse = if (colorAsString.length == 6) {
                        "ff$colorAsString"
                    } else {
                        colorAsString
                    }

                    Color.Absolute(colorToParse.toLong(16))
                } catch (e: NumberFormatException) {
                    throw ParserImpl.ParserException("Could not parse color: $raw", e)
                }

            raw.startsWith("@color/") -> Color.Resource(name = raw.removePrefix("@color/"))

            // There are multiple more color formats that need to be supported.
            else -> throw ParserImpl.ParserException("Unknown color format: $raw")
        }
    }
}
