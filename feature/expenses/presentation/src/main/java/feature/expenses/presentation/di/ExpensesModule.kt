package feature.expenses.presentation.di

import dagger.Module
import dagger.Provides
import feature.expenses.presentation.navigation.FeatureExpensesNavigation
import feature.expenses.presentation.navigation.FeatureExpensesNavigationImpl
import feature.expenses.presentation.screens.expenses_add.ExpensesAddScreenViewModel
import feature.expenses.presentation.screens.expenses_add.ExpensesAddScreenViewModelFactory
import feature.expenses.presentation.screens.expenses_edit.ExpensesEditScreenViewModel
import feature.expenses.presentation.screens.expenses_edit.ExpensesEditScreenViewModelFactory
import feature.expenses.presentation.screens.expenses_history.ExpensesHistoryScreenViewModel
import feature.expenses.presentation.screens.expenses_history.ExpensesHistoryScreenViewModelFactory
import feature.expenses.presentation.screens.expenses_today.ExpensesTodayScreenViewModel
import feature.expenses.presentation.screens.expenses_today.ExpensesTodayScreenViewModelFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module()
object ExpensesModule {

    @Provides
    @Singleton
    fun provideFeatureExpensesNavigation(
        expensesTodayScreenViewModelFactory: ExpensesTodayScreenViewModelFactory,
        expensesHistoryScreenViewModelFactory: ExpensesHistoryScreenViewModelFactory,
        expensesAddScreenViewModelFactory: ExpensesAddScreenViewModelFactory,
        expensesEditScreenViewModelFactory: ExpensesEditScreenViewModelFactory
    ): FeatureExpensesNavigation {
        return FeatureExpensesNavigationImpl(
            expensesTodayScreenViewModelFactory = expensesTodayScreenViewModelFactory,
            expensesHistoryScreenViewModelFactory = expensesHistoryScreenViewModelFactory,
            expensesAddScreenViewModelFactory = expensesAddScreenViewModelFactory,
            expensesEditScreenViewModelFactory = expensesEditScreenViewModelFactory
        )
    }

    @Provides
    @Singleton
    fun provideExpensesTodayScreenViewModelFactory(
        expensesTodayScreenViewModelProvider: Provider<ExpensesTodayScreenViewModel>
    ): ExpensesTodayScreenViewModelFactory {
        return ExpensesTodayScreenViewModelFactory(
            expensesTodayScreenViewModelProvider = expensesTodayScreenViewModelProvider
        )
    }

    @Provides
    @Singleton
    fun provideExpensesHistoryScreenViewModelFactory(
        expensesHistoryScreenViewModelProvider: Provider<ExpensesHistoryScreenViewModel>
    ): ExpensesHistoryScreenViewModelFactory {
        return ExpensesHistoryScreenViewModelFactory(
            expensesHistoryScreenViewModelProvider = expensesHistoryScreenViewModelProvider
        )
    }

    @Provides
    @Singleton
    fun provideExpensesAddScreenViewModelFactory(
        expensesAddScreenViewModelProvider: Provider<ExpensesAddScreenViewModel>
    ): ExpensesAddScreenViewModelFactory {
        return ExpensesAddScreenViewModelFactory(
            expensesAddScreenViewModelProvider = expensesAddScreenViewModelProvider
        )
    }

    @Provides
    @Singleton
    fun provideExpensesEditScreenViewModelFactory(
        expensesEditScreenViewModelProvider: Provider<ExpensesEditScreenViewModel>
    ): ExpensesEditScreenViewModelFactory {
        return ExpensesEditScreenViewModelFactory(
            expensesEditScreenViewModelProvider = expensesEditScreenViewModelProvider
        )
    }

}