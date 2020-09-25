/*
 * Copyright 2010-2015 JetBrains s.r.o.
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
import java.awt.Container
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.Action

/**
 * This dialog asks if the user wants to convert the pasted Xml to Compose
 */
class KotlinPasteFromXmlDialog(project: Project) : DialogWrapper(project, true) {
    lateinit var panel: JPanel
    @Suppress("unused") lateinit var donTShowThisCheckBox: JCheckBox
    @Suppress("unused") lateinit var questionLabel: JLabel

    init {
        init()
        isModal = true
        title = "Convert XML to Compose"
    }
    override fun createCenterPanel(): JComponent = panel

    override fun getContentPane(): Container = panel

    override fun createActions(): Array<Action> {
        setOKButtonText(CommonBundle.getYesButtonText())
        setCancelButtonText(CommonBundle.getNoButtonText())
        return arrayOf(okAction, cancelAction)
    }
}
