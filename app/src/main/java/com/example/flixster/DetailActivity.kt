package com.example.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers
import org.w3c.dom.Text

private const val TAG = "DetailActivity"
private const val YOUTUBE_API_KEY ="AIzaSyCWaFd-ewZVdw83cos_mg3af2x-DkjsJsg"
private const val TRAILER_URL ="https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"

class DetailActivity : YouTubeBaseActivity() {
    private lateinit var tvTitle: TextView
    private lateinit var tvOverview: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var ytPlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        tvTitle= findViewById(R.id.tvTitle)
        tvOverview= findViewById(R.id.tvOverview)
        ratingBar= findViewById(R.id.rbVoteAverage)
        ytPlayerView= findViewById(R.id.player)

        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie
        tvTitle.text = movie.title
        tvOverview.text= movie.overview
        ratingBar.rating= movie.voteAverage.toFloat()

        val client = AsyncHttpClient()
        client.get(TRAILER_URL.format(movie.movieId), object : JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                Log.e(TAG, "on failure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                val results= json.jsonObject.getJSONArray("results")
                if (results.length()==0){
                    Log.w(TAG, "No movie trailer")
                    return
                }
                val movieTrailerJson = results.getJSONObject(0)
                val ytKey= movieTrailerJson.getString("key")

                initializeYoutube(ytKey)
            }
        })

    }

    private fun initializeYoutube(ytKey: String) {
        ytPlayerView.initialize(YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                player?.cueVideo(ytKey)
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.e(TAG, "onInitializationFailure")
            }

        })
    }
}