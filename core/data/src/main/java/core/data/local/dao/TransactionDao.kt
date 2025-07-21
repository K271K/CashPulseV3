package core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import core.data.local.entity.TransactionEntity

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE isDeleted = 0 ORDER BY transactionDate DESC")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE accountId = :accountId AND isDeleted = 0")
    suspend fun getByAccountId(accountId: Int): List<TransactionEntity>

    @Query("""
        SELECT * FROM transactions
        WHERE accountId = :accountId
        AND transactionDate BETWEEN :startDate AND :endDate
        AND isDeleted = 0
        ORDER BY transactionDate DESC
    """)
    suspend fun getByAccountAndPeriod(
        accountId: Int,
        startDate: String,
        endDate: String
    ) : List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id = :id AND isDeleted = 0")
    suspend fun getById(id: Int): TransactionEntity?

    @Query("SELECT id FROM transactions WHERE isDeleted = 0")
    suspend fun getAllTransactionIds(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>)

    @Query("UPDATE transactions SET isDeleted = 1 WHERE id = :id")
    suspend fun softDeleteById(id: Int)

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()
}