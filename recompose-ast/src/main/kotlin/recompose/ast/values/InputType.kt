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
 * InputType values, e.g. used by `EditText` in `android:inputType`.
 */
sealed class InputType {
    object Text : InputType()
    object Number : InputType()
    object Phone : InputType()
    object Uri : InputType()
    object Email : InputType()
    object Password : InputType()
    object NumberPassword : InputType()
}
