package com.example.util

import com.example.R

data class VeoSceneData(
    val sceneNumber: Int,
    val timeRange: String, // e.g. "00:00 - 00:05"
    val title: String,
    val visualPrompt: String,
    val cameraMovement: String,
    val voiceover: String,
    val sfx: String,
    val previewDrawableRes: Int
)

data class Veo20sProject(
    val title: String,
    val logline: String,
    val masterPrompt: String,
    val negativePrompt: String,
    val scenes: List<VeoSceneData>,
    val fullVoiceover: String,
    val freeWorkflowGuide: String,
    val rawMarkdown: String,
    val visualStyle: String = "Photorealistic 8K Cinema",
    val aspectRatio: String = "9:16 (Shorts/Reels)",
    val cameraMotion: String = "Cinematic Drone Sweep & Dolly Zoom"
)

object VeoVideoHelper {

    val sampleProjects: List<Veo20sProject> = listOf(
        Veo20sProject(
            title = "ফিউচারিস্টিক সাইবারপাঙ্ক ঢাকা ২০৫০ (Cyberpunk Dhaka 2050)",
            logline = "ভবিষ্যতের নিয়ন ঝলমলে ঢাকার আকাশে উড়ন্ত গাড়ি ও হাই-টেক মেট্রোরেলের ২০ সেকেন্ডের সিনেমাটিক সফর।",
            masterPrompt = "Master Google Veo 3 Cinematic 20s Shot: Ultra-photorealistic 8K drone sweeping across futuristic Neo-Dhaka in 2050 at golden hour twilight. Massive holographic billboards glowing in Bengali typography, sleek flying electric vehicles cruising through neon skyways, aerodynamic bullet trains hovering over the Buriganga cyber river, reflections of neon cyan and amber lights on polished chrome, cinematic anamorphic lens flares, 60fps --ar 9:16 --motion 6",
            negativePrompt = "--no cartoonish, blurry, morphing vehicles, low resolution, warped text, deformed geometry, stuttering frame rate",
            scenes = listOf(
                VeoSceneData(
                    sceneNumber = 1,
                    timeRange = "00:00 - 00:05",
                    title = "উদ্বোধনী হুক: নিয়ন স্কাইলাইন ড্রোন ফ্লাইট",
                    visualPrompt = "Wide cinematic drone swoop descending over futuristic illuminated Dhaka skyscrapers with massive 3D holograms of Sharif AI Tech and glowing cyber traffic --ar 9:16",
                    cameraMovement = "Cinematic FPV High-Altitude Forward Sweep & Tilt Down",
                    voiceover = "কল্পনা করুন ২০৫০ সালের আমাদের ঢাকা! যেখানে প্রযুক্তি আর স্বপ্নের মিলন...",
                    sfx = "🔊 Cinematic deep bass drop + high-tech electronic vehicle whoosh",
                    previewDrawableRes = R.drawable.img_veo_scene_1
                ),
                VeoSceneData(
                    sceneNumber = 2,
                    timeRange = "00:05 - 00:10",
                    title = "দৃশ্য ২: সাইবার মেট্রো ও রোবোটিক ট্রাফিক",
                    visualPrompt = "Close-up cinematic tracking shot alongside a magnetic levitation high-speed cyber train with transparent holographic windows glowing in electric cyan --ar 9:16",
                    cameraMovement = "Low-Angle Parallel Tracking Shot, 35mm f/1.4 shallow depth of field",
                    voiceover = "বুড়িগঙ্গার ওপর উড়ন্ত ম্যাগলেভ ট্রেন আর কৃত্রিম বুদ্ধিমত্তার তৈরি ট্রাফিক গ্রিড!",
                    sfx = "🔊 Magnetic levitation hum + futuristic metallic chime",
                    previewDrawableRes = R.drawable.img_veo_scene_2
                ),
                VeoSceneData(
                    sceneNumber = 3,
                    timeRange = "00:10 - 00:15",
                    title = "দৃশ্য ৩: কোয়ান্টাম লাইট পোর্টাল ও এআই সেন্টার",
                    visualPrompt = "Cinematic high-speed rush through a bustling neon street bazaar with hovering holographic drones and street food steam lit by neon violet lights --ar 9:16",
                    cameraMovement = "Dynamic Hyperlapse Push-Through with rapid zoom",
                    voiceover = "প্রতিটি কোণায় নতুন সম্ভাবনা, মানুষের হাতে সুপার পাওয়ারের মতো কৃত্রিম বুদ্ধিমত্তা!",
                    sfx = "🔊 Quantum energy pulsing + fast cybernetic acceleration",
                    previewDrawableRes = R.drawable.img_veo_scene_3
                ),
                VeoSceneData(
                    sceneNumber = 4,
                    timeRange = "00:15 - 00:20",
                    title = "দৃশ্য ৪: সমাপ্তি ও আউটরো সিগনেচার",
                    visualPrompt = "Epic wide sunset silhouette looking over the city as twin moons rise and golden fireworks light up the sky, Sharif AI Tech logo glowing softly --ar 9:16",
                    cameraMovement = "Slow Cinematic Dolly-Out and Crane Up into the twilight sky",
                    voiceover = "ভবিষ্যৎ এখন আর কোনো দূরবর্তী স্বপ্ন নয়। সাবস্ক্রাইব করুন পরবর্তী পর্বে দেখতে!",
                    sfx = "🔊 Uplifting cinematic orchestral crescendo + gentle fade out",
                    previewDrawableRes = R.drawable.img_veo_scene_4
                )
            ),
            fullVoiceover = "কল্পনা করুন ২০৫০ সালের আমাদের ঢাকা! যেখানে প্রযুক্তি আর স্বপ্নের মিলন... বুড়িগঙ্গার ওপর উড়ন্ত ম্যাগলেভ ট্রেন আর কৃত্রিম বুদ্ধিমত্তার তৈরি ট্রাফিক গ্রিড! প্রতিটি কোণায় নতুন সম্ভাবনা, মানুষের হাতে সুপার পাওয়ারের মতো কৃত্রিম বুদ্ধিমত্তা! ভবিষ্যৎ এখন আর কোনো দূরবর্তী স্বপ্ন নয়। সাবস্ক্রাইব করুন পরবর্তী পর্বে দেখতে!",
            freeWorkflowGuide = """
                ১. Google VideoFX (Labs.google) অথবা Kling AI তে যান (ফ্রি অ্যাকাউন্ট)।
                ২. উপরের Master Prompt অথবা ৪টি আলাদা ৫-সেকেন্ডের প্রম্পট কপি করুন।
                ৩. চারটি ক্লিপ ফ্রি ডাউনলোড করে CapCut বা InShot অ্যাপে পাশাপাশি রাখুন।
                ৪. সঙ্গে দেওয়া বাংলা ভয়েসওভার ও সাউন্ড ইফেক্ট কিউ যোগ করুন = সম্পূর্ণ ফ্রি ২০ সেকেন্ডের 4K ভাইরাল ভিডিও তৈরি!
            """.trimIndent(),
            rawMarkdown = ""
        ),
        Veo20sProject(
            title = "এআই রোবট ও মানুষের বন্ধুত্ব (AI Companion)",
            logline = "একটি একাকী রোবটের সাথে ছোট্ট শিশুর পাহাড়ি উপত্যকায় মন ছোঁয়া বন্ধুত্বের ২০ সেকেন্ডের ভিজ্যুয়াল গল্প।",
            masterPrompt = "Master Google Veo 3 Cinematic 20s Shot: Ultra-heartwarming cinematic sequence of a sleek white bioluminescent AI robot holding a glowing wild daisy with a smiling child in a sunlit Alpine meadow. Golden hour lens flare, soft mountain breeze fluttering wildflowers, photorealistic 8K, Pixar meets Denis Villeneuve realism, 60fps --ar 9:16",
            negativePrompt = "--no scary robot, dark horror, deformed fingers, robotic glitches, low quality, stuttering animation",
            scenes = listOf(
                VeoSceneData(
                    sceneNumber = 1,
                    timeRange = "00:00 - 00:05",
                    title = "উদ্বোধনী হুক: প্রথম দৃষ্টি ও ফুলের স্পর্শ",
                    visualPrompt = "Macro close-up of a high-tech robotic hand gently picking a glowing yellow dandelion in green grass, warm golden sunlight --ar 9:16",
                    cameraMovement = "Slow Macro Dolly Push-In with extreme bokeh",
                    voiceover = "যন্ত্র কখনো কি ভালোবাসার ভাষা বুঝতে পারে?",
                    sfx = "🔊 Soft acoustic piano chord + gentle morning wind",
                    previewDrawableRes = R.drawable.img_veo_scene_2
                ),
                VeoSceneData(
                    sceneNumber = 2,
                    timeRange = "00:05 - 00:10",
                    title = "দৃশ্য ২: শিশুর হাসিমুখ ও বন্ধুত্ব",
                    visualPrompt = "Medium shot of the friendly robot gently handing the dandelion to a joyful 6-year-old child in overalls --ar 9:16",
                    cameraMovement = "Eye-Level Smooth Steadicam Arc Shot 180 degrees",
                    voiceover = "যখন হৃদয়ের স্পর্শ পায়, তখন ধাতব শরীরও বন্ধু হয়ে ওঠে!",
                    sfx = "🔊 Uplifting warm strings + child cheerful laughter",
                    previewDrawableRes = R.drawable.img_veo_scene_1
                ),
                VeoSceneData(
                    sceneNumber = 3,
                    timeRange = "00:10 - 00:15",
                    title = "দৃশ্য ৩: পাহাড়ের উপত্যকায় একসঙ্গে দৌড়",
                    visualPrompt = "Wide cinematic shot of the robot and child running joyfully side-by-side along a lush mountain ridge at sunset --ar 9:16",
                    cameraMovement = "Dynamic Low-Angle Tracking Sweep alongside characters",
                    voiceover = "ভবিষ্যতের পৃথিবী এমন এক পৃথিবী, যেখানে প্রযুক্তির একমাত্র লক্ষ্য হবে মানুষের মুখে হাসি ফোটানো!",
                    sfx = "🔊 Emotional orchestral swell + mountain wind ambiance",
                    previewDrawableRes = R.drawable.img_veo_scene_3
                ),
                VeoSceneData(
                    sceneNumber = 4,
                    timeRange = "00:15 - 00:20",
                    title = "দৃশ্য ৪: সমাপ্তি ও শেষ ফ্রেম",
                    visualPrompt = "Sunset silhouette of the child sitting on the robot's shoulder looking at the vast twilight horizon with fireflies --ar 9:16",
                    cameraMovement = "Slow Crane Pull-Back revealing the epic mountain range",
                    voiceover = "বন্ধুত্ব কোনো কোডিং দিয়ে মাপা যায় না। ভালো লাগলে লাইক ও শেয়ার করতে ভুলবেন না!",
                    sfx = "🔊 Deep emotional piano finale + fading wind",
                    previewDrawableRes = R.drawable.img_veo_scene_4
                )
            ),
            fullVoiceover = "যন্ত্র কখনো কি ভালোবাসার ভাষা বুঝতে পারে? যখন হৃদয়ের স্পর্শ পায়, তখন ধাতব শরীরও বন্ধু হয়ে ওঠে! ভবিষ্যতের পৃথিবী এমন এক পৃথিবী, যেখানে প্রযুক্তির একমাত্র লক্ষ্য হবে মানুষের মুখে হাসি ফোটানো! বন্ধুত্ব কোনো কোডিং দিয়ে মাপা যায় না। ভালো লাগলে লাইক ও শেয়ার করতে ভুলবেন না!",
            freeWorkflowGuide = """
                ১. Google VideoFX (Labs.google) অথবা Runway Gen-3 এ Master Prompt টি দিন।
                ২. অথবা ৪টি সিন প্রম্পট আলাদা করে জেনারেট করে বিনামূল্যে ৫+৫+৫+৫ = ২০ সেকেন্ড করুন।
                ৩. মোবাইল এডিটর (CapCut/InShot) দিয়ে ২০ সেকেন্ডে রেন্ডার করুন!
            """.trimIndent(),
            rawMarkdown = ""
        )
    )

    fun parseVeoOutput(rawText: String, topic: String): Veo20sProject {
        // Fallback default scenes with the generated drawables
        val defaultDrawables = listOf(
            R.drawable.img_veo_scene_1,
            R.drawable.img_veo_scene_2,
            R.drawable.img_veo_scene_3,
            R.drawable.img_veo_scene_4
        )

        val timeRanges = listOf(
            "00:00 - 00:05",
            "00:05 - 00:10",
            "00:10 - 00:15",
            "00:15 - 00:20"
        )

        // Extract master prompt if exists
        var masterPrompt = ""
        val masterMatch = Regex("""(?i)(?:Master(?: Google)? Veo 3(?: Prompt)?|মাস্টার প্রম্পট)[:\s*]+([^\n]+(?:\n[^\n]+){1,4})""").find(rawText)
        if (masterMatch != null) {
            masterPrompt = masterMatch.groupValues[1].trim().replace("**", "")
        }
        if (masterPrompt.isBlank()) {
            masterPrompt = "Ultra-cinematic 8K video for '$topic', cinematic camera motion, realistic lighting, volumetric rays, 60fps --ar 9:16 --motion 6"
        }

        // Extract negative prompt if exists
        var negativePrompt = "--no low resolution, blurry, morphing, glitches, distorted limbs, flickering"
        val negMatch = Regex("""(?i)(?:Negative Prompt|নেগেটিভ প্রম্পট)[:\s*]+([^\n]+)""").find(rawText)
        if (negMatch != null) {
            negativePrompt = negMatch.groupValues[1].trim().replace("**", "")
        }

        // Try to parse 4 scenes
        val sceneBlocks = rawText.split(Regex("""(?i)(?:###\s*Scene|Scene\s*[1-4]|দৃশ্য\s*[১-৪])"""))
            .filter { it.isNotBlank() && it.length > 30 }

        val parsedScenes = mutableListOf<VeoSceneData>()
        val count = minOf(4, if (sceneBlocks.size >= 4) sceneBlocks.size else 4)

        for (i in 0 until count) {
            val block = sceneBlocks.getOrNull(i) ?: ""
            val time = timeRanges.getOrElse(i) { "00:00 - 00:05" }
            val drawableRes = defaultDrawables.getOrElse(i) { R.drawable.img_veo_scene_1 }

            val titleLine = block.lines().firstOrNull { it.isNotBlank() }?.replace("#", "")?.replace("*", "")?.trim()
                ?: "Scene ${i + 1} (${time})"

            var promptLine = ""
            val pMatch = Regex("""(?i)(?:Prompt|প্রম্পট)[:\s*]+([^\n]+(?:\n[^\n]+){0,2})""").find(block)
            if (pMatch != null) {
                promptLine = pMatch.groupValues[1].trim().replace("**", "")
            }
            if (promptLine.isBlank()) {
                promptLine = "Cinematic shot representing scene ${i + 1} of '$topic', photorealistic 8K, dynamic camera movement --ar 9:16"
            }

            var cameraLine = "Cinematic camera tracking with smooth stabilization"
            val cMatch = Regex("""(?i)(?:Camera|ক্যামেরা)[:\s*]+([^\n]+)""").find(block)
            if (cMatch != null) {
                cameraLine = cMatch.groupValues[1].trim().replace("**", "")
            }

            var voiceLine = "দৃশ্য ${i + 1}: ${topic.take(30)} এর সিনেমাটিক বিবরণ।"
            val vMatch = Regex("""(?i)(?:Voiceover|Voice|ভয়েস|ডায়লগ)[:\s*]+([^\n]+)""").find(block)
            if (vMatch != null) {
                voiceLine = vMatch.groupValues[1].trim().replace("**", "").replace("\"", "")
            }

            var sfxLine = "🔊 Cinematic ambient atmospheric sound + riser"
            val sMatch = Regex("""(?i)(?:SFX|সাউন্ড)[:\s*]+([^\n]+)""").find(block)
            if (sMatch != null) {
                sfxLine = sMatch.groupValues[1].trim().replace("**", "")
            }

            parsedScenes.add(
                VeoSceneData(
                    sceneNumber = i + 1,
                    timeRange = time,
                    title = titleLine.take(45),
                    visualPrompt = promptLine,
                    cameraMovement = cameraLine,
                    voiceover = voiceLine,
                    sfx = sfxLine,
                    previewDrawableRes = drawableRes
                )
            )
        }

        // If parsedScenes is empty or less than 4, fill remaining
        while (parsedScenes.size < 4) {
            val idx = parsedScenes.size
            parsedScenes.add(
                VeoSceneData(
                    sceneNumber = idx + 1,
                    timeRange = timeRanges[idx],
                    title = "দৃশ্য ${idx + 1}: অ্যাকশন সিকোয়েন্স",
                    visualPrompt = "Cinematic continuation shot for '$topic', dramatic lighting and action --ar 9:16",
                    cameraMovement = "Cinematic push-in with dynamic lighting",
                    voiceover = "এই ২০ সেকেন্ডের ভিডিও ক্লিপটি দর্শকদের দৃষ্টি আকর্ষণ করবে।",
                    sfx = "🔊 Cinematic sound effect",
                    previewDrawableRes = defaultDrawables[idx]
                )
            )
        }

        val fullVoice = parsedScenes.joinToString(" ") { it.voiceover }

        return Veo20sProject(
            title = topic,
            logline = "২০ সেকেন্ডের ফুল সিনেমাটিক Veo 3 এআই ভিডিও প্রজেক্ট",
            masterPrompt = masterPrompt,
            negativePrompt = negativePrompt,
            scenes = parsedScenes,
            fullVoiceover = fullVoice,
            freeWorkflowGuide = """
                ১. Google VideoFX (Labs.google) অথবা Kling AI তে সম্পূর্ণ ফ্রিতে অ্যাকাউন্ট খুলুন।
                ২. Master Prompt অথবা ৪টি আলাদা ৫-সেকেন্ডের প্রম্পট কপি করে ভিডিও তৈরি করুন।
                ৩. ৪টি ক্লিপ ফ্রি CapCut / InShot মোবাইল এডিটরে ড্রপ করে জোড়া দিন।
                ৪. অ্যাপে থাকা ২০ সেকেন্ডের বাংলা ভয়েসওভার ও সাউন্ড ইফেক্ট প্রয়োগ করুন = রেডি প্রফেশনাল ২০-সেকেন্ডের ভিডিও!
            """.trimIndent(),
            rawMarkdown = rawText
        )
    }
}
