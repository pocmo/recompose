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

package recompose.parser.xml

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParser.END_DOCUMENT
import org.xmlpull.v1.XmlPullParser.START_DOCUMENT
import org.xmlpull.v1.XmlPullParser.START_TAG
import recompose.ast.Layout
import recompose.ast.ViewNode

/**
 * Parses a complete layout structure potentially containing multiple view elements.
 */
internal fun XmlPullParser.layout(): Layout {
    val children = mutableListOf<ViewNode>()

    var event = eventType
    while (event != END_DOCUMENT) {
        when (event) {
            START_DOCUMENT -> Unit
            START_TAG -> {
                val child = view()
                children.add(child)
            }
        }

        event = next()
    }

    return Layout(children)
}
