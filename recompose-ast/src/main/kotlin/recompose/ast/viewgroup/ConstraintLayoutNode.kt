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

package recompose.ast.viewgroup

import recompose.ast.ViewGroupNode
import recompose.ast.attributes.ViewAttributes
import recompose.ast.attributes.ViewGroupAttributes
import recompose.visitor.Visitor

/**
 * Data class holding data of a parsed "ConstraintLayout".
 *
 * https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout
 */
data class ConstraintLayoutNode(
    override val view: ViewAttributes,
    override val viewGroup: ViewGroupAttributes
) : ViewGroupNode {
    override fun accept(visitor: Visitor) = visitor.visitConstraintLayout(this)
}
