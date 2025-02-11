package cat.dam.andy.database_room_compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.dam.andy.database_room_compose.data.repository.ContactRepository
import cat.dam.andy.database_room_compose.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    val allContacts: Flow<List<Item>> = repository.allContacts

    fun findContacts(name: String): Flow<List<Item>> = repository.findContacts(name)

    fun insert(item: Item) = viewModelScope.launch {
        repository.insert(item)
    }

    fun update(item: Item) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: Item) = viewModelScope.launch {
        repository.delete(item)
    }
}