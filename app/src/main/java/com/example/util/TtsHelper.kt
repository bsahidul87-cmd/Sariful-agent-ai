package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TtsHelper(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("TtsHelper", "Failed to initialize TTS", e)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            // Try to set Bengali locale first, fallback to English
            val bengaliLocale = Locale("bn", "BD")
            val result = tts?.setLanguage(bengaliLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.language = Locale.US
            }
            tts?.setSpeechRate(0.95f)
            tts?.setPitch(1.05f)
        } else {
            isInitialized = false
        }
    }

    fun speak(text: String, isBengali: Boolean = true) {
        if (!isInitialized || tts == null) return

        try {
            if (isBengali) {
                val bengaliLocale = Locale("bn", "BD")
                val result = tts?.setLanguage(bengaliLocale)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.language = Locale.US
                }
            } else {
                tts?.language = Locale.US
            }
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "veo_tts_id")
        } catch (e: Exception) {
            Log.e("TtsHelper", "Error speaking text", e)
        }
    }

    fun stop() {
        try {
            tts?.stop()
        } catch (e: Exception) {
            Log.e("TtsHelper", "Error stopping TTS", e)
        }
    }

    fun release() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
            isInitialized = false
        } catch (e: Exception) {
            Log.e("TtsHelper", "Error shutting down TTS", e)
        }
    }
}
