/*
 * Copyright 2020 Sebastian Kaspari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package recompose.composer.visitor

import com.jds.recompose.nodes.*
import com.jds.recompose.values.Orientation
import com.jds.recompose.visitor.Visitor
import recompose.composer.ext.findChains
import recompose.composer.ext.findRefs
import recompose.composer.writer.*

/**
 * [Visitor] implementation that traverses the parsed [Layout] and transforms it into `Composable" calls.
 */
@Suppress("TooManyFunctions")
internal class ComposingVisitor : Visitor {
    private val writer = KotlinWriter()

    fun getResult(): String {
        return writer.getString()
    }

    private fun visitLayout(layout: Layout) {
        layout.children.forEach { view -> view.accept(this) }
    }

    private fun visitView(node: ViewNode) {
        val modifier = ModifierBuilder(node)

        writer.writeCall(
            "Box",
            parameters = listOf(
                modifier.toCallParameter()
            )
        )
    }

    private fun visitButton(node: ButtonNode) {
        val modifier = ModifierBuilder(node)

        writer.writeCall(
            name = "Button",
            parameters = listOf(
                CallParameter(name = "onClick", value = ParameterValue.EmptyLambdaValue),
                modifier.toCallParameter()
            )
        ) {
            writeCall(
                name = "Text",
                parameters = listOf(
                    CallParameter(name = "text", value = ParameterValue.StringValue(node.text)),
                    CallParameter(name = "textAlign", value = ParameterValue.RawValue("TextAlign.Center"))
                )
            )
        }
    }

    private fun visitTextView(node: TextViewNode) {
        val modifier = ModifierBuilder(node)

        writer.writeCall(
            name = "Text",
            parameters = listOf(
                CallParameter(name = "text", value = ParameterValue.StringValue(node.text)),
                node.textColor?.let { CallParameter(name = "color", value = ParameterValue.ColorValue(it)) },
                node.textSize?.let { CallParameter(name = "fontSize", value = ParameterValue.SizeValue(it)) },
                node.maxLines?.let { CallParameter(name = "maxLines", value = ParameterValue.RawValue(it)) },
                modifier.toCallParameter()
            )
        )
    }

    private fun visitCheckBox(node: CheckBoxNode) {
        val rowModifier = ModifierBuilder(node)

        writer.writeCall(
            "Row",
            parameters = listOf(
                rowModifier.toCallParameter()
            )
        ) {
            writeCall(
                name = "Checkbox",
                parameters = listOf(
                    CallParameter(name = "checked", value = ParameterValue.RawValue(node.checked)),
                    CallParameter(name = "onCheckedChange", value = ParameterValue.EmptyLambdaValue)
                )
            )
            node.text?.let { text ->
                val textModifier = ModifierBuilder()
                textModifier.add(
                    Modifier(
                        "align",
                        listOf(
                            CallParameter(ParameterValue.RawValue("Alignment.CenterVertically"))
                        )
                    )
                )

                writeCall(
                    name = "Text",
                    parameters = listOf(
                        CallParameter(ParameterValue.StringValue(text)),
                        textModifier.toCallParameter()
                    )
                )
            }
        }
    }

    private fun visitRadioButton(node: RadioButtonNode) {
        val rowModifier = ModifierBuilder(node)

        writer.writeCall(
            "Row",
            parameters = listOf(
                rowModifier.toCallParameter()
            )
        ) {
            writeCall(
                name = "RadioButton",
                parameters = listOf(
                    CallParameter(name = "selected", value = ParameterValue.RawValue(node.checked)),
                    CallParameter(name = "onClick", value = ParameterValue.EmptyLambdaValue)
                )
            )
            node.text?.let { text ->
                val textModifier = ModifierBuilder()
                textModifier.add(
                    Modifier(
                        name = "align",
                        parameters = listOf(
                            CallParameter(
                                ParameterValue.RawValue("Alignment.CenterVertically")
                            )
                        )
                    )
                )

                writeCall(
                    name = "Text",
                    parameters = listOf(
                        CallParameter(ParameterValue.StringValue(text)),
                        textModifier.toCallParameter()
                    )
                )
            }
        }
    }

    private fun visitCardView(node: CardViewNode) {
        val modifier = ModifierBuilder(node)

        writer.writeCall(
            name = "Card",
            parameters = listOf(
                modifier.toCallParameter()
            )
        ) {
            node.viewGroupAttributes.children.forEach { view -> view.accept(this@ComposingVisitor) }
        }
    }

    private fun visitImageView(node: ImageViewNode) {
        val modifier = ModifierBuilder(node)

        writer.writeCall(
            name = "Image",
            parameters = listOf(
                node.src?.let { CallParameter(ParameterValue.DrawableValue(it)) },
                modifier.toCallParameter()
            )
        )
    }

    private fun visitLinearLayout(node: LinearLayoutNode) {
        val composable = when (node.orientation) {
            Orientation.Vertical -> "Column"
            Orientation.Horizontal -> "Row"
        }

        writer.writeCall(composable) {
            node.viewGroupAttributes.children.forEach { view -> view.accept(this@ComposingVisitor) }
        }
    }

    private fun visitFrameLayout(node: FrameLayoutNode) {
        val rowModifier = ModifierBuilder(node)
        writer.writeCall(name = "Box", parameters = listOf(rowModifier.toCallParameter())) {
            node.viewGroupAttributes.children.forEach { it.accept(this@ComposingVisitor) }
        }
    }

    private fun visitConstraintLayout(node: ConstraintLayoutNode) {
        val modifier = ModifierBuilder(node)

        writer.writeCall(
            name = "ConstraintLayout",
            parameters = listOf(
                modifier.toCallParameter()
            )
        ) {
            val refs = node.findRefs()
            if (refs.isNotEmpty()) {
                writer.writeRefsDeclaration(refs)
            }

            val chains = node.findChains()
            if (chains.isNotEmpty()) {
                writer.writeChains(chains)
            }

            node.viewGroupAttributes.children.forEach { view -> view.accept(this@ComposingVisitor) }
        }
    }

    private fun visitUnknown(node: UnknownNode) {
        val block: (KotlinWriter.() -> Unit)? = if (node.viewGroupAttributes.children.isEmpty()) {
            null
        } else {
            { node.viewGroupAttributes.children.forEach { view -> view.accept(this@ComposingVisitor) } }
        }

        val modifier = ModifierBuilder(node)

        writer.writeCall(
            node.unknownNodeName,
            parameters = listOf(modifier.toCallParameter()),
            linePrefix = "// ",
            block = block
        )
    }

    private fun visitEditText(node: EditTextNode) {
        val modifier = ModifierBuilder(node)
        val hintParameterValue = if (node.hint.isNotBlank()) {
            ParameterValue.LambdaValue {
                writeCall(
                    name = "Text",
                    parameters = listOf(
                        CallParameter(name = "text", value = ParameterValue.StringValue(node.hint)),
                        node.textColorHint?.let {
                            CallParameter(name = "color", value = ParameterValue.ColorValue(it))
                        },
                    ),
                    endLine = false
                )
            }
        } else {
            null
        }
        writer.writeCall(
            name = "TextField",
            parameters = listOf(
                CallParameter(name = "value", value = ParameterValue.StringValue(node.text)),
                CallParameter(name = "onValueChange", value = ParameterValue.EmptyLambdaValue),
                CallParameter(name = "keyboardType", value = ParameterValue.KeyboardTypeValue(node.inputType)),
                hintParameterValue?.let { CallParameter(name = "label", value = it) },
                modifier.toCallParameter(),
            )
        )
    }

    private fun visitSwitch(node: SwitchNode) {
        val modifier = ModifierBuilder(node)

        writer.writeCall(
            name = "Switch",
            parameters = listOf(
                modifier.toCallParameter(),
                CallParameter(name = "checked", value = ParameterValue.RawValue(node.checked)),
                CallParameter(name = "onCheckedChange", value = ParameterValue.EmptyLambdaValue)
            )
        )
    }

    override fun visit(node: Node) {
        when (node) {
            is ButtonNode -> visitButton(node)
            is CardViewNode -> visitCardView(node)
            is CheckBoxNode -> visitCheckBox(node)
            is ConstraintLayoutNode -> visitConstraintLayout(node)
            is EditTextNode -> visitEditText(node)
            is FrameLayoutNode -> visitFrameLayout(node)
            is ImageViewNode -> visitImageView(node)
            is Layout -> visitLayout(node)
            is LinearLayoutNode -> visitLinearLayout(node)
            is RadioButtonNode -> visitRadioButton(node)
            is SwitchNode -> visitSwitch(node)
            is TextViewNode -> visitTextView(node)
            is UnknownNode -> visitUnknown(node)
            is ViewNode -> visitView(node)
            else -> {}
        }
    }
}
