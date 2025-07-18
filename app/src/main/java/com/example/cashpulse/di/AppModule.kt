package com.example.cashpulse.di

import android.app.Application
import android.content.Context
import com.example.cashpulse.MainActivityViewModel
import com.example.cashpulse.MainActivityViewModelFactory
import com.example.cashpulse.navigation.DefaultNavigator
import core.data.di.CoreDataModule
import core.data.repository.TransactionsRepositoryImpl
import core.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import feature.expenses.presentation.ExpensesModule
import feature.expenses.presentation.navigation.FeatureExpensesNavigation
import javax.inject.Provider
import javax.inject.Singleton

@Module(
    includes = [
        ExpensesModule::class,
        CoreDataModule::class
    ]
)
object AppModule {

    @Provides
    @Singleton
    fun provideDefaultNavigator(
        featureExpenses: FeatureExpensesNavigation
    ) : DefaultNavigator {
        return DefaultNavigator(
            featureExpenses = featureExpenses
        )
    }

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
    fun provideTransactionRepositoryImpl() : TransactionRepository {
        return TransactionsRepositoryImpl()
    }

}