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
import com.jds.recompose.values.Color
import com.jds.recompose.values.Size
import com.jds.recompose.visitor.Visitor

/**
 * Data class holding values of a parsed `<TextView>`.
 *
 * https://developer.android.com/reference/kotlin/android/widget/TextView
 */
class TextViewNode(
    view: ViewAttributes,
    val text: String,
    val textColor: Color? = null,
    val textSize: Size? = null,
    val maxLines: Int? = null
) : ViewNode(view) {
    override val name: String
        get() = "TextView"

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TextViewNode) return false
        if (!super.equals(other)) return false

        if (text != other.text) return false
        if (textColor != other.textColor) return false
        if (textSize != other.textSize) return false
        if (maxLines != other.maxLines) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + (textColor?.hashCode() ?: 0)
        result = 31 * result + (textSize?.hashCode() ?: 0)
        result = 31 * result + (maxLines ?: 0)
        return result
    }

    override fun toString(): String {
        return "TextViewNode(text='$text', textColor=$textColor, textSize=$textSize, maxLines=$maxLines, name='$name',view=$view)"
    }
}
