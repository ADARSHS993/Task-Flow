package com.example.taskflow.presentation.profile

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBarDefaults.windowInsets
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onMenuClick: () -> Unit = {},
    onLogout: () -> Unit,
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    var notificationsEnabled by remember {
        mutableStateOf(true)
    }

    var darkModeEnabled by remember {
        mutableStateOf(false)
    }

    var showEditDialog by remember {
        mutableStateOf(false)
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

                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE3E5EF)),
                    ) {

                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color(0xFF555555),
                            modifier = Modifier.size(20.dp)
                        )

                    }

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
                    if (profile?.photoUrl != null) {
                        AsyncImage(
                            model = profile.photoUrl,
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
                            showEditDialog = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = Color.White,
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
                color = Color.Black
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            //Email
            Text(
                text = profile?.email ?: "",
                fontSize = 11.sp,
                color = Color.Gray
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
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(
                                modifier = Modifier.width(5.dp)
                            )

                            Text(
                                text = "Tasks Completed",
                                fontSize = 10.sp,
                                color = Color.White
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "124",
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
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
                    contentColor = Color.White
                )
            ) {
                //Dark Mode

                SettingSwitchRow(
                    icon = Icons.Default.DarkMode,
                    title = "Dark Mode",
                    checked = darkModeEnabled,
                    onCheckedChange = {
                        darkModeEnabled = it
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

                SettingRow(
                    icon = Icons.Default.Lock,
                    title = "Privacy Policy",
                    onClick = {
                        // Open privacy policy
                    }
                )

                SettingDivider()

                // About

                SettingRow(
                    icon = Icons.Default.Info,
                    title = "About TaskFlow",
                    onClick = {
                        // Open about page
                    }
                )
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

        Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = null,
            tint = Color(0xFFB8BBD0),
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
            .background(Color(0xFFF0F0F5))
    )

}

