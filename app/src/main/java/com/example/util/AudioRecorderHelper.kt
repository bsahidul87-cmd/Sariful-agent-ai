package com.example.util

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
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

class AudioRecorderHelper(private val context: Context) {
    private var mediaRecorder: MediaRecorder? = null
    private var currentFile: File? = null
    private var amplitudeJob: Job? = null

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _amplitude = MutableStateFlow(0)
    val amplitude: StateFlow<Int> = _amplitude.asStateFlow()

    private val _durationSeconds = MutableStateFlow(0)
    val durationSeconds: StateFlow<Int> = _durationSeconds.asStateFlow()

    fun startRecording(coroutineScope: CoroutineScope): File? {
        try {
            val file = File(context.cacheDir, "rec_${System.currentTimeMillis()}.m4a")
            currentFile = file

            val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }

            recorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
                setOutputFile(file.absolutePath)
                prepare()
                start()
            }
            mediaRecorder = recorder
            _isRecording.value = true
            _durationSeconds.value = 0

            amplitudeJob?.cancel()
            amplitudeJob = coroutineScope.launch(Dispatchers.IO) {
                var secondsCounter = 0
                var subSecond = 0
                while (isActive && _isRecording.value) {
                    delay(100)
                    subSecond += 1
                    if (subSecond >= 10) {
                        secondsCounter++
                        subSecond = 0
                        _durationSeconds.value = secondsCounter
                    }
                    try {
                        val maxAmp = mediaRecorder?.maxAmplitude ?: 0
                        _amplitude.value = maxAmp
                    } catch (_: Exception) {}
                }
            }
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            _isRecording.value = false
            return null
        }
    }

    fun stopRecording(): File? {
        amplitudeJob?.cancel()
        amplitudeJob = null
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaRecorder = null
            _isRecording.value = false
            _amplitude.value = 0
        }
        return currentFile
    }

    fun release() {
        amplitudeJob?.cancel()
        try {
            mediaRecorder?.release()
        } catch (_: Exception) {}
        mediaRecorder = null
        _isRecording.value = false
    }
}
