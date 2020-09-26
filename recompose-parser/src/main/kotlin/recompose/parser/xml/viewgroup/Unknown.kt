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

package recompose.parser.xml.viewgroup

import org.xmlpull.v1.XmlPullParser
import recompose.ast.viewgroup.UnknownNode
import recompose.parser.xml.viewAttributes
import recompose.parser.xml.viewGroupAttributes

/**
 * Parses an unknown layout element, a custom view or a not yet support View.
 */
fun XmlPullParser.unknown(): UnknownNode {
    return UnknownNode(
        name = name,
        view = viewAttributes(),
        viewGroup = viewGroupAttributes()
    )
}
