package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.ui.graphics.Color
import com.example.R
import com.example.data.api.Content
import com.example.data.api.Part
import com.example.data.db.AppDatabase
import com.example.data.db.ChatConversation
import com.example.data.db.ChatMessage
import com.example.data.db.SavedInsight
import com.example.data.repository.AiRepository
import com.example.data.repository.ChatRepository
import com.example.util.TtsHelper
import com.example.util.Veo20sProject
import com.example.util.VeoSceneData
import com.example.util.VeoVideoHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen(val title: String, val bengaliLabel: String) {
    CHAT("Bengali AI Chat", "চ্যাট"),
    VEO_STUDIO("AI Video & Photo", "ভিডিও ও ফটো"),
    MEDIA_EDITOR("Photo & Video Editor", "এডিটর"),
    FREE_HOSTING("Free Domain & Hosting", "হোস্টিং"),
    HISTORY("Saved Content", "সংরক্ষিত")
}

data class ChatUiMessage(
    val id: Long = 0,
    val role: String, // "user" or "model"
    val text: String,
    val sources: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    val repository = AiRepository(database.insightDao())
    val chatRepository = ChatRepository(database.chatDao())

    // --- Theme State (Dark / Light Mode) ---
    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    val ttsHelper = TtsHelper(application)

    fun toggleTheme() {
        _isDarkMode.value = !_isDarkMode.value
    }

    // --- Navigation ---
    private val _currentScreen = MutableStateFlow(Screen.CHAT)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    // --- Bengali AI Chat with Web Search ---
    private val _chatInput = MutableStateFlow("")
    val chatInput: StateFlow<String> = _chatInput.asStateFlow()

    private val _isWebSearchEnabled = MutableStateFlow(false)
    val isWebSearchEnabled: StateFlow<Boolean> = _isWebSearchEnabled.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatUiMessage>>(
        listOf(
            ChatUiMessage(
                role = "model",
                text = "আসসালামু আলাইকুম! আমি **Sariful AI Agent** (Sharif AI Tech)।\n\nআপনি বাংলায় বা ইংরেজিতে যে কোনো প্রশ্ন করতে পারেন। আমি ইউটিউব স্ক্রিপ্ট, ভিডিও সিন প্রম্পট, কোডিং, ফ্রিল্যান্সিং বা টেক ইনফরমেশনে আপনাকে সাহায্য করতে প্রস্তুত!\n\n🌐 প্রয়োজন হলে নিচের **Web Search** অন করে রিয়েল-টাইম তথ্য অনুসন্ধান করতে পারেন।"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatUiMessage>> = _chatMessages.asStateFlow()

    private var currentConversationId: Long = 0L

    fun setChatInput(text: String) {
        _chatInput.value = text
    }

    fun toggleWebSearch() {
        _isWebSearchEnabled.value = !_isWebSearchEnabled.value
    }

    fun sendChatMessage(overrideText: String? = null) {
        val messageText = (overrideText ?: _chatInput.value).trim()
        if (messageText.isBlank() || _isChatLoading.value) return

        _chatInput.value = ""
        val userMsg = ChatUiMessage(role = "user", text = messageText)
        _chatMessages.value = _chatMessages.value + userMsg

        _isChatLoading.value = true

        viewModelScope.launch {
            if (currentConversationId == 0L) {
                currentConversationId = chatRepository.createConversation(
                    title = if (messageText.length > 30) messageText.take(28) + "..." else messageText,
                    category = "Bengali Chat"
                )
            }
            chatRepository.addMessage(
                conversationId = currentConversationId,
                role = "user",
                content = messageText
            )

            val apiHistory = _chatMessages.value.takeLast(10).map { msg ->
                Content(
                    parts = listOf(Part(text = msg.text)),
                    role = if (msg.role == "user") "user" else "model"
                )
            }

            val result = repository.chatWithBengaliAi(apiHistory, _isWebSearchEnabled.value)
            _isChatLoading.value = false

            result.onSuccess { response ->
                val modelMsg = ChatUiMessage(
                    role = "model",
                    text = response.replyText,
                    sources = response.webSources
                )
                _chatMessages.value = _chatMessages.value + modelMsg
                chatRepository.addMessage(
                    conversationId = currentConversationId,
                    role = "model",
                    content = response.replyText
                )
            }.onFailure { err ->
                val errorMsg = ChatUiMessage(
                    role = "model",
                    text = "দুঃখিত, কোনো ত্রুটি হয়েছে: ${err.message}\n(অনুগ্রহ করে AI Studio Secrets panel এ GEMINI_API_KEY সেট করা আছে কিনা নিশ্চিত করুন)"
                )
                _chatMessages.value = _chatMessages.value + errorMsg
            }
        }
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            ChatUiMessage(
                role = "model",
                text = "নতুন চ্যাট শুরু হয়েছে! বলুন, আজ আপনাকে কী বিষয়ে সাহায্য করতে পারি?"
            )
        )
        currentConversationId = 0L
    }

    // --- YouTube Shorts Script Generator ---
    private val _shortsTopic = MutableStateFlow("")
    val shortsTopic: StateFlow<String> = _shortsTopic.asStateFlow()

    private val _shortsTone = MutableStateFlow("Exciting & Fast-Paced")
    val shortsTone: StateFlow<String> = _shortsTone.asStateFlow()

    private val _shortsDuration = MutableStateFlow("30-60 Seconds")
    val shortsDuration: StateFlow<String> = _shortsDuration.asStateFlow()

    private val _shortsLanguage = MutableStateFlow("Bengali (বাংলা)")
    val shortsLanguage: StateFlow<String> = _shortsLanguage.asStateFlow()

    private val _isShortsLoading = MutableStateFlow(false)
    val isShortsLoading: StateFlow<Boolean> = _isShortsLoading.asStateFlow()

    private val _shortsResult = MutableStateFlow<String?>(null)
    val shortsResult: StateFlow<String?> = _shortsResult.asStateFlow()

    private val _shortsError = MutableStateFlow<String?>(null)
    val shortsError: StateFlow<String?> = _shortsError.asStateFlow()

    private val _isShortsSaved = MutableStateFlow(false)
    val isShortsSaved: StateFlow<Boolean> = _isShortsSaved.asStateFlow()

    fun setShortsTopic(text: String) { _shortsTopic.value = text }
    fun setShortsTone(tone: String) { _shortsTone.value = tone }
    fun setShortsDuration(duration: String) { _shortsDuration.value = duration }
    fun setShortsLanguage(lang: String) { _shortsLanguage.value = lang }

    fun generateShortsScript() {
        val topic = _shortsTopic.value.trim()
        if (topic.isBlank() || _isShortsLoading.value) return

        _isShortsLoading.value = true
        _shortsError.value = null
        _shortsResult.value = null
        _isShortsSaved.value = false

        viewModelScope.launch {
            val result = repository.generateShortsScript(
                topic = topic,
                tone = _shortsTone.value,
                duration = _shortsDuration.value,
                language = _shortsLanguage.value
            )
            _isShortsLoading.value = false
            result.onSuccess { script ->
                _shortsResult.value = script
            }.onFailure { err ->
                _shortsError.value = err.message ?: "Failed to generate Shorts script"
            }
        }
    }

    fun saveShortsResult() {
        val script = _shortsResult.value ?: return
        viewModelScope.launch {
            repository.saveInsight(
                type = "SHORTS_SCRIPT",
                title = "Shorts: ${_shortsTopic.value.take(30)}",
                prompt = _shortsTopic.value,
                content = script,
                modelUsed = "gemini-3.5-flash",
                durationOrMeta = "${_shortsLanguage.value} • ${_shortsDuration.value}"
            )
            _isShortsSaved.value = true
        }
    }

    // --- Scene-by-Scene Video Prompt Generator ---
    private val _videoStoryline = MutableStateFlow("")
    val videoStoryline: StateFlow<String> = _videoStoryline.asStateFlow()

    private val _videoEngine = MutableStateFlow("Veo 2 / Runway Gen-3")
    val videoEngine: StateFlow<String> = _videoEngine.asStateFlow()

    private val _videoSceneCount = MutableStateFlow(4)
    val videoSceneCount: StateFlow<Int> = _videoSceneCount.asStateFlow()

    private val _isVideoPromptLoading = MutableStateFlow(false)
    val isVideoPromptLoading: StateFlow<Boolean> = _isVideoPromptLoading.asStateFlow()

    private val _videoPromptResult = MutableStateFlow<String?>(null)
    val videoPromptResult: StateFlow<String?> = _videoPromptResult.asStateFlow()

    private val _videoPromptError = MutableStateFlow<String?>(null)
    val videoPromptError: StateFlow<String?> = _videoPromptError.asStateFlow()

    private val _isVideoPromptSaved = MutableStateFlow(false)
    val isVideoPromptSaved: StateFlow<Boolean> = _isVideoPromptSaved.asStateFlow()

    fun setVideoStoryline(text: String) { _videoStoryline.value = text }
    fun setVideoEngine(engine: String) { _videoEngine.value = engine }
    fun setVideoSceneCount(count: Int) { _videoSceneCount.value = count }

    fun generateScenePrompts() {
        val storyline = _videoStoryline.value.trim()
        if (storyline.isBlank() || _isVideoPromptLoading.value) return

        _isVideoPromptLoading.value = true
        _videoPromptError.value = null
        _videoPromptResult.value = null
        _isVideoPromptSaved.value = false

        viewModelScope.launch {
            val result = repository.generateSceneByScenePrompts(
                storyline = storyline,
                aiEngine = _videoEngine.value,
                sceneCount = _videoSceneCount.value
            )
            _isVideoPromptLoading.value = false
            result.onSuccess { output ->
                _videoPromptResult.value = output
            }.onFailure { err ->
                _videoPromptError.value = err.message ?: "Failed to generate video prompts"
            }
        }
    }

    fun saveVideoPromptResult() {
        val output = _videoPromptResult.value ?: return
        viewModelScope.launch {
            repository.saveInsight(
                type = "VIDEO_PROMPTS",
                title = "Video Prompts: ${_videoStoryline.value.take(28)}",
                prompt = _videoStoryline.value,
                content = output,
                modelUsed = "gemini-3.5-flash",
                durationOrMeta = "${_videoEngine.value} • ${_videoSceneCount.value} Scenes"
            )
            _isVideoPromptSaved.value = true
        }
    }

    // --- Thumbnail Prompt Generator ---
    private val _thumbnailTitle = MutableStateFlow("")
    val thumbnailTitle: StateFlow<String> = _thumbnailTitle.asStateFlow()

    private val _thumbnailStyle = MutableStateFlow("Cyberpunk & High-Tech Glow")
    val thumbnailStyle: StateFlow<String> = _thumbnailStyle.asStateFlow()

    private val _thumbnailAudience = MutableStateFlow("Tech & AI Enthusiasts")
    val thumbnailAudience: StateFlow<String> = _thumbnailAudience.asStateFlow()

    private val _isThumbnailLoading = MutableStateFlow(false)
    val isThumbnailLoading: StateFlow<Boolean> = _isThumbnailLoading.asStateFlow()

    private val _thumbnailResult = MutableStateFlow<String?>(null)
    val thumbnailResult: StateFlow<String?> = _thumbnailResult.asStateFlow()

    private val _thumbnailError = MutableStateFlow<String?>(null)
    val thumbnailError: StateFlow<String?> = _thumbnailError.asStateFlow()

    private val _isThumbnailSaved = MutableStateFlow(false)
    val isThumbnailSaved: StateFlow<Boolean> = _isThumbnailSaved.asStateFlow()

    fun setThumbnailTitle(text: String) { _thumbnailTitle.value = text }
    fun setThumbnailStyle(style: String) { _thumbnailStyle.value = style }
    fun setThumbnailAudience(audience: String) { _thumbnailAudience.value = audience }

    fun generateThumbnailPrompt() {
        val title = _thumbnailTitle.value.trim()
        if (title.isBlank() || _isThumbnailLoading.value) return

        _isThumbnailLoading.value = true
        _thumbnailError.value = null
        _thumbnailResult.value = null
        _isThumbnailSaved.value = false

        viewModelScope.launch {
            val result = repository.generateThumbnailPrompt(
                videoTitle = title,
                style = _thumbnailStyle.value,
                targetAudience = _thumbnailAudience.value
            )
            _isThumbnailLoading.value = false
            result.onSuccess { output ->
                _thumbnailResult.value = output
            }.onFailure { err ->
                _thumbnailError.value = err.message ?: "Failed to generate thumbnail prompt"
            }
        }
    }

    fun saveThumbnailResult() {
        val output = _thumbnailResult.value ?: return
        viewModelScope.launch {
            repository.saveInsight(
                type = "THUMBNAIL_PROMPT",
                title = "Thumbnail: ${_thumbnailTitle.value.take(28)}",
                prompt = _thumbnailTitle.value,
                content = output,
                modelUsed = "gemini-3.5-flash",
                durationOrMeta = _thumbnailStyle.value
            )
            _isThumbnailSaved.value = true
        }
    }

    // --- Saved Content / History ---
    private val _historySearchQuery = MutableStateFlow("")
    val historySearchQuery: StateFlow<String> = _historySearchQuery.asStateFlow()

    private val _historyFilterType = MutableStateFlow("ALL")
    val historyFilterType: StateFlow<String> = _historyFilterType.asStateFlow()

    val filteredInsights: StateFlow<List<SavedInsight>> = combine(
        repository.allInsights,
        _historySearchQuery,
        _historyFilterType
    ) { insights, query, filter ->
        insights.filter { insight ->
            val matchesFilter = if (filter == "ALL") true else insight.type == filter
            val matchesQuery = if (query.isBlank()) true else {
                insight.title.contains(query, ignoreCase = true) ||
                        insight.content.contains(query, ignoreCase = true) ||
                        insight.prompt.contains(query, ignoreCase = true)
            }
            matchesFilter && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setHistorySearchQuery(query: String) { _historySearchQuery.value = query }
    fun setHistoryFilterType(type: String) { _historyFilterType.value = type }
    fun deleteInsight(id: Long) { viewModelScope.launch { repository.deleteInsight(id) } }
    fun toggleFavorite(id: Long) { viewModelScope.launch { repository.toggleFavorite(id) } }

    // =========================================================================
    // --- Google Veo 3 (20-Second) AI Video Studio & Simulation Player ---
    // =========================================================================
    private val _veoTopic = MutableStateFlow("ভবিষ্যতের সাইবারপাঙ্ক ঢাকা ২০৫০")
    val veoTopic: StateFlow<String> = _veoTopic.asStateFlow()

    private val _veoCameraMotion = MutableStateFlow("Cinematic Drone Sweep & Dolly Zoom")
    val veoCameraMotion: StateFlow<String> = _veoCameraMotion.asStateFlow()

    private val _veoVisualStyle = MutableStateFlow("Photorealistic 8K Cinema")
    val veoVisualStyle: StateFlow<String> = _veoVisualStyle.asStateFlow()

    private val _veoAspectRatio = MutableStateFlow("9:16 (Shorts/Reels)")
    val veoAspectRatio: StateFlow<String> = _veoAspectRatio.asStateFlow()

    private val _veoLanguage = MutableStateFlow("Bengali (বাংলা)")
    val veoLanguage: StateFlow<String> = _veoLanguage.asStateFlow()

    private val _isVeoLoading = MutableStateFlow(false)
    val isVeoLoading: StateFlow<Boolean> = _isVeoLoading.asStateFlow()

    private val _veoProject = MutableStateFlow<Veo20sProject?>(VeoVideoHelper.sampleProjects.firstOrNull())
    val veoProject: StateFlow<Veo20sProject?> = _veoProject.asStateFlow()

    private val _veoError = MutableStateFlow<String?>(null)
    val veoError: StateFlow<String?> = _veoError.asStateFlow()

    private val _isVeoSaved = MutableStateFlow(false)
    val isVeoSaved: StateFlow<Boolean> = _isVeoSaved.asStateFlow()

    // 20-Second Player Simulator States
    private val _veoPlayerTimeMs = MutableStateFlow(0L) // 0 to 20,000 ms
    val veoPlayerTimeMs: StateFlow<Long> = _veoPlayerTimeMs.asStateFlow()

    private val _isVeoPlayerPlaying = MutableStateFlow(false)
    val isVeoPlayerPlaying: StateFlow<Boolean> = _isVeoPlayerPlaying.asStateFlow()

    private val _isVeoPlayerTtsEnabled = MutableStateFlow(false)
    val isVeoPlayerTtsEnabled: StateFlow<Boolean> = _isVeoPlayerTtsEnabled.asStateFlow()

    private val _isVeoPlayerShortsMode = MutableStateFlow(true)
    val isVeoPlayerShortsMode: StateFlow<Boolean> = _isVeoPlayerShortsMode.asStateFlow()

    private var playerJob: Job? = null
    private var lastSpokenSceneIndex: Int = -1

    fun setVeoTopic(topic: String) { _veoTopic.value = topic }
    fun setVeoCameraMotion(motion: String) { _veoCameraMotion.value = motion }
    fun setVeoVisualStyle(style: String) { _veoVisualStyle.value = style }
    fun setVeoAspectRatio(ratio: String) {
        _veoAspectRatio.value = ratio
        _isVeoPlayerShortsMode.value = ratio.startsWith("9:16")
    }
    fun setVeoLanguage(lang: String) { _veoLanguage.value = lang }

    fun loadPresetVeoProject(project: Veo20sProject) {
        pauseVeoVideo()
        _veoTopic.value = project.title
        _veoProject.value = project
        _veoError.value = null
        _isVeoSaved.value = false
        resetVeoVideo()
    }

    fun generateVeo20SecondVideo() {
        val topic = _veoTopic.value.trim()
        if (topic.isBlank() || _isVeoLoading.value) return

        pauseVeoVideo()
        _isVeoLoading.value = true
        _veoError.value = null
        _isVeoSaved.value = false

        viewModelScope.launch {
            val result = repository.generateVeo20SecondVideoProject(
                topic = topic,
                cameraMotion = _veoCameraMotion.value,
                visualStyle = _veoVisualStyle.value,
                aspectRatio = _veoAspectRatio.value,
                language = _veoLanguage.value
            )
            _isVeoLoading.value = false
            result.onSuccess { rawOutput ->
                val project = VeoVideoHelper.parseVeoOutput(rawOutput, topic)
                _veoProject.value = project
                resetVeoVideo()
            }.onFailure { err ->
                _veoError.value = err.message ?: "Failed to generate Veo 3 project"
                if (_veoProject.value == null) {
                    _veoProject.value = VeoVideoHelper.sampleProjects.firstOrNull()
                }
            }
        }
    }

    fun saveVeoProject() {
        val proj = _veoProject.value ?: return
        viewModelScope.launch {
            val cleanContent = if (proj.rawMarkdown.isNotBlank()) {
                proj.rawMarkdown
            } else {
                "Master Veo 3 Prompt:\n${proj.masterPrompt}\n\nNegative Prompt:\n${proj.negativePrompt}\n\nScenes:\n" +
                        proj.scenes.joinToString("\n\n") { "Scene ${it.sceneNumber} (${it.timeRange}): ${it.visualPrompt}\nVoiceover: ${it.voiceover}" } +
                        "\n\nFree Creation Guide:\n${proj.freeWorkflowGuide}"
            }

            repository.saveInsight(
                type = "VEO_20S_VIDEO",
                title = "Veo 3 (20s): ${proj.title.take(28)}",
                prompt = "${_veoTopic.value} [${_veoCameraMotion.value}]",
                content = cleanContent,
                modelUsed = "veo-3.1-generate-preview",
                durationOrMeta = "20s • ${_veoAspectRatio.value}"
            )
            _isVeoSaved.value = true
        }
    }

    // --- 20-Second Video Player Simulator Functions ---
    fun playVeoVideo() {
        if (_isVeoPlayerPlaying.value) return
        _isVeoPlayerPlaying.value = true

        playerJob?.cancel()
        playerJob = viewModelScope.launch {
            if (_veoPlayerTimeMs.value >= 20000L) {
                _veoPlayerTimeMs.value = 0L
                lastSpokenSceneIndex = -1
            }

            while (_isVeoPlayerPlaying.value && _veoPlayerTimeMs.value < 20000L) {
                delay(50L)
                val newTime = _veoPlayerTimeMs.value + 50L
                _veoPlayerTimeMs.value = minOf(20000L, newTime)

                // Check scene boundaries for TTS voiceover
                val currentSceneIndex = (newTime / 5000L).toInt().coerceIn(0, 3)
                if (_isVeoPlayerTtsEnabled.value && currentSceneIndex != lastSpokenSceneIndex) {
                    lastSpokenSceneIndex = currentSceneIndex
                    val scene = _veoProject.value?.scenes?.getOrNull(currentSceneIndex)
                    if (scene != null && scene.voiceover.isNotBlank()) {
                        ttsHelper.speak(scene.voiceover, isBengali = _veoLanguage.value.contains("Bengali") || _veoLanguage.value.contains("বাংলা"))
                    }
                }
            }

            if (_veoPlayerTimeMs.value >= 20000L) {
                _isVeoPlayerPlaying.value = false
                ttsHelper.stop()
            }
        }
    }

    fun pauseVeoVideo() {
        _isVeoPlayerPlaying.value = false
        playerJob?.cancel()
        playerJob = null
        ttsHelper.stop()
    }

    fun seekVeoVideo(timeMs: Long) {
        val clamped = timeMs.coerceIn(0L, 20000L)
        _veoPlayerTimeMs.value = clamped
        lastSpokenSceneIndex = (clamped / 5000L).toInt().coerceIn(0, 3)
        if (_isVeoPlayerPlaying.value) {
            pauseVeoVideo()
            playVeoVideo()
        }
    }

    fun resetVeoVideo() {
        pauseVeoVideo()
        _veoPlayerTimeMs.value = 0L
        lastSpokenSceneIndex = -1
    }

    fun toggleVeoPlayerTts() {
        val next = !_isVeoPlayerTtsEnabled.value
        _isVeoPlayerTtsEnabled.value = next
        if (!next) {
            ttsHelper.stop()
        } else if (_isVeoPlayerPlaying.value) {
            val currentSceneIndex = (_veoPlayerTimeMs.value / 5000L).toInt().coerceIn(0, 3)
            val scene = _veoProject.value?.scenes?.getOrNull(currentSceneIndex)
            if (scene != null && scene.voiceover.isNotBlank()) {
                ttsHelper.speak(scene.voiceover, isBengali = _veoLanguage.value.contains("Bengali") || _veoLanguage.value.contains("বাংলা"))
            }
        }
    }

    fun toggleVeoPlayerAspect() {
        _isVeoPlayerShortsMode.value = !_isVeoPlayerShortsMode.value
    }

    // =========================================================================
    // --- High-Quality AI Photo Generator from Prompts ---
    // =========================================================================
    private val _photoPrompt = MutableStateFlow("ফিউচারিস্টিক সাইবারপাঙ্ক শহরের আকাশে নিয়ন ড্রোন ও উড়ন্ত গাড়ি")
    val photoPrompt: StateFlow<String> = _photoPrompt.asStateFlow()

    private val _photoStyle = MutableStateFlow("Photorealistic 8K Cinema")
    val photoStyle: StateFlow<String> = _photoStyle.asStateFlow()

    private val _photoAspectRatio = MutableStateFlow("1:1 (Square)")
    val photoAspectRatio: StateFlow<String> = _photoAspectRatio.asStateFlow()

    private val _isPhotoLoading = MutableStateFlow(false)
    val isPhotoLoading: StateFlow<Boolean> = _isPhotoLoading.asStateFlow()

    private val _photoResult = MutableStateFlow<String?>(null)
    val photoResult: StateFlow<String?> = _photoResult.asStateFlow()

    private val _photoError = MutableStateFlow<String?>(null)
    val photoError: StateFlow<String?> = _photoError.asStateFlow()

    private val _isPhotoSaved = MutableStateFlow(false)
    val isPhotoSaved: StateFlow<Boolean> = _isPhotoSaved.asStateFlow()

    fun setPhotoPrompt(text: String) { _photoPrompt.value = text }
    fun setPhotoStyle(style: String) { _photoStyle.value = style }
    fun setPhotoAspectRatio(ratio: String) { _photoAspectRatio.value = ratio }

    fun generatePhotoPrompt() {
        val idea = _photoPrompt.value.trim()
        if (idea.isBlank() || _isPhotoLoading.value) return

        _isPhotoLoading.value = true
        _photoError.value = null
        _photoResult.value = null
        _isPhotoSaved.value = false

        viewModelScope.launch {
            val result = repository.generateHighQualityPhotoPrompt(
                idea = idea,
                style = _photoStyle.value,
                aspectRatio = _photoAspectRatio.value
            )
            _isPhotoLoading.value = false
            result.onSuccess { output ->
                _photoResult.value = output
            }.onFailure { err ->
                _photoError.value = err.message ?: "Failed to generate photo prompt"
            }
        }
    }

    fun savePhotoResult() {
        val output = _photoResult.value ?: return
        viewModelScope.launch {
            repository.saveInsight(
                type = "AI_PHOTO",
                title = "AI Photo: ${_photoPrompt.value.take(28)}",
                prompt = _photoPrompt.value,
                content = output,
                modelUsed = "gemini-3.5-flash",
                durationOrMeta = "${_photoStyle.value} • ${_photoAspectRatio.value}"
            )
            _isPhotoSaved.value = true
        }
    }

    // =========================================================================
    // --- In-App Photo & Video Editor ---
    // =========================================================================
    private val _selectedEditImageRes = MutableStateFlow(R.drawable.img_veo_scene_1)
    val selectedEditImageRes: StateFlow<Int> = _selectedEditImageRes.asStateFlow()

    // Photo Adjustments
    private val _photoBrightness = MutableStateFlow(0f) // -50..50
    val photoBrightness: StateFlow<Float> = _photoBrightness.asStateFlow()

    private val _photoContrast = MutableStateFlow(1f) // 0.5..2.0
    val photoContrast: StateFlow<Float> = _photoContrast.asStateFlow()

    private val _photoSaturation = MutableStateFlow(1f) // 0.0..2.0
    val photoSaturation: StateFlow<Float> = _photoSaturation.asStateFlow()

    private val _photoWarmth = MutableStateFlow(0f) // -50..50
    val photoWarmth: StateFlow<Float> = _photoWarmth.asStateFlow()

    private val _photoActiveFilter = MutableStateFlow("NORMAL")
    val photoActiveFilter: StateFlow<String> = _photoActiveFilter.asStateFlow()

    private val _photoCropRatio = MutableStateFlow("1:1")
    val photoCropRatio: StateFlow<String> = _photoCropRatio.asStateFlow()

    private val _photoOverlayText = MutableStateFlow("Sharif AI Studio")
    val photoOverlayText: StateFlow<String> = _photoOverlayText.asStateFlow()

    private val _photoTextColor = MutableStateFlow(Color.White)
    val photoTextColor: StateFlow<Color> = _photoTextColor.asStateFlow()

    private val _photoTextPosition = MutableStateFlow("BOTTOM")
    val photoTextPosition: StateFlow<String> = _photoTextPosition.asStateFlow()

    private val _photoTextSize = MutableStateFlow(18f)
    val photoTextSize: StateFlow<Float> = _photoTextSize.asStateFlow()

    private val _isPhotoEditSaved = MutableStateFlow(false)
    val isPhotoEditSaved: StateFlow<Boolean> = _isPhotoEditSaved.asStateFlow()

    fun selectEditImage(resId: Int) {
        _selectedEditImageRes.value = resId
        _isPhotoEditSaved.value = false
    }

    fun setPhotoBrightness(b: Float) { _photoBrightness.value = b }
    fun setPhotoContrast(c: Float) { _photoContrast.value = c }
    fun setPhotoSaturation(s: Float) { _photoSaturation.value = s }
    fun setPhotoWarmth(w: Float) { _photoWarmth.value = w }
    fun setPhotoCropRatio(ratio: String) { _photoCropRatio.value = ratio }
    fun setPhotoOverlayText(text: String) { _photoOverlayText.value = text }
    fun setPhotoTextColor(color: Color) { _photoTextColor.value = color }
    fun setPhotoTextPosition(pos: String) { _photoTextPosition.value = pos }
    fun setPhotoTextSize(size: Float) { _photoTextSize.value = size }

    fun applyPhotoFilter(filterName: String) {
        _photoActiveFilter.value = filterName
        when (filterName) {
            "CYBERPUNK" -> {
                _photoBrightness.value = 10f
                _photoContrast.value = 1.35f
                _photoSaturation.value = 1.6f
                _photoWarmth.value = -15f
            }
            "VINTAGE" -> {
                _photoBrightness.value = -5f
                _photoContrast.value = 0.9f
                _photoSaturation.value = 0.75f
                _photoWarmth.value = 30f
            }
            "BW_NOIR" -> {
                _photoBrightness.value = 5f
                _photoContrast.value = 1.4f
                _photoSaturation.value = 0.0f
                _photoWarmth.value = 0f
            }
            "GOLDEN_HOUR" -> {
                _photoBrightness.value = 8f
                _photoContrast.value = 1.15f
                _photoSaturation.value = 1.3f
                _photoWarmth.value = 40f
            }
            "VIBRANT" -> {
                _photoBrightness.value = 5f
                _photoContrast.value = 1.25f
                _photoSaturation.value = 1.7f
                _photoWarmth.value = 5f
            }
            else -> { // NORMAL
                _photoBrightness.value = 0f
                _photoContrast.value = 1f
                _photoSaturation.value = 1f
                _photoWarmth.value = 0f
            }
        }
    }

    fun resetPhotoAdjustments() {
        applyPhotoFilter("NORMAL")
        _photoOverlayText.value = ""
        _isPhotoEditSaved.value = false
    }

    fun saveEditedPhoto() {
        viewModelScope.launch {
            repository.saveInsight(
                type = "EDITED_PHOTO",
                title = "Edited Photo (${_photoActiveFilter.value})",
                prompt = "Brightness: ${_photoBrightness.value.toInt()}, Contrast: ${_photoContrast.value}, Saturation: ${_photoSaturation.value}",
                content = "Filter: ${_photoActiveFilter.value}\nCrop Ratio: ${_photoCropRatio.value}\nOverlay Text: ${_photoOverlayText.value}\nText Color: ${_photoTextColor.value}\nAdjustments: Brightness ${_photoBrightness.value}, Contrast ${_photoContrast.value}, Saturation ${_photoSaturation.value}, Warmth ${_photoWarmth.value}",
                modelUsed = "In-App Photo Studio Engine",
                durationOrMeta = "${_photoActiveFilter.value} • ${_photoCropRatio.value}"
            )
            _isPhotoEditSaved.value = true
        }
    }

    // --- Video Editor States & Actions ---
    private val _videoTrimDuration = MutableStateFlow("20s")
    val videoTrimDuration: StateFlow<String> = _videoTrimDuration.asStateFlow()

    private val _videoSpeed = MutableStateFlow(1.0f)
    val videoSpeed: StateFlow<Float> = _videoSpeed.asStateFlow()

    private val _videoLutFilter = MutableStateFlow("TEAL_ORANGE")
    val videoLutFilter: StateFlow<String> = _videoLutFilter.asStateFlow()

    private val _videoSubtitleText = MutableStateFlow("আমার প্রথম ২০ সেকেন্ড এআই সিনেমাটিক ভিডিও")
    val videoSubtitleText: StateFlow<String> = _videoSubtitleText.asStateFlow()

    private val _videoIsSubtitlesEnabled = MutableStateFlow(true)
    val videoIsSubtitlesEnabled: StateFlow<Boolean> = _videoIsSubtitlesEnabled.asStateFlow()

    private val _videoMusicVolume = MutableStateFlow(80)
    val videoMusicVolume: StateFlow<Int> = _videoMusicVolume.asStateFlow()

    private val _videoMusicMood = MutableStateFlow("Cyberpunk Synthwave")
    val videoMusicMood: StateFlow<String> = _videoMusicMood.asStateFlow()

    private val _isVideoEditSaved = MutableStateFlow(false)
    val isVideoEditSaved: StateFlow<Boolean> = _isVideoEditSaved.asStateFlow()

    fun setVideoTrimDuration(d: String) { _videoTrimDuration.value = d }
    fun setVideoSpeed(speed: Float) { _videoSpeed.value = speed }
    fun setVideoLutFilter(lut: String) { _videoLutFilter.value = lut }
    fun setVideoSubtitleText(text: String) { _videoSubtitleText.value = text }
    fun toggleVideoSubtitles() { _videoIsSubtitlesEnabled.value = !_videoIsSubtitlesEnabled.value }
    fun setVideoMusicVolume(vol: Int) { _videoMusicVolume.value = vol }
    fun setVideoMusicMood(mood: String) { _videoMusicMood.value = mood }

    fun saveEditedVideoProject() {
        viewModelScope.launch {
            repository.saveInsight(
                type = "EDITED_VIDEO",
                title = "Edited Video: ${_videoTrimDuration.value} (${_videoLutFilter.value})",
                prompt = "Duration: ${_videoTrimDuration.value}, Speed: ${_videoSpeed.value}x, Music: ${_videoMusicMood.value}",
                content = "Color Grading LUT: ${_videoLutFilter.value}\nDuration: ${_videoTrimDuration.value}\nSpeed: ${_videoSpeed.value}x\nSubtitles: ${_videoSubtitleText.value} (Enabled: ${_videoIsSubtitlesEnabled.value})\nBGM Mood: ${_videoMusicMood.value} (Volume: ${_videoMusicVolume.value}%)\nReady for 4K rendering & export!",
                modelUsed = "In-App Video Studio Engine",
                durationOrMeta = "${_videoTrimDuration.value} • ${_videoSpeed.value}x Speed"
            )
            _isVideoEditSaved.value = true
        }
    }

    // =========================================================================
    // --- 100% Free Domain & Web Hosting Studio ---
    // =========================================================================
    private val _hostingSiteType = MutableStateFlow("AI Video & Photo Portfolio")
    val hostingSiteType: StateFlow<String> = _hostingSiteType.asStateFlow()

    private val _hostingCreatorName = MutableStateFlow("Sharif AI Tech")
    val hostingCreatorName: StateFlow<String> = _hostingCreatorName.asStateFlow()

    private val _hostingFeatures = MutableStateFlow("Showcase 20s AI videos, 8K photo gallery, contact button, free tools")
    val hostingFeatures: StateFlow<String> = _hostingFeatures.asStateFlow()

    private val _isHostingCodeLoading = MutableStateFlow(false)
    val isHostingCodeLoading: StateFlow<Boolean> = _isHostingCodeLoading.asStateFlow()

    private val _hostingGeneratedCode = MutableStateFlow<String?>(null)
    val hostingGeneratedCode: StateFlow<String?> = _hostingGeneratedCode.asStateFlow()

    private val _hostingError = MutableStateFlow<String?>(null)
    val hostingError: StateFlow<String?> = _hostingError.asStateFlow()

    private val _isHostingSaved = MutableStateFlow(false)
    val isHostingSaved: StateFlow<Boolean> = _isHostingSaved.asStateFlow()

    fun setHostingSiteType(type: String) { _hostingSiteType.value = type }
    fun setHostingCreatorName(name: String) { _hostingCreatorName.value = name }
    fun setHostingFeatures(features: String) { _hostingFeatures.value = features }

    fun generateWebsiteCode() {
        val siteType = _hostingSiteType.value.trim()
        val creator = _hostingCreatorName.value.trim()
        if (siteType.isBlank() || _isHostingCodeLoading.value) return

        _isHostingCodeLoading.value = true
        _hostingError.value = null
        _hostingGeneratedCode.value = null
        _isHostingSaved.value = false

        viewModelScope.launch {
            val result = repository.generateFreeWebsiteCode(
                siteType = siteType,
                creatorName = creator,
                features = _hostingFeatures.value
            )
            _isHostingCodeLoading.value = false
            result.onSuccess { code ->
                _hostingGeneratedCode.value = code
            }.onFailure { err ->
                _hostingError.value = err.message ?: "Failed to generate website code"
            }
        }
    }

    fun saveWebsiteCode() {
        val code = _hostingGeneratedCode.value ?: return
        viewModelScope.launch {
            repository.saveInsight(
                type = "FREE_HOSTING_SITE",
                title = "Website: ${_hostingCreatorName.value} (${_hostingSiteType.value})",
                prompt = "${_hostingSiteType.value} by ${_hostingCreatorName.value}",
                content = code,
                modelUsed = "gemini-3.5-flash",
                durationOrMeta = "HTML5 + Tailwind CSS • Free Hosting Ready"
            )
            _isHostingSaved.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerJob?.cancel()
        ttsHelper.release()
    }
}
