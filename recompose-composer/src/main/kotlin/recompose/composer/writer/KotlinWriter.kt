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
import recompose.ast.values.Size

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
        parameters: Map<String, ParameterValue?> = emptyMap(),
        block: (KotlinWriter.() -> Unit)? = null
    ) {
        writer.startLine(name)

        writeParameters(parameters, block != null)

        if (block == null) {
            writer.endLine()
        } else {
            writer.endLine(" {")
            writeBlock(block)
            writer.writeLine("}")
        }
    }

    fun writeRefsDeclaration(refs: Set<String>) {
        writer.startLine("val (")
        writer.continueLine(refs.joinToString(", "))
        writer.endLine(") = createRefs()")
        writer.writeLine()
    }

    private fun writeParameters(parameters: Map<String, ParameterValue?>, isFollowedByLambda: Boolean) {
        if (parameters.isEmpty() && isFollowedByLambda) {
            return
        }

        writer.continueLine("(")

        var addComma = false
        parameters.forEach { (key, value) ->
            if (value != null) {
                writeSingleParameter(key, value, addComma)
                addComma = true
            }
        }

        writer.continueLine(")")
    }

    private fun writeSingleParameter(
        key: String,
        value: ParameterValue,
        addComma: Boolean
    ) {
        if (addComma) {
            writer.continueLine(", ")
        }

        writer.continueLine(key)
        writer.continueLine(" = ")
        writeParameterValue(value)
    }

    private fun writeParameterValue(value: ParameterValue) {
        when (value) {
            is ParameterValue.StringValue -> {
                writer.continueLine("\"")
                writer.continueLine(value.raw)
                writer.continueLine("\"")
            }
            is ParameterValue.EmptyLambdaValue -> {
                writer.continueLine("{}")
            }
            is ParameterValue.ColoValue -> {
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
            is ParameterValue.ModifierValue -> {
                var addComma = false
                writer.continueLine("Modifier.")
                value.builder.getModifiers().forEach { modifier ->
                    if (addComma) {
                        writer.continueLine(".")
                    }

                    writer.continueLine(modifier.name)

                    writer.continueLine("(")
                    writer.continueLine(modifier.parameters.joinToString(", "))
                    writer.continueLine(")")

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
            is ParameterValue.RawValue -> {
                writer.continueLine(value.raw)
            }
            is ParameterValue.SizeValue -> {
                when (value.size) {
                    is Size.Dp -> writer.continueLine("${value.size.value}.dp")
                }
            }
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
