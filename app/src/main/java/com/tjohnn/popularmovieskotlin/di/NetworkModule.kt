package com.tjohnn.popularmovieskotlin.di

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.tjohnn.popularmovieskotlin.BuildConfig
import com.tjohnn.popularmovieskotlin.data.remote.ApiService
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient, gson: Gson): ApiService {
        val baseUrl = BuildConfig.API_BASE_URL
        val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
        retrofitBuilder.baseUrl(baseUrl)
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        retrofitBuilder.client(okHttpClient)
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create(gson))
        return retrofitBuilder.build().create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkhttp(interceptor: Interceptor, httpLoggingInterceptor: HttpLoggingInterceptor, cache: Cache): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .cache(cache)
                .addInterceptor(interceptor)
                .addInterceptor(httpLoggingInterceptor)
        return builder.build()
    }


    @Provides
    @Singleton
    fun provideCache(context: Application): Cache {
        val cacheFile = File(context.cacheDir, "HttpCache")
        cacheFile.mkdirs()

        return Cache(cacheFile, (10 * 1000 * 1000).toLong()) //10 MB
    }


    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor{ chain ->
            val original = chain.request()
            val originalHttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.API_KEY)
                    .build()

            val requestBuilder = original.newBuilder()
                    .url(url)

            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)

        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor { message -> Timber.d(message) }
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

}
