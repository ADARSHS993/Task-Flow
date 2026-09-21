package com.example.taskflow.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Project
import com.example.taskflow.domain.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onMenuClick: () -> Unit = {},
    onAddProject: () -> Unit,
    onProjectClick: (Project) -> Unit,
    onTaskClick: (Task) -> Unit,
    onNavigateToLogin: () -> Unit
){

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var projectToDelete by remember {
        mutableStateOf<Project?>(null)
    }

    Scaffold(
        containerColor = Color(0xFFF8F8FC),

        topBar = {
           HomeTopBar()
        },

        floatingActionButton = {
            FloatingAddProjectButton(
                onClick = onAddProject
            )
        }
    ){ innerpadding ->

        if(state.isLoading){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerpadding),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()

            }
        } else{

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerpadding)
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = 6.dp
                    )
            ){

                //Greeting

                Text(
                    text = "Good morning , Adarsh",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(
                        top = 8.dp,
                        start = 4.dp
                    )
                )

                Text(
                    text = getCurrentDate(),
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(
                        top = 3.dp,
                        start = 4.dp
                    )
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                //Daily Progress

                DailyProgressCard(
                    completedTasks = state.completedTasks,
                    totalTasks = state.totalTasks,
                    percentage = state.progressPercentage
                )

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                //Today's Focus
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Today's Focus",
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "See all",
                        fontSize = 10.sp,
                        color = Color(0xFF3D2DD2),
                        modifier = Modifier.clickable{
                            //Later navigate to Tasks
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                if(state.todayTasks.isEmpty()){
                    EmptyTodayTasks()
                } else {

                    Column(
                        verticalArrangement =
                            Arrangement.spacedBy(7.dp)
                    ){

                        state.todayTasks
                            .take(3)
                            .forEach { task ->

                                TodayTaskCard(
                                    task = task,
                                    onClick = {
                                        onTaskClick(task)
                                    }
                                )
                            }
                    }
                }

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                //Recent Projects

                Text(
                    text = "Recent Projects",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                if(state.recentProjects.isEmpty()){
                    EmptyProjects(
                        onAddProject = onAddProject
                    )
                }else{

                    LazyRow(
                        contentPadding = PaddingValues(
                            end = 6.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ){

                        items(
                            items = state.recentProjects,
                            key = {
                                it.id
                            }
                        ){ project ->

                            ProjectCard(
                               project = project,
                                onClick = {
                                    onProjectClick(project)
                                },
                                onDelete = {
                                    projectToDelete = project
                                }
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(90.dp)
                )
            }
            //Main Column
        }
    }
    if (projectToDelete != null) {

        AlertDialog(

            onDismissRequest = {
                projectToDelete = null
            },

            title = {
                Text(
                    text = "Delete Project?"
                )
            },

            text = {
                Text(
                    text = "Are you sure you want to delete \"${projectToDelete?.name}\"?"
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        projectToDelete?.let { project ->

                            viewModel.deleteProject(project)

                        }

                        projectToDelete = null
                    }
                ) {

                    Text(
                        text = "DELETE",
                        color = Color(0xFFE53935),
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        projectToDelete = null
                    }
                ) {

                    Text(
                        text = "CANCEL"
                    )
                }
            }
        )
    }
}

@Composable
fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = {}
        ) {

            Icon(imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color.Black)
        }

        Text(
            text = "TaskFlow",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2F1FC6)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.width(6.dp)
        )
    }
}

@Composable
private fun DailyProgressCard(
    completedTasks: Int,
    totalTasks: Int,
    percentage: Int
){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4232D5)
        )
    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){

            Text(
                text = "Daily Progress",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(
                    alpha = 0.8f
                )
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){

                Column(
                    modifier = Modifier.weight(1f)
                ){
                    Text(
                        text = "$percentage% Done",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "$completedTasks of $totalTasks tasks completed",
                        fontSize = 9.sp,
                        color = Color.White.copy(
                            alpha = 0.8f
                        )
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Box(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(20.dp)
                            )
                            .background(
                                Color(0xFF55D9EF)
                            )
                            .clickable {
                                //View Report later
                            }
                            .padding(
                                horizontal = 14.dp,
                                vertical = 6.dp
                            )
                    ){
                        Text(
                            text = "View Report",
                            fontSize = 9.sp,
                            color = Color(0xFF16385B),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Text(
                    text = "$percentage%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

    }
}

@Composable
private fun TodayTaskCard(
    task: Task,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(15.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 11.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Icon(
                imageVector = if (task.isCompleted) {
                    Icons.Default.CheckCircle
                } else {
                    Icons.Default.TaskAlt
                },

                contentDescription = null,

                tint = if (task.isCompleted) {
                    Color(0xFF4535D7)
                } else {
                    Color(0xFF8D8D9A)
                },

                modifier = Modifier.size(20.dp)
            )

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = task.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (task.isCompleted) {
                        Color.Gray
                    } else {
                        Color(0xFF1F1F2A)
                    }
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    PriorityLabel(
                        priority = task.priority
                    )

                    Spacer(
                        modifier = Modifier.width(7.dp)
                    )

                    Text(
                        text = "Today",
                        fontSize = 9.sp,
                        color = Color.Gray
                    )
                }
            }

            Text(
                text = "≡",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun PriorityLabel(priority: Priority) {

    val backgroundColor = when (priority) {

        Priority.HIGH ->
            Color(0xFFFFD7D7)

        Priority.MEDIUM ->
            Color(0xFFFFE9C7)

        Priority.LOW ->
            Color(0xFFDDF5E2)
    }

    val textColor = when (priority) {

        Priority.HIGH ->
            Color(0xFFE14343)

        Priority.MEDIUM ->
            Color(0xFFC17A16)

        Priority.LOW ->
            Color(0xFF398A4D)
    }

    Text(
        text = priority.name
            .lowercase()
            .replaceFirstChar {
                it.uppercase()
            },

        fontSize = 8.sp,

        color = textColor,

        modifier = Modifier
            .clip(
                RoundedCornerShape(10.dp)
            )
            .background(
                backgroundColor
            )
            .padding(
                horizontal = 7.dp,
                vertical = 3.dp
            )
    )
}

@Composable
private fun ProjectCard(
    project: Project,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier
            .width(145.dp)
            .height(105.dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F3FF)
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(
                            RoundedCornerShape(9.dp)
                        )
                        .background(
                            Color(0xFF5141DB)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Project",
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = project.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = project.description,
                fontSize = 8.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun EmptyProjects(
    onAddProject: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(105.dp)
            .clickable {
                onAddProject()
            },

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F3FF)
        )
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.Center
        ) {

            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = null,
                tint = Color(0xFF5141DB)
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "Create your first project",
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun EmptyTodayTasks() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),

        shape = RoundedCornerShape(15.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "No tasks due today 🎉",
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun FloatingAddProjectButton(
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(Color(0xFF3D2DD2))
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Project",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
    }
}

private fun getCurrentDate(): String {

    val formatter =
        SimpleDateFormat(
            "EEEE, MMMM dd",
            Locale.getDefault()
        )

    return formatter.format(
        Date()
    )
}