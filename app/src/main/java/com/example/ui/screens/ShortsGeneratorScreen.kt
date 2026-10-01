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
import androidx.compose.material.icons.filled.OndemandVideo
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
import com.example.ui.theme.CyanSecondary
import com.example.ui.theme.EmeraldAudio
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.RoseVideo

@Composable
fun ShortsGeneratorScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val topic by viewModel.shortsTopic.collectAsState()
    val tone by viewModel.shortsTone.collectAsState()
    val duration by viewModel.shortsDuration.collectAsState()
    val language by viewModel.shortsLanguage.collectAsState()
    val isLoading by viewModel.isShortsLoading.collectAsState()
    val resultText by viewModel.shortsResult.collectAsState()
    val errorMessage by viewModel.shortsError.collectAsState()
    val isSaved by viewModel.isShortsSaved.collectAsState()

    val topicPresets = listOf(
        "৫টি সেরা ফ্রি AI টুলস যা সবার জানা দরকার",
        "ChatGPT দিয়ে ঘরে বসে আয় করার ৩টি উপায়",
        "স্মার্টফোনের ৫টি অজানা গোপন ট্রিকস",
        "মহাকাশের সবচেয়ে রহস্যময় ৩টি ঘটনা"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("shorts_generator_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, RoseVideo.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ModelBadge(
                            modelName = "YouTube Shorts Creator",
                            modeTag = "Viral Retention",
                            accentColor = RoseVideo
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "YouTube Shorts স্ক্রিপ্ট জেনারেটর",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "মাত্র কয়েক সেকেন্ডে তৈরি করুন হাই-রিটেনশন ৩-সেকেন্ড হুক, সিন-বাই-সিন ভিজ্যুয়াল ডিরেকশন এবং সম্পূর্ণ ভয়েসওভার স্ক্রিপ্ট।",
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
                        text = "ভিডিও টপিক বা ধারণা (Topic)",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { viewModel.setShortsTopic(it) },
                        placeholder = { Text("যেমন: কৃত্রিম বুদ্ধিমত্তার ভবিষ্যৎ...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("shorts_topic_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseVideo)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Preset chips
                    Text(
                        text = "জনপ্রিয় উদাহরণ:",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        topicPresets.forEach { preset ->
                            Surface(
                                shape = RoundedCornerShape(100.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, RoseVideo.copy(alpha = 0.25f)),
                                modifier = Modifier.clickable { viewModel.setShortsTopic(preset) }
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

                    // Duration & Language selectors
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "সময়কাল (Duration)",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("30 Seconds", "60 Seconds").forEach { dur ->
                                    FilterChip(
                                        selected = duration == dur,
                                        onClick = { viewModel.setShortsDuration(dur) },
                                        label = { Text(dur, style = MaterialTheme.typography.labelSmall) },
                                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = RoseVideo.copy(alpha = 0.25f), selectedLabelColor = RoseVideo)
                                    )
                                }
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ভাষা (Language)",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("Bengali (বাংলা)", "English").forEach { lang ->
                                    FilterChip(
                                        selected = language == lang,
                                        onClick = { viewModel.setShortsLanguage(lang) },
                                        label = { Text(lang.split(" ")[0], style = MaterialTheme.typography.labelSmall) },
                                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = RoseVideo.copy(alpha = 0.25f), selectedLabelColor = RoseVideo)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Generate Button
                    Button(
                        onClick = { viewModel.generateShortsScript() },
                        enabled = topic.isNotBlank() && !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = RoseVideo, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("generate_shorts_button")
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("স্ক্রিপ্ট তৈরি হচ্ছে...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate Shorts Script", fontWeight = FontWeight.Bold)
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

        // Results Card with Copy Buttons
        if (resultText != null) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "সম্পূর্ণ স্ক্রিপ্ট আউটপুট",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = RoseVideo)
                        )
                        ModelBadge(modelName = "gemini-3.5-flash", accentColor = RoseVideo)
                    }

                    FormattedContentCard(content = resultText!!, accentColor = RoseVideo)

                    // Action Copy Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Shorts Script", resultText!!)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "সম্পূর্ণ স্ক্রিপ্ট কপি হয়েছে! (Copied)", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RoseVideo),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("copy_shorts_script_button")
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy Script", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copy Script")
                        }

                        OutlinedButton(
                            onClick = { viewModel.saveShortsResult() },
                            enabled = !isSaved,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("save_shorts_script_button")
                        ) {
                            Icon(if (isSaved) Icons.Default.Check else Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isSaved) "Saved" else "Save Script")
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(28.dp)) }
    }
}
