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
