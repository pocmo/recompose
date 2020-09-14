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

package recompose.composer

import recompose.ast.Layout
import recompose.composer.visitor.ComposingVisitor

/**
 * The Composer takes a [Layout] as input and turns it into a String of Kotlin code calling `Composable`s.
 *
 * Currently this just returns a String. Eventually this needs to return a more rich data structure that holds
 * additional information, like for example the list of classes that need to be automatically imported when pasting
 * this code.
 */
class Composer {
    fun compose(layout: Layout): String {
        val visitor = ComposingVisitor()
        layout.accept(visitor)
        return visitor.getResult()
    }
}
