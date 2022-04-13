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
import com.jds.recompose.visitor.Visitor

/**
 * Data class holding values of a parsed `<View>`.
 *
 * Note that other nodes like [TextViewNode] do not extend [ViewNode]. We are not rebuilding the Android inheritance
 * tree. Instead nodes may implement a shared interface with shared "attribute" properties (for example see
 * [ViewGroupNode] an [ViewGroupAttributes]).
 *
 * https://developer.android.com/reference/android/view/View
 */
open class ViewNode(val view: ViewAttributes) : Node() {
    override val name: String
        get() = "View"

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ViewNode) return false

        if (view != other.view) return false

        return true
    }

    override fun hashCode(): Int {
        return view.hashCode()
    }

    override fun toString(): String {
        return "ViewNode(view=$view)"
    }
}
