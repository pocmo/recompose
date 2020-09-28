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

package recompose.composer.writer

import recompose.ast.values.Color
import recompose.ast.values.Constraints
import recompose.ast.values.Drawable
import recompose.ast.values.Size
import recompose.composer.ext.getRef
import recompose.composer.model.Chain

/**
 * Helper class for writing Kotlin code to a String.
 *
 * It's a bit rough.. But does the job.
 */
@Suppress("TooManyFunctions")
internal class KotlinWriter {
    private val writer = LineWriter()

    /**
     * Writes a function call.
     *
     * `name(parameters) { block }`
     */
    fun writeCall(
        name: String,
        parameters: List<CallParameter?> = emptyList(),
        linePrefix: String = "",
        endLine: Boolean = true,
        block: (KotlinWriter.() -> Unit)? = null
    ) {
        writer.startLine("$linePrefix$name")

        writeParameters(parameters, block != null)

        if (block == null) {
            if (endLine) {
                writer.endLine()
            }
        } else {
            writer.endLine(" {")
            writeBlock(block)
            writer.startLine("$linePrefix}")
            if (endLine) {
                writer.endLine()
            }
        }
    }

    fun writeRefsDeclaration(refs: Set<String>) {
        writer.startLine("val (")
        writer.continueLine(refs.joinToString(", "))
        writer.endLine(") = createRefs()")
        writer.writeLine()
    }

    fun writeChains(chains: Set<Chain>) {
        chains.forEach { chain ->
            if (chain.direction == Chain.Direction.HORIZONTAL) {
                writer.startLine("createHorizontalChain")
            } else {
                writer.startLine("createVerticalChain")
            }

            val parameters = (listOf(chain.head) + chain.elements).map { node ->
                CallParameter(ParameterValue.RawValue(node.getRef()))
            }.toMutableList()

            when (chain.style) {
                Constraints.Chain.Style.PACKED ->
                    parameters.add(
                        CallParameter(
                            name = "chainStyle",
                            value = ParameterValue.RawValue("ChainStyle.Packed")
                        )
                    )
                Constraints.Chain.Style.SPREAD ->
                    parameters.add(
                        CallParameter(
                            name = "chainStyle",
                            value = ParameterValue.RawValue("ChainStyle.Spread")
                        )
                    )
                Constraints.Chain.Style.SPREAD_INSIDE ->
                    parameters.add(
                        CallParameter(
                            name = "chainStyle",
                            value = ParameterValue.RawValue("ChainStyle.SpreadInside")
                        )
                    )
            }

            writeParameters(parameters, false)
            writer.endLine()
        }
        writer.writeLine()
    }

    private fun writeString(value: ParameterValue.StringValue) {
        writer.continueLine("\"")
        writer.continueLine(value.raw)
        writer.continueLine("\"")
    }

    private fun writeEmptyLambda() {
        writer.continueLine("{}")
    }

    private fun writeColor(value: ParameterValue.ColorValue) {
        when (val color = value.color) {
            is Color.Absolute -> {
                writer.continueLine("Color(")
                writer.continueLine("0x")
                @Suppress("MagicNumber")
                writer.continueLine(color.value.toString(16))
                writer.continueLine(".toInt()")
                writer.continueLine(")")
            }
        }
    }

    private fun writeModifierValue(value: ParameterValue.ModifierValue) {
        var addComma = false
        writer.continueLine("Modifier.")
        value.builder.getModifiers().forEach { modifier ->
            if (addComma) {
                writer.continueLine(".")
            }

            writer.continueLine(modifier.name)

            writeParameters(
                modifier.parameters,
                isFollowedByLambda = modifier.lambda != null
            )

            modifier.lambda?.let { lambda ->
                writer.endLine(" {")
                writeBlock {
                    lambda.invoke(this)
                }
                writer.startLine("}")
            }

            addComma = true
        }
    }

    private fun writeParameters(parameters: List<CallParameter?>, isFollowedByLambda: Boolean) {
        if (parameters.isEmpty() && isFollowedByLambda) {
            // Without lambda we need to write an empty parameter list: Foo()
            // Otherwise we can just continue without parameters: Foo { .. }
            return
        }

        writer.continueLine("(")

        var addSeparator = false
        parameters.filterNotNull().forEach { parameter ->
            if (addSeparator) {
                writer.continueLine(", ")
            }

            if (parameter.name != null) {
                writer.continueLine(parameter.name)
                writer.continueLine(" = ")
            }

            writeParameterValue(parameter.value)

            addSeparator = true
        }

        writer.continueLine(")")
    }

    private fun writeSize(value: ParameterValue.SizeValue) {
        when (value.size) {
            is Size.Dp -> writer.continueLine("${value.size.value}.dp")
            is Size.Sp -> writer.continueLine("${value.size.value}.sp")
        }
    }

    private fun writeDrawable(value: ParameterValue.DrawableValue) {
        when (value.drawable) {
            is Drawable.ColorValue -> writeColor(
                // Repackaging as ParameterValue.ColorValue. That's a bit hacky. We probably want to re-use the
                // write code instead.
                ParameterValue.ColorValue(value.drawable.color)
            )
            is Drawable.Resource -> {
                writeCall(
                    name = "imageResource",
                    parameters = listOf(
                        CallParameter(ParameterValue.RawValue("R.drawable.${value.drawable.name}"))
                    ),
                    endLine = false
                )
            }
            is Drawable.AndroidResource -> {
                writeCall(
                    name = "imageResource",
                    parameters = listOf(
                        CallParameter(ParameterValue.RawValue("android.R.drawable.${value.drawable.name}"))
                    ),
                    endLine = false
                )
            }
        }
    }

    private fun writeParameterValue(value: ParameterValue) {
        when (value) {
            is ParameterValue.StringValue -> writeString(value)
            is ParameterValue.EmptyLambdaValue -> writeEmptyLambda()
            is ParameterValue.ColorValue -> writeColor(value)
            is ParameterValue.ModifierValue -> writeModifierValue(value)
            is ParameterValue.RawValue -> writer.continueLine(value.raw)
            is ParameterValue.SizeValue -> writeSize(value)
            is ParameterValue.DrawableValue -> writeDrawable(value)
        }
    }

    internal fun writeRelativePositioningConstraint(
        from: String,
        id: Constraints.Id,
        to: String
    ) {
        writer.startLine("$from.linkTo(")
        writeConstraintId(id)
        writer.endLine(".$to)")
    }

    private fun writeConstraintId(id: Constraints.Id) {
        if (id is Constraints.Id.Parent) {
            writer.continueLine("parent")
        } else if (id is Constraints.Id.View) {
            writer.continueLine(id.id)
        }
    }

    private fun writeBlock(block: KotlinWriter.() -> Unit) {
        writer.writeBlock {
            block()
        }
    }

    fun getString(): String {
        return writer.getString()
    }
}
