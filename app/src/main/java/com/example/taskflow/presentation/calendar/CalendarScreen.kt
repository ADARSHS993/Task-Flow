package com.example.taskflow.presentation.calendar

import android.R.attr.tint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskflow.domain.model.Task
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


private val BackgroundColor = Color(0xFFF8F8FC)
private val PrimaryColor = Color(0xFF3D27D5)
private val CalendarCardColor = Color(0xFFFAFAFF)
private val EventCardColor = Color(0xFFEFF2FF)

@Composable
fun CalendarScreen(
    onAddTask: () -> Unit,
    onTaskClick: (Task) -> Unit,
    viewModel: CalendarViewModel = hiltViewModel()
){

    val state by viewModel.uiState
        .collectAsStateWithLifecycle()

    Scaffold (
        containerColor = BackgroundColor,

        topBar = {
            CalendarTopBar()
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTask,
                modifier = Modifier
                    .size(54.dp),
                shape = RoundedCornerShape(17.dp),
                containerColor = PrimaryColor,
                contentColor = Color.White
            ) {
                Text(
                    text = "+",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    ){innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

            contentPadding = PaddingValues(
                start = 8.dp,
                end = 8.dp,
                bottom = 90.dp
            ),

            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            item {

                CalendarMonthHeader(
                    currentMonth = state.currentMonth,
                    selectedDate = state.selectedDate,
                    onPreviousMonth = {
                        viewModel.previousMonth()
                    },
                    onNextMonth = {
                        viewModel.nextMonth()
                    }
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                CalendarGrid(
                    currentMonth = state.currentMonth,
                    selectedDate = state.selectedDate,
                    tasks = state.tasks,
                    onDateSelected = { date ->
                        viewModel.selectDate(date)
                    }
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                ScheduleHeader()

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }

            if (state.selectedTasks.isEmpty()) {

                item {
                    EmptyScheduleCard()
                }

            } else {

                items(
                    items = state.selectedTasks,
                    key = { task -> task.id }
                ) { task ->

                    val projectName =
                        state.projects
                            .find { project ->
                                project.id == task.projectId
                            }
                            ?.name

                    val categoryName =
                        state.categories
                            .find { category ->
                                category.id == task.categoryId
                            }
                            ?.name

                    CalendarTaskCard(
                        task = task,
                        projectName = projectName,
                        categoryName = categoryName,
                        onClick = {
                            onTaskClick(task)
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarMonthHeader(
    currentMonth: LocalDate,
    selectedDate: LocalDate,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(start = 2.dp, end = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        Column(
            modifier = Modifier.weight(1f)
        ){

            Text(
                text = currentMonth.format(
                    DateTimeFormatter.ofPattern(
                        "MMMM yyyy",
                        Locale.getDefault()
                    )
                ),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            )

            Text(
                text = "SELECTED: ${
                    selectedDate.format(
                        DateTimeFormatter.ofPattern(
                            "EEEE, dd",
                            Locale.getDefault()
                        )
                    ).uppercase()
                }",
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                color = Color(0xFF423C67)
            )
        }

        IconButton(
            onClick = onPreviousMonth
        ) {

            Icon(imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Previous Month",
                tint = Color.Black,
                modifier = Modifier.size(17.dp))
        }

        Spacer(modifier = Modifier.width(4.dp))

        IconButton(
            onClick = onNextMonth
        ) {

            Icon(imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Next Month",
                tint = Color.Black,
                modifier = Modifier.size(17.dp))
        }
    }
}

@Composable
private fun ScheduleHeader() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "Schedule",
            modifier = Modifier.weight(1f),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        Text(
            text = "View All",
            fontSize = 12.sp,
            color = PrimaryColor,
            modifier = Modifier.clickable {
                // Later
            }
        )
    }
}

@Composable
private fun CalendarTaskCard(
    task: Task,
    projectName: String?,
    categoryName: String?,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        verticalAlignment = Alignment.CenterVertically
    ) {

        // Time
        Column(
            modifier = Modifier.width(58.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = formatTime(task.dueDate),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF26232D)
            )

            Text(
                text = formatAmPm(task.dueDate),
                fontSize = 9.sp,
                color = Color.Gray
            )
        }

        Spacer(
            modifier = Modifier.width(6.dp)
        )

        // Task card
        Card(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 105.dp),

            shape = RoundedCornerShape(20.dp),

            colors = CardDefaults.cardColors(
                containerColor = EventCardColor
            ),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        ) {

            Row(
                modifier = Modifier.fillMaxSize()
            ) {

                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxSize()
                        .background(
                            PrimaryColor
                        )
                )

                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = 14.dp,
                            vertical = 13.dp
                        )
                        .weight(1f)
                ) {

                    Text(
                        text = task.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF171722)
                    )

                    if (projectName != null) {

                        Spacer(
                            modifier = Modifier.height(5.dp)
                        )

                        Text(
                            text = projectName,
                            fontSize = 12.sp,
                            color = Color(0xFF5550A9),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (categoryName != null) {

                        Spacer(
                            modifier = Modifier.height(9.dp)
                        )

                        Box(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(12.dp)
                                )
                                .background(
                                    Color(0xFFDCDFFF)
                                )
                                .padding(
                                    horizontal = 9.dp,
                                    vertical = 4.dp
                                )
                        ) {

                            Text(
                                text = categoryName,
                                fontSize = 9.sp,
                                color = Color(0xFF4F47B9)
                            )
                        }
                    }
                }

                Text(
                    text = "⋮",
                    fontSize = 22.sp,
                    color = Color(0xFF45444D),
                    modifier = Modifier.padding(
                        end = 10.dp,
                        top = 9.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun EmptyScheduleCard() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "No tasks scheduled for this date 🎉",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

private fun formatTime(
    timestamp: Long?
): String {

    if (timestamp == null) return ""

    return java.text.SimpleDateFormat(
        "hh:mm",
        Locale.getDefault()
    ).format(
        java.util.Date(timestamp)
    )
}

private fun formatAmPm(
    timestamp: Long?
): String {

    if (timestamp == null) return ""

    return java.text.SimpleDateFormat(
        "a",
        Locale.getDefault()
    ).format(
        java.util.Date(timestamp)
    )
}

@Composable
fun CalendarGrid(
    currentMonth: LocalDate,
    selectedDate: LocalDate,
    tasks: List<Task>,
    onDateSelected: (LocalDate) -> Unit
) {
    val yearMonth = YearMonth.from(currentMonth)

    val firstDay = yearMonth.atDay(1)

    val offset = firstDay.dayOfWeek.value - DayOfWeek.MONDAY.value

    val totalDays = yearMonth.lengthOfMonth()

    val previousMonth = yearMonth.minusMonths(1)

    val previousMonthDays = previousMonth.lengthOfMonth()

    val dates = mutableListOf<LocalDate?>()

    //Previous month dates
    repeat(offset){index ->
        dates.add(
            previousMonth.atDay(
                previousMonthDays - offset + index + 1
            )
        )

    }

    //Current month
    for(day in 1..totalDays){
        dates.add(yearMonth.atDay(day))
    }

    //Next month dates
    while(dates.size % 7 != 0){
      val nextDay = dates.count{
          it?.month == currentMonth.month
      }  + 1

        dates.add(
            if(dates.size < 42){
                yearMonth
                    .plusMonths(1)
                    .atDay(
                        dates.size - (offset + totalDays) + 1
                    )
            }else{
                null
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),

        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = CalendarCardColor
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 10.dp,
                    vertical = 12.dp
                )
        ) {

            // Day names
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                listOf(
                    "M",
                    "T",
                    "W",
                    "T",
                    "F",
                    "S",
                    "S"
                ).forEach { day ->

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = day,
                            fontSize = 10.sp,
                            color = Color(0xFF2D2B39),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            dates.chunked(7).forEach { week ->

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    week.forEach { date ->

                        CalendarDateCell(
                            date = date,
                            currentMonth = currentMonth,
                            selectedDate = selectedDate,
                            tasks = tasks,
                            onClick = {
                                date?.let {
                                    onDateSelected(it)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDateCell(
    date: LocalDate?,
    currentMonth: LocalDate,
    selectedDate: LocalDate,
    tasks: List<Task>,
    onClick: () -> Unit
) {

    if (date == null) {

        Box(
            modifier = Modifier
                .height(52.dp)
        )

        return
    }

    val isSelected =
        date == selectedDate

    val isCurrentMonth =
        date.month == currentMonth.month

    val hasTask =
        tasks.any { task ->

            task.dueDate?.let { timestamp ->

                val taskDate =
                    Instant.ofEpochMilli(timestamp)
                        .atZone(
                            ZoneId.systemDefault()
                        )
                        .toLocalDate()

                taskDate == date

            } ?: false
        }

    Box(
        modifier = Modifier
            .height(52.dp),

        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    )
                    .then(
                        if (isSelected) {
                            Modifier.background(
                                PrimaryColor
                            )
                        } else {
                            Modifier
                        }
                    )
                    .then(
                        if (!isSelected &&
                            date == LocalDate.now()
                        ) {
                            Modifier.border(
                                width = 1.dp,
                                color = Color(0xFFD4D4EE),
                                shape = RoundedCornerShape(12.dp)
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clickable {
                        onClick()
                    },
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = date.dayOfMonth.toString(),
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    },
                    color = when {
                        isSelected ->
                            Color.White

                        !isCurrentMonth ->
                            Color(0xFFC7C7D2)

                        else ->
                            Color(0xFF101018)
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            if (hasTask) {

                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) {
                                Color.White
                            } else {
                                PrimaryColor
                            }
                        )
                )
            }
        }
    }
}

@Composable
fun CalendarTopBar(){

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
            text = "Calendar",
            modifier = Modifier.weight(1f),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2F1FC6)
        )

        IconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Black
            )
        }

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

        Spacer(
            modifier = Modifier.width(6.dp)
        )


    }
}
