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
 * Different forms of color values as they can appear in layouts.
 */
sealed class Color {
    /**
     * An absolute color like 0xFFFF0000.
     */
    data class Absolute(val value: Long) : Color()
    /**
     * A resource reference color, e.g. @color/nice_color
     */
    data class Resource(val name: String) : Color()
}
