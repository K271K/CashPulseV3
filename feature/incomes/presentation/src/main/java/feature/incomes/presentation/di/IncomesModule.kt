package feature.incomes.presentation.di

import dagger.Module
import dagger.Provides
import feature.incomes.presentation.navigation.FeatureIncomesNavigation
import feature.incomes.presentation.navigation.FeatureIncomesNavigationImpl
import feature.incomes.presentation.screens.incomes_add.IncomesAddScreenViewModelFactory
import feature.incomes.presentation.screens.incomes_edit.IncomesEditScreenViewModelFactory
import feature.incomes.presentation.screens.incomes_history.IncomesHistoryScreenViewModelFactory
import feature.incomes.presentation.screens.incomes_today.IncomesTodayScreenViewModel
import feature.incomes.presentation.screens.incomes_today.IncomesTodayScreenViewModelFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module
object IncomesModule {

    @Provides
    @Singleton
    fun provideFeatureIncomesNavigation(
        incomesTodayScreenViewModelFactory: IncomesTodayScreenViewModelFactory,
        incomesHistoryScreenViewModelFactory: IncomesHistoryScreenViewModelFactory,
        incomesAddScreenViewModelFactory: IncomesAddScreenViewModelFactory,
        incomesEditScreenViewModelFactory: IncomesEditScreenViewModelFactory
    ): FeatureIncomesNavigation {
        return FeatureIncomesNavigationImpl(
            incomesTodayScreenViewModelFactory = incomesTodayScreenViewModelFactory,
            incomesHistoryScreenViewModelFactory = incomesHistoryScreenViewModelFactory,
            incomesAddScreenViewModelFactory = incomesAddScreenViewModelFactory,
            incomesEditScreenViewModelFactory = incomesEditScreenViewModelFactory
        )
    }

    @Provides
    @Singleton
    fun provideIncomesTodayScreenViewModelFactory(
        incomesTodayScreenViewModelProvider: Provider<IncomesTodayScreenViewModel>
    ): IncomesTodayScreenViewModelFactory {
        return IncomesTodayScreenViewModelFactory(
            incomesTodayScreenViewModelProvider = incomesTodayScreenViewModelProvider
        )
    }

}