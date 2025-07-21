package core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val userId: Int?,
    val createdAt: String?,
    val updatedAt: String?,

    // Поля для оффлайн синхронизации
    val isLocalOnly: Boolean = false,
    val needsSync: Boolean = false,
    val localId: String? = null,
    val lastModified: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
)
