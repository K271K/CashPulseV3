package core.data.di

import android.content.Context
import androidx.room.Room
import core.data.local.dao.AccountDao
import core.data.local.dao.AccountStatsDao
import core.data.local.dao.CategoryDao
import core.data.local.dao.TransactionDao
import core.data.local.database.AppDatabase
import core.data.remote.connection.AndroidConnectivityObserver
import core.data.remote.connection.ConnectivityObserver
import core.data.remote.retrofit.RemoteDataSource
import core.data.remote.retrofit.RetrofitNetwork
import core.data.sync.SyncManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object CoreDataModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cashpulse_db"
        ).build()

    @Singleton
    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao =
        db.transactionDao()

    @Singleton
    @Provides
    fun provideAccountDao(db: AppDatabase): AccountDao =
        db.accountDao()

    @Singleton
    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao =
        db.categoryDao()

    @Singleton
    @Provides
    fun provideAccountStatsDao(db: AppDatabase): AccountStatsDao =
        db.accountStatsDao()

    @Singleton
    @Provides
    fun provideConnectivityObserver(context: Context): ConnectivityObserver =
        AndroidConnectivityObserver(context = context)

    @Provides
    fun providesRemoteDataSource(): RemoteDataSource {
        return RetrofitNetwork()
    }

    @Singleton
    @Provides
    fun provideSyncManager(
        remoteDataSource: RemoteDataSource,
        connectivityObserver: ConnectivityObserver,
        accountDao: AccountDao,
        categoryDao: CategoryDao,
        transactionDao: TransactionDao
    ): SyncManager {
        return SyncManager(
            remoteDataSource = remoteDataSource,
            connectivityObserver = connectivityObserver,
            accountDao = accountDao,
            categoryDao = categoryDao,
            transactionDao = transactionDao
        )
    }

}