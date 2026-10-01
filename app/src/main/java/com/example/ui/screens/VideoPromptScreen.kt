package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.components.FormattedContentCard
import com.example.ui.components.ModelBadge
import com.example.ui.theme.AmberThinking
import com.example.ui.theme.CyanSecondary
import com.example.ui.theme.IndigoPrimary

@Composable
fun VideoPromptScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val storyline by viewModel.videoStoryline.collectAsState()
    val aiEngine by viewModel.videoEngine.collectAsState()
    val sceneCount by viewModel.videoSceneCount.collectAsState()
    val isLoading by viewModel.isVideoPromptLoading.collectAsState()
    val resultText by viewModel.videoPromptResult.collectAsState()
    val errorMessage by viewModel.videoPromptError.collectAsState()
    val isSaved by viewModel.isVideoPromptSaved.collectAsState()

    val aiEngineOptions = listOf("Veo 2", "Runway Gen-3", "Sora", "Luma Dream Machine", "Midjourney Video")

    val storyPresets = listOf(
        "ভবিষ্যতের সাইবারপাঙ্ক ঢাকা সিটিতে উড়ন্ত গাড়ি ও নিয়ন ড্রোন",
        "একটি রোবটের সাথে ছোট্ট শিশুর পাহাড়ি উপত্যকায় বন্ধুত্ব",
        "অতল সমুদ্রের নিচে আলোকিত রহস্যময় প্রাণী ও প্রাচীন ধ্বংসাবশেষ",
        "টাইম ট্রাভেলার যখন প্রাচীন মিশরের পিরামিড তৈরি দেখতে পায়"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("video_prompt_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, AmberThinking.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ModelBadge(
                            modelName = "Scene Prompt Engine",
                            modeTag = "Veo / Runway / Sora",
                            accentColor = AmberThinking
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "সিন-বাই-সিন ভিডিও প্রম্পট জেনারেটর",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "যেকোনো গল্পের আইডিয়া থেকে তৈরি করুন সিনেমাটিক ক্যামেরা অ্যাঙ্গেল, লাইটিং ও মোশন সহ এআই ভিডিও জেনারেটরের জন্য পারফেক্ট প্রম্পট।",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }

        // Inputs Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ভিডিওর গল্প বা থিম (Storyline / Concept)",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = storyline,
                        onValueChange = { viewModel.setVideoStoryline(it) },
                        placeholder = { Text("আপনার ভিডিওর মূল গল্প বা সিন এখানে লিখুন...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("video_storyline_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AmberThinking)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Preset chips
                    Text(
                        text = "উদাহরণ ধারণা:",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        storyPresets.forEach { preset ->
                            Surface(
                                shape = RoundedCornerShape(100.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, AmberThinking.copy(alpha = 0.25f)),
                                modifier = Modifier.clickable { viewModel.setVideoStoryline(preset) }
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

                    // Target AI Engine Chips
                    Text(
                        text = "ভিডিও এআই টুল (Target AI Engine)",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        aiEngineOptions.forEach { engine ->
                            FilterChip(
                                selected = aiEngine == engine,
                                onClick = { viewModel.setVideoEngine(engine) },
                                label = { Text(engine, style = MaterialTheme.typography.labelSmall) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AmberThinking.copy(alpha = 0.25f),
                                    selectedLabelColor = AmberThinking
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Scene count selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "সিনের সংখ্যা (Scene Count):",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(3, 4, 6).forEach { count ->
                                FilterChip(
                                    selected = sceneCount == count,
                                    onClick = { viewModel.setVideoSceneCount(count) },
                                    label = { Text("$count Scenes", style = MaterialTheme.typography.labelSmall) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = AmberThinking.copy(alpha = 0.25f),
                                        selectedLabelColor = AmberThinking
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Generate Button
                    Button(
                        onClick = { viewModel.generateScenePrompts() },
                        enabled = storyline.isNotBlank() && !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = AmberThinking, contentColor = Color(0xFF271302)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("generate_scene_prompts_button")
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color(0xFF271302), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("প্রম্পট তৈরি হচ্ছে...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.MovieCreation, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate Scene Prompts", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Error Banner
        if (errorMessage != null) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF450A0A)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDC2626)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = Color(0xFFFCA5A5))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = errorMessage ?: "Error", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFFECACA)))
                    }
                }
            }
        }

        // Results Card with Copy Action Buttons
        if (resultText != null) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "সিন-বাই-সিন প্রম্পট গাইড",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = AmberThinking)
                        )
                        ModelBadge(modelName = aiEngine, accentColor = AmberThinking)
                    }

                    FormattedContentCard(content = resultText!!, accentColor = AmberThinking)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Video Prompts", resultText!!)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "সবগুলো প্রম্পট কপি হয়েছে! (Copied)", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AmberThinking, contentColor = Color(0xFF271302)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("copy_all_video_prompts_button")
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copy All Prompts", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { viewModel.saveVideoPromptResult() },
                            enabled = !isSaved,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("save_video_prompts_button")
                        ) {
                            Icon(if (isSaved) Icons.Default.Check else Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isSaved) "Saved" else "Save Prompts")
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(28.dp)) }
    }
}
