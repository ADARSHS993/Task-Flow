package com.example.taskflow.navigation

sealed class AppDestination(val route: String) {
    data object Splash : AppDestination("splash")
    data object Login : AppDestination("login")
    data object Register : AppDestination("register")
    data object ForgotPassword : AppDestination("forgot_password")
    data object Home : AppDestination("home")

    data object AddProject : AppDestination("add_project")

    data object EditProject : AppDestination("edit_project/{projectId"){
        fun createRoutr(projectId: String): String{
            return "edit_project/$projectId"
        }
    }

    data object Task : AppDestination("task")
    data object AddTask : AppDestination("add_task")
    data object EditTask : AppDestination("edit_task/{taskId}") {
        fun createRoute(taskId: String): String {
            return "edit_task/$taskId"
        }
    }
    data object Calendar : AppDestination("calendar")
    data object Stats : AppDestination("stats")
    data object Profile : AppDestination("profile")
}