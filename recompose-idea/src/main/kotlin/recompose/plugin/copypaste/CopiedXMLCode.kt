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

package recompose.plugin.copypaste

import com.intellij.codeInsight.editorActions.TextBlockTransferableData
import java.awt.datatransfer.DataFlavor

/**
 * Holder keeping track of a copied XML code.
 *
 * @param text All text of the file that was copied from, potentially containing more text than was copied.
 * @param startOffsets The start offsets of the selections in the text (can be multi-select).
 * @param endOffsets The end offsets of the selection in the text.
 */
class CopiedXMLCode(
    val text: String,
    val startOffsets: IntArray,
    val endOffsets: IntArray
) : TextBlockTransferableData {
    override fun getFlavor() = DATA_FLAVOR
    override fun getOffsetCount() = 0

    override fun getOffsets(offsets: IntArray?, index: Int) = index
    override fun setOffsets(offsets: IntArray?, index: Int) = index

    companion object {
        val DATA_FLAVOR: DataFlavor = DataFlavor(
            RecomposeCopyPasteProcessor::class.java,
            "class: RecomposeCopyPasteProcessor"
        )
    }
}
