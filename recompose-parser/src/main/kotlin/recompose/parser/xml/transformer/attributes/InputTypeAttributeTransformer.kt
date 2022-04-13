package recompose.parser.xml.transformer.attributes

import com.jds.recompose.values.InputType
import org.xmlpull.v1.XmlPullParser
import recompose.parser.ParserImpl

class InputTypeAttributeTransformer : AttributeTransformer<XmlPullParser, InputType> {
    override fun toAttributes(input: XmlPullParser, name: String?): InputType {
        return when (val value = input.getAttributeValue(null, "android:inputType")) {
            "text" -> InputType.Text
            "number" -> InputType.Number
            "phone" -> InputType.Phone
            "textUri" -> InputType.Uri
            "textEmailAddress" -> InputType.Email
            "textPassword" -> InputType.Password
            "numberPassword" -> InputType.NumberPassword
            null -> InputType.Text
            else -> throw ParserImpl.ParserException("Unknown inputType: $value")
        }
    }
}
