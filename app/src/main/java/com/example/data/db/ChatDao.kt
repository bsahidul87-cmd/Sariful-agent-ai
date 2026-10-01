package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    // --- Conversations ---

    @Query("SELECT * FROM chat_conversations ORDER BY isPinned DESC, updatedAt DESC")
    fun getAllConversations(): Flow<List<ChatConversation>>

    @Query("SELECT * FROM chat_conversations WHERE id = :id")
    fun getConversationById(id: Long): Flow<ChatConversation?>

    @Query("SELECT * FROM chat_conversations WHERE id = :id")
    suspend fun getConversationByIdOnce(id: Long): ChatConversation?

    @Transaction
    @Query("SELECT * FROM chat_conversations ORDER BY isPinned DESC, updatedAt DESC")
    fun getAllConversationsWithMessages(): Flow<List<ConversationWithMessages>>

    @Transaction
    @Query("SELECT * FROM chat_conversations WHERE id = :conversationId")
    fun getConversationWithMessages(conversationId: Long): Flow<ConversationWithMessages?>

    @Transaction
    @Query("SELECT * FROM chat_conversations WHERE id = :conversationId")
    suspend fun getConversationWithMessagesOnce(conversationId: Long): ConversationWithMessages?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ChatConversation): Long

    @Update
    suspend fun updateConversation(conversation: ChatConversation)

    @Query("UPDATE chat_conversations SET updatedAt = :timestamp WHERE id = :id")
    suspend fun updateConversationTimestamp(id: Long, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE chat_conversations SET isPinned = NOT isPinned WHERE id = :id")
    suspend fun togglePinConversation(id: Long)

    @Delete
    suspend fun deleteConversation(conversation: ChatConversation)

    @Query("DELETE FROM chat_conversations WHERE id = :id")
    suspend fun deleteConversationById(id: Long)

    @Query("DELETE FROM chat_conversations")
    suspend fun deleteAllConversations()

    @Query("SELECT * FROM chat_conversations WHERE title LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    fun searchConversations(query: String): Flow<List<ChatConversation>>

    // --- Messages ---

    @Query("SELECT * FROM chat_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesForConversation(conversationId: Long): Flow<List<ChatMessage>>

    @Query("SELECT * FROM chat_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    suspend fun getMessagesForConversationOnce(conversationId: Long): List<ChatMessage>

    @Query("SELECT * FROM chat_messages WHERE id = :id")
    suspend fun getMessageById(id: Long): ChatMessage?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessage>): List<Long>

    @Update
    suspend fun updateMessage(message: ChatMessage)

    @Delete
    suspend fun deleteMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages WHERE id = :id")
    suspend fun deleteMessageById(id: Long)

    @Query("DELETE FROM chat_messages WHERE conversationId = :conversationId")
    suspend fun deleteMessagesForConversation(conversationId: Long)

    @Query("SELECT * FROM chat_messages WHERE content LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchMessages(query: String): Flow<List<ChatMessage>>
}
