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

import recompose.ast.Layout
import recompose.ast.values.Orientation
import recompose.ast.view.ButtonNode
import recompose.ast.view.CheckBoxNode
import recompose.ast.view.EditTextNode
import recompose.ast.view.ImageViewNode
import recompose.ast.view.TextViewNode
import recompose.ast.view.ViewNode
import recompose.ast.viewgroup.CardViewNode
import recompose.ast.viewgroup.ConstraintLayoutNode
import recompose.ast.viewgroup.FrameLayoutNode
import recompose.ast.viewgroup.LinearLayoutNode
import recompose.ast.viewgroup.UnknownNode
import recompose.composer.ext.findChains
import recompose.composer.ext.findRefs
import recompose.composer.writer.CallParameter
import recompose.composer.writer.KotlinWriter
import recompose.composer.writer.Modifier
import recompose.composer.writer.ModifierBuilder
import recompose.composer.writer.ParameterValue
import recompose.visitor.Visitor

/**
 * [Visitor] implementation that traverses the parsed [Layout] and transforms it into `Composable" calls.
 */
@Suppress("TooManyFunctions")
internal class ComposingVisitor : Visitor {
    private val writer = KotlinWriter()

    fun getResult(): String {
        return writer.getString()
    }

    override fun visitLayout(layout: Layout) {
        layout.children.forEach { view -> view.accept(this) }
    }

    override fun visitView(node: ViewNode) {
        val modifier = ModifierBuilder(node)

        writer.writeCall(
            "Box",
            parameters = listOf(
                modifier.toCallParameter()
            )
        )
    }

    override fun visitButton(node: ButtonNode) {
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

    override fun visitTextView(node: TextViewNode) {
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

    override fun visitCheckBox(node: CheckBoxNode) {
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

    override fun visitCardView(node: CardViewNode) {
        val modifier = ModifierBuilder(node)

        writer.writeCall(
            name = "Card",
            parameters = listOf(
                modifier.toCallParameter()
            )
        ) {
            node.viewGroup.children.forEach { view -> view.accept(this@ComposingVisitor) }
        }
    }

    override fun visitImageView(node: ImageViewNode) {
        val modifier = ModifierBuilder(node)

        writer.writeCall(
            name = "Image",
            parameters = listOf(
                node.src?.let { CallParameter(ParameterValue.DrawableValue(it)) },
                modifier.toCallParameter()
            )
        )
    }

    override fun visitLinearLayout(node: LinearLayoutNode) {
        val composable = when (node.orientation) {
            Orientation.Vertical -> "Column"
            Orientation.Horizontal -> "Row"
        }

        writer.writeCall(composable) {
            node.viewGroup.children.forEach { view -> view.accept(this@ComposingVisitor) }
        }
    }

    override fun visitFrameLayout(node: FrameLayoutNode) {
        val rowModifier = ModifierBuilder(node)
        writer.writeCall(name = "Box", parameters = listOf(rowModifier.toCallParameter())) {
            node.viewGroup.children.forEach { it.accept(this@ComposingVisitor) }
        }
    }

    override fun visitConstraintLayout(node: ConstraintLayoutNode) {
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

            node.viewGroup.children.forEach { view -> view.accept(this@ComposingVisitor) }
        }
    }

    override fun visitUnknown(node: UnknownNode) {
        val block: (KotlinWriter.() -> Unit)? = if (node.viewGroup.children.isEmpty()) {
            null
        } else {
            { node.viewGroup.children.forEach { view -> view.accept(this@ComposingVisitor) } }
        }

        val modifier = ModifierBuilder(node)

        writer.writeCall(
            node.name,
            parameters = listOf(modifier.toCallParameter()),
            linePrefix = "// ",
            block = block
        )
    }

    override fun visitEditText(node: EditTextNode) {
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
            ParameterValue.EmptyLambdaValue
        }
        writer.writeCall(
            name = "TextField",
            parameters = listOf(
                CallParameter(name = "value", value = ParameterValue.StringValue(node.text)),
                CallParameter(name = "onValueChange", value = ParameterValue.EmptyLambdaValue),
                CallParameter(name = "keyboardType", value = ParameterValue.KeyboardTypeValue(node.inputType)),
                CallParameter(name = "label", value = hintParameterValue),
                modifier.toCallParameter(),
            )
        )
    }
}
