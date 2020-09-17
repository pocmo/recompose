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

package recompose.composer.writer

import recompose.ast.attributes.ViewAttributes
import recompose.ast.values.LayoutSize

class ModifierBuilder(
    viewAttributes: ViewAttributes
) {
    private val modifiers = mutableMapOf<String, List<String>>()

    init {
        when (viewAttributes.width) {
            is LayoutSize.Dp -> addLayoutSize("width", viewAttributes.width as LayoutSize.Dp)
            is LayoutSize.MatchParent -> modifiers["fillMaxWidth"] = emptyList()
        }

        when (viewAttributes.height) {
            is LayoutSize.Dp -> addLayoutSize("height", viewAttributes.height as LayoutSize.Dp)
            is LayoutSize.MatchParent -> modifiers["fillMaxHeight"] = emptyList()
        }
    }

    fun addLayoutSize(name: String, size: LayoutSize.Dp) {
        modifiers[name] = listOf("${size.value}.dp")
    }

    fun getModifiers(): Map<String, List<String>> {
        return modifiers
    }

    fun hasModifiers(): Boolean {
        return modifiers.isNotEmpty()
    }
}
