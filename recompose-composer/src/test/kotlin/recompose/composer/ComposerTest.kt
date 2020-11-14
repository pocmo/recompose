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
    fun `TextView with text color resource reference`() {
        assertComposing(
            fileName = "textview-color-resource-reference.xml",
            """
                Text(text = "color ref", color = Color(ContextCompat.getColor(ContextAmbient.current, R.color.green)))
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
                Text(text = "I am a test", color = Color(0xffffcc00.toInt()), fontSize = 20.sp, maxLines = 100, modifier = Modifier.width(100.dp).background(Color(0xaa0000ff.toInt())))
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
                    
                    createHorizontalChain(one, two, three, chainStyle = ChainStyle.Spread)
                    createHorizontalChain(four, five)
                    createVerticalChain(two, four, chainStyle = ChainStyle.Packed)
                    
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

    @Test
    fun `ConstraintLayout rendering Face`() {
        assertComposing(
            fileName = "constraintlayout-face.xml",
            """
                ConstraintLayout(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                    val (eye_right, eye_left, nose, mouth) = createRefs()
                    
                    createHorizontalChain(eye_left, eye_right, chainStyle = ChainStyle.Packed)
                    
                    Text(text = "👁", fontSize = 50.sp, modifier = Modifier.background(Color(0xffffcc00.toInt())).constrainAs(eye_left) {
                        end.linkTo(eye_right.start)
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }.absolutePadding(left = 10.dp))
                    Text(text = "👁", fontSize = 50.sp, modifier = Modifier.background(Color(0xffffcc00.toInt())).constrainAs(eye_right) {
                        end.linkTo(parent.end)
                        start.linkTo(eye_left.end)
                        top.linkTo(parent.top)
                    }.absolutePadding(right = 10.dp))
                    Text(text = "👃", fontSize = 50.sp, modifier = Modifier.background(Color(0xffffcc00.toInt())).constrainAs(nose) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        top.linkTo(eye_right.bottom)
                    }.absolutePadding(left = 20.dp, right = 20.dp))
                    Text(text = "👄", fontSize = 50.sp, modifier = Modifier.background(Color(0xffffcc00.toInt())).constrainAs(mouth) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        top.linkTo(nose.bottom)
                    }.padding(5.dp))
                }
            """.trimIndent()
        )
    }

    @Test
    fun `Unknown view`() {
        assertComposing(
            fileName = "unknown-view.xml",
            """
                // xyz.pocmo.AwesomeView(modifier = Modifier.width(100.dp).height(100.dp).background(Color(0xffffcc00.toInt())))
            """.trimIndent()
        )
    }

    @Test
    fun `Unknown view with children`() {
        assertComposing(
            fileName = "unknown-view-with-children.xml",
            """
                // xyz.pocmo.AwesomeGroup(modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color(0xffffcc00.toInt()))) {
                    Text(text = "Hello", modifier = Modifier.background(Color(0xffff0000.toInt())).padding(10.dp))
                    // xyz.pocmo.AwesomeView(modifier = Modifier.background(Color(0xffffcc00.toInt())))
                // }
            """.trimIndent()
        )
    }

    @Test
    fun `Basic ImageView`() {
        assertComposing(
            fileName = "imageview.xml",
            """
                Image(imageResource(R.drawable.ic_lock_power_off), modifier = Modifier.width(100.dp).height(100.dp))
            """.trimIndent()
        )
    }

    @Test
    fun `ImageView - srcCompat`() {
        assertComposing(
            fileName = "imageview-appcompat.xml",
            """
                Image(imageResource(R.drawable.ic_lock_power_off), modifier = Modifier.width(100.dp).height(100.dp))
            """.trimIndent()
        )
    }

    @Test
    fun `ImageView - Android resource reference`() {
        assertComposing(
            fileName = "imageview-android-reference.xml",
            """
                Image(imageResource(android.R.drawable.ic_lock_power_off), modifier = Modifier.width(100.dp).height(100.dp))
            """.trimIndent()
        )
    }

    @Test
    fun `CardView with TextView and Button`() {
        assertComposing(
            fileName = "cardview-textview-button.xml",
            """
                Card(modifier = Modifier.width(300.dp).height(100.dp)) {
                    Column {
                        Text(text = "Hello World!", color = Color(0xffff0000.toInt()))
                        Button(onClick = {}) {
                            Text(text = "Click me!", textAlign = TextAlign.Center)
                        }
                    }
                }
            """.trimIndent()
        )
    }

    @Test
    fun `Basic CheckBox`() {
        assertComposing(
            fileName = "checkbox.xml",
            """
                Row(modifier = Modifier.fillMaxWidth()) {
                    Checkbox(checked = true, onCheckedChange = {})
                    Text("Hello World", modifier = Modifier.align(Alignment.CenterVertically))
                }
            """.trimIndent()
        )
    }

    @Test
    fun `Basic EditText`() {
        assertComposing(
            fileName = "edittext.xml",
            """
                TextField(value = "", onValueChange = {}, keyboardType = KeyboardType.Text, label = { Text(text = "text", color = Color(ContextCompat.getColor(ContextAmbient.current, R.color.green))) }, modifier = Modifier.width(200.dp).height(50.dp))
            """.trimIndent()
        )
    }

    @Test
    fun `FrameLayout with TextView and Button`() {
        assertComposing(
            fileName = "framelayout-textview-button.xml",
            """
                Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(16.dp)) {
                    Text(text = "Center", fontSize = 20.sp, modifier = Modifier.fillMaxWidth().fillMaxHeight())
                    Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Button", textAlign = TextAlign.Center)
                    }
                }
            """.trimIndent()
        )
    }

    @Test
    fun `Basic Style Attributes`() {
        assertComposing(
            fileName = "styleattributes.xml",
            """
                Text(text = "", modifier = Modifier.width(200.dp).height(50.dp).background(imageAttribute(selectableItemBackground)))
            """.trimIndent()
        )
    }
}
