package net.simno.andere.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.recyclerView
import net.simno.andere.AndereApp
import net.simno.andere.BuildConfig
import net.simno.andere.R
import net.simno.andere.data.AuthService
import net.simno.andere.data.Prefs
import net.simno.andere.data.RedditService
import net.simno.andere.data.model.Auth
import okhttp3.Credentials
import timber.log.Timber
import java.time.Instant
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject lateinit var redditService: RedditService
  @Inject lateinit var authService: AuthService
  @Inject lateinit var prefs: Prefs

  private val redditAdapter = RedditAdapter {
    Timber.d("onclick %s", it)
  }
  private val layoutManager = LinearLayoutManager(this)
  private var disposable: Disposable? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AndereApp.appComponent.inject(this)
    setContentView(R.layout.activity_main)

    recyclerView.layoutManager = layoutManager
    recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    recyclerView.adapter = redditAdapter
  }

  override fun onStart() {
    super.onStart()
    fetchPosts()
  }

  override fun onStop() {
    super.onStop()
    disposable?.dispose()
  }

  private fun fetchPosts() {
    disposable?.dispose()
    disposable = authorize()
        .flatMap { redditService.hot("", 0) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ hot ->
          for ((_, data) in hot.data.children) {
            Timber.d("item %s", data.title)
          }

          redditAdapter.items = hot.data.children
          redditAdapter.notifyDataSetChanged()

          Timber.d("hot response")
          Timber.d("adapter size %s", redditAdapter.itemCount)
        }, { e ->
          Timber.e(e, "error")
        })
  }

  fun authorize(): Single<String> {
    return Single.defer {
      val token = prefs.getString("token", "")
      val expires = Instant.ofEpochMilli(prefs.getLong("expires", 0))
      if (token.isNotEmpty() and Instant.now().isBefore(expires)) {
        return@defer Single.just(token)
      }

      authService.accessToken(Credentials.basic(BuildConfig.REDDIT_CLIENT_ID, ""),
          "https://oauth.reddit.com/grants/installed_client", "DO_NOT_TRACK_THIS_DEVICE")
          .doOnSuccess { token ->
            prefs.putString("token", token.access_token)
            prefs.putLong("expires", Instant.now().plusSeconds(token.expires_in).toEpochMilli())
          }
          .map(Auth::access_token)
    }
  }
}
