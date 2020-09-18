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

package recompose.composer.ext

import recompose.ast.values.Constraints

/**
 * Returns true if at least one constraint is set in [Constraints].
 */
internal fun Constraints.hasConstraints(): Boolean {
    return this != Constraints()
}

/**
 * Create all [Constraints.Id.View] of views referenced in the [Constraints].
 */
internal fun Constraints.collectRefs(): Set<Constraints.Id.View> {
    return listOfNotNull(
        bottomToBottom,
        bottomToTop,
        endToEnd,
        endToStart,
        leftToLeft,
        leftToRight,
        rightToLeft,
        rightToRight,
        startToEnd,
        startToStart,
        topToBottom,
        topToTop
    ).filter {
        it != Constraints.Id.Parent
    }.map {
        it as Constraints.Id.View
    }.toSet()
}
