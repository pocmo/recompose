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

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "RecomposeEditorOptions", storages = [Storage("recomposeEditor.xml")])
class RecomposeEditorOptions : PersistentStateComponent<EditorState> {
    var editorState: EditorState = EditorState()

    override fun getState(): EditorState {
        return editorState
    }

    override fun loadState(state: EditorState) {
        editorState = state
    }

    companion object {
        val instance: RecomposeEditorOptions
            get() = ServiceManager.getService(RecomposeEditorOptions::class.java)
    }
}
