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
import com.jds.recompose.values.InputType
import com.jds.recompose.visitor.Visitor

/**
 * Data class holding the attributes of a parsed `<EditText>`.
 *
 * https://developer.android.com/reference/android/widget/EditText
 */
class EditTextNode(
    view: ViewAttributes,
    val text: String,
    val inputType: InputType,
    val hint: String,
    val textColorHint: Color? = null,
) : ViewNode(view) {

    override val name: String
        get() = "EditText"

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EditTextNode) return false
        if (!super.equals(other)) return false

        if (text != other.text) return false
        if (inputType != other.inputType) return false
        if (hint != other.hint) return false
        if (textColorHint != other.textColorHint) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + inputType.hashCode()
        result = 31 * result + hint.hashCode()
        result = 31 * result + (textColorHint?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "EditTextNode(text='$text', inputType=$inputType, hint='$hint', textColorHint=$textColorHint, name='$name',view=$view)"
    }
}
