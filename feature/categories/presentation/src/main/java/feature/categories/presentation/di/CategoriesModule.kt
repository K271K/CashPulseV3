package feature.categories.presentation.di

import dagger.Module
import dagger.Provides
import feature.categories.presentation.navigation.FeatureCategoriesNavigation
import feature.categories.presentation.navigation.FeatureCategoriesNavigationImpl
import javax.inject.Singleton

@Module
object CategoriesModule {

    @Provides
    @Singleton
    fun provideFeatureCategoriesNavigation(): FeatureCategoriesNavigation {
        return FeatureCategoriesNavigationImpl()
    }

}