package net.simno.andere

import dagger.Component
import net.simno.andere.data.DataModule
import net.simno.andere.ui.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
    AndereModule::class,
    DataModule::class
))
interface AndereComponent {
  fun inject(activity: MainActivity)
}
