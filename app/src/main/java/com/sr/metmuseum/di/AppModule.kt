package com.sr.metmuseum.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sr.metmuseum.BuildConfig
import com.sr.metmuseum.api.Api
import com.sr.metmuseum.util.HttpConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApiBaseUrl(): String = BuildConfig.API_URL

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(HttpConstants.OKHTTP_TIMEOUT_CONNECT, TimeUnit.MILLISECONDS)
            .readTimeout(HttpConstants.OKHTTP_TIMEOUT_READ, TimeUnit.MILLISECONDS)
            .writeTimeout(HttpConstants.OKHTTP_TIMEOUT_WRITE, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.BASIC)).build()

    @Provides
    fun provideApi(
        apiUrl: String,
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): Api {
        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(Api::class.java)
    }
}