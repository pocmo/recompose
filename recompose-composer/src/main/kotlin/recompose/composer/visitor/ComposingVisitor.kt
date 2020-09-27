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
import recompose.ast.view.*
import recompose.ast.viewgroup.CardViewNode
import recompose.ast.viewgroup.ConstraintLayoutNode
import recompose.ast.viewgroup.LinearLayoutNode
import recompose.ast.viewgroup.UnknownNode
import recompose.composer.ext.findChains
import recompose.composer.ext.findRefs
import recompose.composer.writer.CallParameter
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

    override fun visitView(node: ViewNode) {
        val modifier = ModifierBuilder(node)

        writer.writeCall(
            "Box",
            parameters = listOf(
                modifier.toCallParameter()
            )
        )
    }

    override fun visitButton(node: ButtonNode) {
        val modifier = ModifierBuilder(node)

        writer.writeCall(
            name = "Button",
            parameters = listOf(
                CallParameter(name = "onClick", value = ParameterValue.EmptyLambdaValue),
                modifier.toCallParameter()
            )
        ) {
            writeCall(
                name = "Text",
                parameters = listOf(
                    CallParameter(name = "text", value = ParameterValue.StringValue(node.text)),
                    CallParameter(name = "textAlign", value = ParameterValue.RawValue("TextAlign.Center"))
                )
            )
        }
    }

    override fun visitTextView(node: TextViewNode) {
        val modifier = ModifierBuilder(node)

        writer.writeCall(
            name = "Text",
            parameters = listOf(
                CallParameter(name = "text", value = ParameterValue.StringValue(node.text)),
                node.textColor?.let { CallParameter(name = "color", value = ParameterValue.ColoValue(it)) },
                node.textSize?.let { CallParameter(name = "fontSize", value = ParameterValue.SizeValue(it)) },
                modifier.toCallParameter()
            )
        )
    }

    override fun visitCardView(node: CardViewNode) {
        val modifier = ModifierBuilder(node)

        writer.writeCall(
            name = "Card",
            parameters = listOf(
                modifier.toCallParameter()
            )
        ) {
            node.viewGroup.children.forEach{ view -> view.accept(this@ComposingVisitor) }
        }
    }

    override fun visitImageView(node: ImageViewNode) {
        val modifier = ModifierBuilder(node)

        // Translate "src" to resource wrapped in imageResource().
        // https://github.com/pocmo/recompose/issues/9
        writer.writeCall(
            name = "Image",
            parameters = listOf(
                modifier.toCallParameter()
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
        val modifier = ModifierBuilder(node)

        writer.writeCall(
            name = "ConstraintLayout",
            parameters = listOf(
                modifier.toCallParameter()
            )
        ) {
            val refs = node.findRefs()
            if (refs.isNotEmpty()) {
                writer.writeRefsDeclaration(refs)
            }

            val chains = node.findChains()
            if (chains.isNotEmpty()) {
                writer.writeChains(chains)
            }

            node.viewGroup.children.forEach { view -> view.accept(this@ComposingVisitor) }
        }
    }

    override fun visitUnknown(node: UnknownNode) {
        val block: (KotlinWriter.() -> Unit)? = if (node.viewGroup.children.isEmpty()) {
            null
        } else {
            { node.viewGroup.children.forEach { view -> view.accept(this@ComposingVisitor) } }
        }

        val modifier = ModifierBuilder(node)

        writer.writeCall(
            node.name,
            parameters = listOf(modifier.toCallParameter()),
            linePrefix = "// ",
            block = block
        )
    }
}
