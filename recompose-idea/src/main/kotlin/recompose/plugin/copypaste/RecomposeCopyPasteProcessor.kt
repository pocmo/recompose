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

import com.intellij.codeInsight.editorActions.CopyPastePostProcessor
import com.intellij.codeInsight.editorActions.TextBlockTransferableData
import com.intellij.ide.highlighter.XmlFileType
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.RangeMarker
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.idea.KotlinFileType
import recompose.composer.Composer
import recompose.parser.Parser
import java.awt.datatransfer.Transferable

/**
 * [CopyPastePostProcessor] implementation that takes copied layout XML and pastes it back as Kotlin code calling
 * `Composable`s.
 */
class RecomposeCopyPasteProcessor : CopyPastePostProcessor<TextBlockTransferableData>() {

    // On copy: Collect transferable data
    override fun collectTransferableData(
        file: PsiFile,
        editor: Editor,
        startOffsets: IntArray,
        endOffsets: IntArray
    ): List<TextBlockTransferableData> {
        if (file.fileType != XmlFileType.INSTANCE) {
            // If this is not an XML file then we do not need to care about this copy operation.
            return emptyList()
        }

        return listOf(
            CopiedXMLCode(file.text!!, startOffsets, endOffsets)
        )
    }

    // On paste: Extract transferable data
    override fun extractTransferableData(
        content: Transferable
    ): List<TextBlockTransferableData> {
        if (content.isDataFlavorSupported(CopiedXMLCode.DATA_FLAVOR)) {
            // There's some matching data in this paste, let's return it to process it.
            return listOf(
                content.getTransferData(CopiedXMLCode.DATA_FLAVOR) as TextBlockTransferableData
            )
        }

        return emptyList()
    }

    // Perform paste: Process transferable data
    override fun processTransferableData(
        project: Project,
        editor: Editor,
        bounds: RangeMarker,
        caretOffset: Int,
        indented: Ref<Boolean>,
        values: List<TextBlockTransferableData>
    ) {

        // We check whether we are pasting into a Kotlin file here. If not then there's no reason to
        // paste the Composable code.
        if ((editor as EditorImpl).virtualFile.fileType != KotlinFileType.INSTANCE) {
            return
        }

        if (confirmConvertXmlOnPaste(project)) {

            val value = values.single() as CopiedXMLCode

            val parser = Parser()
            // Currently we parse all text of the document we copied from. Obviously this is wrong and we should only
            // take the selection and parse this. But this may require that we fix up the XML since the copied part may
            // be incomplete.
            val layout = parser.parse(value.text)

            val composer = Composer()
            val code = composer.compose(layout)

            // We also should update the list of imports here to include the necessary classes.
            runWriteAction {
                editor.document.replaceString(
                    bounds.startOffset,
                    bounds.endOffset,
                    code
                )
            }
        }
    }

    private fun confirmConvertXmlOnPaste(project: Project): Boolean {
        val dialog = ConvertXmlToComposeConfirmationDialog(project)
        dialog.show()
        return dialog.isOK
    }
}
