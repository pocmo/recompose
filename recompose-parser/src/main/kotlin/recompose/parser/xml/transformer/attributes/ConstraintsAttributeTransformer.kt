package recompose.parser.xml.transformer.attributes

import com.jds.recompose.values.Constraints
import org.xmlpull.v1.XmlPullParser
import recompose.parser.ParserImpl

class ConstraintsAttributeTransformer : AttributeTransformer<XmlPullParser, Constraints> {

    private val id = IdAtrributeTransformer()

    override fun toAttributes(input: XmlPullParser, name: String?): Constraints {
        return Constraints(
            // We assume the "app" namespace is used for constraints. That's probably a reasonable assumption since that
            // is also what is used in all the docs. But technically an app could use a different namespace.

            // Relative Positioning Constraints
            relative = Constraints.RelativePositioning(
                bottomToBottom = constraintId(input, "app:layout_constraintBottom_toBottomOf"),
                bottomToTop = constraintId(input, "app:layout_constraintBottom_toTopOf"),
                endToEnd = constraintId(input, "app:layout_constraintEnd_toEndOf"),
                endToStart = constraintId(input, "app:layout_constraintEnd_toStartOf"),
                leftToLeft = constraintId(input, "app:layout_constraintLeft_toLeftOf"),
                leftToRight = constraintId(input, "app:layout_constraintLeft_toRightOf"),
                rightToLeft = constraintId(input, "app:layout_constraintRight_toLeftOf"),
                rightToRight = constraintId(input, "app:layout_constraintRight_toRightOf"),
                startToEnd = constraintId(input, "app:layout_constraintStart_toEndOf"),
                startToStart = constraintId(input, "app:layout_constraintStart_toStartOf"),
                topToBottom = constraintId(input, "app:layout_constraintTop_toBottomOf"),
                topToTop = constraintId(input, "app:layout_constraintTop_toTopOf")
            ),

            // Chains
            chain = Constraints.Chain(
                horizontalStyle = chainStyle(input, "app:layout_constraintHorizontal_chainStyle"),
                verticalStyle = chainStyle(input, "app:layout_constraintVertical_chainStyle")
            )
        )
    }

    private fun constraintId(input: XmlPullParser, name: String): Constraints.Id? {
        return if (input.getAttributeValue(null, name) == "parent") {
            Constraints.Id.Parent
        } else {
            id.toAttributes(input, name)?.let { Constraints.Id.View(it.value) }
        }
    }

    private fun chainStyle(input: XmlPullParser, name: String): Constraints.Chain.Style? {
        return when (val value = input.getAttributeValue(null, name)) {
            null -> null
            "spread" -> Constraints.Chain.Style.SPREAD
            "spread_inside" -> Constraints.Chain.Style.SPREAD_INSIDE
            "packed" -> Constraints.Chain.Style.PACKED
            else -> throw ParserImpl.ParserException("Unknown chain style: $value")
        }
    }
}
