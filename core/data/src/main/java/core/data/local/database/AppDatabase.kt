package core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import core.data.local.dao.AccountDao
import core.data.local.dao.AccountStatsDao
import core.data.local.dao.CategoryDao
import core.data.local.dao.TransactionDao
import core.data.local.entity.AccountEntity
import core.data.local.entity.AccountStatsEntity
import core.data.local.entity.CategoryEntity
import core.data.local.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        AccountEntity::class,
        CategoryEntity::class,
        AccountStatsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun accountStatsDao(): AccountStatsDao
}