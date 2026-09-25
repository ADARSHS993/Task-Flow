package com.example.taskflow.presentation.profile


import androidx.activity.compose.rememberLauncherForActivityResult
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.taskflow.presentation.Theme.ThemeViewModel
import com.example.taskflow.presentation.components.ProfileAvatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onMenuClick: () -> Unit = {},
    onLogout: () -> Unit,
    themeViewModel: ThemeViewModel,
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val photoUrl by viewModel.photoUrl.collectAsStateWithLifecycle()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        uri?.let {
            viewModel.uploadProfileImage(it)
        }
    }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    var notificationsEnabled by remember {
        mutableStateOf(true)
    }

    val darkModeEnabled by themeViewModel.isDarkMode.collectAsStateWithLifecycle()

    var showEditDialog by remember {
        mutableStateOf(false)
    }

    var expandedSection by remember {
        mutableStateOf<String?>(null)
    }

    val profile = state.profile

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                navigationIcon = {

                    IconButton(
                        onClick = onMenuClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
                title = {

                    Text(
                        text = "TaskFlow",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = Color(0xFF3427B8)
                    )

                },
                actions = {
                        ProfileAvatar(
                            photoUrl = photoUrl,
                            modifier = Modifier.size(34.dp)
                        )
                },
            )

        }
    ) { innerpadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 6.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.size(4.dp))

            Box(
                modifier = Modifier
                    .size(92.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (!photoUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(52.dp),
                            tint = Color.Gray
                        )
                    }
                }

                //Edit Button
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4433D7))
                        .align(Alignment.BottomEnd)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            //Name
            Text(
                text = profile?.name
                    ?.takeIf { it.isNotBlank() }
                    ?: "TaskFlow User",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            //Email
            Text(
                text = profile?.email ?: "",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Edit Profile",
                fontSize = 11.sp,
                color = Color(0xFF4433D7),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    showEditDialog = true
                }
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            //STATISTICS

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Tasks Completed

                Card(
                    modifier = Modifier
                        .weight(1.15f)
                        .height(104.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF5145E5)
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {


                            Icon(
                                imageVector =
                                    Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(
                                modifier = Modifier.width(5.dp)
                            )

                            Text(
                                text = "Tasks Completed",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.surface
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "124",
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.surface
                        )

                        Text(
                            text = "Tasks Completed",
                            fontSize = 9.sp,
                            color = Color.White.copy(
                                alpha = 0.8f
                            )
                        )
                    }
                }

                // Top 5%

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            Color(0xFF55D7EF)
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = Color(0xFF263C65),
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(
                            modifier =
                                Modifier.width(10.dp)
                        )

                        Text(
                            text = "Top 5% user",
                            fontSize = 10.sp,
                            color = Color(0xFF263C65)
                        )

                    }

                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            //SETTINGS

            Text(
                text = "SETTINGS",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 3.dp,
                        bottom = 8.dp
                    ),
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF3525D1)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    contentColor = MaterialTheme.colorScheme.surface
                )
            ) {
                //Dark Mode

                SettingSwitchRow(
                    icon = Icons.Default.DarkMode,
                    title = "Dark Mode",
                    checked = darkModeEnabled,
                    onCheckedChange = {
                        themeViewModel.setDarkMode(it)
                    }
                )

                SettingDivider()

                //Notification

                SettingRow(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    onClick = {
                        // Open notification settings
                    }
                )

                SettingDivider()

                //Privacy

                SettingExpandableRow(
                    icon = Icons.Default.Lock,
                    title = "Privacy Policy",
                    expanded = expandedSection == "privacy",
                    onClick = {
                        expandedSection =
                            if (expandedSection == "privacy") null else "privacy"
                    }
                )

                AnimatedVisibility(
                    visible = expandedSection == "privacy"
                ) {
                    InfoContentCard(
                        title = "Privacy Policy",
                        paragraphs = listOf(
                            "TaskFlow is designed to help you manage tasks, projects, categories, calendar activities, and productivity information.",
                            "TaskFlow uses Firebase Authentication to allow users to create an account and securely sign in to the application.",
                            "Task and project information may be stored locally on your device and synchronized with Firebase services when cloud synchronization is enabled.",
                            "Profile pictures are uploaded to Firebase Storage and the corresponding image URL is stored with your profile information.",
                            "Your information is used to provide the features of TaskFlow and is not displayed publicly through the application.",
                            "You should update this policy before publishing the application to accurately describe your final data collection, storage, analytics, and third-party services."
                        )
                    )
                }

                SettingDivider()

                // About

                SettingExpandableRow(
                    icon = Icons.Default.Info,
                    title = "About TaskFlow",
                    expanded = expandedSection == "about",
                    onClick = {
                        expandedSection =
                            if (expandedSection == "about") null else "about"
                    }
                )

                AnimatedVisibility(
                    visible = expandedSection == "about"
                ) {
                    InfoContentCard(
                        title = "About TaskFlow",
                        paragraphs = listOf(
                            "TaskFlow is a productivity and task management application designed to help users organize their daily work.",
                            "You can create and manage tasks, organize tasks into projects and categories, set priorities, and add due dates.",
                            "TaskFlow also provides calendar-based task management and productivity statistics to help you understand your progress.",
                            "The application supports user authentication and cloud synchronization so that important application data can be associated with your account.",
                            "TaskFlow is built with modern Android technologies including Kotlin and Jetpack Compose."
                        )
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            //Logout

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clickable {
                        showLogoutDialog = true
                    },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFE7E7)
                )
            ) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "↪  Logout ${profile?.name ?: ""}",
                        fontSize = 11.sp,
                        color = Color(0xFFE24A4A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    //Logout Dialog

    if (showLogoutDialog) {

        AlertDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },
            title = {
                Text("Logout")
            },
            text = {
                Text(
                    "Are you sure you want to logout?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.logout()
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {

                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showEditDialog) {

        EditProfileDialog(
            currentName = profile?.name ?: "",

            onDismiss = {
                showEditDialog = false
            },

            onSave = { newName ->

                viewModel.updateProfile(newName)

                showEditDialog = false
            }
        )
    }
}

@Composable
fun SettingExpandableRow(icon: ImageVector, title: String, expanded: Boolean, onClick: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "arrowRotation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable {
                onClick()
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(17.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Icon(
            imageVector = Icons.Default.ExpandMore,
            contentDescription = if (expanded) {
                "Collapse"
            } else {
                "Expand"
            },
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(22.dp)
                .rotate(rotation)
        )
    }
}

@Composable
fun InfoContentCard(title: String, paragraphs: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant
            )
            .padding(
                horizontal = 16.dp,
                vertical = 14.dp
            )
    ) {

        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        paragraphs.forEach { paragraph ->

            Text(
                text = paragraph,
                fontSize = 11.sp,
                lineHeight = 17.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(
                    bottom = 10.dp
                )
            )
        }
    }
}

fun onClick() {
    TODO("Not yet implemented")
}

@Composable
private fun EditProfileDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {

    var name by remember(currentName) {
        mutableStateOf(currentName)
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(
                text = "Edit Profile",
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = {
                    Text("Name")
                },
                placeholder = {
                    Text("Enter your name")
                }
            )
        },

        confirmButton = {

            Button(
                onClick = {

                    val newName = name.trim()

                    if (newName.isNotEmpty()) {
                        onSave(newName)
                    }
                },

                enabled = name.trim().isNotEmpty()
            ) {
                Text("Save")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

//SETTING ROW

@Composable
private fun SettingRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(17.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(13.dp)
        )
    }
}

//Switch Row

@Composable

private fun SettingSwitchRow(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8EEFC)),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF52628B),
                modifier = Modifier.size(17.dp)
            )

        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 11.sp,
            color = Color(0xFF1C2340)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )

    }

}

//Divider

@Composable
private fun SettingDivider() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                MaterialTheme.colorScheme.outlineVariant
            )
    )

}

