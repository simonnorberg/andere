package net.simno.andere

import android.app.Application
import timber.log.Timber

class AndereApp : Application() {
  companion object {
    lateinit var appComponent: AndereComponent
  }

  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    appComponent = DaggerAndereComponent.builder()
        .andereModule(AndereModule(this))
        .build()
  }
}
