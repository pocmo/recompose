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

package com.jds.recompose.values

import com.jds.recompose.attributes.Attribute

/**
 * Holder for parsed padding values.
 */
data class Padding(
    val all: Size? = null,
    val left: Size? = null,
    val right: Size? = null,
    val start: Size? = null,
    val end: Size? = null,
    val top: Size? = null,
    val bottom: Size? = null,
    val horizontal: Size? = null,
    val vertical: Size? = null
) : Attribute
