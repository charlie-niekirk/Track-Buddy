package me.cniekirk.trackbuddy.di

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.cniekirk.trackbuddy.BuildConfig
import me.cniekirk.trackbuddy.data.remote.GwrService
import me.cniekirk.trackbuddy.data.remote.HuxleyService
import me.cniekirk.trackbuddy.data.util.SingleOrListAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache {
        return Cache(context.cacheDir, 102400L)
    }

    @Provides
    @Singleton
    fun provideOkHttp(cache: Cache): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .cache(cache)
                .readTimeout(5000L, TimeUnit.MILLISECONDS)
                .build()
        } else {
            OkHttpClient.Builder()
                .cache(cache)
                .readTimeout(5000L, TimeUnit.MILLISECONDS)
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(SingleOrListAdapterFactory)
            .build()
    }

    @Provides
    @Singleton
    @Named("main")
    fun provideMainRetrofit(okHttpClient: Lazy<OkHttpClient>, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://huxley2.azurewebsites.net/")
            .callFactory { okHttpClient.get().newCall(it) }
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    @Named("gwr")
    fun provideGwrRetrofit(okHttpClient: Lazy<OkHttpClient>, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://prod.mobileapi.gwr.com/")
            .callFactory { okHttpClient.get().newCall(it) }
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideHuxleyService(@Named("main") retrofit: Retrofit): HuxleyService {
        return retrofit.create(HuxleyService::class.java)
    }

    @Provides
    @Singleton
    fun provideGwrService(@Named("gwr") retrofit: Retrofit): GwrService {
        return retrofit.create(GwrService::class.java)
    }
}