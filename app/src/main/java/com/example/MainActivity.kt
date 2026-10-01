package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Cloud
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.screens.BengaliChatScreen
import com.example.ui.screens.FreeHostingScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.MediaEditorScreen
import com.example.ui.screens.ShortsGeneratorScreen
import com.example.ui.screens.ThumbnailGeneratorScreen
import com.example.ui.screens.Veo3VideoStudioScreen
import com.example.ui.theme.AmberThinking
import com.example.ui.theme.CyanSecondary
import com.example.ui.theme.EmeraldAudio
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RoseVideo

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val isDarkMode by viewModel.isDarkMode.collectAsState()

            MyApplicationTheme(darkTheme = isDarkMode) {
                SarifulAiAgentApp(viewModel = viewModel, isDarkMode = isDarkMode)
            }
        }
    }
}

data class NavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector,
    val testTag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SarifulAiAgentApp(
    viewModel: MainViewModel,
    isDarkMode: Boolean
) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    // Handle back button on secondary screens
    if (currentScreen != Screen.CHAT) {
        BackHandler {
            viewModel.navigateTo(Screen.CHAT)
        }
    }

    val navItems = listOf(
        NavItem(Screen.CHAT, "চ্যাট", Icons.Default.Chat, "nav_item_chat"),
        NavItem(Screen.VEO_STUDIO, "ভিডিও ও ফটো", Icons.Default.MovieCreation, "nav_item_veo"),
        NavItem(Screen.MEDIA_EDITOR, "এডিটর", Icons.Default.Brush, "nav_item_editor"),
        NavItem(Screen.FREE_HOSTING, "হোস্টিং", Icons.Default.Cloud, "nav_item_hosting"),
        NavItem(Screen.HISTORY, "সংরক্ষিত", Icons.Default.Bookmark, "nav_item_history")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_sharif_profile),
                            contentDescription = "Sharif AI Tech Logo",
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .border(1.2.dp, CyanSecondary, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentScreen == Screen.CHAT) "Sariful AI Agent" else currentScreen.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.4.sp
                            )
                        )
                    }
                },
                navigationIcon = {
                    if (currentScreen != Screen.CHAT) {
                        IconButton(
                            onClick = { viewModel.navigateTo(Screen.CHAT) },
                            modifier = Modifier.testTag("top_bar_back_button")
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to Chat"
                            )
                        }
                    }
                },
                actions = {
                    // Dark / Light Mode Toggle Button
                    IconButton(
                        onClick = { viewModel.toggleTheme() },
                        modifier = Modifier.testTag("theme_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                            tint = if (isDarkMode) AmberThinking else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.testTag("bottom_navigation_bar")
            ) {
                navItems.forEach { item ->
                    val isSelected = currentScreen == item.screen
                    val activeColor = when (item.screen) {
                        Screen.CHAT -> CyanSecondary
                        Screen.VEO_STUDIO -> RoseVideo
                        Screen.MEDIA_EDITOR -> CyanSecondary
                        Screen.FREE_HOSTING -> EmeraldAudio
                        Screen.HISTORY -> IndigoPrimary
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.navigateTo(item.screen) },
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = activeColor,
                            selectedTextColor = activeColor,
                            indicatorColor = activeColor.copy(alpha = 0.2f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier.testTag(item.testTag)
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Crossfade(
            targetState = currentScreen,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            label = "screenTransition"
        ) { screen ->
            when (screen) {
                Screen.CHAT -> BengaliChatScreen(viewModel = viewModel)
                Screen.VEO_STUDIO -> Veo3VideoStudioScreen(viewModel = viewModel)
                Screen.MEDIA_EDITOR -> MediaEditorScreen(viewModel = viewModel)
                Screen.FREE_HOSTING -> FreeHostingScreen(viewModel = viewModel)
                Screen.HISTORY -> HistoryScreen(viewModel = viewModel)
            }
        }
    }
}
