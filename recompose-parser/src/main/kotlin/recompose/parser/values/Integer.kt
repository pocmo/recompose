package recompose.parser.values

import org.xmlpull.v1.XmlPullParser
import recompose.parser.Parser

internal fun XmlPullParser.integer(name: String): Int? {
    val value = getAttributeValue(null, name)

    try {
        return when (value) {
            null -> null
            else -> Integer.parseInt(value)
        }
    } catch (e: NumberFormatException) {
        throw Parser.ParserException("Could not parse integer: $value", e)
    }
}
