package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_insights")
data class SavedInsight(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "THINKING", "TRANSCRIPTION", "VIDEO_ANALYSIS"
    val title: String,
    val prompt: String,
    val content: String,
    val modelUsed: String,
    val durationOrMeta: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
