package net.simno.andere.data

import io.reactivex.Single
import net.simno.andere.BuildConfig
import net.simno.andere.data.model.Listing
import org.jetbrains.annotations.Nullable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RedditService {
  @Headers(BuildConfig.REDDIT_USER_AGENT)
  @GET("/r/androiddev/hot")
  fun hot(@Nullable @Query("after") after: String,
          @Nullable @Query("count") count: Int): Single<Listing>
}
