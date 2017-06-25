package net.simno.andere.data

interface Prefs {
  fun contains(key: String): Boolean
  fun remove(key: String)
  fun getString(key: String, defaultValue: String): String
  fun putString(key: String, value: String)
  fun getLong(key: String, defaultValue: Long): Long
  fun putLong(key: String, value: Long)
}
