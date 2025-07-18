package core.data.remote.retrofit

import core.data.BuildConfig
import core.domain.model.transaction.TransactionDomainModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

internal interface NetworkApi {

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsByPeriod(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?,
    ): List<TransactionDomainModel>

}

@Singleton
internal class RetrofitNetwork @Inject constructor() : RemoteDataSource {

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.BEARER_TOKEN}")
            .build()
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authInterceptor)
        .build()

    private val networkApi = Retrofit.Builder()
        .baseUrl("https://shmr-finance.ru/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(NetworkApi::class.java)

    override suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<TransactionDomainModel> =
        networkApi.getTransactionsByPeriod(
            accountId,
            startDate,
            endDate
        )


}