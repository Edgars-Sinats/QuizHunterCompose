package com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView

@Composable
fun VideoPlayerScreen(uriLink: String) {
    val context = LocalContext.current

    var playWhenReady by remember { mutableStateOf(true) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uriLink))
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            playWhenReady = playWhenReady
            prepare()
            play()
        }
    }
    DisposableEffect(
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                StyledPlayerView(context).apply {
                    player = exoPlayer
                    useController = true
                    FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            }
        )
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}