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
import recompose.ast.values.Drawable
import recompose.parser.Parser

/**
 * Parses a [Drawable] attribute.
 */
@Suppress("MagicNumber")
fun XmlPullParser.drawable(name: String): Drawable? {
    val value = getAttributeValue(null, name)

    return when {
        value == null -> null

        value.startsWith("#") -> color(name)?.let { Drawable.ColorValue(it) }

        value.startsWith("@drawable/") -> Drawable.Resource(name = value.substring(10))

        value.startsWith("@android:drawable/") -> Drawable.AndroidResource(name = value.substring(18))

        value.startsWith("?") -> Drawable.StyleAttribute(name = value.substring(1))

        else -> throw Parser.ParserException("Unknown drawable format: $value")
    }
}
