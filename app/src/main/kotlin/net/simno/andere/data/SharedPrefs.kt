package net.simno.andere.data

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefs @Inject constructor(private val prefs: SharedPreferences) : Prefs {
  override fun contains(key: String): Boolean {
    return prefs.contains(key)
  }

  override fun remove(key: String) {
    prefs.edit().remove(key).apply()
  }

  override fun getString(key: String, defaultValue: String): String {
    return prefs.getString(key, defaultValue)
  }

  override fun putString(key: String, value: String) {
    prefs.edit().putString(key, value).apply()
  }

  override fun getLong(key: String, defaultValue: Long): Long {
    return prefs.getLong(key, defaultValue)
  }

  override fun putLong(key: String, value: Long) {
    prefs.edit().putLong(key, value).apply()
  }
}
