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

import recompose.ast.Node
import java.util.WeakHashMap

private val refMap = WeakHashMap<Node, String>()
private var refCounter = 0

/**
 * Gets a unique reference for this [Node] to be used in a `ConstraintLayout`.
 *
 * If available this returns the ID of the `View`. Otherwise a unique reference will be generated.
 */
@Suppress("ReturnCount")
internal fun Node.getRef(): String {
    // If this view has an ID then we can use this as a unique reference.
    view.id?.apply { return this }

    // Otherwise we create one using the ref counter. Initially I wanted to use `System.identityHashCode` here.
    // But this is less predictable and makes testing awkward. A ref counter with a prefix is sufficient too
    // and depending on the prefix it is also unlikely to conflict with existing view ids.

    // Do we already have a ref for this view?
    refMap[this]?.apply { return this }

    refCounter++
    val ref = "ref_$refCounter"
    refMap[this] = ref

    return ref
}
