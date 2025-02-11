package cat.dam.andy.database_room_compose.data.dao

import androidx.room.*
import cat.dam.andy.database_room_compose.model.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Query("SELECT * FROM contacts")
    fun getAll(): Flow<List<Item>>

    @Query("SELECT * FROM contacts WHERE LOWER(name) LIKE '%' || LOWER(:name) || '%'")
    fun findContacts(name: String): Flow<List<Item>>

    @Insert
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)
}