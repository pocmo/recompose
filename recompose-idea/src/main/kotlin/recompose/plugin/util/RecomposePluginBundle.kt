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

package recompose.plugin.util

import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey
import java.util.ResourceBundle

/**
 * Wrapper for the RecomposePluginBundle.properties
 */
object RecomposePluginBundle {

    @NonNls
    private const val BUNDLE = "strings.RecomposePluginBundle"

    private val resourceBundle: ResourceBundle = ResourceBundle.getBundle(BUNDLE)

    /**
     * Returns the string from the [resourceBundle] for the given [key]
     */
    fun message(@NonNls @PropertyKey(resourceBundle = BUNDLE) key: String): String = resourceBundle.getString(key)
}
