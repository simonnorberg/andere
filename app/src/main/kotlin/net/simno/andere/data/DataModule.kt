package net.simno.andere.data

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(
    ApiModule::class
))
class DataModule {
  @Provides @Singleton fun providePrefs(context: Context): Prefs {
    return SharedPrefs(context.getSharedPreferences("andere.prefs", Context.MODE_PRIVATE))
  }
}
