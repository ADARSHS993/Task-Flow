package com.example.taskflow.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.domain.model.Project
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProjectScreen(
    project: Project? = null,
    onBack: () -> Unit,
    onSave: (Project) -> Unit,
) {

    var name by remember {
        mutableStateOf(project?.name ?: "")
    }

    var description by remember {
        mutableStateOf(project?.description ?: "")
    }

    var selectedColor by remember {
        mutableStateOf(
            project?.color ?: "#5141D5"
        )
    }

    val colors = listOf(
        "#5141D5",
        "#42C7E8",
        "#FF6B6B",
        "#FFB74D",
        "#66BB6A",
        "#AB47BC"
    )

    Scaffold(

        topBar = {

            CenterAlignedTopAppBar(

                title = {
                    Text(
                        text = if (project == null)
                            "Create Project"
                        else
                            "Edit Project",

                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {

                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Project Preview

            BoxProjectPreview(
                color = selectedColor,
                name = name
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            //Project Details

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surfaceVariant
                            .copy(alpha = 0.3f)
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Project Details",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(
                            bottom = 12.dp
                        )
                    )

                    //Project Name
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Project name")
                        },
                        placeholder = {
                            Text(
                                "Write your project name here"
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    // Description

                    OutlinedTextField(

                        value = description,

                        onValueChange = {
                            description = it
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        label = {
                            Text("Description")
                        },

                        placeholder = {
                            Text(
                                "Add project description..."
                            )
                        },

                        minLines = 4,

                        maxLines = 5,

                        shape =
                            RoundedCornerShape(16.dp),

                        colors =
                            OutlinedTextFieldDefaults.colors(
                                focusedContainerColor =
                                    Color.White,

                                unfocusedContainerColor =
                                    Color.White
                            )
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            //Project Color

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Project Color",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier =
                        Modifier.padding(
                            start = 4.dp,
                            bottom = 12.dp
                        )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(14.dp)
                ) {

                    colors.forEach { colorHex ->

                        val color =
                            Color(
                                android.graphics.Color
                                    .parseColor(colorHex)
                            )

                        val isSelected =
                            selectedColor == colorHex

                        BoxColorPicker(

                            color = color,

                            isSelected = isSelected,

                            onClick = {
                                selectedColor = colorHex
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(40.dp)
            )

            // Save Button

            Button(

                onClick = {

                    val newProject = Project(

                        id =
                            project?.id
                                ?: UUID.randomUUID()
                                    .toString(),

                        name = name.trim(),

                        description =
                            description.trim(),

                        color = selectedColor
                    )

                    onSave(newProject)

                },

                enabled = name.isNotBlank(),

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                shape =
                    RoundedCornerShape(16.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            MaterialTheme.colorScheme.primary
                    )
            ) {

                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text =
                        if (project == null)
                            "CREATE PROJECT"
                        else
                            "UPDATE PROJECT",

                    fontSize = 15.sp,

                    fontWeight = FontWeight.Bold,

                    letterSpacing = 1.sp
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}

@Composable
private fun BoxProjectPreview(
    color: String,
    name: String
) {

    val previewColor = Color(
        android.graphics.Color.parseColor(color)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(
                    RoundedCornerShape(22.dp)
                )
                .background(previewColor),

            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text =
                if (name.isBlank())
                    "New Project"
                else
                    name,

            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun BoxColorPicker(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(
                if (isSelected)
                    color.copy(alpha = 0.25f)
                else
                    Color.Transparent
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(color)
        )

        if (isSelected) {

            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(17.dp)
            )
        }
    }
}