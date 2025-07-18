package feature.expenses.presentation

import dagger.Module
import dagger.Provides
import feature.expenses.presentation.navigation.FeatureExpensesNavigation
import feature.expenses.presentation.navigation.FeatureExpensesNavigationImpl
import feature.expenses.presentation.screens.expenses_today.ExpensesTodayScreenViewModel
import feature.expenses.presentation.screens.expenses_today.ExpensesTodayScreenViewModelFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module()
object ExpensesModule {

    @Provides
    @Singleton
    fun provideFeatureExpensesNavigation(
        expensesTodayScreenViewModelFactory: ExpensesTodayScreenViewModelFactory
    ): FeatureExpensesNavigation {
        return FeatureExpensesNavigationImpl(
            expensesTodayScreenViewModelFactory = expensesTodayScreenViewModelFactory
        )
    }

    @Provides
    @Singleton
    fun provideExpensesTodayScreenViewModelFactory(
        expensesTodayScreenViewModelProvider: Provider<ExpensesTodayScreenViewModel>
    ) : ExpensesTodayScreenViewModelFactory {
        return ExpensesTodayScreenViewModelFactory(
            expensesTodayScreenViewModelProvider = expensesTodayScreenViewModelProvider
        )
    }

}