package core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean,

    // Поля для оффлайн синхронизации
    val isLocalOnly: Boolean = false,
    val needsSync: Boolean = false,
    val localId: String? = null,
    val lastModified: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
)
