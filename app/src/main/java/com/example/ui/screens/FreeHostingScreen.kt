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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
fun FreeHostingScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedHostingTab by remember { mutableIntStateOf(0) }

    val siteType by viewModel.hostingSiteType.collectAsState()
    val creatorName by viewModel.hostingCreatorName.collectAsState()
    val features by viewModel.hostingFeatures.collectAsState()
    val isCodeLoading by viewModel.isHostingCodeLoading.collectAsState()
    val generatedCode by viewModel.hostingGeneratedCode.collectAsState()
    val errorMessage by viewModel.hostingError.collectAsState()
    val isSaved by viewModel.isHostingSaved.collectAsState()

    val siteTypePresets = listOf(
        "AI Video & Photo Portfolio",
        "YouTube Creator Landing Page",
        "Personal Digital Resume",
        "Creative AI Art Showcase"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("free_hosting_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. HERO BANNER ---
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.2.dp, EmeraldAudio.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_free_hosting),
                        contentDescription = "Free Domain & Hosting",
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
                                modelName = "Cloud Engine",
                                modeTag = "100% Free Hosting",
                                accentColor = EmeraldAudio
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "১০০% ফ্রি ডোমেইন ও হোস্টিং হাব",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "আপনার এআই ভিডিও, ফটো গ্যালারি বা ক্রিয়েটর পোর্টফোলিওর জন্য লাইফটাইম ফ্রি ডোমেইন ও হোস্টিং সেটআপ এবং ১-ক্লিক ওয়েবসাইট কোড জেনারেটর।",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }
        }

        // --- 2. SUB-TABS SELECTOR ---
        item {
            TabRow(
                selectedTabIndex = selectedHostingTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = EmeraldAudio,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
            ) {
                listOf(
                    "ফ্রি হোস্টিং" to Icons.Default.Cloud,
                    "ফ্রি ডোমেইন" to Icons.Default.Language,
                    "এআই সাইট কোড" to Icons.Default.Code,
                    "ডিপ্লয় গাইড" to Icons.Default.HelpOutline
                ).forEachIndexed { index, (label, icon) ->
                    Tab(
                        selected = selectedHostingTab == index,
                        onClick = { selectedHostingTab = index },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(label, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    )
                }
            }
        }

        // --- 3. TAB CONTENT ---
        when (selectedHostingTab) {
            0 -> {
                // FREE HOSTING PROVIDERS
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = "🚀 সেরা ৫টি ১০০% আজীবন ফ্রি ক্লাউড হোস্টিং প্রোভাইডার",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldAudio)
                        )

                        FreeHostingProviderCard(
                            name = "Firebase Hosting (Google Cloud)",
                            badge = "Google Official • Recommended",
                            accentColor = AmberThinking,
                            features = listOf(
                                "১০ জিবি ফ্রি স্টোরেজ ও ফ্রি এসএসডি ব্যান্ডউইথ",
                                "গুগলের আল্ট্রা-ফাস্ট গ্লোবাল CDN",
                                "সম্পূর্ণ ফ্রি কাস্টম ডোমেইন ও অটোমেটিক ফ্রি SSL",
                                "Google CLI দিয়ে মাত্র ১ কমান্ডে ডিপ্লয়"
                            ),
                            setupCommand = "npm install -g firebase-tools\nfirebase login\nfirebase init hosting\nfirebase deploy"
                        )

                        FreeHostingProviderCard(
                            name = "Vercel Cloud",
                            badge = "Unlimited Sites • Ultra Fast",
                            accentColor = CyanSecondary,
                            features = listOf(
                                "অসংখ্য ফ্রি ওয়েবসাইট ও প্রজেক্ট হোস্টিং",
                                "GitHub এর সাথে অটোমেটিক কানেক্ট ও লাইভ ডিপ্লয়",
                                "ফ্রি *.vercel.app ডোমেইন + কাস্টম ডোমেইন সাপোর্ট",
                                "Next.js, HTML5, React ও ভিডিও স্ট্রিমিং সাপোর্ট"
                            ),
                            setupCommand = "npx vercel"
                        )

                        FreeHostingProviderCard(
                            name = "Cloudflare Pages",
                            badge = "Unlimited Bandwidth • Zero Cost",
                            accentColor = EmeraldAudio,
                            features = listOf(
                                "সম্পূর্ণ আনলিমিটেড ব্যান্ডউইথ (Unlimited Bandwidth)",
                                "বিশ্বখ্যাত Cloudflare DDoS প্রটেকশন",
                                "ফ্রি *.pages.dev সাবডোমেইন ও আনলিমিটেড কাস্টম ডোমেইন",
                                "এআই ভিডিও ও ইমেজ লোড করার জন্য সুপার ফাস্ট"
                            ),
                            setupCommand = "npx wrangler pages deploy dist"
                        )

                        FreeHostingProviderCard(
                            name = "GitHub Pages",
                            badge = "100% Free Forever",
                            accentColor = IndigoPrimary,
                            features = listOf(
                                "সরাসরি আপনার GitHub রিপোজিটরি থেকে ফ্রি হোস্টিং",
                                "ফ্রি username.github.io ডোমেইন",
                                "কোনো ক্রেডিট কার্ড বা সাবস্ক্রিপশন লাগবে না"
                            ),
                            setupCommand = "git push origin main"
                        )
                    }
                }
            }
            1 -> {
                // FREE DOMAINS
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = "🌐 কীভাবে ১০০% ফ্রিতে কাস্টম ডোমেইন পাবেন?",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = CyanSecondary)
                        )

                        FreeDomainCard(
                            domainExtension = ".is-a.dev",
                            title = "ফ্রি ডেভ/ক্রিয়েটর কাস্টম ডোমেইন (is-a.dev)",
                            description = "ক্রিয়েটর ও ডেভেলপারদের জন্য বিশ্বব্যাপী জনপ্রিয় ১০০% ফ্রি ওপেন-সোর্স ডোমেইন। আপনার পছন্দের নাম (যেমন: sharif.is-a.dev) সম্পূর্ণ ফ্রিতে পাবেন।",
                            steps = listOf(
                                "is-a.dev এর অফিসিয়াল GitHub রিপোজিটরিতে যান",
                                "domains ফোল্ডারে আপনার কাঙ্ক্ষিত নামের JSON ফাইল তৈরি করুন",
                                "আপনার Vercel বা GitHub Pages এর CNAME বা IP দিন = ২৪ ঘণ্টায় ডোমেইন লাইভ!"
                            )
                        )

                        FreeDomainCard(
                            domainExtension = "username.vercel.app",
                            title = "Vercel প্রফেশনাল ফ্রি সাবডোমেইন",
                            description = "কোনো ডোমেইন না কিনেও সরাসরি Vercel থেকে আপনার সাইটের জন্য স্মার্ট ও প্রফেশনাল লিংক তৈরি করুন (যেমন: sharif-ai.vercel.app)।",
                            steps = listOf(
                                "Vercel ড্যাশবোর্ডে প্রজেক্ট সেটিংসে যান",
                                "Domains অপশনে গিয়ে আপনার পছন্দের নাম লিখুন",
                                "১ সেকেন্ডে ইনস্ট্যান্ট লাইভ এবং অটোমেটিক SSL সার্টিফিকেট যুক্ত হয়ে যাবে!"
                            )
                        )

                        FreeDomainCard(
                            domainExtension = "Cloudflare Free DNS & SSL",
                            title = "ক্লাউডফ্লেয়ার ফ্রি SSL ও সিকিউরিটি",
                            description = "যেকোনো ফ্রি বা পেইড ডোমেইনে আজীবন ফ্রি SSL (https://), ফায়ারওয়াল এবং গতি বৃদ্ধির জন্য Cloudflare সম্পূর্ণ ফ্রি প্ল্যান ব্যবহার করুন।",
                            steps = listOf(
                                "Cloudflare.com এ ফ্রি একাউন্ট খুলুন",
                                "ডোমেইনের নেমসার্ভার Cloudflare এ পয়েন্ট করুন",
                                "ইনস্ট্যান্ট ফ্রি SSL এবং রকেট স্পিড অ্যাক্টিভ হয়ে যাবে!"
                            )
                        )
                    }
                }
            }
            2 -> {
                // 1-CLICK AI WEBSITE CODE GENERATOR
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "১-ক্লিক এআই ওয়েবসাইট কোড জেনারেটর (Gemini AI)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldAudio)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "ফ্রি হোস্টিংয়ে সরাসরি আপলোড করার জন্য সম্পূর্ণ রেডি HTML5 + Tailwind CSS ওয়েবসাইট কোড জেনারেট করুন।",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "সাইটের ক্যাটাগরি (Website Type):",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                siteTypePresets.forEach { preset ->
                                    FilterChip(
                                        selected = siteType == preset,
                                        onClick = { viewModel.setHostingSiteType(preset) },
                                        label = { Text(preset, style = MaterialTheme.typography.labelSmall) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = EmeraldAudio.copy(alpha = 0.25f),
                                            selectedLabelColor = EmeraldAudio
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "আপনার নাম বা ব্র্যান্ডের নাম (Creator / Brand Name):",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = creatorName,
                                onValueChange = { viewModel.setHostingCreatorName(it) },
                                placeholder = { Text("যেমন: Sharif AI Tech...") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldAudio)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "সাইটের প্রধান ফিচার বা বিবরণ (Key Features):",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = features,
                                onValueChange = { viewModel.setHostingFeatures(it) },
                                placeholder = { Text("সাইটের হাইলাইটস...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(85.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldAudio)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { viewModel.generateWebsiteCode() },
                                enabled = siteType.isNotBlank() && !isCodeLoading,
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldAudio, contentColor = Color(0xFF022416)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("generate_website_code_button")
                            ) {
                                if (isCodeLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color(0xFF022416), strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("ওয়েবসাইট কোড লেখা হচ্ছে...", fontWeight = FontWeight.Bold)
                                } else {
                                    Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Generate Free Website Code", fontWeight = FontWeight.Bold)
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
                            border = BorderStroke(1.dp, Color(0xFFDC2626)),
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

                // Generated Code Display
                if (generatedCode != null) {
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117)),
                            border = BorderStroke(1.2.dp, EmeraldAudio.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "index.html (Tailwind CSS Ready)",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EmeraldAudio)
                                    )

                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Button(
                                            onClick = {
                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                val clip = ClipData.newPlainText("Website Code", generatedCode!!)
                                                clipboard.setPrimaryClip(clip)
                                                Toast.makeText(context, "সম্পূর্ণ কোড কপি হয়েছে! (index.html ফাইলে সেভ করুন)", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldAudio, contentColor = Color(0xFF022416)),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.height(34.dp)
                                        ) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Copy Code", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        OutlinedButton(
                                            onClick = { viewModel.saveWebsiteCode() },
                                            enabled = !isSaved,
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.height(34.dp)
                                        ) {
                                            Icon(if (isSaved) Icons.Default.Check else Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(if (isSaved) "Saved" else "Save", fontSize = 11.sp)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFF161B22),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp)
                                ) {
                                    LazyColumn(modifier = Modifier.padding(10.dp)) {
                                        item {
                                            Text(
                                                text = generatedCode!!,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontFamily = FontFamily.Monospace,
                                                    fontSize = 11.sp,
                                                    color = Color(0xFFC9D1D9),
                                                    lineHeight = 16.sp
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            3 -> {
                // DEPLOYMENT GUIDE IN BENGALI
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null, tint = EmeraldAudio)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "৪টি সহজ ধাপে সম্পূর্ণ ফ্রিতে ওয়েবসাইট লাইভ করার নিয়ম",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldAudio)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            StepItem(
                                stepNumber = "১",
                                title = "কোড সেভ করুন (index.html)",
                                description = "উপরের 'এআই সাইট কোড' ট্যাব থেকে জেনারেট হওয়া কোড কপি করে আপনার কম্পিউটারে বা মোবাইলে 'index.html' নামে একটি ফাইলে পেস্ট করে সেভ করুন।"
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            StepItem(
                                stepNumber = "২",
                                title = "GitHub এ আপলোড করুন",
                                description = "GitHub.com এ ফ্রি একাউন্ট খুলে একটি নতুন 'Repository' তৈরি করুন এবং 'index.html' ফাইলটি ড্র্যাগ অ্যান্ড ড্রপ করে আপলোড করে দিন।"
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            StepItem(
                                stepNumber = "৩",
                                title = "Vercel বা GitHub Pages এ কানেক্ট করুন",
                                description = "Vercel.com এ গিয়ে 'Continue with GitHub' দিন এবং আপনার রিপোজিটরিটি সিলেক্ট করে 'Deploy' চাপুন। মাত্র ৬০ সেকেন্ডের মধ্যে আপনার ওয়েবসাইট বিশ্বজুড়ে লাইভ হয়ে যাবে!"
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            StepItem(
                                stepNumber = "৪",
                                title = "ফ্রি কাস্টম ডোমেইন যুক্ত করুন",
                                description = "Vercel প্রজেক্ট সেটিংসে গিয়ে 'Domains' এ আপনার পছন্দের নাম বা 'is-a.dev' এর ফ্রি ডোমেইন যুক্ত করে দিন। সম্পূর্ণ আজীবন ফ্রি হোস্টিং ও ফ্রি SSL সিকিউরিটি রেডি!"
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(28.dp)) }
    }
}

@Composable
fun FreeHostingProviderCard(
    name: String,
    badge: String,
    accentColor: Color,
    features: List<String>,
    setupCommand: String
) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )

                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = accentColor.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall.copy(color = accentColor, fontWeight = FontWeight.Bold, fontSize = 9.5.sp),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            features.forEach { feature ->
                Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = accentColor, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = feature, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF0D1117),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = setupCommand,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = CyanSecondary
                        )
                    )

                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Setup Command", setupCommand)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "কমান্ড কপি হয়েছে!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy command", tint = Color.Gray, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FreeDomainCard(
    domainExtension: String,
    title: String,
    description: String,
    steps: List<String>
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, CyanSecondary.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = CyanSecondary.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = domainExtension,
                        style = MaterialTheme.typography.labelSmall.copy(color = CyanSecondary, fontWeight = FontWeight.Bold, fontSize = 10.sp),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.5.sp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "কীভাবে সেটআপ করবেন:",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(4.dp))

            steps.forEachIndexed { index, step ->
                Text(
                    text = "${index + 1}. $step",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                )
            }
        }
    }
}

@Composable
fun StepItem(stepNumber: String, title: String, description: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Surface(
            shape = CircleShape,
            color = EmeraldAudio,
            contentColor = Color(0xFF022416),
            modifier = Modifier.size(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = stepNumber, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = description, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
        }
    }
}
