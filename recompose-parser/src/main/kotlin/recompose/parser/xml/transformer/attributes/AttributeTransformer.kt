package recompose.parser.xml.transformer.attributes

import com.jds.recompose.attributes.Attribute

interface AttributeTransformer<INPUT, OUTPUT : Attribute?> {
    fun toAttributes(input: INPUT, name: String? = null): OUTPUT
}
