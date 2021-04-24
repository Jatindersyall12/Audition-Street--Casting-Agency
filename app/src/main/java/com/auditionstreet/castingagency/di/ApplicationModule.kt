package com.silo.di

import android.content.Context
import com.auditionstreet.castingagency.AppCastingAgency
import com.auditionstreet.castingagency.BuildConfig
import com.auditionstreet.castingagency.api.*
import com.auditionstreet.castingagency.api.ApiConstant.Companion.ACCEPT
import com.auditionstreet.castingagency.api.ApiConstant.Companion.APPLICATION_JSON
import com.auditionstreet.castingagency.api.ApiConstant.Companion.CONTENT_TYPE
import com.auditionstreet.castingagency.api.ApiConstant.Companion.PLATFORM
import com.auditionstreet.castingagency.api.ApiConstant.Companion.PLATFORM_TYPE
import com.auditionstreet.castingagency.storage.UserManager
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.google.gson.GsonBuilder
import com.silo.api.*
import com.silo.utils.network.InternetInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module()
@InstallIn(SingletonComponent ::class)
class ApplicationModule {

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun getApplicationContext() = AppCastingAgency()

    @Provides
    @Singleton
    fun getUserManager(preferences: Preferences) : UserManager {
        return UserManager(preferences)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(userManager: UserManager,internetInterceptor: InternetInterceptor) : OkHttpClient{
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(30, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.MINUTES)
            .writeTimeout(30, TimeUnit.MINUTES)
            .connectionPool(ConnectionPool(0, 30, TimeUnit.MINUTES))
            .protocols(listOf(Protocol.HTTP_1_1))
            .followRedirects(true)
            .followSslRedirects(true)
            .addInterceptor(internetInterceptor)
            .addInterceptor { chain ->
               val newRequest = chain.request().newBuilder()
                        .addHeader(ACCEPT, APPLICATION_JSON)
                        .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .addHeader(PLATFORM, PLATFORM_TYPE)
                        //.addHeader("Authorization", "Token ")
                        .build()
                    chain.proceed(newRequest)
            }
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            return builder.addInterceptor(loggingInterceptor)
                .build()
        } else
            return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().excludeFieldsWithModifiers().create()))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun providePreference(@ApplicationContext context : Context) : Preferences {
        return Preferences(context)
    }
}