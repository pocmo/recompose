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

package com.jds.recompose.nodes

import com.jds.recompose.attributes.ViewAttributes
import com.jds.recompose.values.Orientation
import com.jds.recompose.visitor.Visitor

/**
 * Data class holding values of a parsed `LinearLayout`.
 *
 * https://developer.android.com/reference/android/widget/LinearLayout
 */
class LinearLayoutNode(
    view: ViewAttributes,
    viewGroup: ViewGroupNode,
    val orientation: Orientation = Orientation.Vertical
) : ViewGroupNode(view, viewGroup.viewGroupAttributes) {
    override val name: String
        get() = "LinearLayout"

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinearLayoutNode) return false
        if (!super.equals(other)) return false

        if (orientation != other.orientation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + orientation.hashCode()
        return result
    }

    override fun toString(): String {
        return "LinearLayoutNode(orientation=$orientation, name='$name',view=$view,viewGroup=$viewGroupAttributes)"
    }
}
