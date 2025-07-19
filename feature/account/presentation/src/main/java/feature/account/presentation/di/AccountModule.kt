package feature.account.presentation.di

import dagger.Module
import dagger.Provides
import feature.account.presentation.navigation.FeatureAccountNavigation
import feature.account.presentation.navigation.FeatureAccountNavigationImpl
import feature.account.presentation.screens.account_edit.AccountEditScreenViewModelFactory
import feature.account.presentation.screens.account_main_screen.AccountScreenViewModelFactory
import javax.inject.Singleton

@Module
object AccountModule {

    @Provides
    @Singleton
    fun provideFeatureIncomesNavigation(
        accountScreenViewModelFactory: AccountScreenViewModelFactory,
        accountEditScreenViewModelFactory: AccountEditScreenViewModelFactory
    ): FeatureAccountNavigation {
        return FeatureAccountNavigationImpl(
            accountScreenViewModelFactory = accountScreenViewModelFactory,
            accountEditScreenViewModelFactory = accountEditScreenViewModelFactory
        )
    }

}