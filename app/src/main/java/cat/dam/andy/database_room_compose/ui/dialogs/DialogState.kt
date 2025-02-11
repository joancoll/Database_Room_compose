package cat.dam.andy.database_room_compose.ui.dialogs

import cat.dam.andy.database_room_compose.model.Item

sealed class DialogState {
    object None : DialogState()
    object Add : DialogState()
    data class Edit(val item: Item) : DialogState()
    data class Delete(val item: Item) : DialogState()
}