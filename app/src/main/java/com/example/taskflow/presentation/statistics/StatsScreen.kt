package com.example.taskflow.presentation.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Task
import com.example.taskflow.presentation.components.ProfileAvatar
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.abs
import kotlin.math.max

private val StatsBackground = Color(0xFFF8F8FC)
private val PrimaryPurple = Color(0xFF3D27D5)
private val CardBackground = Color.White
private val TextDark = Color(0xFF22222B)
private val MutedText = Color(0xFF7D7D89)

@Composable
fun StatsScreen(
    viewModel: StateViewModel = hiltViewModel(),
    photoUrl: String?,
    onNavigateToProfile : () -> Unit
){

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = StatsBackground,

        topBar = {
            StatsTopBar(
                photoUrl = photoUrl,
                onProfileClick = onNavigateToProfile
            )
        }
    ){ innerPadding ->

        if(state.isLoading){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else{

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 8.dp),

                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){

                item {
                    WeekSelector(
                        weekStart = state.selectedWeekStart,
                        onPrevious = {
                            viewModel.previousWeek()
                        },
                        onNext = {
                            viewModel.nextWeek()
                        }
                    )
                }

                item {
                    MomentumCard(
                        tasks = state.tasks,
                        selectedWeekStart = state.selectedWeekStart
                    )
                }

                item {
                    StatsMetricGrid(
                        tasks = state.tasks,
                        selectedWeekStart = state.selectedWeekStart,
                    )
                }

                item {
                    WeeklyProductivityCard(
                        tasks = state.tasks,
                        selectedWeekStart = state.selectedWeekStart
                    )
                }

                item {
                    CategoryStatsCard(
                        tasks = state.tasks,
                        categories = state.categories,
                        projectsCount = state.projects.size
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier.height(70.dp)
                    )
                }
            }
        }
    }
}


@Composable
private fun StatsTopBar(
    photoUrl : String?,
    onProfileClick : () -> Unit
){

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
            text = "Stats",
            modifier = Modifier.weight(1f),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2F1FC6)
        )
            IconButton(
                onClick = onProfileClick
            ) {
                ProfileAvatar(
                    photoUrl = photoUrl,
                    modifier = Modifier.size(34.dp)
                )

        }

        Spacer(
            modifier = Modifier.width(6.dp)
        )


    }
}

@Composable
private fun WeekSelector(
    weekStart : LocalDate,
    onPrevious: () -> Unit,
    onNext: () -> Unit
){

    val weekEnd = weekStart.plusDays(6)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ){

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ){

            IconButton(
                onClick = onPrevious
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Previous week",
                    modifier = Modifier.size(16.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                Text(
                    text = "This Week",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryPurple
                )

                Text(
                    text = "${weekStart.dayOfMonth} ${
                        weekStart.month.name
                            .lowercase()
                            .replaceFirstChar { 
                                it.uppercase()
                            }
                    } - ${weekEnd.dayOfMonth}",
                    fontSize = 9.sp,
                    color = MutedText
                )
            }

            IconButton(
                onClick = onNext
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = "Next week",
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun MomentumCard(
    tasks: List<Task>,
    selectedWeekStart: LocalDate
){

    val completed = completedTasksInWeek(
        tasks,
        selectedWeekStart
    )

    val streak =
        calculateStreak(tasks)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp),

        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryPurple
        )
    ){
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {

                Text(
                    text = "• CURRENT MOMENTUM",
                    fontSize = 8.sp,
                    color = Color.White.copy(
                        alpha = 0.8f
                    )
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "$streak Day Streak",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = when {
                        streak == 0 ->
                            "Start completing tasks to build your streak"

                        streak < 7 ->
                            "Nice progress. Keep going!"

                        else ->
                            "Excellent consistency. Keep it going!"
                    },
                    fontSize = 8.sp,
                    color = Color.White.copy(
                        alpha = 0.85f
                    )
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = "Week Goal: $completed / 7 days",
                    fontSize = 8.sp,
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        Color.White.copy(
                            alpha = 0.15f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "🔥",
                    fontSize = 18.sp
                )
            }
        }
    }
}

private fun calculateStreak(
    tasks: List<Task>
): Int {

    val completedDates =
        tasks
            .filter { it.isCompleted }
            .map {
                timestampToDate(it.updatedAt)
            }
            .toSet()

    var streak = 0
    var date = LocalDate.now()

    while (completedDates.contains(date)) {
        streak++
        date = date.minusDays(1)
    }

    return streak
}

@Composable
private fun StatsMetricGrid(
    tasks: List<Task>,
    selectedWeekStart: LocalDate
){
    val completed =
        completedTasksInWeek(
            tasks,
            selectedWeekStart
        )

    val total =
        tasksInWeek(
            tasks,
            selectedWeekStart
        )

    val progress =
        if (total == 0) 0
        else ((completed.toFloat() / total) * 100)
            .toInt()

    val pending =
        tasks.count {
            !it.isCompleted
        }

    val critical =
        tasks.count {
            !it.isCompleted &&
                    it.priority.name == "HIGH"
        }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            StatCard(
                modifier = Modifier.weight(1f),
                title = "Progress",
                value = "$progress%",
                subtitle = "+4.4%",
                positive = true
            )

            StatCard(
                modifier = Modifier.weight(1f),
                title = "Completed",
                value = completed.toString(),
                subtitle = "This week",
                positive = true
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            StatCard(
                modifier = Modifier.weight(1f),
                title = "Pending",
                value = pending.toString(),
                subtitle = "In queue",
                positive = false
            )

            StatCard(
                modifier = Modifier.weight(1f),
                title = "Critical",
                value = critical.toString(),
                subtitle = "Needs action",
                positive = false
            )
        }
    }
}

private fun tasksInWeek(
    tasks: List<Task>,
    weekStart: LocalDate
): Int{

    val weekEnd = weekStart.plusDays(6)

    return tasks.count { task ->
        task.dueDate?.let { timestamp ->

            val date = timestampToDate(timestamp)

            date >= weekStart && date <= weekEnd
        } ?: false
    }
}

private fun completedTasksInWeek(
    tasks: List<Task>,
    weekStart: LocalDate
): Int {

    val weekEnd =
        weekStart.plusDays(6)

    return tasks.count { task ->

        if (!task.isCompleted) {
            false
        } else {

            val date =
                timestampToDate(task.updatedAt)

            date >= weekStart &&
                    date <= weekEnd
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    positive: Boolean
) {

    Card(
        modifier = modifier
            .height(95.dp),

        shape = RoundedCornerShape(14.dp),

        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = title,
                    fontSize = 9.sp,
                    color = MutedText,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = if (positive) "↗" else "!",
                    fontSize = 10.sp,
                    color = if (positive) {
                        Color(0xFF00A67A)
                    } else {
                        Color(0xFFE53935)
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = value,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Text(
                text = subtitle,
                fontSize = 8.sp,
                color = MutedText
            )
        }
    }
}

@Composable
private fun WeeklyProductivityCard(
    tasks: List<Task>,
    selectedWeekStart: LocalDate
) {

    val dailyCompleted = remember(
        tasks,
        selectedWeekStart
    ) {
        (0..6).map { offset ->

            val date =
                selectedWeekStart.plusDays(
                    offset.toLong()
                )

            tasks.count { task ->

                if (!task.isCompleted) {
                    false
                } else {

                    timestampToDate(
                        task.updatedAt
                    ) == date
                }
            }
        }
    }

    val maxValue =
        max(
            1,
            dailyCompleted.maxOrNull() ?: 1
        )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Weekly Productivity",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )

                Spacer(
                    modifier = Modifier.width(6.dp)
                )

                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(10.dp)
                        )
                        .background(
                            Color(0xFFDDF6EC)
                        )
                        .padding(
                            horizontal = 6.dp,
                            vertical = 3.dp
                        )
                ) {

                    Text(
                        text = "+18% vs avg",
                        fontSize = 7.sp,
                        color = Color(0xFF1F9A6F)
                    )
                }
            }

            Text(
                text = "Daily completed tasks with daily 20-task target",
                fontSize = 8.sp,
                color = MutedText
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                FilterChip(
                    selected = true,
                    onClick = {},
                    label = {
                        Text(
                            text = "Last 7 Days",
                            fontSize = 8.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor =
                            Color(0xFFE4E1FF),
                        selectedLabelColor =
                            PrimaryPurple
                    )
                )

                FilterChip(
                    selected = false,
                    onClick = {},
                    label = {
                        Text(
                            text = "Last 30 Days",
                            fontSize = 8.sp
                        )
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            WeeklyBarChart(
                values = dailyCompleted,
                maxValue = maxValue,
                weekStart = selectedWeekStart
            )
        }
    }
}

private fun timestampToDate(
    timestamp: Long
): LocalDate{
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

@Composable
private fun WeeklyBarChart(
    values: List<Int>,
    maxValue: Int,
    weekStart: LocalDate
) {

    val dayNames = listOf(
        "Mon",
        "Tue",
        "Wed",
        "Thu",
        "Fri",
        "Sat",
        "Sun"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        values.forEachIndexed { index, value ->

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = value.toString(),
                    fontSize = 7.sp,
                    color = MutedText
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            maxOf(
                                12.dp,
                                (
                                        75f *
                                                value.coerceAtMost(maxValue) /
                                                maxValue
                                        ).dp
                            )
                        )
                        .padding(
                            horizontal = 5.dp
                        )
                        .clip(
                            RoundedCornerShape(
                                topStart = 7.dp,
                                topEnd = 7.dp
                            )
                        )
                        .background(
                            if (index == 3) {
                                PrimaryPurple
                            } else {
                                Color(0xFF7266E8)
                            }
                        )
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = dayNames[index],
                    fontSize = 8.sp,
                    color = MutedText
                )
            }
        }
    }
}

@Composable
private fun CategoryStatsCard(
    tasks: List<Task>,
    categories: List<Category>,
    projectsCount: Int
) {

    val categoryCounts =
        categories.map { category ->

            category to tasks.count {
                it.categoryId == category.id
            }
        }.filter {
            it.second > 0
        }

    val total =
        categoryCounts.sumOf {
            it.second
        }

    Card(
        modifier = Modifier
            .fillMaxWidth(),

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Tasks by Category",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(10.dp)
                        )
                        .background(
                            Color(0xFFE4E1FF)
                        )
                        .padding(
                            horizontal = 6.dp,
                            vertical = 3.dp
                        )
                ) {

                    Text(
                        text = "$projectsCount Active Projects",
                        fontSize = 7.sp,
                        color = PrimaryPurple
                    )
                }
            }

            Text(
                text = "$total categorized tasks breakdown",
                fontSize = 8.sp,
                color = MutedText
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            CategoryProgressBar(
                counts = categoryCounts.map {
                    it.second
                },
                total = total
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {

                categoryCounts.chunked(2).forEach { rowItems ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        rowItems.forEach { (category, count) ->

                            CategoryLegendItem(
                                modifier = Modifier.weight(1f),
                                category = category,
                                count = count,
                                total = total
                            )
                        }

                        if (rowItems.size == 1) {
                            Spacer(
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryLegendItem(
    modifier: Modifier,
    category: Category,
    count: Int,
    total: Int
) {

    val percentage =
        if (total == 0) {
            0
        } else {
            ((count.toFloat() / total) * 100)
                .toInt()
        }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(
                    categoryColor(category.id)
                )
        )

        Spacer(
            modifier = Modifier.width(5.dp)
        )

        Column {

            Text(
                text = category.name,
                fontSize = 8.sp,
                color = TextDark
            )

            Text(
                text = "$count ($percentage%)",
                fontSize = 7.sp,
                color = MutedText
            )
        }
    }
}

private fun categoryColor(
    categoryId: String
): Color {

    val colors = listOf(
        Color(0xFF4B39D8),
        Color(0xFF00A8B5),
        Color(0xFF5CCDA4),
        Color(0xFFFFB000),
        Color(0xFF5E7CE2)
    )

    return colors[
        abs(categoryId.hashCode()) %
                colors.size
    ]
}

@Composable
private fun CategoryProgressBar(
    counts: List<Int>,
    total: Int
) {

    if (total == 0) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(
                    RoundedCornerShape(8.dp)
                )
                .background(
                    Color(0xFFE8E8EF)
                )
        )

        return
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(
                RoundedCornerShape(8.dp)
            )
    ) {

        val colors = listOf(
            Color(0xFF4B39D8),
            Color(0xFF00A8B5),
            Color(0xFF5CCDA4),
            Color(0xFFFFB000),
            Color(0xFF5E7CE2),
            Color(0xFFD0D0D8)
        )

        counts.forEachIndexed { index, count ->

            Box(
                modifier = Modifier
                    .weight(
                        count.toFloat()
                            .coerceAtLeast(0.1f)
                    )
                    .fillMaxHeight()
                    .background(
                        colors[index % colors.size]
                    )
            )
        }
    }
}

