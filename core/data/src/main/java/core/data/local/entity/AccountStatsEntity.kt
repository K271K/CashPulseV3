package core.data.local.entity

import androidx.room.*

@Entity(
    tableName = "account_stats",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["accountId"])]
)
data class AccountStatsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val accountId: Int,
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: String,
    val isIncome: Boolean // true для incomeStats, false для expenseStats
)