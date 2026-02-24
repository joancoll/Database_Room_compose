package cat.dam.andy.database_room_compose.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cat.dam.andy.database_room_compose.model.Item
import cat.dam.andy.database_room_compose.ui.components.ContactList
import cat.dam.andy.database_room_compose.ui.components.FilterField
import cat.dam.andy.database_room_compose.ui.dialogs.AddContactDialog
import cat.dam.andy.database_room_compose.ui.dialogs.DeleteContactDialog
import cat.dam.andy.database_room_compose.ui.dialogs.DialogState
import cat.dam.andy.database_room_compose.ui.dialogs.EditContactDialog
import cat.dam.andy.database_room_compose.ui.dialogs.PhotoPickerDialog
import cat.dam.andy.database_room_compose.viewmodel.ContactViewModel

private sealed class PhotoPickerTarget {
    data object ForAdd : PhotoPickerTarget()
    data object ForEdit : PhotoPickerTarget()
    data class ForChangePhoto(val item: Item) : PhotoPickerTarget()
}

@Composable
fun MainScreen(viewModel: ContactViewModel) {
    var dialogState by remember { mutableStateOf<DialogState>(DialogState.None) }
    var filter by remember { mutableStateOf("") }
    var addPhotoBlob by remember { mutableStateOf<ByteArray?>(null) }
    var editPhotoBlob by remember { mutableStateOf<ByteArray?>(null) }
    var photoPickerTarget by remember { mutableStateOf<PhotoPickerTarget?>(null) }

    val allContacts by viewModel.allContacts.collectAsState(initial = emptyList())
    val filteredContacts by viewModel.findContacts(filter).collectAsState(initial = emptyList())

    LaunchedEffect(dialogState) {
        when (val s = dialogState) {
            is DialogState.Add -> addPhotoBlob = null
            is DialogState.Edit -> editPhotoBlob = s.item.photoBlob
            else -> {}
        }
    }

    if (photoPickerTarget != null) {
        PhotoPickerDialog(
            onPhotoPicked = { bytes ->
                when (val target = photoPickerTarget) {
                    is PhotoPickerTarget.ForAdd -> addPhotoBlob = bytes
                    is PhotoPickerTarget.ForEdit -> editPhotoBlob = bytes
                    is PhotoPickerTarget.ForChangePhoto -> viewModel.update(
                        target.item.copy(photoBlob = bytes)
                    )
                    null -> {}
                }
                photoPickerTarget = null
            },
            onDismiss = { photoPickerTarget = null }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                FilterField(filter, onFilterChange = { filter = it })
                ContactList(
                    contacts = if (filter.isBlank()) allContacts else filteredContacts,
                    onEditClick = { dialogState = DialogState.Edit(it) },
                    onDeleteClick = { dialogState = DialogState.Delete(it) },
                    onPhotoClick = { photoPickerTarget = PhotoPickerTarget.ForChangePhoto(it) }
                )
            }

            FloatingActionButton(
                onClick = { dialogState = DialogState.Add },
                modifier = Modifier.align(Alignment.BottomEnd),
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Afegir")
            }

            when (val currentState = dialogState) {
                is DialogState.Add -> AddContactDialog(
                    onDismiss = { dialogState = DialogState.None },
                    onConfirm = { name, phone, photoBlob ->
                        viewModel.insert(
                            Item(name = name, tel = phone, photoBlob = photoBlob)
                        )
                        dialogState = DialogState.None
                    },
                    onPickPhoto = { photoPickerTarget = PhotoPickerTarget.ForAdd },
                    selectedPhotoBlob = addPhotoBlob
                )

                is DialogState.Edit -> EditContactDialog(
                    item = currentState.item,
                    onDismiss = { dialogState = DialogState.None },
                    onConfirm = { id, name, phone, photoBlob ->
                        viewModel.update(
                            Item(id = id, name = name, tel = phone, photoBlob = photoBlob)
                        )
                        dialogState = DialogState.None
                    },
                    onPickPhoto = { photoPickerTarget = PhotoPickerTarget.ForEdit },
                    selectedPhotoBlob = editPhotoBlob
                )

                is DialogState.Delete -> DeleteContactDialog(
                    item = currentState.item,
                    onDismiss = { dialogState = DialogState.None },
                    onConfirm = { item ->
                        viewModel.delete(item)
                        dialogState = DialogState.None
                    }
                )

                is DialogState.None -> {}
            }
        }
    }
}