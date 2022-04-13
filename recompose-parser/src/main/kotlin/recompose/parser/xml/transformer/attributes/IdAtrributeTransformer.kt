package recompose.parser.xml.transformer.attributes

import com.jds.recompose.values.Id
import org.xmlpull.v1.XmlPullParser
import recompose.parser.ParserImpl

class IdAtrributeTransformer : AttributeTransformer<XmlPullParser, Id?> {
    override fun toAttributes(input: XmlPullParser, name: String?): Id? {
        val id = input.getAttributeValue(null, name ?: "android:id") ?: return null

        return when {
            id.startsWith("@+id/") -> Id(id.substring(5))
            id.startsWith("@id/") -> Id(id.substring(4))
            else -> throw ParserImpl.ParserException("Cannot parse id: $id")
        }
    }
}
