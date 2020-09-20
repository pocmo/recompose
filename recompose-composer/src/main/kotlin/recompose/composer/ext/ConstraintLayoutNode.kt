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
import recompose.ast.values.Constraints
import recompose.ast.viewgroup.ConstraintLayoutNode
import recompose.composer.model.Chain

/**
 * Finds all references used in constraints.
 */
internal fun ConstraintLayoutNode.findRefs(): Set<String> {
    return viewGroup.children
        .map { node -> node.view.constraints.collectRefs().map { it.id } + node.getRef() }
        .flatten()
        .toSet()
}

/**
 * Finds all horizontal and vertical chains in the ConstraintLayout.
 */
internal fun ConstraintLayoutNode.findChains(): Set<Chain> {
    return findChainsIn(Chain.Direction.HORIZONTAL) + findChainsIn(Chain.Direction.VERTICAL)
}

/**
 * Finds all chains in the provided [direction].
 */
private fun ConstraintLayoutNode.findChainsIn(direction: Chain.Direction): Set<Chain> {
    val lookupMap = mutableMapOf<String, Node>()
    viewGroup.children.forEach { node ->
        lookupMap[node.getRef()] = node
    }

    val chainBuilder = ChainBuilder(direction)

    viewGroup.children.forEach { node ->
        val tailNode = node.findTailNodeIn(direction, lookupMap)
        if (tailNode != null) {
            chainBuilder.addLink(node, tailNode)
        }
    }

    return chainBuilder.build()
}

/**
 * Finds the tail node of this node.
 *
 * Tail node here means the node following this node in a chain.
 */
@Suppress("ReturnCount")
private fun Node.findTailNodeIn(
    direction: Chain.Direction,
    lookupMap: Map<String, Node>
): Node? {
    val id = view.constraints.tailId(direction) ?: return null
    if (id == Constraints.Id.Parent) {
        return null
    }

    val tailRef = (id as Constraints.Id.View).id
    val tail = lookupMap[tailRef]

    val headId = tail?.view?.constraints?.headId(direction) ?: return null
    if (headId !is Constraints.Id.View) {
        return null
    }

    return if (headId.id == this.getRef()) {
        tail
    } else {
        return null
    }
}

private fun Constraints.tailId(direction: Chain.Direction): Constraints.Id? {
    return when (direction) {
        Chain.Direction.HORIZONTAL -> horizontalTailId()
        Chain.Direction.VERTICAL -> verticalTailId()
    }
}

private fun Constraints.headId(direction: Chain.Direction): Constraints.Id? {
    return when (direction) {
        Chain.Direction.HORIZONTAL -> horizontalHeadId()
        Chain.Direction.VERTICAL -> verticalHeadId()
    }
}

private fun Constraints.horizontalTailId(): Constraints.Id? {
    return when {
        relative.endToStart != null -> relative.endToStart
        relative.rightToLeft != null -> relative.rightToLeft
        else -> null
    }
}

private fun Constraints.verticalTailId(): Constraints.Id? {
    return when {
        relative.bottomToTop != null -> relative.bottomToTop
        else -> null
    }
}

private fun Constraints.horizontalHeadId(): Constraints.Id? {
    return when {
        relative.startToEnd != null -> relative.startToEnd
        relative.leftToRight != null -> relative.leftToRight
        else -> null
    }
}

private fun Constraints.verticalHeadId(): Constraints.Id? {
    return when {
        relative.topToBottom != null -> relative.topToBottom
        else -> null
    }
}

/**
 * Builds a [Set] of [Chain]s from the provided links via [addLink].
 */
private class ChainBuilder(
    private val direction: Chain.Direction
) {
    val chains = mutableMapOf<Node, Set<Node>>()

    private fun headWithTail(chains: Map<Node, Set<Node>>, tail: Node): Node? {
        chains.forEach { (head, chain) ->
            if (chain.last() == tail) {
                return head
            }
        }
        return null
    }

    fun addLink(head: Node, tail: Node) {
        if (tail in chains) {
            // The tail of the new chain is the head of another chain. Let's remove it and make the new head the head
            // of the combined chain
            val existingChain = chains[tail]!!
            chains.remove(tail)
            chains[head] = setOf(tail) + existingChain
            return
        }

        val existingHead = headWithTail(chains, head)
        if (existingHead != null) {
            // The head of the new chain matches the tail of an already existing chain. Add this chain to it.
            val newChain = chains[existingHead]!! + tail
            chains[existingHead] = newChain
            return
        }

        // This is a completely new chain, let's just add it.
        chains[head] = setOf(tail)
    }

    fun build(): Set<Chain> {
        return chains.map { (head, elements) ->
            Chain(direction, head, elements)
        }.toSet()
    }
}
