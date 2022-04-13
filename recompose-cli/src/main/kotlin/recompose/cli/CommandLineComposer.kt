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

package recompose.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import recompose.composer.Composer
import recompose.parser.ParserImpl
import java.io.File
import java.lang.Exception

/**
 * Command-line interface (CLI) for translating layout XML files into Kotlin code using Jetpack Compose.
 */
class CommandLineComposer : CliktCommand(
    name = "recompose"
) {
    private val input by argument(
        help = "Layout XML files to convert to Kotlin"
    ).file(
        mustExist = true,
        canBeDir = false,
        mustBeReadable = true
    ).multiple(
        required = true
    )

    private val outputDirectory by option(
        "-o",
        "--output",
        help = "Output directory for Kotlin code"
    ).file(
        canBeDir = true,
        canBeFile = false,
        mustExist = true,
        mustBeWritable = true
    )

    private val parser = ParserImpl()
    private val composer = Composer()

    override fun run() {
        for (file in input) {
            val code = translate(file) ?: continue

            val target = determineTarget(outputDirectory, file)
            write(target, code)

            println("Translated: ${file.name} -> ${target.path}")
        }
    }

    private fun translate(file: File): String? {
        file.bufferedReader().use { reader ->
            return try {
                val layout = parser.parse(reader)
                composer.compose(layout)
            } catch (e: ParserImpl.ParserException) {
                showError(file, e)
                null
            } catch (e: Composer.ComposerException) {
                showError(file, e)
                null
            }
        }
    }

    private fun write(target: File, code: String) {
        target.outputStream().bufferedWriter().use { writer ->
            writer.write(code)
        }
    }

    private fun determineTarget(outputDirectory: File?, input: File): File {
        val targetDirectory = outputDirectory ?: input.parentFile

        val extension = input.name.lastIndexOf('.')
        val fileName = if (extension != -1) {
            input.name.substring(0, extension) + ".kt"
        } else {
            input.name + ".kt"
        }

        return File(targetDirectory, fileName)
    }

    private fun showError(file: File, e: Exception) {
        System.err.println("Could not translate file: ${file.path}")
        System.err.println(" - ${e.message}")
    }
}
