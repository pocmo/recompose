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

package recompose.ast.values

/**
 * Data classes holding `Drawable` values.
 */
sealed class Drawable {
    /**
     * A color value drawable, e.g. #ffcc00.
     */
    data class ColorValue(val color: Color) : Drawable()

    /**
     * A resource reference drawable, e.g. @drawable/my_image.
     */
    data class Resource(val name: String) : Drawable()

    /**
     * An Android resource reference drawable, e.g. @android:drawable/an_image.
     */
    data class AndroidResource(val name: String) : Drawable()

    /**
     * An Android style attribute, e.g. ?my_image or ?attr/my_image.
     */
    data class StyleAttribute(val name: String) : Drawable()
}
