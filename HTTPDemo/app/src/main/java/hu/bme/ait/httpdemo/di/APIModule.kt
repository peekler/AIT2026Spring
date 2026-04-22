package hu.bme.ait.httpdemo.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.ait.httpdemo.network.MoneyAPI
import hu.bme.ait.httpdemo.network.NewsAPI
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MoneyExchangeAPIHost

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NewsAPIHost


@Module
@InstallIn(SingletonComponent::class)
class APIModule {

    @Provides
    @MoneyExchangeAPIHost
    @Singleton
    fun provideMoneyAPIRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl("http://data.fixer.io/")
            .addConverterFactory(
                Json{ ignoreUnknownKeys = true }.asConverterFactory(
                    "application/json".toMediaType()) )
            .client(client)
            .build()
    }

    @Provides
    @NewsAPIHost
    @Singleton
    fun provideNewsAPIRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl("https://newsapi.org")
            .addConverterFactory(
                Json{ ignoreUnknownKeys = true }.asConverterFactory(
                    "application/json".toMediaType()) )
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoneyAPI(@MoneyExchangeAPIHost retrofit: Retrofit): MoneyAPI {
        return retrofit.create(MoneyAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsAPI(@NewsAPIHost retrofit: Retrofit): NewsAPI {
        return retrofit.create(NewsAPI::class.java)
    }
}
