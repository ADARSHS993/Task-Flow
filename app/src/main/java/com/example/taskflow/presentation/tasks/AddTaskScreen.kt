package com.example.taskflow.presentation.tasks


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Project
import com.example.taskflow.domain.model.Task
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addEditTaskScreen(
    initialProjectId: String? = null,

    task: Task? = null,

    onBack: () -> Unit,

    projects: List<Project>,

    viewModel: TaskViewModel = hiltViewModel(),
) {

    val state by viewModel.uiState.collectAsState()

    var title by remember { mutableStateOf(task?.title ?: "") }

    var description by remember { mutableStateOf(task?.description ?: "") }

    var priority by remember(task?.id) {
        mutableStateOf(
            task?.priority ?: Priority.MEDIUM
        )
    }

    var selectedCategory by remember(task?.id, state.categories) {
        mutableStateOf(
            state.categories.find {
                it.id == task?.categoryId
            }
        )
    }

    var selectedProjectId by remember {
        mutableStateOf(
            task?.projectId ?: initialProjectId
        )
    }

    var dueDate by remember(task?.id) {
        mutableStateOf<Long?>(task?.dueDate)
    }

    var categoryExpanded by remember {
        mutableStateOf(false)
    }

    var showAddCategoryDialog by remember {
        mutableStateOf(false)
    }

    var showDeleteCategoryDialog by remember {
        mutableStateOf(false)
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var categoryName by remember {
        mutableStateOf("")
    }

    val dateFormatter = remember {
        SimpleDateFormat(
            "dd MMM yyyy",
            java.util.Locale.getDefault()
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (task == null) "Create New Task" else "Edit Task",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                )
                .navigationBarsPadding()
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Task Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = {
                            Text("Task Title")
                        },
                        placeholder = {
                            Text("What needs to be done?")
                        },
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            if (it.length <= 500) {
                                description = it
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 5,
                        maxLines = 8,
                        label = {
                            Text("Description")
                        },
                        placeholder = {
                            Text("Add notes ot description ...")
                        },
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${description.length}/500",
                        modifier = Modifier.align(
                            Alignment.End
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {

                        //Prority
                        Text(
                            text = "Priority",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Priority.entries.forEach { item ->

                                FilterChip(
                                    selected = priority == item,
                                    onClick = {
                                        priority = item
                                    },
                                    modifier = Modifier.weight(1f),
                                    label = {
                                        Text(
                                            when (item) {
                                                Priority.LOW -> "Low"
                                                Priority.HIGH -> "High"
                                                Priority.MEDIUM -> "Medium"
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    //Project DropDown
                    if (initialProjectId != null) {

                        val selectedProject = projects.find {
                            it.id == initialProjectId
                        }

                        if (selectedProject != null) {

                            OutlinedTextField(
                                value = selectedProject.name,
                                onValueChange = {},
                                readOnly = true,
                                enabled = false,
                                label = {
                                    Text("Project")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            )
                        }

                    } else {

                        ProjectDropdown(
                            projects = projects,
                            selectedProjectId = selectedProjectId,
                            onProjectSelected = { projectId ->
                                selectedProjectId = projectId
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))


                    Column(modifier = Modifier.fillMaxWidth()) {

                        Text(
                            text = "Category",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        ExposedDropdownMenuBox(
                            expanded = categoryExpanded,
                            onExpandedChange = {
                                categoryExpanded = !categoryExpanded
                            }
                        ) {

                            OutlinedTextField(
                                value = selectedCategory?.name ?: "",
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                label = {
                                    Text("Select Category")
                                },
                                placeholder = {
                                    Text("Choose a category")
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = categoryExpanded
                                    )
                                },
                                shape = RoundedCornerShape(16.dp)
                            )

                            ExposedDropdownMenu(
                                expanded = categoryExpanded,
                                onDismissRequest = {
                                    categoryExpanded = false
                                }
                            ) {
                                if (state.categories.isEmpty()) {
                                    DropdownMenuItem(
                                        text = { Text("No categories available") },
                                        onClick = { categoryExpanded = false }
                                    )
                                } else {

                                    state.categories.forEach { category ->

                                        DropdownMenuItem(

                                            text = { Text(category.name) },
                                            onClick = {
                                                selectedCategory = category
                                                categoryExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            OutlinedButton(
                                onClick = {
                                    categoryName = ""
                                    showAddCategoryDialog = true
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text("Add Category")
                            }

                            if (selectedCategory != null) {
                                OutlinedButton(
                                    onClick = {
                                        showDeleteCategoryDialog = true
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null
                                    )

                                    Spacer(modifier = Modifier.width(6.dp))

                                    Text("Delete")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Due Date",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = {
                                showDatePicker = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {

                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(

                                text =
                                    if (dueDate == null) {
                                        "Select Due Date"
                                    } else {
                                        dateFormatter.format(
                                            Date(dueDate!!)
                                        )
                                    }
                            )
                        }

                        if (dueDate != null) {

                            TextButton(
                                onClick = {
                                    dueDate = null
                                }
                            ) {
                                Text("Remove Due Date")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(

                        onClick = {
                            if (title.isBlank()) {
                                return@Button
                            }

                            if (task == null) {
                                viewModel.saveTask(
                                    title = title.trim(),
                                    description = description.trim(),
                                    priority = priority,
                                    categoryId = selectedCategory?.id,
                                    dueDate = dueDate,
                                    projectId = selectedProjectId
                                )
                            } else {
                                viewModel.editTask(
                                    oldTask = task,
                                    title = title.trim(),
                                    description = description.trim(),
                                    priority = priority,
                                    categoryId = selectedCategory?.id,
                                    dueDate = dueDate,
                                    projectId = selectedProjectId
                                )
                            }
                            onBack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = title.isNotBlank(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text =
                                if (task == null)
                                    "SAVE TASK"
                                else
                                    "UPDATE TASK",
                            fontSize = 16.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            //ADD CATEGORY DIALOG

            if (showAddCategoryDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showAddCategoryDialog = false
                    },
                    title = {
                        Text(text = "Add Category")
                    },
                    text = {

                        OutlinedTextField(
                            value = categoryName,
                            onValueChange = {
                                categoryName = it
                            },
                            modifier =
                                Modifier.fillMaxWidth(),

                            singleLine = true,

                            label = {
                                Text("Category Name")
                            },

                            placeholder = {
                                Text("e.g. Work")
                            }
                        )
                    },
                    confirmButton = {

                        Button(onClick = {
                            if(categoryName.isNotBlank()){
                                val newCategory = Category(
                                    id = java.util.UUID.randomUUID().toString(),
                                    name = categoryName.trim(),
                                    color = "#6200EE",
                                    icon = "folder"
                                )
                                viewModel.addCategory(newCategory)

                                selectedCategory = newCategory

                                showAddCategoryDialog = false

                                categoryName = ""
                            }
                        },
                            enabled = categoryName.isNotBlank())
                        {
                            Text("Add")
                        }
                    },

                    dismissButton = {
                        TextButton(
                            onClick = {
                                showAddCategoryDialog = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }

            //DELETE CATEGORY DIALOG

            if(showDeleteCategoryDialog){
                AlertDialog(

                    onDismissRequest = {
                        showDeleteCategoryDialog = false
                    },

                    title = {
                        Text(text = "Delete Category")
                    },

                    text = {
                        Text(text = "Are you sure you want to delete " + "\"${selectedCategory?.name}\"?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                selectedCategory?.let{
                                    viewModel.deleteCategory(it)
                                }

                                selectedCategory = null

                                showDeleteCategoryDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(text = "Delete")
                        }
                    },

                    dismissButton = {
                        TextButton(
                            onClick = {
                                showDeleteCategoryDialog = false
                            }
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }

            //DATE PICKER

            if(showDatePicker) {

                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = dueDate
                )

                DatePickerDialog(
                    onDismissRequest = {
                        showDatePicker = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                dueDate = datePickerState.selectedDateMillis

                                showDatePicker = false
                            }
                        ) {
                            Text(text = "OK")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showDatePicker = false
                            }
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDropdown(
    projects: List<Project>,
    selectedProjectId: String?,
    onProjectSelected: (String?) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val selectedProject = projects.find {
        it.id == selectedProjectId
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "Project",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(
                start = 4.dp,
                bottom = 10.dp
            )
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {

            OutlinedTextField(

                value = selectedProject?.name ?: "No Project",

                onValueChange = {},

                readOnly = true,

                label = {
                    Text("Select Project")
                },

                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),

                shape = RoundedCornerShape(16.dp),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            ExposedDropdownMenu(

                expanded = expanded,

                onDismissRequest = {
                    expanded = false
                }
            ) {

                // No Project option

                DropdownMenuItem(

                    text = {
                        Text("No Project")
                    },

                    onClick = {

                        onProjectSelected(null)

                        expanded = false
                    }
                )

                projects.forEach { project ->

                    DropdownMenuItem(

                        text = {
                            Text(project.name)
                        },

                        onClick = {

                            onProjectSelected(
                                project.id
                            )

                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
