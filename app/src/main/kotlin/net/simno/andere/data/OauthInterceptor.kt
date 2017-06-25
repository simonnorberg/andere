package net.simno.andere.data

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class OauthInterceptor @Inject constructor(private val prefs: Prefs) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val builder = chain.request().newBuilder()

    if (prefs.contains("token")) {
      builder.addHeader("Authorization", "bearer " + prefs.getString("token", ""))
    }

    val response = chain.proceed(builder.build())

    if (!response.isSuccessful) {
      if ((response.code() / 100) == 4) {
        // Remove token if authorization failed
        prefs.remove("token")
        prefs.remove("expires")
      }
    }

    return response
  }
}
