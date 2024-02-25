package com.example.quizhuntercompose.network

import com.example.quizhuntercompose.cor.util.AppConstants.BASE_CSDD_TEST_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object NetworkModuleC {

    @Provides
    @Singleton
    fun providesOkhttpInterceptor(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(OkhttpInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideCImagesApi(okHttpClient: OkHttpClient): DriversCApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_CSDD_TEST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DriversCApi::class.java)
    }
}