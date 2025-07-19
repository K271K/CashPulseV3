package feature.settings.presentation.di

import dagger.Module
import dagger.Provides
import feature.settings.presentation.navigation.FeatureSettingsNavigation
import feature.settings.presentation.navigation.FeatureSettingsNavigationImpl
import javax.inject.Singleton

@Module
object SettingsModule {

    @Provides
    @Singleton
    fun provideFeatureSettingsNavigation(): FeatureSettingsNavigation {
        return FeatureSettingsNavigationImpl()
    }

}