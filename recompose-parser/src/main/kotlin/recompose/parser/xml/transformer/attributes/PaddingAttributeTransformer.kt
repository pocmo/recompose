package recompose.parser.xml.transformer.attributes

import com.jds.recompose.values.Padding
import org.xmlpull.v1.XmlPullParser

class PaddingAttributeTransformer : AttributeTransformer<XmlPullParser, Padding?> {
    private val size: SizeAttributeTransformer = SizeAttributeTransformer()

    override fun toAttributes(input: XmlPullParser, name: String?): Padding {
        return Padding(
            all = size.toAttributes(input, "android:padding"),
            left = size.toAttributes(input, "android:paddingLeft"),
            right = size.toAttributes(input, "android:paddingRight"),
            start = size.toAttributes(input, "android:paddingStart"),
            end = size.toAttributes(input, "android:paddingEnd"),
            top = size.toAttributes(input, "android:paddingTop"),
            bottom = size.toAttributes(input, "android:paddingBottom"),
            horizontal = size.toAttributes(input, "android:paddingHorizontal"),
            vertical = size.toAttributes(input, "android:paddingVertical")
        )
    }
}
