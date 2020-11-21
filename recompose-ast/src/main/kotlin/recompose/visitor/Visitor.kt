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

package recompose.visitor

import recompose.ast.Layout
import recompose.ast.view.ButtonNode
import recompose.ast.view.CheckBoxNode
import recompose.ast.view.EditTextNode
import recompose.ast.view.ImageButtonNode
import recompose.ast.view.ImageViewNode
import recompose.ast.view.SwitchNode
import recompose.ast.view.TextViewNode
import recompose.ast.view.ViewNode
import recompose.ast.viewgroup.CardViewNode
import recompose.ast.viewgroup.ConstraintLayoutNode
import recompose.ast.viewgroup.FrameLayoutNode
import recompose.ast.viewgroup.LinearLayoutNode
import recompose.ast.viewgroup.UnknownNode

/**
 * Interface for a visitor for performing operations on a parsed [Layout].
 */
@Suppress("TooManyFunctions")
interface Visitor {
    fun visitLayout(layout: Layout)
    fun visitView(node: ViewNode)
    fun visitButton(node: ButtonNode)
    fun visitTextView(node: TextViewNode)
    fun visitEditText(node: EditTextNode)
    fun visitCardView(node: CardViewNode)
    fun visitImageView(node: ImageViewNode)
    fun visitLinearLayout(node: LinearLayoutNode)
    fun visitFrameLayout(node: FrameLayoutNode)
    fun visitCheckBox(node: CheckBoxNode)
    fun visitConstraintLayout(node: ConstraintLayoutNode)
    fun visitUnknown(node: UnknownNode)
    fun visitSwitch(node: SwitchNode)
    fun visitImageButton(node: ImageButtonNode)
}
