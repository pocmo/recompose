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
import recompose.ast.values.Padding

fun XmlPullParser.padding(): Padding {
    return Padding(
        all = size("android:padding"),
        left = size("android:paddingLeft"),
        right = size("android:paddingRight"),
        start = size("android:paddingStart"),
        end = size("android:paddingEnd"),
        top = size("android:paddingTop"),
        bottom = size("android:paddingBottom"),
        horizontal = size("android:paddingHorizontal"),
        vertical = size("android:paddingVertical")
    )
}
