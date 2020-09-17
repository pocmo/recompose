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

import org.junit.Assert.assertEquals
import org.junit.Test
import recompose.composer.Composer
import recompose.parser.Parser

class ComposerTest {
    @Test
    fun `LinearLayout with TextView and Button`() {
        val parser = Parser()
        val layout = parser.parse(TestData.load("linearlayout-textview-button.xml"))

        val composer = Composer()
        val result = composer.compose(layout)

        assertEquals(
            """
                Column {
                    Text(text = "Hello World!", color = Color(0xffff0000.toInt()))
                    Button(onClick = {}) {
                        Text(text = "Click me!", textAlign = TextAlign.Center)
                    }
                }
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `TextView with absolute dp sizes`() {
        val parser = Parser()
        val layout = parser.parse(TestData.load("textview-absolute-dp-sizes.xml"))

        val composer = Composer()
        val result = composer.compose(layout)

        assertEquals(
            """
                Text(text = "I am a test", modifier = Modifier.width(100.dp).height(50.dp))
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `ConstraintLayout with Buttons`() {
        val parser = Parser()
        val layout = parser.parse(TestData.load("constraintlayout-buttons.xml"))

        val composer = Composer()
        val result = composer.compose(layout)

        assertEquals(
            """
                ConstraintLayout(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                    Button(onClick = {}) {
                        Text(text = "000", textAlign = TextAlign.Center)
                    }
                    Button(onClick = {}) {
                        Text(text = "001", textAlign = TextAlign.Center)
                    }
                    Button(onClick = {}) {
                        Text(text = "010", textAlign = TextAlign.Center, modifier = Modifier.width(0.dp))
                    }
                }
            """.trimIndent(),
            result
        )
    }
}
