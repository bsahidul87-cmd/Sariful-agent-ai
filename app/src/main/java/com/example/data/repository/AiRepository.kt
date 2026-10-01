package com.example.data.repository

import android.util.Base64
import com.example.data.api.Content
import com.example.data.api.GenerateContentRequest
import com.example.data.api.GenerationConfig
import com.example.data.api.InlineData
import com.example.data.api.Part
import com.example.data.api.RetrofitClient
import com.example.data.api.ThinkingConfig
import com.example.data.api.ThinkingLevel
import com.example.data.api.Tool
import com.example.data.db.InsightDao
import com.example.data.db.SavedInsight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

data class AiChatResponse(
    val replyText: String,
    val webSources: List<String> = emptyList()
)

class AiRepository(private val insightDao: InsightDao) {

    val allInsights: Flow<List<SavedInsight>> = insightDao.getAllInsights()

    fun getInsightsByType(type: String): Flow<List<SavedInsight>> =
        insightDao.getInsightsByType(type)

    fun searchInsights(query: String): Flow<List<SavedInsight>> =
        insightDao.searchInsights(query)

    suspend fun saveInsight(
        type: String,
        title: String,
        prompt: String,
        content: String,
        modelUsed: String,
        durationOrMeta: String? = null
    ): Long = withContext(Dispatchers.IO) {
        val insight = SavedInsight(
            type = type,
            title = title,
            prompt = prompt,
            content = content,
            modelUsed = modelUsed,
            durationOrMeta = durationOrMeta
        )
        insightDao.insertInsight(insight)
    }

    suspend fun deleteInsight(id: Long) = withContext(Dispatchers.IO) {
        insightDao.deleteInsightById(id)
    }

    suspend fun toggleFavorite(id: Long) = withContext(Dispatchers.IO) {
        insightDao.toggleFavorite(id)
    }

    /**
     * Conversational Bengali AI Chat with optional real-time Google Web Search grounding.
     */
    suspend fun chatWithBengaliAi(
        conversationHistory: List<Content>,
        enableWebSearch: Boolean = false
    ): Result<AiChatResponse> = withContext(Dispatchers.IO) {
        val apiKey = RetrofitClient.apiKey
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured. Please add your key in the AI Studio Secrets panel.")
            )
        }

        try {
            val systemInstruction = Content(
                parts = listOf(
                    Part(
                        text = "You are 'Sariful AI Agent', a friendly, brilliant, and beginner-friendly AI assistant created for the Sharif AI Tech community. " +
                                "You excel at communicating fluently in Bengali (বাংলা) and English. " +
                                "Explain things simply, clearly, and politely. Use emojis, numbered steps, and bullet points where helpful. " +
                                "If the user asks in Bengali, reply primarily in fluent Bengali. If in English, reply in English. " +
                                "Always provide complete, accurate, and insightful answers."
                    )
                )
            )

            val tools = if (enableWebSearch) listOf(Tool(googleSearch = emptyMap())) else null

            val request = GenerateContentRequest(
                contents = conversationHistory,
                systemInstruction = systemInstruction,
                tools = tools,
                generationConfig = GenerationConfig(temperature = 0.7f)
            )

            val response = RetrofitClient.apiService.generateContent(
                model = "gemini-3.5-flash",
                apiKey = apiKey,
                request = request
            )

            if (response.error != null) {
                return@withContext Result.failure(
                    Exception("API Error (${response.error.code}): ${response.error.message}")
                )
            }

            val candidate = response.candidates?.firstOrNull()
            val text = candidate
                ?.content
                ?.parts
                ?.joinToString("\n\n") { it.text.orEmpty() }
                ?.trim()

            val sources = mutableListOf<String>()
            candidate?.groundingMetadata?.groundingChunks?.forEach { chunk ->
                val web = chunk.web
                if (web?.title != null && web.uri != null) {
                    sources.add("${web.title}: ${web.uri}")
                }
            }

            if (!text.isNullOrEmpty()) {
                Result.success(AiChatResponse(replyText = text, webSources = sources))
            } else {
                Result.failure(Exception("No reply received from Sariful AI Agent."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Generates a viral, engaging YouTube Shorts script with Hooks, Visuals, and Voiceover.
     */
    suspend fun generateShortsScript(
        topic: String,
        tone: String,
        duration: String,
        language: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = RetrofitClient.apiKey
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured. Please add your key in the AI Studio Secrets panel.")
            )
        }

        try {
            val prompt = """
                You are an elite YouTube Shorts & Reels viral creator for Sharif AI Tech.
                Create a high-retention, engaging YouTube Shorts script on the topic: "$topic".
                
                Parameters:
                - Tone: $tone
                - Target Duration: $duration
                - Language: $language (If Bengali, write natural modern spoken Bengali/Banglish cues).
                
                Format the output strictly into clear sections:
                1. 🎬 Catchy Title & SEO Description
                2. 🪝 The 3-Second Visual & Audio Hook (Crucial for retention)
                3. 📜 Scene-by-Scene Script Breakdown:
                   - Time range (e.g. 00:00 - 00:05)
                   - [Visual / Camera Direction]
                   - [On-Screen Text Overlay]
                   - [Voiceover Audio Script]
                4. 🔔 Powerful Call To Action (CTA) (e.g., Like & Subscribe to Sharif AI Tech)
                5. 🏷️ Top 10 High-Volume Hashtags & Viral Tags
                
                Make it captivating, snappy, and tailored for mobile viewers!
            """.trimIndent()

            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt)))),
                generationConfig = GenerationConfig(temperature = 0.75f)
            )

            val response = RetrofitClient.apiService.generateContent(
                model = "gemini-3.5-flash",
                apiKey = apiKey,
                request = request
            )

            val text = response.candidates?.firstOrNull()?.content?.parts?.joinToString("\n\n") { it.text.orEmpty() }?.trim()
            if (!text.isNullOrEmpty()) Result.success(text) else Result.failure(Exception("Failed to generate script."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Generates scene-by-scene video prompts optimized for AI video generators (Veo, Runway, Sora, Midjourney).
     */
    suspend fun generateSceneByScenePrompts(
        storyline: String,
        aiEngine: String,
        sceneCount: Int
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = RetrofitClient.apiKey
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured. Please add your key in the AI Studio Secrets panel.")
            )
        }

        try {
            val prompt = """
                You are a master cinematic AI video director.
                Create a professional, production-grade Scene-by-Scene Video Prompt guide for:
                Storyline/Theme: "$storyline"
                Target AI Generator: $aiEngine
                Number of Scenes: $sceneCount
                
                For EACH scene, provide:
                - Scene Number & Duration (e.g. Scene 1 | 4s)
                - Scene Overview & Action
                - 📋 Copy-Ready Prompt for $aiEngine:
                  (Include hyper-detailed visual descriptions: cinematic lighting, camera movements like drone pan / slow push-in, depth of field, color grading, photorealistic textures, 8k resolution keywords, and aspect ratio --ar 9:16 or --ar 16:9)
                - Negative Prompt / Avoid keywords
                
                End with a 'Full Story Flow' summary.
            """.trimIndent()

            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt)))),
                generationConfig = GenerationConfig(temperature = 0.7f)
            )

            val response = RetrofitClient.apiService.generateContent(
                model = "gemini-3.5-flash",
                apiKey = apiKey,
                request = request
            )

            val text = response.candidates?.firstOrNull()?.content?.parts?.joinToString("\n\n") { it.text.orEmpty() }?.trim()
            if (!text.isNullOrEmpty()) Result.success(text) else Result.failure(Exception("Failed to generate scene prompts."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Generates a complete 20-Second Google Veo 3 AI Video production project.
     * Includes a Master 20s prompt, 4 chained 5-second scenes (0-5s, 5-10s, 10-15s, 15-20s),
     * camera choreography, Bengali & English voiceover, sound FX, and free generation workflow.
     */
    suspend fun generateVeo20SecondVideoProject(
        topic: String,
        cameraMotion: String,
        visualStyle: String,
        aspectRatio: String = "9:16 (Shorts/Reels)",
        language: String = "Bengali (বাংলা)"
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = RetrofitClient.apiKey
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured. Please add your key in the AI Studio Secrets panel.")
            )
        }

        try {
            val prompt = """
                You are Google DeepMind's master cinema director and prompting genius for Google Veo 3 / VideoFX.
                The user wants to generate a stunning, cinematic, high-retention 20-SECOND AI VIDEO completely for free!
                
                Topic / Storyline: "$topic"
                Target Duration: Exactly 20 Seconds (00:00 to 00:20)
                Camera Motion Style: $cameraMotion
                Visual Aesthetic: $visualStyle
                Aspect Ratio: $aspectRatio
                Narration / Voiceover Language: $language
                
                Create a comprehensive production blueprint formatted with clear markdown sections:
                
                ## 🎬 Master Veo 3 Prompt (Single 20-Second Continuous Take)
                Provide the full hyper-detailed prompt for Google Veo 3 / VideoFX:
                [Include photorealistic lighting, cinematic camera lenses, depth of field, 8K render keywords, motion speed, volumetric shadows, color palette, and aspect ratio tags]
                
                ## 🚫 Negative Prompt
                Provide the exact negative prompt to prevent artifacts:
                [e.g., --no morphing, distortion, extra limbs, low resolution, flickering, cartoonish, warped text]
                
                ## 🎞️ 20-Second 4-Scene Breakdown (Chained 5s Shots for 100% Free AI Video Generators)
                Break down the 20 seconds into 4 seamless 5-second chapters that can be generated individually on free AI tools and stitched together:
                
                ### Scene 1: The Visual Hook (00:00 - 00:05)
                - Title: [Punchy Scene 1 Title]
                - Camera: [Exact camera motion, lens mm, and angle]
                - Prompt: [Complete standalone copy-ready prompt for 0-5s]
                - Voiceover: [Natural spoken Bengali/English dialogue for seconds 0 to 5]
                - SFX: [Specific sound effects, e.g. bass drop, swoosh, ambient tone]
                
                ### Scene 2: Subject Exploration & Movement (00:05 - 00:10)
                - Title: [Scene 2 Title]
                - Camera: [Camera transition, pan/tracking motion]
                - Prompt: [Complete standalone copy-ready prompt for 5-10s]
                - Voiceover: [Voiceover dialogue for seconds 5 to 10]
                - SFX: [Mechanical hum, environmental acoustics, rhythm shift]
                
                ### Scene 3: Peak Emotion & Cinematic Climax (00:10 - 00:15)
                - Title: [Scene 3 Title]
                - Camera: [Dynamic camera acceleration, Dutch angle, or macro push]
                - Prompt: [Complete standalone copy-ready prompt for 10-15s]
                - Voiceover: [High-impact emotional voiceover for seconds 10 to 15]
                - SFX: [Climactic riser, dramatic synth swell, impact hit]
                
                ### Scene 4: Resolution & Outro Call to Action (00:15 - 00:20)
                - Title: [Scene 4 Title]
                - Camera: [Slow crane pull-back, aerial fade, or dramatic freeze]
                - Prompt: [Complete standalone copy-ready prompt for 15-20s]
                - Voiceover: [Memorable closing sentence + call to action]
                - SFX: [Harmonic resolution, melodic outro chime]
                
                ## 🎙️ Full 20-Second Continuous Narration Track
                [Complete contiguous voiceover script ready to read or record in exactly 20 seconds]
                
                ## 💡 ১০০% ফ্রিতে ২০ সেকেন্ডের ভিডিও তৈরি করার গাইড (Free Veo 3 Creation Recipe)
                [Clear, encouraging 4-step Bengali instructions explaining how to paste these 4 scene prompts into Google VideoFX / YouTube Dream Screen / Kling AI / Luma Dream Machine completely free and stitch them in CapCut or InShot without spending any money!]
            """.trimIndent()

            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt)))),
                generationConfig = GenerationConfig(temperature = 0.75f)
            )

            val response = RetrofitClient.apiService.generateContent(
                model = "gemini-3.5-flash",
                apiKey = apiKey,
                request = request
            )

            val text = response.candidates?.firstOrNull()?.content?.parts?.joinToString("\n\n") { it.text.orEmpty() }?.trim()
            if (!text.isNullOrEmpty()) Result.success(text) else Result.failure(Exception("Failed to generate 20s Veo 3 project."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Generates high-click-through-rate YouTube & Shorts thumbnail prompts and design concepts.
     */
    suspend fun generateThumbnailPrompt(
        videoTitle: String,
        style: String,
        targetAudience: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = RetrofitClient.apiKey
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured. Please add your key in the AI Studio Secrets panel.")
            )
        }

        try {
            val prompt = """
                You are a world-class YouTube thumbnail designer and behavioral psychology expert.
                Design high-CTR thumbnail prompts and concepts for:
                Video Title: "$videoTitle"
                Visual Style: $style
                Target Audience: $targetAudience
                
                Provide 3 distinct thumbnail variations:
                
                VARIATION 1: High Emotion & Curiosity Hook
                VARIATION 2: Dramatic High-Tech / Futuristic Contrast
                VARIATION 3: Storytelling / Before & After Split
                
                For each variation:
                - 🎨 Master AI Image Prompt (formatted for Midjourney v6 / DALL-E 3 / Imagen 3 with lighting, colors, camera framing)
                - 🔤 Bold On-Screen Text Overlay (Maximum 2 to 4 punchy words, high contrast color scheme)
                - 👤 Main Subject & Expression (e.g., shocked facial expression, glowing neon tech device)
                - 🌈 Dominant Color Palette (e.g. Electric Cyan #06B6D4, Neon Amber #F59E0B against deep navy blue)
                - 📈 Why It Clicks (Psychological hook)
            """.trimIndent()

            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt)))),
                generationConfig = GenerationConfig(temperature = 0.8f)
            )

            val response = RetrofitClient.apiService.generateContent(
                model = "gemini-3.5-flash",
                apiKey = apiKey,
                request = request
            )

            val text = response.candidates?.firstOrNull()?.content?.parts?.joinToString("\n\n") { it.text.orEmpty() }?.trim()
            if (!text.isNullOrEmpty()) Result.success(text) else Result.failure(Exception("Failed to generate thumbnail prompt."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * High Thinking query with gemini-3.1-pro-preview and thinkingLevel = HIGH.
     */
    suspend fun executeHighThinkingQuery(prompt: String): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = RetrofitClient.apiKey
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured. Please add your key in the AI Studio Secrets panel.")
            )
        }

        try {
            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt)))),
                generationConfig = GenerationConfig(
                    thinkingConfig = ThinkingConfig(thinkingLevel = ThinkingLevel.HIGH.value)
                )
            )

            val response = RetrofitClient.apiService.generateContent(
                model = "gemini-3.1-pro-preview",
                apiKey = apiKey,
                request = request
            )

            val text = response.candidates?.firstOrNull()?.content?.parts?.joinToString("\n\n") { it.text.orEmpty() }?.trim()
            if (!text.isNullOrEmpty()) Result.success(text) else Result.failure(Exception("No response generated."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Transcribes audio using model gemini-3.5-transcribe.
     */
    suspend fun transcribeAudio(
        audioBytes: ByteArray,
        mimeType: String = "audio/mp4",
        promptText: String? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = RetrofitClient.apiKey
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured. Please add your key in the AI Studio Secrets panel.")
            )
        }

        try {
            val base64Audio = Base64.encodeToString(audioBytes, Base64.NO_WRAP)
            val instruction = promptText ?: "Please provide an accurate verbatim transcription of this speech recording."

            val request = GenerateContentRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(inlineData = InlineData(mimeType = mimeType, data = base64Audio)),
                            Part(text = instruction)
                        )
                    )
                )
            )

            val response = RetrofitClient.apiService.generateContent(
                model = "gemini-3.5-transcribe",
                apiKey = apiKey,
                request = request
            )

            val text = response.candidates?.firstOrNull()?.content?.parts?.joinToString("\n\n") { it.text.orEmpty() }?.trim()
            if (!text.isNullOrEmpty()) Result.success(text) else Result.failure(Exception("No transcript returned."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Video analysis with gemini-3.1-pro-preview.
     */
    suspend fun analyzeVideo(
        videoBytes: ByteArray,
        mimeType: String = "video/mp4",
        analysisPrompt: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = RetrofitClient.apiKey
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured. Please add your key in the AI Studio Secrets panel.")
            )
        }

        try {
            val base64Video = Base64.encodeToString(videoBytes, Base64.NO_WRAP)
            val request = GenerateContentRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(inlineData = InlineData(mimeType = mimeType, data = base64Video)),
                            Part(text = analysisPrompt)
                        )
                    )
                )
            )

            val response = RetrofitClient.apiService.generateContent(
                model = "gemini-3.1-pro-preview",
                apiKey = apiKey,
                request = request
            )

            val text = response.candidates?.firstOrNull()?.content?.parts?.joinToString("\n\n") { it.text.orEmpty() }?.trim()
            if (!text.isNullOrEmpty()) Result.success(text) else Result.failure(Exception("No video analysis generated."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Generates a high-quality AI photo prompt blueprint with lighting, camera lenses, colors,
     * negative prompts, and visual descriptors for Imagen 3, Midjourney, and DALL-E.
     */
    suspend fun generateHighQualityPhotoPrompt(
        idea: String,
        style: String,
        aspectRatio: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = RetrofitClient.apiKey
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured. Please add your key in the AI Studio Secrets panel.")
            )
        }

        try {
            val prompt = """
                You are a world-class AI visual art director and prompt engineer.
                Generate a master prompt for an ultra high-quality, photorealistic or stylized image.
                
                Image Concept: "$idea"
                Style: $style
                Aspect Ratio: $aspectRatio
                
                Format the response into clean sections:
                1. 🎨 Master AI Image Prompt:
                   (Include ultra-detailed visual descriptors, 8k resolution keywords, camera lens e.g. 85mm f/1.4, cinematic volumetric lighting, raytracing reflections, photorealistic textures)
                2. 🚫 Negative Prompt:
                   (Avoid words, e.g. --no blurry, oversaturated, deformed hands, extra fingers, cartoonish, lowres)
                3. 💡 Lighting & Color Palette:
                   (Specify exact color accents, hex highlights, and lighting directions)
                4. 📸 Composition & Framing:
                   (Rule of thirds, depth of field, leading lines, bokeh)
            """.trimIndent()

            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt)))),
                generationConfig = GenerationConfig(temperature = 0.75f)
            )

            val response = RetrofitClient.apiService.generateContent(
                model = "gemini-3.5-flash",
                apiKey = apiKey,
                request = request
            )

            val text = response.candidates?.firstOrNull()?.content?.parts?.joinToString("\n\n") { it.text.orEmpty() }?.trim()
            if (!text.isNullOrEmpty()) Result.success(text) else Result.failure(Exception("Failed to generate photo prompt."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Generates clean, production-ready, responsive single-page HTML5 + Tailwind CSS portfolio code
     * to host AI photos and videos completely for free on Vercel, Firebase, GitHub Pages, or Netlify.
     */
    suspend fun generateFreeWebsiteCode(
        siteType: String,
        creatorName: String,
        features: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = RetrofitClient.apiKey
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured. Please add your key in the AI Studio Secrets panel.")
            )
        }

        try {
            val prompt = """
                You are a senior full-stack web developer.
                Generate a complete, modern, gorgeous, mobile-responsive single-page website ready to deploy on FREE hosting (Vercel, GitHub Pages, Firebase Hosting, Netlify).
                
                Site Category: "$siteType"
                Creator / Brand: "$creatorName"
                Key Highlights: "$features"
                
                Requirements:
                - Output complete valid HTML5 code with Tailwind CSS via CDN (<script src="https://cdn.tailwindcss.com"></script>)
                - Dark cyberpunk/sleek theme with glowing neon cyan and amber accents
                - Hero Section with glowing gradient typography, badge, and CTA buttons
                - AI Video Showcase Grid with video preview placeholders and play modals
                - AI Photo Gallery with hover zoom effects
                - Features & Free Tools Section
                - Contact / Social links & Footer
                - Return strictly the complete HTML document inside a markdown code block.
            """.trimIndent()

            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt)))),
                generationConfig = GenerationConfig(temperature = 0.7f)
            )

            val response = RetrofitClient.apiService.generateContent(
                model = "gemini-3.5-flash",
                apiKey = apiKey,
                request = request
            )

            val text = response.candidates?.firstOrNull()?.content?.parts?.joinToString("\n\n") { it.text.orEmpty() }?.trim()
            if (!text.isNullOrEmpty()) Result.success(text) else Result.failure(Exception("Failed to generate website code."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
