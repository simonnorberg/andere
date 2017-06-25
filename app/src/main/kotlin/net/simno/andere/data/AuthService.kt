package net.simno.andere.data

import io.reactivex.Single
import net.simno.andere.BuildConfig
import net.simno.andere.data.model.Auth
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
  @Headers(BuildConfig.REDDIT_USER_AGENT)
  @FormUrlEncoded
  @POST("/api/v1/access_token")
  fun accessToken(@Header("Authorization") authorization: String,
                  @Field("grant_type") grantType: String,
                  @Field("device_id") deviceId: String): Single<Auth>
}
