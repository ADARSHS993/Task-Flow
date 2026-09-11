package com.example.taskflow.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.taskflow.presentation.auth.forgotpassword.ForgotPasswordScreen
import com.example.taskflow.presentation.auth.login.LoginScreen
import com.example.taskflow.presentation.auth.register.RegisterScreen
import com.example.taskflow.presentation.auth.splash.SplashScreen
import com.example.taskflow.presentation.home.AddEditProjectScreen
import com.example.taskflow.presentation.home.HomeScreen
import com.example.taskflow.presentation.home.HomeViewModel
import com.example.taskflow.presentation.profile.ProfileScreen
import com.example.taskflow.presentation.tasks.TaskScreen
import com.example.taskflow.presentation.tasks.TaskViewModel
import com.example.taskflow.presentation.tasks.addEditTaskScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestination.Splash.route,
) {

    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry?.destination?.route

    // These are the screens where bottom navigation
    // should be visible.
    val bottomNavRoutes = setOf(

        AppDestination.Home.route,

        AppDestination.Task.route,

        AppDestination.Calendar.route,

        AppDestination.Stats.route,

        AppDestination.Profile.route

    )

    val showBottomBar =
        currentRoute in bottomNavRoutes

    Scaffold(

        bottomBar = {

            if (showBottomBar) {

                BottomBar (

                    currentRoute = currentRoute,

                    onItemClick = { item ->

                        if (currentRoute != item.route) {

                            navController.navigate(
                                item.route
                            ) {

                                popUpTo(
                                    AppDestination.Home.route
                                ) {

                                    saveState = true

                                }

                                launchSingleTop = true

                                restoreState = true

                            }

                        }

                    }

                )

            }

        }

    ) { paddingValues ->

        NavHost(

            navController = navController,

            startDestination = startDestination,

            modifier = Modifier
                .padding(paddingValues)
            ) {

            // Splash Destination
            composable(AppDestination.Splash.route) {
                SplashScreen(
                    onNavigateToHome = {
                        navController.navigate(AppDestination.Home.route) {
                            popUpTo(AppDestination.Splash.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(AppDestination.Login.route) {
                            popUpTo(AppDestination.Splash.route) { inclusive = true }
                        }
                    }
                )
            }


            // Login Destination
            composable(AppDestination.Login.route) {
                LoginScreen(
                    onNavigateToHome = {
                        navController.navigate(AppDestination.Home.route) {
                            popUpTo(AppDestination.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(AppDestination.Register.route)
                    },
                    onNavigateToForgotPassword = {
                        navController.navigate(AppDestination.ForgotPassword.route)
                    }
                )
            }

            // Register Destination
            composable(AppDestination.Register.route) {
                RegisterScreen(
                    onNavigateToLogin = {
                        navController.popBackStack()
                    },
                    onNavigateToHome = {
                        navController.navigate(AppDestination.Home.route) {
                            popUpTo(AppDestination.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // Forgot Password Destination
            composable(AppDestination.ForgotPassword.route) {
                ForgotPasswordScreen(
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            // Home Destination
            composable(AppDestination.Home.route) {
                HomeScreen(
                    onAddProject = {
                        navController.navigate(
                            AppDestination.AddProject.route
                        )
                    },
                    onProjectClick = {project ->
                        navController.navigate(
                            AppDestination.ProjectTasks.createRoute(
                                project.id
                            )
                        )
                    },
                    onTaskClick = { task ->
                        navController.navigate(
                            AppDestination.EditTask.createRoute(
                                task.id
                            )
                        )
                    },
                    onNavigateToLogin = {
                        navController.navigate(
                            AppDestination.Login.route
                        ) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            //AddProject
            composable(AppDestination.AddProject.route){
                val viewModel : HomeViewModel = hiltViewModel()

                AddEditProjectScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onSave = {project ->
                        viewModel.addProject(project)
                        navController.popBackStack()
                    }
                )
            }

            // Task Destination
            composable(AppDestination.Task.route) {
                TaskScreen(
                    onAddTask = {
                        navController.navigate(AppDestination.AddTask.route)
                    },
                    onTaskClick = { task ->
                        navController.navigate(
                            AppDestination.EditTask.createRoute(task.id)
                        )
                    }
                )
            }

                //project task

            composable(
                AppDestination.ProjectTasks.route
            ) { backStackEntry ->

                val projectId =
                    backStackEntry.arguments?.getString("projectId")

                TaskScreen(
                    projectId = projectId,

                    onAddTask = {
                        navController.navigate(
                            AppDestination.AddTask.createRoute(projectId)
                        )
                    },

                    onTaskClick = { task ->
                        navController.navigate(
                            AppDestination.EditTask.createRoute(task.id)
                        )
                    }
                )
            }
            //add Task
            // Add Task
            composable(
                route = AppDestination.AddTask.route
            ) { backStackEntry ->

                val projectIdArg =
                    backStackEntry.arguments?.getString("projectId")

                val projectId =
                    if (projectIdArg == "none") {
                        null
                    } else {
                        projectIdArg
                    }

                val viewModel: TaskViewModel = hiltViewModel()
                val state by viewModel.uiState.collectAsState()

                addEditTaskScreen(
                    projects = state.projects,

                    initialProjectId = projectId,

                    onBack = {
                        navController.popBackStack()
                    },

                    viewModel = viewModel
                )
            }

            //edit Task
            composable(
                route = AppDestination.EditTask.route
            ) { backStackEntry ->

                val taskId =
                    backStackEntry.arguments?.getString("taskId")

                val viewModel: TaskViewModel = hiltViewModel()

                val state by viewModel.uiState.collectAsState()

                val task =
                    state.tasks.find {
                        it.id == taskId
                    }

                if (task != null) {

                    addEditTaskScreen(

                        task = task,

                        projects = state.projects,

                        onBack = {
                            navController.popBackStack()
                        }
                    )

                }

            }

            composable(
                route = AppDestination.Profile.route
            ){
                ProfileScreen(
                    onLogout = {
                        navController.navigate(
                            AppDestination.Login.route
                        ){
                            popUpTo(0){
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}

