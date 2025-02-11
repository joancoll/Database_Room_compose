package cat.dam.andy.database_room_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.dam.andy.database_room_compose.data.database.AppDatabase
import cat.dam.andy.database_room_compose.data.repository.ContactRepository
import cat.dam.andy.database_room_compose.ui.screens.MainScreen
import cat.dam.andy.database_room_compose.viewmodel.ContactViewModel
import cat.dam.andy.database_room_compose.viewmodel.ContactViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialitzem la base de dades i el repositori
        val database = AppDatabase.getDatabase(this)
        val repository = ContactRepository(database.contactDao())

        // Configuració de la interfície d'usuari amb Compose
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Passem el ViewModel a la pantalla principal
                    val viewModel: ContactViewModel = viewModel(
                        factory = ContactViewModelFactory(repository)
                    )
                    MainScreen(viewModel)
                }
            }
        }
    }
}