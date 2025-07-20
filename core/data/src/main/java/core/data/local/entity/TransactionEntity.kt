package core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"]
        ),
        ForeignKey(
            entity = CategoryEntity::class,    // ссылаемся на таблицу categories
            parentColumns = ["id"],            // поле id в таблице categories
            childColumns = ["categoryId"]     // поле categoryId в таблице transactions
        )
    ],
    indices = [
        Index(value = ["accountId"]),
        Index(value = ["categoryId"]),
        Index(value = ["transactionDate"]),
        Index(value = ["localId"], unique = true) // уникальный индекс для localId

    ]
)
data class TransactionEntity(
    @PrimaryKey
    val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val comment: String,
    val transactionDate: String,
    val createdAt: String,
    val updatedAt: String,

    //Поля для оффлайн sync
    val isLocalOnly: Boolean = false,
    val needsSync: Boolean = false,
    val localId: String? = null,
    val lastModified: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
)
