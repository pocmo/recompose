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

import com.intellij.CommonBundle
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import recompose.plugin.editor.RecomposeEditorOptions
import recompose.plugin.util.RecomposePluginBundle
import java.awt.Container
import javax.swing.Action
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * This dialog asks if the user wants to convert the pasted Xml to Compose
 */
class ConvertXmlToComposeConfirmationDialog(project: Project) : DialogWrapper(project, true) {
    lateinit var panel: JPanel
    @Suppress("unused")
    lateinit var questionLabel: JLabel

    @Suppress("unused")
    lateinit var dontShowThisCheckBox: JCheckBox

    init {
        init()
        isModal = true
        title = RecomposePluginBundle.message("convertxmltocomposeconfirmationdialog.title")
    }

    override fun createCenterPanel(): JComponent = panel

    override fun getContentPane(): Container = panel

    override fun createActions(): Array<Action> {
        setOKButtonText(CommonBundle.getYesButtonText())
        setCancelButtonText(CommonBundle.getNoButtonText())
        return arrayOf(okAction, cancelAction)
    }

    override fun doOKAction() {
        if (dontShowThisCheckBox.isSelected) {
            RecomposeEditorOptions.instance.state.isDontShowConversionDialog = true
        }
        super.doOKAction()
    }
}
