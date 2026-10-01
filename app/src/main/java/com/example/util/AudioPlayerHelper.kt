package com.example.util

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File

class AudioPlayerHelper(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration.asStateFlow()

    fun playFile(file: File, scope: CoroutineScope) {
        stop()
        try {
            val player = MediaPlayer().apply {
                setDataSource(file.absolutePath)
                prepare()
                start()
            }
            mediaPlayer = player
            _duration.value = player.duration
            _isPlaying.value = true

            player.setOnCompletionListener {
                _isPlaying.value = false
                _currentPosition.value = 0
                progressJob?.cancel()
            }

            progressJob?.cancel()
            progressJob = scope.launch(Dispatchers.Main) {
                while (isActive && _isPlaying.value) {
                    delay(150)
                    try {
                        _currentPosition.value = mediaPlayer?.currentPosition ?: 0
                    } catch (_: Exception) {}
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _isPlaying.value = false
        }
    }

    fun togglePlayPause(file: File, scope: CoroutineScope) {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
                _isPlaying.value = false
            } else {
                player.start()
                _isPlaying.value = true
                progressJob?.cancel()
                progressJob = scope.launch(Dispatchers.Main) {
                    while (isActive && _isPlaying.value) {
                        delay(150)
                        try {
                            _currentPosition.value = mediaPlayer?.currentPosition ?: 0
                        } catch (_: Exception) {}
                    }
                }
            }
        } ?: run {
            playFile(file, scope)
        }
    }

    fun stop() {
        progressJob?.cancel()
        progressJob = null
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null
        _isPlaying.value = false
        _currentPosition.value = 0
    }
}
