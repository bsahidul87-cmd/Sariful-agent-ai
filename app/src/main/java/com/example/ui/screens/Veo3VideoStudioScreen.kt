package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.FormattedContentCard
import com.example.ui.components.ModelBadge
import com.example.ui.theme.AmberThinking
import com.example.ui.theme.CyanSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.RoseVideo
import com.example.util.Veo20sProject
import com.example.util.VeoSceneData
import com.example.util.VeoVideoHelper

@Composable
fun Veo3VideoStudioScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val topic by viewModel.veoTopic.collectAsState()
    val cameraMotion by viewModel.veoCameraMotion.collectAsState()
    val visualStyle by viewModel.veoVisualStyle.collectAsState()
    val aspectRatio by viewModel.veoAspectRatio.collectAsState()
    val language by viewModel.veoLanguage.collectAsState()
    val isLoading by viewModel.isVeoLoading.collectAsState()
    val project by viewModel.veoProject.collectAsState()
    val errorMessage by viewModel.veoError.collectAsState()
    val isSaved by viewModel.isVeoSaved.collectAsState()

    // 20-Second Player States
    val playerTimeMs by viewModel.veoPlayerTimeMs.collectAsState()
    val isPlaying by viewModel.isVeoPlayerPlaying.collectAsState()
    val isTtsEnabled by viewModel.isVeoPlayerTtsEnabled.collectAsState()
    val isShortsMode by viewModel.isVeoPlayerShortsMode.collectAsState()

    var showFreeGuideDialog by remember { mutableStateOf(false) }
    var selectedStudioTab by remember { mutableIntStateOf(0) } // 0: AI Video, 1: AI Photo

    // Preset Options
    val cameraMotionOptions = listOf(
        "Cinematic Drone Sweep & Dolly Zoom",
        "360° Orbit Around Subject",
        "Fast FPV Dive-In",
        "Low-Angle Tracking Shot",
        "Smooth Steadicam Arc"
    )

    val visualStyleOptions = listOf(
        "Photorealistic 8K Cinema",
        "Cyberpunk Neon Glow",
        "Anime Studio Ghibli",
        "35mm Vintage Hollywood Film",
        "BBC 4K Nature Documentary"
    )

    val presetIdeas = listOf(
        "ভবিষ্যতের সাইবারপাঙ্ক ঢাকা ২০৫০",
        "এআই রোবট ও মানুষের বন্ধুত্ব",
        "টাইম ট্রাভেলার যখন ডাইনোসরের যুগে পৌঁছায়",
        "অতল সমুদ্রের রহস্যময় আলোকিত প্রাণী",
        "সোশ্যাল মিডিয়া ভাইরাল ২০ সেকেন্ড শর্টস"
    )

    // Calculate active scene for the player
    val currentSceneIndex = (playerTimeMs / 5000L).toInt().coerceIn(0, 3)
    val activeScene: VeoSceneData? = project?.scenes?.getOrNull(currentSceneIndex)

    // Dynamic Image for current scene
    val currentDrawableRes = when (currentSceneIndex) {
        0 -> R.drawable.img_veo_scene_1
        1 -> R.drawable.img_veo_scene_2
        2 -> R.drawable.img_veo_scene_3
        else -> R.drawable.img_veo_scene_4
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("veo3_video_studio_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. HERO BANNER & FREE WORKFLOW BADGE ---
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.2.dp, RoseVideo.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Studio Banner Image
                    Image(
                        painter = painterResource(id = R.drawable.img_veo3_studio_banner),
                        contentDescription = "Veo 3 Studio Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xCC090D16),
                                        Color(0xFA090D16)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ModelBadge(
                                modelName = "Google Veo 3",
                                modeTag = "20s Free Cinema",
                                accentColor = RoseVideo
                            )

                            // Free Guide Button
                            Surface(
                                shape = RoundedCornerShape(100.dp),
                                color = AmberThinking.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, AmberThinking.copy(alpha = 0.6f)),
                                modifier = Modifier.clickable { showFreeGuideDialog = true }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.HelpOutline,
                                        contentDescription = null,
                                        tint = AmberThinking,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "১০০% ফ্রি গাইড",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = AmberThinking,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "Veo 3 AI Video Studio (২০ সেকেন্ড)",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "গুগল Veo 3 / VideoFX মডেলের জন্য সিনেমাটিক প্রম্পট, ৪টি ৫-সেকেন্ড চেইন্ড সিন, ক্যামেরা মোশন এবং ২০ সেকেন্ড ফ্রি ভিডিও বানানোর মেগা স্টুডিও।",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }
        }

        // --- 2. STUDIO MODE SELECTOR (AI Video vs AI Photo) ---
        item {
            TabRow(
                selectedTabIndex = selectedStudioTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = RoseVideo,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
            ) {
                Tab(
                    selected = selectedStudioTab == 0,
                    onClick = { selectedStudioTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MovieCreation, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("ভিডিও জেনারেটর (Veo 3)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                )
                Tab(
                    selected = selectedStudioTab == 1,
                    onClick = { selectedStudioTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("ফটো জেনারেটর (8K AI Photo)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                )
            }
        }

        if (selectedStudioTab == 0) {
            // --- 2. INTERACTIVE 20-SECOND VIDEO SIMULATOR PLAYER ---
            item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117)),
                border = BorderStroke(1.dp, CyanSecondary.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("veo_video_player_card")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Player Header Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (isPlaying) Color(0xFF10B981) else AmberThinking)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isPlaying) "PLAYING (চলছে)" else "20-SEC PREVIEW",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPlaying) Color(0xFF34D399) else AmberThinking
                                )
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Aspect Ratio Toggle Button
                            IconButton(
                                onClick = { viewModel.toggleVeoPlayerAspect() },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.AspectRatio,
                                    contentDescription = "Toggle Player Aspect Ratio",
                                    tint = CyanSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // TTS Audio Narration Toggle Button
                            IconButton(
                                onClick = { viewModel.toggleVeoPlayerTts() },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (isTtsEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                                    contentDescription = "Toggle Narration TTS",
                                    tint = if (isTtsEnabled) CyanSecondary else Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0x3306B6D4)
                            ) {
                                Text(
                                    text = if (isShortsMode) "9:16 Shorts" else "16:9 Cinema",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CyanSecondary,
                                        fontSize = 10.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Video Frame Container with simulated camera motion
                    val infiniteTransition = rememberInfiniteTransition(label = "cameraMotion")
                    val cameraScale by infiniteTransition.animateFloat(
                        initialValue = 1.0f,
                        targetValue = if (isPlaying) 1.06f else 1.0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2500, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "cameraScale"
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (isShortsMode) Modifier.height(260.dp) else Modifier.aspectRatio(16f / 9f)
                            )
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.Black)
                            .border(1.dp, Color(0xFF22272E), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Current Scene Keyframe
                        Image(
                            painter = painterResource(id = activeScene?.previewDrawableRes ?: currentDrawableRes),
                            contentDescription = "Veo 3 Scene Preview",
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(cameraScale),
                            contentScale = ContentScale.Crop
                        )

                        // Top Gradient Overlay (Metadata)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color(0xCC000000), Color.Transparent)
                                    )
                                )
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(100.dp),
                                    color = Color(0x99000000)
                                ) {
                                    Text(
                                        text = "Scene ${currentSceneIndex + 1}/4 • ${activeScene?.timeRange ?: "00:00 - 00:05"}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = AmberThinking,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(100.dp),
                                    color = Color(0x99000000)
                                ) {
                                    Text(
                                        text = "Veo 3 • 4K 60fps",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White.copy(alpha = 0.9f),
                                            fontSize = 10.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        // Bottom Gradient Overlay (Subtitles & SFX)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color(0xEE000000))
                                    )
                                )
                                .padding(10.dp)
                        ) {
                            Column {
                                // SFX badge
                                if (activeScene != null && activeScene.sfx.isNotBlank()) {
                                    Text(
                                        text = activeScene.sfx,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = CyanSecondary,
                                            fontSize = 10.5.sp
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                }

                                // Subtitle Narration
                                Text(
                                    text = activeScene?.voiceover ?: "২০ সেকেন্ডের Veo 3 সিনেমাটিক ক্লিপ লোড হচ্ছে...",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    ),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Play indicator overlay when paused in the center
                        if (!isPlaying && playerTimeMs == 0L) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0x99000000),
                                modifier = Modifier
                                    .size(54.dp)
                                    .clickable { viewModel.playVeoVideo() }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.PlayArrow,
                                        contentDescription = "Play Video",
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Time Scrubber Slider (0.0s to 20.0s)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val currentSeconds = (playerTimeMs / 1000f)
                        val formattedCurrent = String.format("00:%02d", currentSeconds.toInt())

                        Text(
                            text = formattedCurrent,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CyanSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )

                        Slider(
                            value = playerTimeMs.toFloat(),
                            onValueChange = { viewModel.seekVeoVideo(it.toLong()) },
                            valueRange = 0f..20000f,
                            colors = SliderDefaults.colors(
                                thumbColor = CyanSecondary,
                                activeTrackColor = CyanSecondary,
                                inactiveTrackColor = Color(0xFF263238)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                                .testTag("veo_player_slider")
                        )

                        Text(
                            text = "00:20",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        )
                    }

                    // Transport Buttons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 4 Scene Indicator Markers
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("0-5s", "5-10s", "10-15s", "15-20s").forEachIndexed { index, label ->
                                val isSceneActive = currentSceneIndex == index
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (isSceneActive) CyanSecondary.copy(alpha = 0.25f) else Color(0xFF1E293B),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSceneActive) CyanSecondary else Color.Transparent
                                    ),
                                    modifier = Modifier.clickable {
                                        viewModel.seekVeoVideo((index * 5000L))
                                    }
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSceneActive) CyanSecondary else Color.Gray,
                                            fontSize = 9.5.sp,
                                            fontWeight = if (isSceneActive) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // Play/Pause & Replay Buttons
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            IconButton(
                                onClick = { viewModel.resetVeoVideo() },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.Default.Replay,
                                    contentDescription = "Restart Video",
                                    tint = Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Button(
                                onClick = {
                                    if (isPlaying) viewModel.pauseVeoVideo() else viewModel.playVeoVideo()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isPlaying) AmberThinking else CyanSecondary,
                                    contentColor = Color(0xFF041E26)
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .height(36.dp)
                                    .testTag("veo_player_play_pause_button")
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Pause" else "Play",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isPlaying) "পজ" else "প্লে ২০ সে.",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 3. INPUT FORM & VE0 3 GENERATOR CONTROLS ---
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ভিডিওর আইডিয়া বা বিষয় (Video Concept)",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = topic,
                        onValueChange = { viewModel.setVeoTopic(it) },
                        placeholder = { Text("যেমন: ভবিষ্যতের সাইবারপাঙ্ক ঢাকা সিটিতে উড়ন্ত গাড়ি ও নিয়ন ড্রোন...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(95.dp)
                            .testTag("veo_topic_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoseVideo,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Preset Chips
                    Text(
                        text = "১-ক্লিক ভাইরাল প্রিসেট আইডিয়া:",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        presetIdeas.forEach { preset ->
                            Surface(
                                shape = RoundedCornerShape(100.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, RoseVideo.copy(alpha = 0.3f)),
                                modifier = Modifier.clickable {
                                    val matchedSample = VeoVideoHelper.sampleProjects.firstOrNull { it.title.contains(preset.take(6)) }
                                    if (matchedSample != null) {
                                        viewModel.loadPresetVeoProject(matchedSample)
                                    } else {
                                        viewModel.setVeoTopic(preset)
                                    }
                                }
                            ) {
                                Text(
                                    text = preset,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Camera Motion Style Selector
                    Text(
                        text = "ক্যামেরা মোশন স্টাইল (Veo 3 Camera Choreography):",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        cameraMotionOptions.forEach { motion ->
                            FilterChip(
                                selected = cameraMotion == motion,
                                onClick = { viewModel.setVeoCameraMotion(motion) },
                                label = { Text(motion, style = MaterialTheme.typography.labelSmall) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = RoseVideo.copy(alpha = 0.25f),
                                    selectedLabelColor = RoseVideo
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Visual Style Selector
                    Text(
                        text = "ভিজ্যুয়াল স্টাইল (Art Direction):",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        visualStyleOptions.forEach { style ->
                            FilterChip(
                                selected = visualStyle == style,
                                onClick = { viewModel.setVeoVisualStyle(style) },
                                label = { Text(style, style = MaterialTheme.typography.labelSmall) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyanSecondary.copy(alpha = 0.25f),
                                    selectedLabelColor = CyanSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Aspect Ratio & Language Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "অনুপাত (Aspect Ratio):",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("9:16 (Shorts/Reels)", "16:9 (Landscape)").forEach { ratio ->
                                    FilterChip(
                                        selected = aspectRatio == ratio,
                                        onClick = { viewModel.setVeoAspectRatio(ratio) },
                                        label = {
                                            Text(
                                                if (ratio.startsWith("9:16")) "9:16 Shorts" else "16:9 Cinema",
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = AmberThinking.copy(alpha = 0.25f),
                                            selectedLabelColor = AmberThinking
                                        )
                                    )
                                }
                            }
                        }

                        Column {
                            Text(
                                text = "ভাষা (Language):",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("Bengali (বাংলা)", "English").forEach { lang ->
                                    FilterChip(
                                        selected = language == lang,
                                        onClick = { viewModel.setVeoLanguage(lang) },
                                        label = {
                                            Text(
                                                if (lang.contains("Bengali")) "বাংলা" else "English",
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = IndigoPrimary.copy(alpha = 0.25f),
                                            selectedLabelColor = IndigoPrimary
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Big Generate Button
                    Button(
                        onClick = { viewModel.generateVeo20SecondVideo() },
                        enabled = topic.isNotBlank() && !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoseVideo,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("generate_veo20s_button")
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Veo 3 ২০ সেকেন্ড প্রজেক্ট তৈরি হচ্ছে...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.MovieCreation, contentDescription = null, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate 20s Veo 3 Video Project", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // --- Error Banner ---
        if (errorMessage != null) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF450A0A)),
                    border = BorderStroke(1.dp, Color(0xFFDC2626)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = Color(0xFFFCA5A5))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = errorMessage ?: "Error",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFFECACA))
                        )
                    }
                }
            }
        }

        // --- 4. MASTER VEO 3 PROMPT CARD ---
        if (project != null) {
            val proj = project!!
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, AmberThinking.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AmberThinking)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Master Google Veo 3 Prompt (২০ সেকেন্ড)",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = AmberThinking
                                    )
                                )
                            }

                            // Copy Master Prompt Button
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Veo Master Prompt", proj.masterPrompt)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Master Veo 3 Prompt কপি হয়েছে! (VideoFX এ পেস্ট করুন)", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.testTag("copy_master_veo_prompt_button")
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy Master Prompt", tint = AmberThinking)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = proj.masterPrompt,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                ),
                                modifier = Modifier.padding(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Negative Prompt
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Negative Prompt:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.Gray)
                            )
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Negative Prompt", proj.negativePrompt)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Negative Prompt কপি হয়েছে!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy Negative Prompt", tint = Color.Gray, modifier = Modifier.size(16.dp))
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = proj.negativePrompt,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }

            // --- 5. 4-SCENE 20-SECOND CHRONOLOGICAL BREAKDOWN ---
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "৪টি সিন ব্রেকডাউন (২০ সেকেন্ড সম্পূর্ণ চেইনিং)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CyanSecondary
                        )
                    )

                    Button(
                        onClick = {
                            val allScenesText = proj.scenes.joinToString("\n\n---\n\n") { s ->
                                "Scene ${s.sceneNumber} (${s.timeRange}):\n${s.title}\nCamera: ${s.cameraMovement}\nPrompt: ${s.visualPrompt}\nVoiceover: ${s.voiceover}\nSFX: ${s.sfx}"
                            }
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("All Veo 3 Scenes", allScenesText)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "৪টি সিনের সবগুলো প্রম্পট একসঙ্গে কপি হয়েছে!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanSecondary.copy(alpha = 0.2f), contentColor = CyanSecondary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy All 4", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Render each of the 4 scenes
            proj.scenes.forEach { scene ->
                item {
                    VeoSceneCard(
                        scene = scene,
                        isActiveInPlayer = currentSceneIndex == (scene.sceneNumber - 1),
                        onJumpToScene = {
                            viewModel.seekVeoVideo(((scene.sceneNumber - 1) * 5000L))
                        },
                        onCopyPrompt = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Scene ${scene.sceneNumber} Prompt", scene.visualPrompt)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Scene ${scene.sceneNumber} প্রম্পট কপি হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            // --- 6. 20-SECOND CONTINUOUS NARRATION & SFX TRACK ---
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🎙️ ২০ সেকেন্ড অডিও ও বাংলা ভয়েসওভার স্ক্রিপ্ট",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = IndigoPrimary
                                )
                            )

                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("20s Voiceover Script", proj.fullVoiceover)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "ভয়েসওভার স্ক্রিপ্ট কপি হয়েছে!", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy Voiceover", tint = IndigoPrimary)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = proj.fullVoiceover,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    lineHeight = 22.sp
                                ),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }

            // --- 7. FREE WORKFLOW CREATION RECIPE CARD ---
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, AmberThinking.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AmberThinking)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "💡 কীভাবে সম্পূর্ণ ফ্রিতে ২০ সেকেন্ডের ভিডিও বানাবেন?",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AmberThinking
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = proj.freeWorkflowGuide,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }

            // --- 8. SAVE & EXPORT ACTION BUTTONS ---
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { viewModel.saveVeoProject() },
                        enabled = !isSaved,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoseVideo,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("save_veo_project_button")
                    ) {
                        Icon(
                            if (isSaved) Icons.Default.Check else Icons.Default.Bookmark,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isSaved) "সংরক্ষিত হয়েছে" else "Save Project", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            val exportText = """
                                🎬 Veo 3 AI Video (20 Seconds)
                                Title: ${proj.title}
                                
                                Master Prompt:
                                ${proj.masterPrompt}
                                
                                Negative:
                                ${proj.negativePrompt}
                                
                                4-Scene Breakdown:
                                ${proj.scenes.joinToString("\n\n") { "Scene ${it.sceneNumber} (${it.timeRange}):\n${it.visualPrompt}\nVoiceover: ${it.voiceover}" }}
                                
                                Voiceover:
                                ${proj.fullVoiceover}
                            """.trimIndent()

                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Complete Veo 3 Project", exportText)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "সম্পূর্ণ প্রজেক্ট কপি হয়েছে!", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("copy_entire_veo_project_button")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export All", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    } else {
        // PHOTO GENERATOR TAB
        item {
            AiPhotoStudioSection(viewModel = viewModel)
        }
    }

    item { Spacer(modifier = Modifier.height(28.dp)) }
}

// --- FREE GUIDE DIALOG ---
if (showFreeGuideDialog) {
    AlertDialog(
        onDismissRequest = { showFreeGuideDialog = false },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AmberThinking)
                Spacer(modifier = Modifier.width(8.dp))
                Text("১০০% ফ্রিতে Veo 3 দিয়ে ২০ সেকেন্ড ভিডিও বানানোর টিপস", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "১. Google VideoFX (Labs.google):",
                    fontWeight = FontWeight.Bold,
                    color = CyanSecondary
                )
                Text(
                    text = "Google Labs ওয়েবসাইটে আপনার জিমেইল দিয়ে লগইন করলে VideoFX তে বিনামূল্যে Veo মডেল দিয়ে ভিডিও জেনারেট করা যায়। আমাদের দেওয়া Master Prompt পেস্ট করে সরাসরি হাই-কোয়ালিটি ভিডিও পাবেন।",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "২. YouTube Shorts Dream Screen (সম্পূর্ণ ফ্রি):",
                    fontWeight = FontWeight.Bold,
                    color = RoseVideo
                )
                Text(
                    text = "ইউটিউব অ্যাপের Shorts ক্রিয়েটরে 'Dream Screen' অপশনে সরাসরি Google Veo প্রযুক্তি ফ্রি যুক্ত রয়েছে। প্রম্পট দিয়ে সরাসরি ব্যাকগ্রাউন্ড ভিডিও বানিয়ে নেওয়া যায়।",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "৩. ৪টি ৫-সেকেন্ড ক্লিপ জোড়া দেওয়া (Chained Method):",
                    fontWeight = FontWeight.Bold,
                    color = AmberThinking
                )
                Text(
                    text = "বেশিরভাগ ফ্রি এআই ভিডিও টুল (Kling AI, Luma Dream Machine, Runway free tier, Veo) ৫ সেকেন্ডের ক্লিপ ফ্রি দেয়। আমাদের অ্যাপ প্রতিটি ভিডিওকে চমৎকারভাবে ৪টি দৃশ্যে (০-৫, ৫-১০, ১০-১৫, ১৫-২০ সেকেন্ড) ভাগ করে দেয়। ক্লিপ ৪টি ফ্রিতে তৈরি করে CapCut বা InShot দিয়ে জোড়া দিন = সম্পূর্ণ নিখরচায় তৈরি হয়ে গেল আপনার ২০ সেকেন্ডের সিনেমাটিক মাস্টারপিস!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { showFreeGuideDialog = false },
                colors = ButtonDefaults.buttonColors(containerColor = AmberThinking, contentColor = Color(0xFF271302))
            ) {
                Text("বুঝেছি, ধন্যবাদ!")
            }
        }
    )
}
}

@Composable
fun VeoSceneCard(
    scene: VeoSceneData,
    isActiveInPlayer: Boolean,
    onJumpToScene: () -> Unit,
    onCopyPrompt: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActiveInPlayer) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            if (isActiveInPlayer) 1.5.dp else 1.dp,
            if (isActiveInPlayer) CyanSecondary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isActiveInPlayer) CyanSecondary else MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "Scene ${scene.sceneNumber} (${scene.timeRange})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isActiveInPlayer) Color(0xFF041E26) else MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    if (isActiveInPlayer) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "▶ NOW IN PLAYER",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CyanSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TextButton(onClick = onJumpToScene) {
                        Text("প্লেয়ার এ দেখুন", fontSize = 11.sp, color = CyanSecondary)
                    }
                    IconButton(onClick = onCopyPrompt, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy Scene Prompt", tint = CyanSecondary, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = scene.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Camera instruction
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🎥 Camera: ",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = AmberThinking)
                )
                Text(
                    text = scene.cameraMovement,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Visual Prompt Box
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = scene.visualPrompt,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    ),
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Voiceover & SFX
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "🗣️ Voice: ",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = IndigoPrimary)
                )
                Text(
                    text = scene.voiceover,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp)
                )
            }
        }
    }
}

@Composable
fun AiPhotoStudioSection(viewModel: MainViewModel) {
    val context = LocalContext.current
    val photoPrompt by viewModel.photoPrompt.collectAsState()
    val photoStyle by viewModel.photoStyle.collectAsState()
    val photoAspectRatio by viewModel.photoAspectRatio.collectAsState()
    val isPhotoLoading by viewModel.isPhotoLoading.collectAsState()
    val photoResult by viewModel.photoResult.collectAsState()
    val isPhotoSaved by viewModel.isPhotoSaved.collectAsState()
    val selectedImageRes by viewModel.selectedEditImageRes.collectAsState()

    val styles = listOf(
        "Photorealistic 8K Cinema",
        "Cyberpunk Neon Glow",
        "3D Pixar Animation",
        "Cinematic Portrait (85mm f/1.4)",
        "Anime Studio Ghibli",
        "Product Commercial Studio"
    )

    val aspectRatios = listOf("1:1 (Square)", "4:5 (Instagram)", "9:16 (Shorts/Story)", "16:9 (Landscape)")

    val photoPresets = listOf(
        "সাইবারপাঙ্ক নিয়ন ঢাকা ২০৫০",
        "বিউটিফুল সিনেমাটিক পোর্ট্রেট (৮৫মিমি)",
        "মহাকাশের অতল গ্যালাক্সি ও নেবুলা",
        "এআই রোবট ও ছোট্ট শিশুর বন্ধুত্ব",
        "থ্রিডি কিউট কার্টুন ক্যারেক্টার"
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Photo Preview Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117)),
            border = BorderStroke(1.2.dp, CyanSecondary.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "হাই-কোয়ালিটি ফটো প্রিভিউ",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CyanSecondary
                        )
                    )
                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = CyanSecondary.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "8K Masterpiece • $photoAspectRatio",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CyanSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = selectedImageRes),
                        contentDescription = "AI Generated Photo Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color(0xEE000000))))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = photoPrompt,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // Action: Jump to Photo Editor
        Button(
            onClick = { viewModel.navigateTo(Screen.MEDIA_EDITOR) },
            colors = ButtonDefaults.buttonColors(containerColor = CyanSecondary, contentColor = Color(0xFF041E26)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
        ) {
            Icon(Icons.Default.Brush, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("🎨 ফটো এডিটরে এডিট করুন (Open in Photo Editor)", fontWeight = FontWeight.Bold)
        }

        // Input Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ছবির প্রম্পট বা কনসেপ্ট (Photo Prompt):",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = photoPrompt,
                    onValueChange = { viewModel.setPhotoPrompt(it) },
                    placeholder = { Text("আপনার কাঙ্ক্ষিত ছবির বিস্তারিত বিবরণ লিখুন...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(95.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyanSecondary)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Presets
                Text(
                    text = "১-ক্লিক ভাইরাল প্রিসেট:",
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    photoPresets.forEach { preset ->
                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, CyanSecondary.copy(alpha = 0.3f)),
                            modifier = Modifier.clickable { viewModel.setPhotoPrompt(preset) }
                        ) {
                            Text(
                                text = preset,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Styles
                Text(
                    text = "আর্ট স্টাইল (Art Style):",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    styles.forEach { st ->
                        FilterChip(
                            selected = photoStyle == st,
                            onClick = { viewModel.setPhotoStyle(st) },
                            label = { Text(st, style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyanSecondary.copy(alpha = 0.25f),
                                selectedLabelColor = CyanSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Aspect Ratio
                Text(
                    text = "অনুপাত (Aspect Ratio):",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    aspectRatios.forEach { ratio ->
                        FilterChip(
                            selected = photoAspectRatio == ratio,
                            onClick = { viewModel.setPhotoAspectRatio(ratio) },
                            label = { Text(ratio.split(" ")[0], style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AmberThinking.copy(alpha = 0.25f),
                                selectedLabelColor = AmberThinking
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.generatePhotoPrompt() },
                    enabled = photoPrompt.isNotBlank() && !isPhotoLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanSecondary, contentColor = Color(0xFF041E26)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    if (isPhotoLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color(0xFF041E26), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("৮কে ফটো ব্লুপ্রিন্ট তৈরি হচ্ছে...", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate 8K AI Photo Blueprint", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Results Card
        if (photoResult != null) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "৮কে ফটো মাস্টার প্রম্পট ব্লুপ্রিন্ট",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = CyanSecondary)
                )

                FormattedContentCard(content = photoResult!!, accentColor = CyanSecondary)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("AI Photo Prompt", photoResult!!)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "মাস্টার ফটো প্রম্পট কপি হয়েছে!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanSecondary, contentColor = Color(0xFF041E26)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copy Prompt", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { viewModel.savePhotoResult() },
                        enabled = !isPhotoSaved,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(if (isPhotoSaved) Icons.Default.Check else Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isPhotoSaved) "Saved" else "Save")
                    }
                }
            }
        }
    }
}
