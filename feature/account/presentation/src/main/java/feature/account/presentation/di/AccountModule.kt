package feature.account.presentation.di

import dagger.Module
import dagger.Provides
import feature.account.presentation.navigation.FeatureAccountNavigation
import feature.account.presentation.navigation.FeatureAccountNavigationImpl
import feature.account.presentation.screens.account_main_screen.AccountScreenViewModelFactory
import javax.inject.Singleton

@Module
object AccountModule {

    @Provides
    @Singleton
    fun provideFeatureIncomesNavigation(
        accountScreenViewModelFactory: AccountScreenViewModelFactory
    ): FeatureAccountNavigation {
        return FeatureAccountNavigationImpl(
            accountScreenViewModelFactory = accountScreenViewModelFactory
        )
    }

}