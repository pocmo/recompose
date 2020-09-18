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

package recompose.parser

import org.junit.Test
import recompose.ast.Layout
import recompose.ast.attributes.ViewAttributes
import recompose.ast.attributes.ViewGroupAttributes
import recompose.ast.values.Color
import recompose.ast.values.Constraints
import recompose.ast.values.LayoutSize
import recompose.ast.values.Orientation
import recompose.ast.values.Size
import recompose.ast.view.ButtonNode
import recompose.ast.view.TextViewNode
import recompose.ast.viewgroup.ConstraintLayoutNode
import recompose.ast.viewgroup.LinearLayoutNode
import recompose.test.utils.assertAST

class ParserTest {
    @Test
    fun `LinearLayout with TextView and Button`() {
        assertAST(
            fileName = "linearlayout-textview-button.xml",
            expected = Layout(
                listOf(
                    LinearLayoutNode(
                        ViewAttributes(
                            width = LayoutSize.MatchParent,
                            height = LayoutSize.MatchParent
                        ),
                        ViewGroupAttributes(
                            listOf(
                                TextViewNode(
                                    ViewAttributes(
                                        width = LayoutSize.WrapContent,
                                        height = LayoutSize.WrapContent
                                    ),
                                    text = "Hello World!",
                                    textColor = Color.Absolute(0xFFFF0000)
                                ),
                                ButtonNode(
                                    ViewAttributes(
                                        width = LayoutSize.WrapContent,
                                        height = LayoutSize.WrapContent
                                    ),
                                    text = "Click me!"
                                )
                            )
                        ),
                        Orientation.Vertical
                    )
                )
            )
        )
    }

    @Test
    fun `TextView with absolute dp sizes`() {
        assertAST(
            "textview-absolute-dp-sizes.xml",
            Layout(
                listOf(
                    TextViewNode(
                        ViewAttributes(
                            width = LayoutSize.Absolute(Size.Dp(100)),
                            height = LayoutSize.Absolute(Size.Dp(50))
                        ),
                        text = "I am a test",
                        textColor = null
                    )
                )
            )
        )
    }

    @Test
    fun `ConstraintLayout with Buttons`() {
        assertAST(
            fileName = "constraintlayout-buttons.xml",
            Layout(
                listOf(
                    ConstraintLayoutNode(
                        ViewAttributes(
                            width = LayoutSize.MatchParent,
                            height = LayoutSize.MatchParent
                        ),
                        ViewGroupAttributes(
                            children = listOf(
                                ButtonNode(
                                    ViewAttributes(
                                        id = "button000",
                                        width = LayoutSize.WrapContent,
                                        height = LayoutSize.WrapContent,
                                        constraints = Constraints(
                                            startToStart = Constraints.Id.Parent,
                                            topToTop = Constraints.Id.Parent
                                        )
                                    ),
                                    text = "000"
                                ),
                                ButtonNode(
                                    ViewAttributes(
                                        id = "button001",
                                        width = LayoutSize.WrapContent,
                                        height = LayoutSize.WrapContent,
                                        constraints = Constraints(
                                            endToEnd = Constraints.Id.Parent,
                                            startToStart = Constraints.Id.Parent,
                                            topToBottom = Constraints.Id.View("button000")
                                        )
                                    ),
                                    text = "001"
                                ),
                                ButtonNode(
                                    ViewAttributes(
                                        width = LayoutSize.Absolute(Size.Dp(0)),
                                        height = LayoutSize.WrapContent,
                                        constraints = Constraints(
                                            bottomToBottom = Constraints.Id.Parent,
                                            endToEnd = Constraints.Id.Parent,
                                            startToStart = Constraints.Id.Parent,
                                            topToBottom = Constraints.Id.View("button001")
                                        )
                                    ),
                                    text = "010"
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `TextView with attributes`() {
        assertAST(
            fileName = "textview-attributes.xml",
            Layout(
                children = listOf(
                    TextViewNode(
                        view = ViewAttributes(
                            id = "title",
                            width = LayoutSize.Absolute(Size.Dp(100)),
                            height = LayoutSize.WrapContent
                        ),
                        text = "I am a test",
                        textSize = Size.Dp(20),
                        textColor = Color.Absolute(0xFFFFCC00)
                    )
                )
            )
        )
    }
}
