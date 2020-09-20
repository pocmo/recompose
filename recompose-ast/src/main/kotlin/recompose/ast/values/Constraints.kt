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

package recompose.ast.values

import recompose.ast.Node
import recompose.ast.viewgroup.ConstraintLayoutNode

/**
 * Data class holding the constraints for a [Node] inside a [ConstraintLayoutNode].
 */
data class Constraints(
    val bottomToBottom: Id? = null,
    val bottomToTop: Id? = null,
    val endToEnd: Id? = null,
    val endToStart: Id? = null,
    val leftToLeft: Id? = null,
    val leftToRight: Id? = null,
    val rightToLeft: Id? = null,
    val rightToRight: Id? = null,
    val startToEnd: Id? = null,
    val startToStart: Id? = null,
    val topToBottom: Id? = null,
    val topToTop: Id? = null
) {
    sealed class Id {
        data class View(
            val id: String
        ) : Id()

        object Parent : Id()
    }
}
