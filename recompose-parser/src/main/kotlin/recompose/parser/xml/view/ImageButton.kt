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

package recompose.parser.xml.view

import org.xmlpull.v1.XmlPullParser
import recompose.ast.view.ImageButtonNode
import recompose.parser.util.assertEndTagNext
import recompose.parser.values.drawable
import recompose.parser.xml.viewAttributes

/**
 * Parses an `<ImageButton>` element.
 *
 * https://developer.android.com/reference/android/widget/ImageButton
 */
fun XmlPullParser.imageButton(): ImageButtonNode {
    val viewAttributes = viewAttributes()
    val compatSrc = drawable("app:srcCompat")
    val src = drawable("android:src")

    assertEndTagNext()

    return ImageButtonNode(
        viewAttributes,
        compatSrc ?: src
    )
}
