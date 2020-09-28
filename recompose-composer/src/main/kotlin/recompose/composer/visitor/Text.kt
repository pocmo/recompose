package recompose.composer.visitor

import recompose.ast.values.Color
import recompose.ast.values.Size
import recompose.composer.writer.CallParameter
import recompose.composer.writer.ParameterValue

object Text {
    const val import = ""
    const val className = "Text"
    const val text = "text"
    const val color = "color"
    const val fontSize = "fontSize"
    const val textAlign = "textAlign"

    internal fun buildParameters(text: String? = null, color: Color? = null, fontSize: Size? = null, textAlign: String? = null, modifier: CallParameter? = null): List<CallParameter> {
        val mutableList = mutableListOf<CallParameter>()

        text?.let {
            mutableList.add(CallParameter(name = this.text, value = ParameterValue.StringValue(it)))
        }

        color?.let {
            mutableList.add(CallParameter(name = this.color, value = ParameterValue.ColoValue(it)))
        }

        fontSize?.let {
            mutableList.add(CallParameter(name = this.fontSize, value = ParameterValue.SizeValue(it)))
        }

        textAlign?.let {
            mutableList.add(CallParameter(name = this.textAlign, value = ParameterValue.StringValue(it)))
        }

        modifier?.let {
            mutableList.add(it)
        }
        return mutableList
    }
}