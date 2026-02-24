package cat.dam.andy.database_room_compose.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val tel: String,
    val photoBlob: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Item
        if (id != other.id) return false
        if (name != other.name) return false
        if (tel != other.tel) return false
        if (photoBlob != null) {
            if (other.photoBlob == null) return false
            if (!photoBlob.contentEquals(other.photoBlob)) return false
        } else if (other.photoBlob != null) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + tel.hashCode()
        result = 31 * result + (photoBlob?.contentHashCode() ?: 0)
        return result
    }
}