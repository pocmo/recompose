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
 * An unknown node that could not be parsed as a node of a specific type (like [TextViewNode]). The node is parsed as a
 * [ViewGroupNode] that may contain children.
 */
class UnknownNode(
    val unknownNodeName: String,
    view: ViewAttributes,
    viewGroup: ViewGroupNode,
) : ViewGroupNode(view, viewGroup.viewGroupAttributes) {
    override val name: String
        get() = "Unknown"

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UnknownNode) return false
        if (!super.equals(other)) return false

        if (unknownNodeName != other.unknownNodeName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + unknownNodeName.hashCode()
        return result
    }

    override fun toString(): String {
        return "UnknownNode(unknownNodeName='$unknownNodeName',view=$view,viewGroup=$viewGroupAttributes)"
    }
}
