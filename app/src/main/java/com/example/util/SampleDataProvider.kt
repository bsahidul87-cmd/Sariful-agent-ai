package com.example.util

import android.content.Context
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class ThinkingPromptCategory(
    val category: String,
    val iconName: String,
    val prompts: List<ThinkingPromptItem>
)

data class ThinkingPromptItem(
    val title: String,
    val prompt: String,
    val complexityLevel: String = "Ultra High"
)

data class SampleAudioClip(
    val id: String,
    val title: String,
    val category: String,
    val durationText: String,
    val speaker: String,
    val description: String,
    val baseFreq: Float,
    val durationSeconds: Int,
    val sampleTranscriptPreview: String
)

data class SampleVideoClip(
    val id: String,
    val title: String,
    val category: String,
    val durationText: String,
    val resolution: String,
    val description: String,
    val defaultPrompt: String,
    val suggestedQuestions: List<String>
)

object SampleDataProvider {

    val thinkingCategories: List<ThinkingPromptCategory> = listOf(
        ThinkingPromptCategory(
            category = "System Architecture & Resilience",
            iconName = "Memory",
            prompts = listOf(
                ThinkingPromptItem(
                    title = "Split-Brain Distributed Consensus",
                    prompt = "Design a globally distributed database operating across 5 cloud regions with a strict zero-data-loss guarantee (RPO=0) during catastrophic partitioned network splits. Rigorously analyze the mathematical tradeoffs between Paxos, Raft, and Spanner's TrueTime atomic clock synchronization, including write latencies, quorum lease invalidation, and Byzantine fault mitigation."
                ),
                ThinkingPromptItem(
                    title = "Lock-Free Memory Allocator",
                    prompt = "Synthesize an architecture for an ultra-low latency, cache-oblivious lock-free memory allocator in systems code. Detail the hazard pointer or epoch-based reclamation protocol, cache line false-sharing prevention, and mathematically model worst-case fragmentation bounds under adversarial allocation patterns."
                )
            )
        ),
        ThinkingPromptCategory(
            category = "Math, Logic & Algorithms",
            iconName = "Calculate",
            prompts = listOf(
                ThinkingPromptItem(
                    title = "Collatz Conjecture Heuristics & Tao Bounds",
                    prompt = "Analyze the Collatz Conjecture (3x + 1 problem). Deconstruct Terence Tao's breakthrough on 'almost all Collatz orbits attaining almost bounded values', evaluate the 2-adic integer ergodic dynamics, and explain why traditional inductive or algebraic number theory approaches have thus far fallen short of a complete proof."
                ),
                ThinkingPromptItem(
                    title = "Quantum Surface Code Error Correction",
                    prompt = "Explain topological surface code quantum error correction. Walk through the toric code Hamiltonian, syndrome measurement extraction for X and Z stabilizers, and compare the minimum-weight perfect matching (MWPM) decoder with union-find decoders in maintaining fault tolerance below the 1% error threshold."
                )
            )
        ),
        ThinkingPromptCategory(
            category = "Biophysics & Deep Science",
            iconName = "Biotech",
            prompts = listOf(
                ThinkingPromptItem(
                    title = "CRISPR Off-Target Energetics",
                    prompt = "Model the thermodynamic free energy landscape of CRISPR-Cas9 off-target DNA cleavage compared to engineered Cas12a. Detail the R-loop formation kinetics, seed sequence mismatch penalties, and conformational checkpoint transitions governing HNH nuclease activation."
                ),
                ThinkingPromptItem(
                    title = "Room-Temp Superconductivity Criteria",
                    prompt = "Rigorously analyze the theoretical constraints on near-ambient superconductivity under BCS Eliashberg electron-phonon coupling theory versus strongly correlated unconventional mechanisms (e.g., cuprates, nickelates). What exact phonon density of states and electron-electron repulsion parameters are strictly necessary?"
                )
            )
        )
    )

    val sampleAudioClips: List<SampleAudioClip> = listOf(
        SampleAudioClip(
            id = "audio_exec_brief",
            title = "Executive Architecture Brief",
            category = "Engineering Strategy",
            durationText = "0:35",
            speaker = "Chief Architect",
            description = "High-level summary of migrating legacy monolith microservices to event-driven edge pipelines.",
            baseFreq = 320f,
            durationSeconds = 6,
            sampleTranscriptPreview = "Good morning team. During yesterday's architectural review, we finalized the timeline for decoupling the checkout service from the core inventory monolith. We're prioritizing the gRPC streaming layer and ensuring event ordering via Kafka partition keys..."
        ),
        SampleAudioClip(
            id = "audio_clinic_note",
            title = "Clinical Consultation Memo",
            category = "Medical Records",
            durationText = "0:42",
            speaker = "Dr. Elena Vance",
            description = "Post-operative follow-up notes for Patient #8104 regarding cardiovascular recovery and medication adjustments.",
            baseFreq = 440f,
            durationSeconds = 7,
            sampleTranscriptPreview = "Patient presented for routine post-operative checkup at week four. Bilateral pulmonary sounds are clear with normal sinus rhythm. We will titrate the beta blocker down to 25 milligrams daily and schedule an echocardiogram in six weeks..."
        ),
        SampleAudioClip(
            id = "audio_startup_pitch",
            title = "Autonomous Drone Fleet Pitch",
            category = "Venture Pitch",
            durationText = "0:38",
            speaker = "Founder & CEO",
            description = "Pitch memo discussing automated beyond-visual-line-of-sight (BVLOS) logistics in rural healthcare delivery.",
            baseFreq = 380f,
            durationSeconds = 6,
            sampleTranscriptPreview = "Our aerial network has completed over twelve thousand medical supply deliveries with ninety-nine point eight percent on-time precision. By utilizing edge computer vision and dual LTE satellite failover, we reduce cold-chain transport times by seventy percent..."
        )
    )

    val sampleVideoClips: List<SampleVideoClip> = listOf(
        SampleVideoClip(
            id = "video_mars_rover",
            title = "Mars Rover Autonomous Navigation",
            category = "Robotics & Space",
            durationText = "0:18",
            resolution = "1080p 60fps",
            description = "Autonomous terrain hazard detection, wheel slip telemetry, and automated robotic arm deployment.",
            defaultPrompt = "Analyze this video with Gemini Pro: Provide a comprehensive breakdown of the planetary rover's locomotion, environmental terrain hazards detected, sensor array movements, and robotic arm actions.",
            suggestedQuestions = listOf(
                "What terrain hazards does the rover encounter?",
                "Provide a timestamped log of the robotic arm movements.",
                "Assess the wheel slippage and traction telemetry."
            )
        ),
        SampleVideoClip(
            id = "video_cleanroom_robot",
            title = "Cleanroom Semiconductor Assembly",
            category = "Industrial Automation",
            durationText = "0:15",
            resolution = "4K Ultra HD",
            description = "High-speed Delta robotic manipulator performing microscopic silicon wafer wire-bonding and optical inspection.",
            defaultPrompt = "Examine this cleanroom assembly video: Detail the pick-and-place precision, cycle time per wafer, optical alignment steps, and any detected calibration shifts.",
            suggestedQuestions = listOf(
                "What is the average pick-and-place cycle time?",
                "Are there any visible micro-defects or anomalies?",
                "Summarize the optical alignment procedure."
            )
        ),
        SampleVideoClip(
            id = "video_espresso_fluid",
            title = "Espresso Extraction Fluid Dynamics",
            category = "Fluid Mechanics",
            durationText = "0:12",
            resolution = "1080p Slow-Mo",
            description = "Macro high-speed photography of bottomless portafilter flow dynamics, channeling prevention, and crema layering.",
            defaultPrompt = "Analyze the fluid dynamics in this video: Observe the pre-infusion saturation, stream unification, channeling occurrences, and color gradation of the crema.",
            suggestedQuestions = listOf(
                "Does channeling or spurting occur during extraction?",
                "Identify when the transition from blonding occurs.",
                "Describe the flow consistency and droplet formation."
            )
        )
    )

    /**
     * Generates a valid standard 16-bit PCM WAV audio file with synthesized voice-like modulated tone.
     * This file is playable by Android MediaPlayer and recognized as audio/wav by Gemini API!
     */
    fun createSampleWavFile(context: Context, clip: SampleAudioClip): File {
        val file = File(context.cacheDir, "${clip.id}.wav")
        if (file.exists() && file.length() > 1000) return file

        val sampleRate = 16000
        val numSamples = sampleRate * clip.durationSeconds
        val pcmData = ShortArray(numSamples)

        // Synthesize an expressive modulated voice harmonic sound
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val envelope = Math.sin(Math.PI * t / clip.durationSeconds) // smooth fade in/out
            val modulation = 1.0 + 0.15 * Math.sin(2.0 * Math.PI * 4.0 * t) // vibrato
            val fundamental = Math.sin(2.0 * Math.PI * (clip.baseFreq * modulation) * t)
            val harmonic2 = 0.4 * Math.sin(2.0 * Math.PI * (clip.baseFreq * 2.0) * t)
            val harmonic3 = 0.2 * Math.sin(2.0 * Math.PI * (clip.baseFreq * 3.0) * t)
            val sampleVal = ((fundamental + harmonic2 + harmonic3) * envelope * 0.5 * Short.MAX_VALUE).toInt()
            pcmData[i] = sampleVal.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }

        val byteBuffer = ByteBuffer.allocate(pcmData.size * 2).order(ByteOrder.LITTLE_ENDIAN)
        for (sample in pcmData) {
            byteBuffer.putShort(sample)
        }
        val rawPcm = byteBuffer.array()

        val totalDataLen = rawPcm.size + 36
        val byteRate = sampleRate * 1 * 16 / 8

        val header = ByteArray(44)
        header[0] = 'R'.code.toByte()
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = ((totalDataLen shr 8) and 0xff).toByte()
        header[6] = ((totalDataLen shr 16) and 0xff).toByte()
        header[7] = ((totalDataLen shr 24) and 0xff).toByte()
        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()
        header[12] = 'f'.code.toByte()
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()
        header[16] = 16 // Subchunk1Size (16 for PCM)
        header[17] = 0
        header[18] = 0
        header[19] = 0
        header[20] = 1 // AudioFormat (1 = PCM)
        header[21] = 0
        header[22] = 1 // NumChannels (1 = Mono)
        header[23] = 0
        header[24] = (sampleRate and 0xff).toByte()
        header[25] = ((sampleRate shr 8) and 0xff).toByte()
        header[26] = ((sampleRate shr 16) and 0xff).toByte()
        header[27] = ((sampleRate shr 24) and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = ((byteRate shr 8) and 0xff).toByte()
        header[30] = ((byteRate shr 16) and 0xff).toByte()
        header[31] = ((byteRate shr 24) and 0xff).toByte()
        header[32] = 2 // BlockAlign (1 * 16 / 8 = 2)
        header[33] = 0
        header[34] = 16 // BitsPerSample
        header[35] = 0
        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()
        header[40] = (rawPcm.size and 0xff).toByte()
        header[41] = ((rawPcm.size shr 8) and 0xff).toByte()
        header[42] = ((rawPcm.size shr 16) and 0xff).toByte()
        header[43] = ((rawPcm.size shr 24) and 0xff).toByte()

        FileOutputStream(file).use { fos ->
            fos.write(header)
            fos.write(rawPcm)
        }
        return file
    }

    /**
     * Creates a dummy valid MP4 / video byte payload for testing sample videos if none selected from camera.
     */
    fun createSampleVideoFile(context: Context, clip: SampleVideoClip): File {
        val file = File(context.cacheDir, "${clip.id}.mp4")
        if (file.exists() && file.length() > 500) return file

        // Standard minimal MP4 file box structure (ftyp isom + moov stub)
        val ftypBox = byteArrayOf(
            0x00, 0x00, 0x00, 0x20, // size 32
            'f'.code.toByte(), 't'.code.toByte(), 'y'.code.toByte(), 'p'.code.toByte(),
            'i'.code.toByte(), 's'.code.toByte(), 'o'.code.toByte(), 'm'.code.toByte(),
            0x00, 0x00, 0x02, 0x00,
            'i'.code.toByte(), 's'.code.toByte(), 'o'.code.toByte(), 'm'.code.toByte(),
            'i'.code.toByte(), 's'.code.toByte(), 'o'.code.toByte(), '2'.code.toByte(),
            'a'.code.toByte(), 'v'.code.toByte(), 'c'.code.toByte(), '1'.code.toByte(),
            'm'.code.toByte(), 'p'.code.toByte(), '4'.code.toByte(), '1'.code.toByte()
        )
        // Additional mock payload bytes representing video frames
        val payload = ByteArray(2048) { (it % 255).toByte() }
        FileOutputStream(file).use { fos ->
            fos.write(ftypBox)
            fos.write(payload)
        }
        return file
    }
}
