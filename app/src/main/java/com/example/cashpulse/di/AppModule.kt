package com.example.cashpulse.di

import android.app.Application
import android.content.Context
import com.example.cashpulse.MainActivityViewModel
import com.example.cashpulse.MainActivityViewModelFactory
import com.example.cashpulse.navigation.DefaultNavigator
import core.data.di.CoreDataModule
import core.data.local.dao.AccountDao
import core.data.local.dao.CategoryDao
import core.data.local.dao.TransactionDao
import core.data.remote.connection.ConnectivityObserver
import core.data.remote.retrofit.RemoteDataSource
import core.data.repository.AccountRepositoryImpl
import core.data.repository.CategoriesRepositoryImpl
import core.data.repository.TransactionsRepositoryImpl
import core.domain.repository.AccountRepository
import core.domain.repository.CategoriesRepository
import core.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import feature.account.presentation.di.AccountModule
import feature.categories.presentation.di.CategoriesModule
import feature.expenses.presentation.di.ExpensesModule
import feature.expenses.presentation.navigation.FeatureExpensesNavigation
import feature.incomes.presentation.di.IncomesModule
import feature.settings.presentation.di.SettingsModule
import javax.inject.Provider
import javax.inject.Singleton

@Module(
    includes = [
        ExpensesModule::class,
        IncomesModule::class,
        AccountModule::class,
        CategoriesModule::class,
        SettingsModule::class,
        CoreDataModule::class,
    ]
)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideMainActivityViewModelFactory(
        viewModelProvider: Provider<MainActivityViewModel>
    ) : MainActivityViewModelFactory {
        return MainActivityViewModelFactory(
            viewModelProvider = viewModelProvider
        )
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(
        remoteDataSource: RemoteDataSource,
        connectivityObserver: ConnectivityObserver,
        transactionDao: TransactionDao,
        accountDao: AccountDao,
        categoryDao: CategoryDao
    ) : TransactionRepository {
        return TransactionsRepositoryImpl(
            remoteDataSource = remoteDataSource,
            connectivityObserver = connectivityObserver,
            transactionDao = transactionDao,
            accountDao = accountDao,
            categoryDao = categoryDao
        )
    }

    @Provides
    @Singleton
    fun provideCategoriesRepository(
        remoteDataSource: RemoteDataSource,
        connectivityObserver: ConnectivityObserver,
        categoryDao: CategoryDao
    ) : CategoriesRepository {
        return CategoriesRepositoryImpl(
            remoteDataSource = remoteDataSource,
            connectivityObserver = connectivityObserver,
            categoryDao = categoryDao
        )
    }

    @Provides
    @Singleton
    fun provideAccountRepository(
        remoteDataSource: RemoteDataSource,
        connectivityObserver: ConnectivityObserver,
        accountDao: AccountDao
    ) : AccountRepository {
        return AccountRepositoryImpl(
            remoteDataSource = remoteDataSource,
            connectivityObserver = connectivityObserver,
            accountDao = accountDao
        )
    }

}