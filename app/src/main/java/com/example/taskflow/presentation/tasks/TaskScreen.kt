package com.example.taskflow.presentation.tasks

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    onAddTask: () -> Unit,
    onTaskClick: (Task) -> Unit,
) {

    val state by viewModel.uiState.collectAsState()

    var selectedFilter by remember {
        mutableStateOf("All")
    }

    Scaffold(
        containerColor = Color(0xFFF8F8FC),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {

                    Text(
                        text = "Tasks",
                        color = Color(0xFF3520C9),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                        )
                    }
                },

                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF8F8FC)
                )
            )
        },
        floatingActionButton = {

            FloatingActionButton(
                onClick = { onAddTask() },
                containerColor = Color(0xFFF8F8FC),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "+",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    ) { innerpadding ->

        Column(
            modifier = Modifier
                .padding(innerpadding)
                .padding(horizontal = 8.dp)
                .fillMaxSize()
        ) {

            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = {
                    viewModel.onSearchQueryChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 8.dp,
                        horizontal = 0.dp
                    ),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Search your tasks...",
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                shape = RoundedCornerShape(12.dp),
            )

            //PRIORITY FILTERS

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            )
            {

                listOf(
                    "All",
                    "High",
                    "Medium",
                    "Low"
                ).forEach { filter ->

                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = {
                            selectedFilter = filter
                        },
                        label = {
                            Text(
                                text = filter,
                                fontSize = 12.sp
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF3D27D5),
                            selectedLabelColor = Color.White,
                            containerColor = Color(0xFFE8ECF8)
                        )
                    )
                }
            }

            //UPCOMING

            Text(

                text = "Upcoming",

                modifier = Modifier.padding(
                    top = 14.dp,
                    bottom = 10.dp
                ),

                fontSize = 16.sp,

                fontWeight = FontWeight.Medium,

                color = Color(0xFF242424)

            )

            // FILTER TASKS

            val filteredTasks = state.tasks
                .filter {
                    when (selectedFilter) {
                        "High" -> it.priority == Priority.HIGH

                        "Medium" -> it.priority == Priority.MEDIUM

                        "Low" -> it.priority == Priority.LOW

                        else -> true
                    }
                }
                .filter { task ->
                    task.title.contains(
                        state.searchQuery,
                        ignoreCase = true
                    ) || task.description.contains(
                        state.searchQuery,
                        ignoreCase = true
                    )
                }

            // TASK LIST

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (filteredTasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tasks found",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(items = filteredTasks, key = { it.id }) { task ->

                        TaskItem(
                            task = task,
                            categories = state.categories,
                            onClick = {
                                onTaskClick(task)
                            },
                            onCheckedChange = {
                                viewModel.toggleTask(task)
                            },
                            onDelete = {
                                viewModel.deleteTask(task)
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun TaskItem(
    task: Task,
    categories: List<Category>,
    onClick: () -> Unit,
    onCheckedChange: () -> Unit,
    onDelete: () -> Unit,
) {
    var showMenu by remember {
        mutableStateOf(false)
    }

    val category = categories.find {
        it.id == task.categoryId
    }

    val priorityColor = when (task.priority) {
        Priority.HIGH ->
            Color(0xFFE53935)

        Priority.MEDIUM ->
            Color(0xFFFF9800)

        Priority.LOW ->
            Color(0xFF4CAF50)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(2.dp),
        colors = CardDefaults.cardColors(
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = {
                        onCheckedChange()
                    }
                )

                //Tasks Info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = task.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF333333),
                        textDecoration = if (task.isCompleted)
                            TextDecoration.LineThrough
                        else
                            TextDecoration.None
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //Category Chip

                        if (category != null) {
                            Box(
                                modifier = Modifier.clip(
                                    RoundedCornerShape(8.dp)
                                ).background(Color(0xFFE8E8FF))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = category.name,
                                    fontSize = 9.sp,
                                    color = Color(0xFF5B5BC7)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        //Date
                        if (task.dueDate != null) {
                            Text(

                                text = formatTaskDate(
                                    task.dueDate
                                ),

                                fontSize = 9.sp,

                                color =
                                    Color(0xFF777777)

                            )
                        }
                    }
                }

                //Priority Flag
                Icon(

                    imageVector =
                        Icons.Default.Face,

                    contentDescription =
                        "Priority",

                    tint =
                        if (task.isCompleted)
                            Color.LightGray
                        else
                            priorityColor,

                    modifier =
                        Modifier
                            .size(20.dp)
                            .padding(end = 2.dp)

                )

                //Menu

                Box {

                    IconButton(
                        onClick = {
                            showMenu = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More"
                        )
                    }

                    DropdownMenu(

                        expanded = showMenu,

                        onDismissRequest = {
                            showMenu = false
                        }

                    ) {

                        DropdownMenuItem(

                            text = {
                                Text("Edit")
                            },

                            onClick = {

                                showMenu = false

                                onClick()

                            }

                        )

                        DropdownMenuItem(

                            text = {
                                Text(
                                    "Delete",
                                    color =
                                        MaterialTheme
                                            .colorScheme
                                            .error
                                )
                            },

                            leadingIcon = {

                                Icon(
                                    imageVector =
                                        Icons.Default.Delete,
                                    contentDescription =
                                        null,
                                    tint =
                                        MaterialTheme
                                            .colorScheme
                                            .error
                                )
                            },
                            onClick = {
                                showMenu = false
                                onDelete()
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun formatTaskDate(
    timestamp: Long
) : String{
    return try{
        SimpleDateFormat(
            "dd MMM",
            Locale.getDefault()
        ).format(
            Date(timestamp)
        )
    } catch (e: Exception){
        ""
    }
}