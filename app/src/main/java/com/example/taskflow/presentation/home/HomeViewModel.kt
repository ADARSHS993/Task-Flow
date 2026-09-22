package com.example.taskflow.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.domain.model.Project
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.usecase.projects.AddProjectUseCase
import com.example.taskflow.domain.usecase.projects.DeleteProjectUseCase
import com.example.taskflow.domain.usecase.projects.GetProjectByIdUseCase
import com.example.taskflow.domain.usecase.projects.GetRecentProjectUseCase
import com.example.taskflow.domain.usecase.projects.UpdateProjectUseCase
import com.example.taskflow.domain.usecase.sync.SyncDataUseCase
import com.example.taskflow.domain.usecase.task.GetAllTasksUSeCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val syncDataUseCase: SyncDataUseCase,

    private val getAllTasksUseCase : GetAllTasksUSeCase,
    private val getRecentProjectUseCase :  GetRecentProjectUseCase,
    private val addProjectUseCase: AddProjectUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val deleteProjectUseCase : DeleteProjectUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeHomeData()
        syncData()
    }

    private fun syncData() {
        viewModelScope.launch {
            try {
                syncDataUseCase()
            }catch (e: Exception){
            }
        }
    }

    private fun observeHomeData(){

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            combine(
                getAllTasksUseCase(),
                getRecentProjectUseCase()
            ){tasks, projects ->

                createHomeState(
                    tasks = tasks,
                    projects = projects
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    private fun createHomeState(
        tasks: List<Task>,
        projects: List<Project>
    ): HomeUiState{
        var totalTasks = tasks.size

        val completedTasks = tasks.count{
            it.isCompleted
        }

        val progressPercentage =
            if(totalTasks == 0){
                0
            }else{
                (completedTasks * 100) / totalTasks
            }

        val todayTasks = getTodayTasks(tasks)

        return HomeUiState(
            tasks = tasks,
            todayTasks = todayTasks,
            recentProjects = projects,
            completedTasks = completedTasks,
            totalTasks = totalTasks,
            progressPercentage = progressPercentage,
            isLoading = false,
            error = null
        )
    }

    private fun getTodayTasks(
        tasks: List<Task>
    ): List<Task>{
        val today = LocalDate.now()

        return tasks.filter { task ->

            task.dueDate?.let { dueDate ->

                val taskDate =
                    Instant.ofEpochMilli(dueDate)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

                taskDate == today
            } ?: false
        }

        fun refresh(){
            observeHomeData()
        }
    }

    //Add Project
    fun addProject(
        project: Project
    ){
        viewModelScope.launch {

            try {
                addProjectUseCase(project)
            }

            catch (e: Exception){

                _uiState.update {
                    it.copy(
                        error = e.message
                    )
                }
            }
        }
    }

    fun deleteProject(
        project: Project
    ){

        viewModelScope.launch {
            try {
                deleteProjectUseCase(project)
            } catch (e : Exception){

                _uiState.update {
                    it.copy(
                        error = e.message
                    )
                }
            }
        }
    }
}