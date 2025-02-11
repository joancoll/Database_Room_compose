package cat.dam.andy.database_room_compose.data.repository

import cat.dam.andy.database_room_compose.data.dao.ContactDao
import cat.dam.andy.database_room_compose.model.Item
import kotlinx.coroutines.flow.Flow

class ContactRepository(private val contactDao: ContactDao) {

    val allContacts: Flow<List<Item>> = contactDao.getAll()

    fun findContacts(name: String): Flow<List<Item>> = contactDao.findContacts(name)

    suspend fun insert(item: Item) {
        contactDao.insert(item)
    }

    suspend fun update(item: Item) {
        contactDao.update(item)
    }

    suspend fun delete(item: Item) {
        contactDao.delete(item)
    }
}