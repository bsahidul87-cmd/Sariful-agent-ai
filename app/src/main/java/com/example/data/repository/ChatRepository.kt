package com.example.data.repository

import com.example.data.db.ChatConversation
import com.example.data.db.ChatDao
import com.example.data.db.ChatMessage
import com.example.data.db.ConversationWithMessages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ChatRepository(private val chatDao: ChatDao) {

    val allConversations: Flow<List<ChatConversation>> = chatDao.getAllConversations()

    val allConversationsWithMessages: Flow<List<ConversationWithMessages>> =
        chatDao.getAllConversationsWithMessages()

    fun getConversationWithMessages(conversationId: Long): Flow<ConversationWithMessages?> =
        chatDao.getConversationWithMessages(conversationId)

    fun getMessagesForConversation(conversationId: Long): Flow<List<ChatMessage>> =
        chatDao.getMessagesForConversation(conversationId)

    fun searchConversations(query: String): Flow<List<ChatConversation>> =
        chatDao.searchConversations(query)

    fun searchMessages(query: String): Flow<List<ChatMessage>> =
        chatDao.searchMessages(query)

    suspend fun createConversation(
        title: String,
        modelUsed: String = "gemini-3.1-pro-preview",
        category: String = "General"
    ): Long = withContext(Dispatchers.IO) {
        val conversation = ChatConversation(
            title = title,
            modelUsed = modelUsed,
            category = category
        )
        chatDao.insertConversation(conversation)
    }

    suspend fun addMessage(
        conversationId: Long,
        role: String,
        content: String,
        thinkingProcess: String? = null,
        mediaUri: String? = null,
        mimeType: String? = null
    ): Long = withContext(Dispatchers.IO) {
        val message = ChatMessage(
            conversationId = conversationId,
            role = role,
            content = content,
            thinkingProcess = thinkingProcess,
            mediaUri = mediaUri,
            mimeType = mimeType
        )
        val messageId = chatDao.insertMessage(message)
        chatDao.updateConversationTimestamp(conversationId, System.currentTimeMillis())
        messageId
    }

    suspend fun togglePinConversation(conversationId: Long) = withContext(Dispatchers.IO) {
        chatDao.togglePinConversation(conversationId)
    }

    suspend fun deleteConversation(conversationId: Long) = withContext(Dispatchers.IO) {
        chatDao.deleteConversationById(conversationId)
    }

    suspend fun deleteMessage(messageId: Long) = withContext(Dispatchers.IO) {
        chatDao.deleteMessageById(messageId)
    }

    suspend fun clearAllConversations() = withContext(Dispatchers.IO) {
        chatDao.deleteAllConversations()
    }
}
