package cat.dam.andy.database_room_compose.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import cat.dam.andy.database_room_compose.ui.components.ContactAvatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, phone: String, photoBlob: ByteArray?) -> Unit,
    onPickPhoto: () -> Unit,
    selectedPhotoBlob: ByteArray?
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Afegir contacte") },
            text = {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                    ) {
                        ContactAvatar(
                            photoBlob = selectedPhotoBlob,
                            size = 72.dp,
                            onClick = onPickPhoto
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            "Clica la imatge per canviar-la (càmera o galeria)",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    val focusManager = LocalFocusManager.current
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nom") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        })
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Telèfon") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            onConfirm(name, phone, selectedPhotoBlob)
                        })
                    )
                }
            },
            confirmButton = {
                Button(onClick = { onConfirm(name, phone, selectedPhotoBlob) }) {
                    Text("Afegir")
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