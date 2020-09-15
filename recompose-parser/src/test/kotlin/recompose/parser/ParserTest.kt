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

import org.junit.Assert.assertEquals
import org.junit.Test
import recompose.ast.Layout
import recompose.ast.attributes.ViewAttributes
import recompose.ast.attributes.ViewGroupAttributes
import recompose.ast.values.Color
import recompose.ast.values.LayoutSize
import recompose.ast.values.Orientation
import recompose.ast.view.ButtonNode
import recompose.ast.view.TextViewNode
import recompose.ast.viewgroup.LinearLayoutNode

class ParserTest {
    @Test
    fun `LinearLayout with TextView and Button`() {
        val parser = Parser()
        val layout = parser.parse(TestData.load("linearlayout-textview-button.xml"))

        assertEquals(
            Layout(listOf(
                LinearLayoutNode(
                    ViewAttributes(LayoutSize.MatchParent, LayoutSize.MatchParent),
                    ViewGroupAttributes(listOf(
                        TextViewNode(
                            ViewAttributes(LayoutSize.WrapContent, LayoutSize.WrapContent),
                            text = "Hello World!",
                            textColor = Color.Absolute(0xFFFF0000)
                        ),
                        ButtonNode(
                            ViewAttributes(LayoutSize.WrapContent, LayoutSize.WrapContent),
                            text = "Click me!"
                        )
                    )),
                    Orientation.Vertical
                )
            )),
            layout
        )
    }
}