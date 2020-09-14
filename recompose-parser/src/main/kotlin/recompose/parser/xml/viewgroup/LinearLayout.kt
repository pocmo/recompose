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
import recompose.ast.values.Orientation
import recompose.ast.viewgroup.LinearLayoutNode
import recompose.parser.values.orientation
import recompose.parser.xml.viewGroupAttributes
import recompose.parser.xml.viewAttributes

/**
 * Parses a `<LinearLayout>` element.
 *
 * https://developer.android.com/reference/android/widget/LinearLayout
 */
internal fun XmlPullParser.linearLayout(): LinearLayoutNode {
    return LinearLayoutNode(
        view = viewAttributes(),
        orientation = orientation() ?: Orientation.Vertical,
        viewGroup = viewGroupAttributes()
    )
}
