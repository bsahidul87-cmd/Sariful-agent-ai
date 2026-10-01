package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.MainViewModel
import com.example.ui.components.ModelBadge
import com.example.ui.theme.AmberThinking
import com.example.ui.theme.CyanSecondary
import com.example.ui.theme.EmeraldAudio
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.RoseVideo

@Composable
fun MediaEditorScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedEditorTab by remember { mutableIntStateOf(0) } // 0: Photo Editor, 1: Video Editor

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("media_editor_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. HERO HEADER ---
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.2.dp, CyanSecondary.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_photo_video_edit),
                        contentDescription = "Photo & Video Editor",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        contentScale = ContentScale.Crop
                    )

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

                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ModelBadge(
                                modelName = "Media Studio Pro",
                                modeTag = "Photo & Video FX",
                                accentColor = CyanSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "ইন-অ্যাপ ফটো ও ভিডিও এডিটর",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "রিয়েল-টাইম কালার গ্রেডিং, ব্রাইটনেস, কন্ট্রাস্ট, ফিল্টার, ক্রপ রেশিও, টেক্সট ওভারলে ও ভিডিও স্পিড/অডিও মিক্সিং।",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }
        }

        // --- 2. EDITOR MODE SELECTOR TABS ---
        item {
            TabRow(
                selectedTabIndex = selectedEditorTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = CyanSecondary,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
            ) {
                Tab(
                    selected = selectedEditorTab == 0,
                    onClick = { selectedEditorTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("ফটো এডিটর (Photo Editor)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                )
                Tab(
                    selected = selectedEditorTab == 1,
                    onClick = { selectedEditorTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MovieCreation, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("ভিডিও এডিটর (Video Editor)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                )
            }
        }

        // --- TAB CONTENT ---
        if (selectedEditorTab == 0) {
            // PHOTO EDITOR
            item {
                PhotoEditorSection(viewModel = viewModel)
            }
        } else {
            // VIDEO EDITOR
            item {
                VideoEditorSection(viewModel = viewModel)
            }
        }

        item { Spacer(modifier = Modifier.height(28.dp)) }
    }
}

@Composable
fun PhotoEditorSection(viewModel: MainViewModel) {
    val context = LocalContext.current

    val selectedImageRes by viewModel.selectedEditImageRes.collectAsState()
    val brightness by viewModel.photoBrightness.collectAsState()
    val contrast by viewModel.photoContrast.collectAsState()
    val saturation by viewModel.photoSaturation.collectAsState()
    val warmth by viewModel.photoWarmth.collectAsState()
    val activeFilter by viewModel.photoActiveFilter.collectAsState()
    val cropRatio by viewModel.photoCropRatio.collectAsState()
    val overlayText by viewModel.photoOverlayText.collectAsState()
    val textColor by viewModel.photoTextColor.collectAsState()
    val textPosition by viewModel.photoTextPosition.collectAsState()
    val textSize by viewModel.photoTextSize.collectAsState()
    val isSaved by viewModel.isPhotoEditSaved.collectAsState()

    val availableImages = listOf(
        R.drawable.img_veo_scene_1 to "Scene 1 (Dhaka)",
        R.drawable.img_veo_scene_2 to "Scene 2 (Robot)",
        R.drawable.img_veo_scene_3 to "Scene 3 (Portal)",
        R.drawable.img_veo_scene_4 to "Scene 4 (Aurora)",
        R.drawable.img_photo_video_edit to "Studio Workstation",
        R.drawable.img_hero_banner to "AI Nexus Banner"
    )

    val filterOptions = listOf(
        "NORMAL" to "Original",
        "CYBERPUNK" to "Cyberpunk Neon",
        "GOLDEN_HOUR" to "Golden Hour",
        "VIBRANT" to "Vibrant Pop",
        "VINTAGE" to "Vintage Film",
        "BW_NOIR" to "B&W Noir"
    )

    val cropRatios = listOf("1:1", "4:5", "9:16", "16:9")

    val textColors = listOf(
        Color.White to "White",
        CyanSecondary to "Cyan",
        AmberThinking to "Amber",
        RoseVideo to "Rose",
        Color(0xFFFBBF24) to "Yellow"
    )

    // Compute dynamic ColorMatrix for brightness, contrast, saturation, and warmth
    val colorMatrix = remember(brightness, contrast, saturation, warmth) {
        val cm = ColorMatrix()

        // Saturation
        cm.setToSaturation(saturation)

        // Contrast & Brightness scale
        val scale = contrast
        val translate = brightness * 2.55f

        val contrastMatrix = ColorMatrix(
            floatArrayOf(
                scale, 0f, 0f, 0f, translate,
                0f, scale, 0f, 0f, translate,
                0f, 0f, scale, 0f, translate,
                0f, 0f, 0f, 1f, 0f
            )
        )
        cm.timesAssign(contrastMatrix)

        // Warmth (tint red up, blue down)
        if (warmth != 0f) {
            val warmthMatrix = ColorMatrix(
                floatArrayOf(
                    1f + (warmth * 0.005f), 0f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f, 0f,
                    0f, 0f, 1f - (warmth * 0.005f), 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            cm.timesAssign(warmthMatrix)
        }

        cm
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // --- PHOTO CANVAS PREVIEW ---
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
                        text = "লাইভ ফটো প্রিভিউ (${cropRatio})",
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
                            text = "Filter: $activeFilter",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CyanSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Aspect ratio box
                val aspectFloat = when (cropRatio) {
                    "1:1" -> 1f / 1f
                    "4:5" -> 4f / 5f
                    "9:16" -> 9f / 16f
                    "16:9" -> 16f / 9f
                    else -> 1f
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Black)
                        .border(1.dp, Color(0xFF22272E), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(aspectFloat),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = selectedImageRes),
                            contentDescription = "Edited Image Canvas",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            colorFilter = ColorFilter.colorMatrix(colorMatrix)
                        )

                        // Render on-screen custom text overlay
                        if (overlayText.isNotBlank()) {
                            val textAlignment = when (textPosition) {
                                "TOP" -> Alignment.TopCenter
                                "CENTER" -> Alignment.Center
                                else -> Alignment.BottomCenter
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                contentAlignment = textAlignment
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0x88000000)
                                ) {
                                    Text(
                                        text = overlayText,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = textColor,
                                            fontSize = textSize.sp,
                                            textAlign = TextAlign.Center
                                        ),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- IMAGE SELECTOR CAROUSEL ---
        Column {
            Text(
                text = "ছবি নির্বাচন করুন (Select Image to Edit):",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(availableImages) { (resId, label) ->
                    val isSelected = selectedImageRes == resId
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            if (isSelected) 2.dp else 1.dp,
                            if (isSelected) CyanSecondary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier
                            .size(76.dp)
                            .clickable { viewModel.selectEditImage(resId) }
                    ) {
                        Image(
                            painter = painterResource(id = resId),
                            contentDescription = label,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        // --- FILTERS ROW ---
        Column {
            Text(
                text = "১-ক্লিক কালার গ্রেডিং ফিল্টার (Filters):",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filterOptions.forEach { (filterKey, label) ->
                    FilterChip(
                        selected = activeFilter == filterKey,
                        onClick = { viewModel.applyPhotoFilter(filterKey) },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CyanSecondary.copy(alpha = 0.25f),
                            selectedLabelColor = CyanSecondary
                        )
                    )
                }
            }
        }

        // --- ADJUSTMENT SLIDERS ---
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ম্যানুয়াল ফাইন অ্যাডজাস্টমেন্ট (Sliders)",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(onClick = { viewModel.resetPhotoAdjustments() }, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = Color.Gray, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Brightness
                SliderControl(
                    label = "Brightness (উজ্জ্বলতা): ${brightness.toInt()}",
                    value = brightness,
                    valueRange = -50f..50f,
                    onValueChange = { viewModel.setPhotoBrightness(it) }
                )

                // Contrast
                SliderControl(
                    label = "Contrast (কন্ট্রাস্ট): ${String.format("%.2f", contrast)}x",
                    value = contrast,
                    valueRange = 0.5f..2.0f,
                    onValueChange = { viewModel.setPhotoContrast(it) }
                )

                // Saturation
                SliderControl(
                    label = "Saturation (রঙের ঘনত্ব): ${String.format("%.2f", saturation)}x",
                    value = saturation,
                    valueRange = 0.0f..2.0f,
                    onValueChange = { viewModel.setPhotoSaturation(it) }
                )

                // Warmth
                SliderControl(
                    label = "Warmth (কালার টোন): ${warmth.toInt()}",
                    value = warmth,
                    valueRange = -50f..50f,
                    onValueChange = { viewModel.setPhotoWarmth(it) }
                )
            }
        }

        // --- ASPECT RATIO & TEXT OVERLAY ---
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ক্রপ রেশিও (Aspect Ratio):",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    cropRatios.forEach { ratio ->
                        FilterChip(
                            selected = cropRatio == ratio,
                            onClick = { viewModel.setPhotoCropRatio(ratio) },
                            label = { Text(ratio, style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AmberThinking.copy(alpha = 0.25f),
                                selectedLabelColor = AmberThinking
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "ছবির ওপর টেক্সট লিখুন (Overlay Text):",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = overlayText,
                    onValueChange = { viewModel.setPhotoOverlayText(it) },
                    placeholder = { Text("ছবির ওপর টেক্সট লিখুন...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyanSecondary)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Text Position & Color Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "পজিশন:",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("TOP", "CENTER", "BOTTOM").forEach { pos ->
                                FilterChip(
                                    selected = textPosition == pos,
                                    onClick = { viewModel.setPhotoTextPosition(pos) },
                                    label = { Text(pos, fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = IndigoPrimary.copy(alpha = 0.25f),
                                        selectedLabelColor = IndigoPrimary
                                    )
                                )
                            }
                        }
                    }

                    Column {
                        Text(
                            text = "টেক্সট কালার:",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            textColors.forEach { (color, name) ->
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(
                                            if (textColor == color) 2.dp else 1.dp,
                                            if (textColor == color) CyanSecondary else Color.Gray,
                                            CircleShape
                                        )
                                        .clickable { viewModel.setPhotoTextColor(color) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- SAVE & EXPORT ACTION BUTTONS ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { viewModel.saveEditedPhoto() },
                enabled = !isSaved,
                colors = ButtonDefaults.buttonColors(containerColor = CyanSecondary, contentColor = Color(0xFF041E26)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("save_edited_photo_button")
            ) {
                Icon(if (isSaved) Icons.Default.Check else Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isSaved) "সংরক্ষিত হয়েছে" else "Save Photo", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = {
                    val configText = """
                        🎨 Edited Photo Configuration
                        Filter: $activeFilter
                        Crop Ratio: $cropRatio
                        Brightness: ${brightness.toInt()}
                        Contrast: ${String.format("%.2f", contrast)}x
                        Saturation: ${String.format("%.2f", saturation)}x
                        Warmth: ${warmth.toInt()}
                        Overlay Text: "$overlayText" ($textPosition)
                    """.trimIndent()
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Photo Config", configText)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "ফটো কনফিগারেশন কপি হয়েছে!", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Copy Specs", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun VideoEditorSection(viewModel: MainViewModel) {
    val context = LocalContext.current

    val trimDuration by viewModel.videoTrimDuration.collectAsState()
    val speed by viewModel.videoSpeed.collectAsState()
    val lutFilter by viewModel.videoLutFilter.collectAsState()
    val subtitleText by viewModel.videoSubtitleText.collectAsState()
    val isSubtitlesEnabled by viewModel.videoIsSubtitlesEnabled.collectAsState()
    val musicVolume by viewModel.videoMusicVolume.collectAsState()
    val musicMood by viewModel.videoMusicMood.collectAsState()
    val isSaved by viewModel.isVideoEditSaved.collectAsState()

    var isPreviewPlaying by remember { mutableStateOf(false) }

    val luts = listOf(
        "TEAL_ORANGE" to "Teal & Orange",
        "CYBERPUNK" to "Cyberpunk Glow",
        "FILM_35MM" to "35mm Vintage",
        "MONO_NOIR" to "Monochrome Noir",
        "CLEAN" to "Clean High-Key"
    )

    val durations = listOf("5s", "10s", "15s", "20s", "30s")
    val speeds = listOf(0.5f to "0.5x Slow", 1.0f to "1.0x Normal", 1.5f to "1.5x Fast", 2.0f to "2.0x Hyper")
    val musicMoods = listOf("Cyberpunk Synthwave", "Epic Cinematic", "Chill Lo-Fi", "Acoustic Piano")

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // --- VIDEO EDITOR CANVAS ---
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117)),
            border = BorderStroke(1.2.dp, RoseVideo.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ভিডিও টাইমলাইন এডিটর ($trimDuration • ${speed}x)",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = RoseVideo
                        )
                    )

                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = RoseVideo.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "LUT: $lutFilter",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = RoseVideo,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
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
                        painter = painterResource(id = R.drawable.img_veo_scene_1),
                        contentDescription = "Video Edit Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Subtitle bar
                    if (isSubtitlesEnabled && subtitleText.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .background(Color(0xCC000000))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = subtitleText,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // Play icon overlay
                    IconButton(
                        onClick = { isPreviewPlaying = !isPreviewPlaying },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0x99000000))
                    ) {
                        Icon(
                            imageVector = if (isPreviewPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }

        // --- COLOR GRADING LUTS ---
        Column {
            Text(
                text = "কালার গ্রেডিং LUT (Cinematic Color Grading):",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                luts.forEach { (key, name) ->
                    FilterChip(
                        selected = lutFilter == key,
                        onClick = { viewModel.setVideoLutFilter(key) },
                        label = { Text(name, style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RoseVideo.copy(alpha = 0.25f),
                            selectedLabelColor = RoseVideo
                        )
                    )
                }
            }
        }

        // --- DURATION TRIM & SPEED SELECTOR ---
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ভিডিও ডিউরেশন ট্রিম (Target Duration):",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    durations.forEach { d ->
                        FilterChip(
                            selected = trimDuration == d,
                            onClick = { viewModel.setVideoTrimDuration(d) },
                            label = { Text(d, style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AmberThinking.copy(alpha = 0.25f),
                                selectedLabelColor = AmberThinking
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "প্লেব্যাক স্পিড (Playback Speed):",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    speeds.forEach { (spVal, label) ->
                        FilterChip(
                            selected = speed == spVal,
                            onClick = { viewModel.setVideoSpeed(spVal) },
                            label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyanSecondary.copy(alpha = 0.25f),
                                selectedLabelColor = CyanSecondary
                            )
                        )
                    }
                }
            }
        }

        // --- SUBTITLES & AUDIO MIXER ---
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "সাবটাইটেল ও ক্যাপশন (Subtitles):",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    FilterChip(
                        selected = isSubtitlesEnabled,
                        onClick = { viewModel.toggleVideoSubtitles() },
                        label = { Text(if (isSubtitlesEnabled) "ON" else "OFF", fontSize = 10.sp) }
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = subtitleText,
                    onValueChange = { viewModel.setVideoSubtitleText(it) },
                    placeholder = { Text("অন-স্ক্রিন সাবটাইটেল লিখুন...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseVideo)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "ব্যাকগ্রাউন্ড মিউজিক (BGM Mood):",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    musicMoods.forEach { mood ->
                        FilterChip(
                            selected = musicMood == mood,
                            onClick = { viewModel.setVideoMusicMood(mood) },
                            label = { Text(mood, style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldAudio.copy(alpha = 0.25f),
                                selectedLabelColor = EmeraldAudio
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Music Volume Slider
                SliderControl(
                    label = "মিউজিক ভলিউম: $musicVolume%",
                    value = musicVolume.toFloat(),
                    valueRange = 0f..100f,
                    onValueChange = { viewModel.setVideoMusicVolume(it.toInt()) }
                )
            }
        }

        // --- SAVE & EXPORT VIDEO PROJECT ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { viewModel.saveEditedVideoProject() },
                enabled = !isSaved,
                colors = ButtonDefaults.buttonColors(containerColor = RoseVideo, contentColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("save_edited_video_button")
            ) {
                Icon(if (isSaved) Icons.Default.Check else Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isSaved) "সংরক্ষিত হয়েছে" else "Save Video Specs", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = {
                    val exportText = """
                        🎬 Video Editing Specifications
                        Duration: $trimDuration
                        Speed: ${speed}x
                        LUT Filter: $lutFilter
                        Subtitles: "$subtitleText" (Active: $isSubtitlesEnabled)
                        BGM Mood: $musicMood (Volume: $musicVolume%)
                    """.trimIndent()
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Video Specs", exportText)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "ভিডিও স্পেসিফিকেশন কপি হয়েছে!", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Copy Specs", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SliderControl(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = CyanSecondary,
                activeTrackColor = CyanSecondary,
                inactiveTrackColor = Color(0xFF263238)
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
