package com.ysn.cctvexo

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import kotlinx.android.synthetic.main.activity_cctv_view.*

class CctvViewActivity : AppCompatActivity() {

    private var player: SimpleExoPlayer? = null
    private var playbackPosition: Long? = null
    private var currentWindow: Int? = null
    private var playWhenReady: Boolean? = null
    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cctv_view)
        setTitleToolbar()
        doLoadDataExtras()
        setLandscapeOrientation()
    }

    override fun onResume() {
        initializePlayer()
        super.onResume()
    }

    override fun onPause() {
        releasePlayer()
        super.onPause()
    }

    private fun initializePlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                this,
                DefaultRenderersFactory(this),
                DefaultTrackSelector(),
                DefaultLoadControl()
            )
            player!!.playWhenReady = true
            player_view_activity_cctv_view.player = player
        }
        val mediaSource = buildMediaSource(Uri.parse(url))
        player!!.prepare(mediaSource, true, false)
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player?.currentPosition
            currentWindow = player?.currentWindowIndex
            playWhenReady = player?.playWhenReady
            player?.release()
            player == null
        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val userAgent = "exoplayer-codelab"
        if (uri.lastPathSegment.contains("mp3") || uri.lastPathSegment.contains("mp4")) {
            return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri)
        } else if (uri.lastPathSegment.contains("m3u8")) {
            return HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri)
        } else {
            val dashChunkSourceFactor = DefaultDashChunkSource.Factory(DefaultHttpDataSourceFactory("ua"))
            val manifestDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
            return DashMediaSource.Factory(dashChunkSourceFactor, manifestDataSourceFactory).createMediaSource(uri)
        }
    }

    private fun setLandscapeOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    private fun setTitleToolbar() {
        supportActionBar?.title = "Live Streaming CCTV"
    }

    private fun doLoadDataExtras() {
        val bundle = intent?.extras
        if (bundle != null) {
            url = bundle.getString("url", "")
        }
    }
}
