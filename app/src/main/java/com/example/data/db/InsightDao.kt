package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface InsightDao {
    @Query("SELECT * FROM saved_insights ORDER BY timestamp DESC")
    fun getAllInsights(): Flow<List<SavedInsight>>

    @Query("SELECT * FROM saved_insights WHERE type = :type ORDER BY timestamp DESC")
    fun getInsightsByType(type: String): Flow<List<SavedInsight>>

    @Query("SELECT * FROM saved_insights WHERE id = :id")
    suspend fun getInsightById(id: Long): SavedInsight?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInsight(insight: SavedInsight): Long

    @Update
    suspend fun updateInsight(insight: SavedInsight)

    @Delete
    suspend fun deleteInsight(insight: SavedInsight)

    @Query("DELETE FROM saved_insights WHERE id = :id")
    suspend fun deleteInsightById(id: Long)

    @Query("UPDATE saved_insights SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Long)

    @Query("SELECT * FROM saved_insights WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchInsights(query: String): Flow<List<SavedInsight>>
}
