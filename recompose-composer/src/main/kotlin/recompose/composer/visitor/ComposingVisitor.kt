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
import recompose.ast.viewgroup.ConstraintLayoutNode
import recompose.ast.viewgroup.LinearLayoutNode
import recompose.composer.writer.KotlinWriter
import recompose.composer.writer.ModifierBuilder
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
        val modifier = ModifierBuilder(node.view)

        writer.writeCall(
            name = "Button",
            parameters = mapOf(
                "onClick" to ParameterValue.EmptyLambdaValue
            )
        ) {
            writeCall(
                name = "Text",
                parameters = mapOf(
                    "text" to ParameterValue.StringValue(node.text),
                    "modifier" to ParameterValue.ModifierValue(modifier).takeIf { it.builder.hasModifiers() }
                )
            )
        }
    }

    override fun visitTextView(node: TextViewNode) {
        val modifier = ModifierBuilder(node.view)

        writer.writeCall(
            name = "Text",
            parameters = mapOf(
                "text" to ParameterValue.StringValue(node.text),
                "color" to node.textColor?.let { ParameterValue.ColoValue(it) },
                "modifier" to ParameterValue.ModifierValue(modifier).takeIf { it.builder.hasModifiers() }
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

    override fun visitConstraintLayout(node: ConstraintLayoutNode) {
        val modifier = ModifierBuilder(node.view)

        // We need to collect and write the constraints here.
        // https://github.com/pocmo/recompose/issues/12
        writer.writeCall(
            name = "ConstraintLayout",
            parameters = mapOf(
                "modifier" to ParameterValue.ModifierValue(modifier).takeIf { it.builder.hasModifiers() }
            )
        ) {
            node.viewGroup.children.forEach { view -> view.accept(this@ComposingVisitor) }
        }
    }
}
