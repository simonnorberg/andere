package net.simno.andere

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndereModule(val app: AndereApp) {
  @Provides @Singleton fun provideContext(): Context {
    return app
  }
}
