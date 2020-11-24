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

package recompose.composer.writer

import recompose.ast.Node
import recompose.ast.values.LayoutSize
import recompose.ast.values.Padding
import recompose.ast.values.Size
import recompose.composer.ext.getRef
import recompose.composer.ext.hasConstraints
import recompose.composer.ext.hasValues

internal class ModifierBuilder(
    node: Node? = null
) {
    private val modifiers = mutableListOf<Modifier>()

    init {
        if (node != null) {
            addViewModifiers(node)
        }
    }

    fun add(modifier: Modifier) {
        modifiers.add(modifier)
    }

    fun addSize(name: String, size: Size) {
        modifiers.add(
            Modifier(
                name,
                listOf(
                    CallParameter(ParameterValue.SizeValue(size))
                )
            )
        )
    }

    fun getModifiers(): List<Modifier> {
        return modifiers
    }

    fun hasModifiers(): Boolean {
        return modifiers.isNotEmpty()
    }

    private fun addViewModifiers(node: Node) {
        val view = node.view

        when (view.width) {
            is LayoutSize.Absolute ->
                // Don't add the width modifier if it is equal to 0. This is needed to ignore the width parameter in
                // ConstraintLayout's children when the width is match_constraints (0dp)
                if (view.width != LayoutSize.Absolute(Size.Dp(0)) || !view.constraints.hasConstraints()) {
                    addSize("width", (view.width as LayoutSize.Absolute).size)
                }
            is LayoutSize.MatchParent -> add(Modifier("fillMaxWidth"))
        }

        when (view.height) {
            is LayoutSize.Absolute ->
                // Don't add the height modifier if it is equal to 0. This is needed to ignore the width parameter in
                // ConstraintLayout's children when the height is match_constraints (0dp)
                if (view.height != LayoutSize.Absolute(Size.Dp(0)) || !view.constraints.hasConstraints()) {
                    addSize("height", (view.height as LayoutSize.Absolute).size)
                }
            is LayoutSize.MatchParent -> add(Modifier("fillMaxHeight"))
        }

        view.background?.let { drawable ->
            add(
                Modifier(
                    name = "background",
                    parameters = listOf(
                        CallParameter(ParameterValue.DrawableValue(drawable))
                    )
                )
            )
        }

        if (view.constraints.hasConstraints()) {
            addConstraints(node)
        }

        if (view.padding.hasValues()) {
            addPadding(view.padding)
        }
    }

    private fun addConstraints(node: Node) {
        val constraints = node.view.constraints
        add(
            Modifier("constrainAs", listOf(CallParameter(ParameterValue.RawValue(node.getRef())))) {
                constraints.relative.bottomToBottom?.let { writeRelativePositioningConstraint("bottom", it, "bottom") }
                constraints.relative.bottomToTop?.let { writeRelativePositioningConstraint("bottom", it, "top") }
                constraints.relative.endToEnd?.let { writeRelativePositioningConstraint("end", it, "end") }
                constraints.relative.endToStart?.let { writeRelativePositioningConstraint("end", it, "start") }
                constraints.relative.leftToLeft?.let { writeRelativePositioningConstraint("left", it, "left") }
                constraints.relative.leftToRight?.let { writeRelativePositioningConstraint("left", it, "right") }
                constraints.relative.rightToLeft?.let { writeRelativePositioningConstraint("right", it, "left") }
                constraints.relative.rightToRight?.let { writeRelativePositioningConstraint("right", it, "right") }
                constraints.relative.startToEnd?.let { writeRelativePositioningConstraint("start", it, "end") }
                constraints.relative.startToStart?.let { writeRelativePositioningConstraint("start", it, "start") }
                constraints.relative.topToBottom?.let { writeRelativePositioningConstraint("top", it, "bottom") }
                constraints.relative.topToTop?.let { writeRelativePositioningConstraint("top", it, "top") }
                writeSizeConstraint("width", node.view.width)
                writeSizeConstraint("height", node.view.height)
            }
        )
    }

    @Suppress("ComplexCondition")
    private fun addPadding(padding: Padding) {
        if (padding.all != null) {
            add(
                Modifier(
                    name = "padding",
                    parameters = listOf(
                        CallParameter(ParameterValue.SizeValue(padding.all!!))
                    )
                )
            )
        }

        if (padding.horizontal != null || padding.vertical != null) {
            add(
                Modifier(
                    name = "padding",
                    parameters = listOf(
                        createCallParameter("horizontal", createSizeParameterValue(padding.horizontal)),
                        createCallParameter("vertical", createSizeParameterValue(padding.vertical))
                    )
                )
            )
        }

        if (padding.left != null || padding.right != null) {
            add(
                Modifier(
                    name = "absolutePadding",
                    parameters = listOf(
                        createCallParameter("left", createSizeParameterValue(padding.left)),
                        createCallParameter("right", createSizeParameterValue(padding.right)),
                        createCallParameter("top", createSizeParameterValue(padding.top)),
                        createCallParameter("bottom", createSizeParameterValue(padding.bottom))
                    )
                )
            )
        } else if (padding.start != null || padding.end != null || padding.top != null || padding.bottom != null) {
            Modifier(
                name = "padding",
                parameters = listOf(
                    createCallParameter("start", createSizeParameterValue(padding.start)),
                    createCallParameter("end", createSizeParameterValue(padding.end)),
                    createCallParameter("top", createSizeParameterValue(padding.top)),
                    createCallParameter("bottom", createSizeParameterValue(padding.bottom))
                )
            )
        }
    }

    fun toCallParameter(): CallParameter? {
        if (!hasModifiers()) {
            return null
        }

        return CallParameter(
            name = "modifier",
            value = ParameterValue.ModifierValue(this)
        )
    }
}

internal data class Modifier(
    val name: String,
    val parameters: List<CallParameter?> = emptyList(),
    val lambda: (KotlinWriter.() -> Unit)? = null
)
