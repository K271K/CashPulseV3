package feature.incomes.presentation.di

import dagger.Module
import dagger.Provides
import feature.incomes.presentation.navigation.FeatureIncomesNavigation
import feature.incomes.presentation.navigation.FeatureIncomesNavigationImpl
import javax.inject.Singleton

@Module
object IncomesModule {

    @Provides
    @Singleton
    fun provideFeatureIncomesNavigation(): FeatureIncomesNavigation {
        return FeatureIncomesNavigationImpl()
    }

}