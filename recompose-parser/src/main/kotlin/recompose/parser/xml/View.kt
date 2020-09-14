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
import org.xmlpull.v1.XmlPullParser.START_TAG
import recompose.ast.ViewNode
import recompose.ast.attributes.ViewAttributes
import recompose.ast.values.LayoutSize
import recompose.parser.Parser
import recompose.parser.values.layoutSize
import recompose.parser.xml.view.button
import recompose.parser.xml.view.textView
import recompose.parser.xml.viewgroup.linearLayout

/**
 * Parses an element representing a `View` implementation.
 */
internal fun XmlPullParser.view(): ViewNode {
    require(START_TAG, null, null)

    return when (name) {
        // ViewGroupNode
        "LinearLayout" -> linearLayout()

        // ViewNode
        "TextView" -> textView()
        "Button" -> button()

        // Currently when hitting an unknown view node, we just bail completely. There's an opportunity
        // to handle this more gracefully. We could either ignore this view or at least translate this into
        // a code comment inside the Compose tree, at the position where we'd call a Composable function if
        // we'd knew which one.
        else -> throw Parser.ParserException("Unknown view node: $name")
    }
}

/**
 * * Parses the attributes that are shared between all `View` implementations.
 */
internal fun XmlPullParser.viewAttributes(): ViewAttributes {
    return ViewAttributes(
        layoutSize("android:layout_width") ?: LayoutSize.WrapContent,
        layoutSize("android:layout_height") ?: LayoutSize.WrapContent,
    )
}
