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
import recompose.ast.values.Size
import recompose.parser.Parser

internal fun XmlPullParser.size(name: String): Size? {
    val value = getAttributeValue(null, name)

    try {
        return when {
            value == null -> null
            value.endsWith("dp") -> Size.Dp(Integer.parseInt(value.substring(0, value.length - 2)))
            value.endsWith("sp") -> Size.Sp(Integer.parseInt(value.substring(0, value.length - 2)))
            else -> throw Parser.ParserException("Unknown size value: $value")
        }
    } catch (e: java.lang.NumberFormatException) {
        throw Parser.ParserException("Cannot parse layout size: $value")
    }
}
