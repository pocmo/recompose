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

/**
 * Helper class for writing Kotlin code to a String.
 *
 * It's a bit rough.. But does the job.
 */
@Suppress("TooManyFunctions")
internal class KotlinWriter {
    private val builder = StringBuilder()
    private var indent = 0

    fun writeCall(
        name: String,
        parameters: Map<String, ParameterValue?> = emptyMap(),
        block: (KotlinWriter.() -> Unit)? = null
    ) {
        startLine(name)

        writeParameters(parameters, block != null)

        if (block == null) {
            endLine()
        } else {
            endLine(" {")
            writeBlock(block)
            writeLine("}")
        }
    }

    fun writeParameters(parameters: Map<String, ParameterValue?>, isFollowedByLambda: Boolean) {
        if (parameters.isEmpty() && isFollowedByLambda) {
            return
        }

        continueLine("(")

        var addComma = false
        parameters.forEach { (key, value) ->
            if (value != null) {
                writeSingleParameter(key, value, addComma)
                addComma = true
            }
        }

        continueLine(")")
    }

    private fun writeSingleParameter(
        key: String,
        value: ParameterValue,
        addComma: Boolean
    ) {
        if (addComma) {
            continueLine(", ")
        }

        continueLine(key)
        continueLine(" = ")
        writeParameterValue(value)
    }

    fun writeParameterValue(value: ParameterValue) {
        when (value) {
            is ParameterValue.StringValue -> {
                continueLine("\"")
                continueLine(value.raw)
                continueLine("\"")
            }
            is ParameterValue.EmptyLambdaValue -> {
                continueLine("{}")
            }
            is ParameterValue.ColoValue -> {
                when (val color = value.color) {
                    is Color.Absolute -> {
                        continueLine("Color(")
                        continueLine("0x")
                        @Suppress("MagicNumber")
                        continueLine(color.value.toString(16))
                        continueLine(".toInt()")
                        continueLine(")")
                    }
                }
            }
            is ParameterValue.ModifierValue -> {
                var addComma = false
                continueLine("Modifier.")
                value.builder.getModifiers().forEach { (name, values) ->
                    if (addComma) {
                        continueLine(".")
                    }
                    continueLine(name)
                    continueLine("(")
                    continueLine(values.joinToString(", "))
                    continueLine(")")
                    addComma = true
                }
            }
        }
    }

    fun writeBlock(block: KotlinWriter.() -> Unit) {
        indent++
        block(this)
        indent--
    }

    fun startLine(text: String) {
        repeat(indent) { builder.append("    ") }
        builder.append(text)
    }

    private fun continueLine(text: String) {
        builder.append(text)
    }

    private fun endLine() {
        builder.append("\n")
    }

    private fun endLine(text: String) {
        continueLine(text)
        endLine()
    }

    private fun writeLine(text: String) {
        startLine(text)
        endLine()
    }

    fun getString(): String {
        return builder.toString().trim()
    }
}
