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

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import recompose.ast.Layout
import recompose.parser.xml.layout
import java.io.BufferedReader
import java.lang.Exception
import kotlin.jvm.Throws

/**
 * Parser for parsing an XML layout and returning the [layout] node of the created abstract syntax tree (AST).
 */
class Parser {
    /**
     * Parses the given String containing an XML layout and returns the AST for it.
     */
    @Throws(ParserException::class)
    fun parse(text: String): Layout {
        return parse(text.reader().buffered())
    }

    /**
     * Parses the given BufferedReader for an XML layout and returns the AST for it.
     */
    @Throws(ParserException::class)
    fun parse(reader: BufferedReader): Layout {
        val factory = XmlPullParserFactory.newInstance()
        // Since we are reading partial documents, we may not see the namespace declaration. So let's just ignore
        // namespaces completely.
        factory.isNamespaceAware = false

        val parser = factory.newPullParser()
        parser.setInput(reader)

        return parse(parser)
    }

    /**
     * Parses the given XMLPullParser for an XML layout and returns the AST for it.
     */
    @Throws(ParserException::class)
    fun parse(parser: XmlPullParser): Layout {
        return parser.layout()
    }

    class ParserException : Exception {
        constructor(message: String) : super(message)
        constructor(message: String, cause: Throwable) : super(message, cause)
    }
}
