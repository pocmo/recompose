package recompose.composer.model.compose.foundation

import recompose.ast.values.Color
import recompose.ast.values.Size
import recompose.composer.writer.CallParameter
import recompose.composer.writer.ParameterValue


/**
 * Helper Class building the parameters of a compose.foundation.Text.
 *
 * https://developer.android.com/reference/kotlin/androidx/compose/foundation/package-summary#text
 */
internal class Text(
    val text: String? = null,
    val color: Color? = null,
    val fontSize: Size? = null,
    val textAlign: String? = null,
    val modifier: CallParameter? = null
) {
    private val textParameter = "text"
    private val colorParameter = "color"
    private val fontSizeParameter = "fontSize"
    private val textAlignParameter = "textAlign"

    fun buildCallParameters(): List<CallParameter> {

        val mutableList = mutableListOf<CallParameter>()

        text?.let {
            mutableList.add(CallParameter(name = textParameter, value = ParameterValue.StringValue(it)))
        }

        color?.let {
            mutableList.add(CallParameter(name = colorParameter, value = ParameterValue.ColoValue(it)))
        }

        fontSize?.let {
            mutableList.add(CallParameter(name = fontSizeParameter, value = ParameterValue.SizeValue(it)))
        }

        textAlign?.let {
            mutableList.add(CallParameter(name = textAlignParameter, value = ParameterValue.StringValue(it)))
        }

        modifier?.let {
            mutableList.add(it)
        }
        return mutableList
    }

    companion object {
        const val className = "Text"
    }

}