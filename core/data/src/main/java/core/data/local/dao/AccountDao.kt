package core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import core.data.local.entity.AccountEntity

@Dao
interface AccountDao {

    @Query("SELECT * FROm accounts WHERE isDeleted = 0 ORDER BY id ASC")
    suspend fun getAllAccounts(): List<AccountEntity>

    @Query("SELECT * FROM accounts WHERE id = :accountId and isDeleted = 0")
    suspend fun getAccountById(accountId: Int): AccountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: AccountEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(accounts: List<AccountEntity>)

    @Update
    suspend fun update(account: AccountEntity)

    @Delete
    suspend fun delete(account: AccountEntity)

    @Query("UPDATE accounts SET isDeleted = 1 WHERE id = :id")
    suspend fun softDeleteById(id: Int)

    @Query("DELETE FROM accounts")
    suspend fun deleteAll()
}