package hu.bme.ait.hiltcomposedemo.analytics

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import hu.bme.ait.hiltcomposedemo.Detail
import hu.bme.ait.hiltcomposedemo.analytics.AnalyticsEngine
import hu.bme.ait.hiltcomposedemo.analytics.RealAnalyitics
import hu.bme.ait.hiltcomposedemo.logging.LogService
import hu.bme.ait.hiltcomposedemo.logging.MainLogger
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BmeDetail

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ElteDetail

@Module
@InstallIn(SingletonComponent::class)
object DetailModule {

    @BmeDetail
    @Provides
    @Singleton
    fun provideDetailBme(
    ): Detail {
        return Detail()
    }

    @ElteDetail
    @Provides
    @Singleton
    fun provideDetailElte(
    ): Detail {
        return Detail()
    }

}