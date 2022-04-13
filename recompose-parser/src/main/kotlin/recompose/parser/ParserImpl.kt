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

import com.jds.recompose.nodes.Layout
import com.jds.recompose.nodes.Node
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import recompose.parser.xml.XMLNodeMatcher
import java.io.BufferedReader

/**
 * Parser for parsing an XML layout and returning the [layout] node of the created abstract syntax tree (AST).
 */
class ParserImpl : Parser<Layout> {
    /**
     * Parses the given String containing an XML layout and returns the AST for it.
     */
    @Throws(ParserException::class)
    override fun parse(input: String): Layout {
        return parse(input.reader().buffered())
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
        try {
            val children = mutableListOf<Node>()
            with(parser) {
                var event = eventType

                while (event != XmlPullParser.END_DOCUMENT) {
                    when (event) {
                        XmlPullParser.START_DOCUMENT -> Unit
                        XmlPullParser.START_TAG -> {
                            XMLNodeMatcher.match(parser)?.let { children.add(it) }
                        }
                    }
                    event = parser.next()
                }
            }

            return Layout(children)
        } catch (e: XmlPullParserException) {
            throw ParserException("Invalid XML: ${e.message}", e)
        }
    }

    class ParserException : Exception {
        constructor(message: String) : super(message)
        constructor(message: String, cause: Throwable) : super(message, cause)
    }
}
