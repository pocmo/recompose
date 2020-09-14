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

package recompose.parser.util

import org.xmlpull.v1.XmlPullParser
import recompose.parser.Parser

/**
 * Moves to the next parse event and assets that it is an END_TAG event. Otherwise throws a
 * [Parser.ParserException].
 */
internal fun XmlPullParser.assertEndTagNext() {
    if (next() != XmlPullParser.END_TAG) {
        throw Parser.ParserException("Expected END_TAG event, but got $eventType")
    }
}
