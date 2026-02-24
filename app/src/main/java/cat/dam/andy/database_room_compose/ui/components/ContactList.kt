package cat.dam.andy.database_room_compose.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cat.dam.andy.database_room_compose.model.Item

@Composable
fun ContactList(
    contacts: List<Item>,
    onEditClick: (Item) -> Unit,
    onDeleteClick: (Item) -> Unit,
    onPhotoClick: (Item) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(contacts) { item ->
            ContactItem(
                item = item,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                onPhotoClick = onPhotoClick
            )
        }
    }
}