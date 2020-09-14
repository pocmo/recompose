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

package recompose.parser.values

import org.xmlpull.v1.XmlPullParser
import recompose.ast.values.Color
import recompose.parser.Parser

/**
 * Parses a color attribute and returns the matching [Color] object. Throws [Parser.ParserException] if the color could
 * not be parsed.
 */
internal fun XmlPullParser.color(name: String): Color? {
    val raw = getAttributeValue(null, name)

    return when {
        raw == null -> null

        raw.startsWith("#") -> try {
            val colorAsString = raw.substring(1)
            val colorToParse = if (colorAsString.length == 6) {
                "ff$colorAsString"
            } else {
                colorAsString
            }

            Color.Absolute(colorToParse.toLong(16))
        } catch (e: NumberFormatException) {
            throw Parser.ParserException("Could not parse color: $raw", e)
        }

        // There are multiple more color formats that need to be supported.
        else -> throw Parser.ParserException("Unknown color format: $raw")
    }
}