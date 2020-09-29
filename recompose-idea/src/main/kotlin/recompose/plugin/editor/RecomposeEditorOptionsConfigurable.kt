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

package recompose.plugin.editor

import com.intellij.openapi.Disposable
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.Configurable.NoScroll
import com.intellij.util.ui.FormBuilder
import recompose.plugin.util.RecomposePluginBundle
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

class RecomposeEditorOptionsConfigurable : Configurable, NoScroll, Disposable {

    private val editorState
        get() = RecomposeEditorOptions.instance.state

    // ui components
    private var convertPastedCheckbox: JCheckBox? =
        JCheckBox(RecomposePluginBundle.message("editor.checkbox.title.convert.pasted.xml"))
    private var dontShowConversionCheckbox: JCheckBox? =
        JCheckBox(RecomposePluginBundle.message("editor.checkbox.title.dont.show.conversion.dialog"))

    override fun createComponent(): JComponent? {
        val formPanel = FormBuilder.createFormBuilder()
            .addComponent(JPanel(FlowLayout(FlowLayout.LEFT)).also { it.add(convertPastedCheckbox) })
            .addComponent(JPanel(FlowLayout(FlowLayout.LEFT)).also { it.add(dontShowConversionCheckbox) })
            .panel

        return JPanel(BorderLayout()).also { it.add(formPanel, BorderLayout.NORTH) }
    }

    override fun isModified(): Boolean {
        return editorState.isDontShowConversionDialog != dontShowConversionCheckbox!!.isSelected ||
            editorState.isEnableXmlToComposeConversion != convertPastedCheckbox!!.isSelected
    }

    override fun apply() {
        editorState.isEnableXmlToComposeConversion = convertPastedCheckbox!!.isSelected
        editorState.isDontShowConversionDialog = dontShowConversionCheckbox!!.isSelected
    }

    override fun getDisplayName(): String = RecomposePluginBundle.message("editor.title.recompose")

    override fun reset() {
        convertPastedCheckbox?.isSelected = editorState.isEnableXmlToComposeConversion
        dontShowConversionCheckbox?.isSelected = editorState.isDontShowConversionDialog
    }

    override fun dispose() {
        convertPastedCheckbox = null
        dontShowConversionCheckbox = null
    }
}
