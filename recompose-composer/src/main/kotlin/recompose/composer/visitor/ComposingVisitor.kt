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

package recompose.composer.visitor

import recompose.ast.Layout
import recompose.ast.values.Orientation
import recompose.ast.view.ButtonNode
import recompose.ast.view.TextViewNode
import recompose.ast.viewgroup.LinearLayoutNode
import recompose.composer.writer.KotlinWriter
import recompose.composer.writer.ParameterValue
import recompose.visitor.Visitor

/**
 * [Visitor] implementation that traverses the parsed [Layout] and transforms it into `Composable" calls.
 */
internal class ComposingVisitor : Visitor {
    private val writer = KotlinWriter()

    fun getResult(): String {
        return writer.getString()
    }

    override fun visitLayout(layout: Layout) {
        layout.children.forEach { view -> view.accept(this) }
    }

    override fun visitButton(node: ButtonNode) {
        writer.writeCall(
            name = "Button",
            parameters = mapOf(
                "onClick" to ParameterValue.EmptyLambdaValue
            )
        ) {
            writeCall(
                name = "Text",
                parameters = mapOf(
                    "text" to ParameterValue.StringValue(node.text)
                )
            )
        }
    }

    override fun visitTextView(node: TextViewNode) {
        writer.writeCall(
            name = "Text",
            parameters = mapOf(
                "text" to ParameterValue.StringValue(node.text),
                "color" to node.textColor?.let { ParameterValue.ColoValue(it) }
            )
        )
    }

    override fun visitLinearLayout(node: LinearLayoutNode) {
        val composable = when (node.orientation) {
            Orientation.Vertical -> "Column"
            Orientation.Horizontal -> "Row"
        }

        writer.writeCall(composable) {
            node.viewGroup.children.forEach { view -> view.accept(this@ComposingVisitor) }
        }
    }
}
