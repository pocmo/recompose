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

import recompose.ast.values.Color
import recompose.ast.values.Drawable
import recompose.ast.values.Size

/**
 * Sealed class for types of parameter values that a passed to a `Composable`.
 */
internal sealed class ParameterValue {
    class RawValue(
        val raw: String
    ) : ParameterValue()

    class StringValue(
        val raw: String
    ) : ParameterValue()

    object EmptyLambdaValue : ParameterValue()

    class ColoValue(
        val color: Color
    ) : ParameterValue()

    class ModifierValue(
        val builder: ModifierBuilder
    ) : ParameterValue()

    class SizeValue(
        val size: Size
    ) : ParameterValue()

    class DrawableValue(
        val drawable: Drawable
    ) : ParameterValue()
}

internal fun createSizeParameterValue(size: Size?): ParameterValue.SizeValue? {
    return size?.let { ParameterValue.SizeValue(it) }
}
