package cat.dam.andy.database_room_compose.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cat.dam.andy.database_room_compose.model.Item
import cat.dam.andy.database_room_compose.ui.components.ContactAvatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactDialog(
    item: Item,
    onDismiss: () -> Unit,
    onConfirm: (id: Int, name: String, phone: String, photoBlob: ByteArray?) -> Unit,
    onPickPhoto: () -> Unit,
    selectedPhotoBlob: ByteArray?
) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedPhone by remember { mutableStateOf(item.tel) }

    Dialog(onDismissRequest = onDismiss) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Editar contacte") },
            text = {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ContactAvatar(
                            photoBlob = selectedPhotoBlob,
                            size = 72.dp,
                            onClick = onPickPhoto
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            "Clica la imatge per canviar-la",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    val focusManager = LocalFocusManager.current
                    TextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Nom") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        })
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = editedPhone,
                        onValueChange = { editedPhone = it },
                        label = { Text("Telèfon") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            onConfirm(item.id, editedName, editedPhone, selectedPhotoBlob)
                        })
                    )
                }
            },
            confirmButton = {
                Button(onClick = { onConfirm(item.id, editedName, editedPhone, selectedPhotoBlob) }) {
                    Text("Guardar canvis")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancel·lar")
                }
            }
        )
    }
}