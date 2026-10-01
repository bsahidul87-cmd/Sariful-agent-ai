package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.OpenableColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

data class VideoInfo(
    val uri: Uri,
    val name: String,
    val sizeBytes: Long,
    val durationMs: Long,
    val width: Int,
    val height: Int,
    val thumbnail: Bitmap?
)

object VideoHelper {
    suspend fun extractVideoInfo(context: Context, uri: Uri): VideoInfo? = withContext(Dispatchers.IO) {
        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, uri)

            val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val durationMs = durationStr?.toLongOrNull() ?: 0L

            val widthStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            val heightStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            val width = widthStr?.toIntOrNull() ?: 0
            val height = heightStr?.toIntOrNull() ?: 0

            val thumbnail = retriever.getFrameAtTime(1_000_000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                ?: retriever.frameAtTime

            var sizeBytes = 0L
            try {
                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (cursor.moveToFirst() && sizeIndex >= 0) {
                        sizeBytes = cursor.getLong(sizeIndex)
                    }
                }
            } catch (_: Exception) {}

            var displayName = "selected_video.mp4"
            try {
                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (cursor.moveToFirst() && nameIndex >= 0) {
                        displayName = cursor.getString(nameIndex) ?: displayName
                    }
                }
            } catch (_: Exception) {}

            retriever.release()

            VideoInfo(
                uri = uri,
                name = displayName,
                sizeBytes = sizeBytes,
                durationMs = durationMs,
                width = width,
                height = height,
                thumbnail = thumbnail
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun readVideoBytes(context: Context, uri: Uri, maxBytes: Long = 18 * 1024 * 1024): ByteArray? =
        withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val buffer = ByteArrayOutputStream()
                    val data = ByteArray(16384)
                    var bytesRead: Int
                    var totalRead = 0L
                    while (inputStream.read(data, 0, data.size).also { bytesRead = it } != -1) {
                        buffer.write(data, 0, bytesRead)
                        totalRead += bytesRead
                        if (totalRead > maxBytes) {
                            break
                        }
                    }
                    buffer.toByteArray()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
}
