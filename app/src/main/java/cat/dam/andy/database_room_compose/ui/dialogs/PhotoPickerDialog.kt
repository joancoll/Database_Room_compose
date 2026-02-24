package cat.dam.andy.database_room_compose.ui.dialogs

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import cat.dam.andy.database_room_compose.util.ImageUtils
import java.io.File

@Composable
fun PhotoPickerDialog(
    onPhotoPicked: (ByteArray) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    var step by remember { mutableStateOf<PhotoPickerStep>(PhotoPickerStep.ChooseSource) }
    var previewBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var rotationDegrees by remember { mutableStateOf(0) }
    var cameraOutputFile by remember { mutableStateOf<File?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        cameraOutputFile?.let { file ->
            if (success && file.exists()) {
                val bitmap = ImageUtils.decodeBytes(file.readBytes())
                bitmap?.let {
                    previewBitmap = it
                    rotationDegrees = 0
                    step = PhotoPickerStep.Preview
                }
            }
            cameraOutputFile = null
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted && activity != null) {
            val file = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
            cameraOutputFile = file
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            takePictureLauncher.launch(uri)
        }
    }

    val getContentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bytes = ImageUtils.uriToBytes(context, it)
            bytes?.let { b ->
                ImageUtils.decodeBytes(b)?.let { bitmap ->
                    previewBitmap = bitmap
                    rotationDegrees = 0
                    step = PhotoPickerStep.Preview
                }
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        when (step) {
            PhotoPickerStep.ChooseSource -> {
                AlertDialog(
                    onDismissRequest = onDismiss,
                    title = { Text("Triar foto") },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Pots fer una foto amb la càmera o triar-ne una de la galeria.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        if (activity != null) {
                                            permissionLauncher.launch(android.Manifest.permission.CAMERA)
                                        }
                                    }
                                ) {
                                    Text("Càmera")
                                }
                                Button(
                                    modifier = Modifier.weight(1f),
                                    onClick = { getContentLauncher.launch("image/*") }
                                ) {
                                    Text("Galeria")
                                }
                            }
                        }
                    },
                    confirmButton = { },
                    dismissButton = {
                        Button(onClick = onDismiss) {
                            Text("Cancel·lar")
                        }
                    }
                )
            }
            PhotoPickerStep.Preview -> {
                val bitmap = previewBitmap
                if (bitmap != null) {
                    val rotated = ImageUtils.rotateBitmap(bitmap, rotationDegrees)
                    AlertDialog(
                        onDismissRequest = onDismiss,
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(onClick = { step = PhotoPickerStep.ChooseSource }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Enrere")
                                }
                                Text("Ajustar foto")
                            }
                        },
                        text = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Image(
                                    bitmap = rotated.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(280.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { rotationDegrees = (rotationDegrees - 90) % 360 }
                                    ) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.RotateLeft,
                                            contentDescription = "Girar 90° esquerra"
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(24.dp))
                                    Text(
                                        "Girar 90°",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.width(24.dp))
                                    IconButton(
                                        onClick = { rotationDegrees = (rotationDegrees + 90) % 360 }
                                    ) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.RotateRight,
                                            contentDescription = "Girar 90° dreta"
                                        )
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    val finalBitmap = ImageUtils.rotateBitmap(bitmap, rotationDegrees)
                                    onPhotoPicked(ImageUtils.bitmapToJpegBytes(finalBitmap))
                                    onDismiss()
                                }
                            ) {
                                Text("Desar")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { step = PhotoPickerStep.ChooseSource }) {
                                Text("Enrere")
                            }
                        }
                    )
                }
            }
        }
    }
}

private sealed class PhotoPickerStep {
    data object ChooseSource : PhotoPickerStep()
    data object Preview : PhotoPickerStep()
}
