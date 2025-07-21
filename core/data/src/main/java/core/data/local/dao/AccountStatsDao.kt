package core.data.local.dao

import androidx.room.*
import core.data.local.entity.AccountStatsEntity

@Dao
interface AccountStatsDao {
    @Query("SELECT * FROM account_stats WHERE accountId = :accountId")
    suspend fun getStatsForAccount(accountId: Int): List<AccountStatsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stats: List<AccountStatsEntity>)

    @Query("DELETE FROM account_stats WHERE accountId = :accountId")
    suspend fun deleteForAccount(accountId: Int)

    @Query("DELETE FROM account_stats")
    suspend fun deleteAll()
}