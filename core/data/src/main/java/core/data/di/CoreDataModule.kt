package core.data.di

import android.content.Context
import core.data.remote.connection.AndroidConnectivityObserver
import core.data.remote.connection.ConnectivityObserver
import core.data.remote.retrofit.RemoteDataSource
import core.data.remote.retrofit.RetrofitNetwork
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object CoreDataModule {

    @Singleton
    @Provides
    fun provideConnectivityObserver(context: Context): ConnectivityObserver =
        AndroidConnectivityObserver(context = context)

    @Provides
    fun providesRemoteDataSource(): RemoteDataSource {
        return RetrofitNetwork()
    }

}