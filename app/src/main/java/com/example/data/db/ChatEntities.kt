package com.example.data.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "chat_conversations",
    indices = [Index(value = ["updatedAt"])]
)
data class ChatConversation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val modelUsed: String = "gemini-3.1-pro-preview",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false,
    val category: String = "General"
)

@Entity(
    tableName = "chat_messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatConversation::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["conversationId"]),
        Index(value = ["timestamp"])
    ]
)
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "conversationId")
    val conversationId: Long,
    val role: String, // "user", "model", "system"
    val content: String,
    val thinkingProcess: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val mediaUri: String? = null,
    val mimeType: String? = null
)

data class ConversationWithMessages(
    @Embedded
    val conversation: ChatConversation,
    @Relation(
        parentColumn = "id",
        entityColumn = "conversationId"
    )
    val messages: List<ChatMessage> = emptyList()
)
