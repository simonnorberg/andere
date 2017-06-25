package net.simno.andere.data

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {
  @Provides @Singleton fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor(Logger())
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
  }

  @Provides @Singleton fun provideOauthInterceptor(prefs: Prefs): OauthInterceptor {
    return OauthInterceptor(prefs)
  }

  @Provides @Singleton fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .build()
  }

  @Provides @Singleton fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory {
    return RxJava2CallAdapterFactory.create()
  }

  @Provides @Singleton fun provideMoshiConverterFactory(): MoshiConverterFactory {
    return MoshiConverterFactory.create()
  }

  @Provides @Singleton @Named("reddit")
  fun provideRedditRetrofit(client: OkHttpClient, oauthInterceptor: OauthInterceptor,
                            rxJava: RxJava2CallAdapterFactory,
                            moshi: MoshiConverterFactory): Retrofit {
    val oauthClient = client.newBuilder()
        .addInterceptor(oauthInterceptor)
        .build()
    return Retrofit.Builder()
        .baseUrl("https://oauth.reddit.com")
        .callFactory(oauthClient)
        .addCallAdapterFactory(rxJava)
        .addConverterFactory(moshi)
        .build()
  }

  @Provides @Singleton @Named("token")
  fun provideAuthRetrofit(client: OkHttpClient, rxJava: RxJava2CallAdapterFactory,
                          moshi: MoshiConverterFactory): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://www.reddit.com")
        .callFactory(client)
        .addCallAdapterFactory(rxJava)
        .addConverterFactory(moshi)
        .build()
  }

  @Provides @Singleton
  fun provideRedditService(@Named("reddit") retrofit: Retrofit): RedditService {
    return retrofit.create(RedditService::class.java)
  }

  @Provides @Singleton fun provideAuthService(@Named("token") retrofit: Retrofit): AuthService {
    return retrofit.create(AuthService::class.java)
  }

  class Logger : HttpLoggingInterceptor.Logger {
    override fun log(message: String?) {
      Timber.d(message)
    }
  }
}
