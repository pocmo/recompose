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

private const val INDENT = "    "

/**
 * Low-level wrapper around [StringBuilder] dealing with writing and identing lines.
 */
internal class LineWriter {
    private val builder = StringBuilder()
    private var indent = 0

    fun writeBlock(block: LineWriter.() -> Unit) {
        indent++
        block(this)
        indent--
    }

    fun startLine(text: String = "") {
        repeat(indent) { builder.append(INDENT) }
        builder.append(text)
    }

    fun continueLine(text: String) {
        builder.append(text)
    }

    fun endLine() {
        builder.append("\n")
    }

    fun endLine(text: String) {
        continueLine(text)
        endLine()
    }

    fun writeLine(text: String = "") {
        startLine(text)
        endLine()
    }

    fun getString(): String {
        return builder.toString().trim()
    }
}
