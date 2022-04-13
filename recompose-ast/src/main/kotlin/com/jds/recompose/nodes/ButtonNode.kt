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
 * Data class holding values of a parsed `<Button>`.
 *
 * https://developer.android.com/reference/android/widget/Button
 */
class ButtonNode(
    view: ViewAttributes,
    val text: String
) : ViewNode(view) {
    override val name: String
        get() = "Button"

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ButtonNode) return false

        if (view != other.view) return false
        if (text != other.text) return false

        return true
    }

    override fun hashCode(): Int {
        var result = view.hashCode()
        result = 31 * result + text.hashCode()
        return result
    }

    override fun toString(): String {
        return "ButtonNode(text='$text', name='$name',view=$view)"
    }
}
