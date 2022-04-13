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

package com.jds.recompose.attributes

import com.jds.recompose.values.*

/**
 * Attributes that are shared between all Android `View`s.
 */
data class ViewAttributes(
    val id: Id? = null,
    val width: LayoutSize,
    val height: LayoutSize,
    val padding: Padding = Padding(),
    val background: Drawable? = null,
    val constraints: Constraints = Constraints.EMPTY
) : Attribute {
    companion object {
        val EMPTY = ViewAttributes(null, LayoutSize.MatchParent, LayoutSize.MatchParent, Padding(), null)
    }
}
