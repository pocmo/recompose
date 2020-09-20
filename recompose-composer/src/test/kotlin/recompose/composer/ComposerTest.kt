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

package recompose.composer

import org.junit.Test
import recompose.test.utils.assertComposing

class ComposerTest {
    @Test
    fun `LinearLayout with TextView and Button`() {
        assertComposing(
            fileName = "linearlayout-textview-button.xml",
            """
                Column {
                    Text(text = "Hello World!", color = Color(0xffff0000.toInt()))
                    Button(onClick = {}) {
                        Text(text = "Click me!", textAlign = TextAlign.Center)
                    }
                }
            """.trimIndent()
        )
    }

    @Test
    fun `TextView with absolute dp sizes`() {
        assertComposing(
            fileName = "textview-absolute-dp-sizes.xml",
            """
                Text(text = "I am a test", modifier = Modifier.width(100.dp).height(50.dp))
            """.trimIndent()
        )
    }

    @Test
    fun `ConstraintLayout with Buttons`() {
        assertComposing(
            fileName = "constraintlayout-buttons.xml",
            """
                ConstraintLayout(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                    val (button000, button001, ref_1) = createRefs()
                    
                    Button(onClick = {}, modifier = Modifier.constrainAs(button000) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }) {
                        Text(text = "000", textAlign = TextAlign.Center)
                    }
                    Button(onClick = {}, modifier = Modifier.constrainAs(button001) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        top.linkTo(button000.bottom)
                    }) {
                        Text(text = "001", textAlign = TextAlign.Center)
                    }
                    Button(onClick = {}, modifier = Modifier.width(0.dp).constrainAs(ref_1) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        top.linkTo(button001.bottom)
                    }) {
                        Text(text = "010", textAlign = TextAlign.Center)
                    }
                }
            """.trimIndent()
        )
    }

    @Test
    fun `TextView with attributes`() {
        assertComposing(
            fileName = "textview-attributes.xml",
            """
                Text(text = "I am a test", color = Color(0xffffcc00.toInt()), fontSize = 20.sp, modifier = Modifier.width(100.dp).background(Color(0xaa0000ff.toInt())))
            """.trimIndent()
        )
    }

    @Test
    fun `TextViews with different padding configurations`() {
        assertComposing(
            fileName = "textview-padding.xml",
            """
                Column {
                    Text(text = "padding", modifier = Modifier.background(Color(0xffff0000.toInt())).padding(10.dp))
                    Text(text = "paddingTop/Left", modifier = Modifier.background(Color(0xff00ff00.toInt())).absolutePadding(left = 10.dp, top = 10.dp))
                    Text(text = "paddingRight/Bottom", modifier = Modifier.background(Color(0xff0000ff.toInt())).absolutePadding(right = 10.dp, bottom = 10.dp))
                    Text(text = "paddingStartEnd", modifier = Modifier.background(Color(0xff00ffff.toInt())))
                    Text(text = "paddingHorizontalVertically", modifier = Modifier.background(Color(0xffff00ff.toInt())).padding(horizontal = 10.dp, vertical = 10.dp))
                }
            """.trimIndent()
        )
    }

    @Test
    fun `Basic Views`() {
        assertComposing(
            fileName = "view.xml",
            """
                Row {
                    Box(modifier = Modifier.width(50.dp).height(50.dp).background(Color(0xffff0000.toInt())))
                    Box(modifier = Modifier.width(50.dp).height(50.dp).background(Color(0xff00ff00.toInt())))
                    Box(modifier = Modifier.width(50.dp).height(50.dp).background(Color(0xff0000ff.toInt())))
                }
            """.trimIndent()
        )
    }

    @Test
    fun `ConstraintLayout chains`() {
        assertComposing(
            fileName = "constraintlayout-chains.xml",
            """
                ConstraintLayout(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                    val (two, one, four, three, five) = createRefs()
                    
                    createHorizontalChain(one, two, three)
                    createHorizontalChain(four, five)
                    createVerticalChain(two, four)
                    
                    Box(modifier = Modifier.width(50.dp).height(50.dp).background(Color(0xffff0000.toInt())).constrainAs(one) {
                        end.linkTo(two.start)
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    })
                    Box(modifier = Modifier.width(50.dp).height(50.dp).background(Color(0xff00ff00.toInt())).constrainAs(two) {
                        bottom.linkTo(four.top)
                        end.linkTo(three.start)
                        start.linkTo(one.end)
                        top.linkTo(parent.top)
                    })
                    Box(modifier = Modifier.width(50.dp).height(50.dp).background(Color(0xff0000ff.toInt())).constrainAs(three) {
                        end.linkTo(parent.end)
                        start.linkTo(two.end)
                        top.linkTo(parent.top)
                    })
                    Box(modifier = Modifier.width(50.dp).height(50.dp).background(Color(0xffff00ff.toInt())).constrainAs(four) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(five.start)
                        start.linkTo(two.start)
                        top.linkTo(two.bottom)
                    })
                    Box(modifier = Modifier.width(50.dp).height(50.dp).background(Color(0xffffff00.toInt())).constrainAs(five) {
                        end.linkTo(parent.end)
                        start.linkTo(four.end)
                        top.linkTo(two.bottom)
                    })
                }
            """.trimIndent()
        )
    }
}
