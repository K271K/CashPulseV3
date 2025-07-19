package feature.account.presentation.di

import dagger.Module
import dagger.Provides
import feature.account.presentation.navigation.FeatureAccountNavigation
import feature.account.presentation.navigation.FeatureAccountNavigationImpl
import javax.inject.Singleton

@Module
object AccountModule {

    @Provides
    @Singleton
    fun provideFeatureIncomesNavigation(): FeatureAccountNavigation {
        return FeatureAccountNavigationImpl()
    }

}