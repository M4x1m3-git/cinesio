package com.example.cinesio.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.cinesio.ui.screens.PrincipalScreen
import com.example.cinesio.data.model.Movie
import com.example.cinesio.data.repository.MovieRepository
import com.example.cinesio.network.NetworkModule
import com.example.cinesio.viewmodel.MovieViewModel

@Composable
fun MainScreen() {

    val movies = listOf(
        Movie(1, "Film 1", "FILM"),
        Movie(2, "Film 2", "FILM"),
        Movie(3, "Film 3", "FILM")
    )
    val navController = rememberNavController()
    val movieViewModel = remember {
        MovieViewModel(MovieRepository(NetworkModule.movieApi))
    }

    Scaffold(
        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Principal") },
                    label = { Text("Principal") },
                    selected = false,
                    onClick = { navController.navigate("principal") }
                )


                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Films") },
                    label = { Text("Film") },
                    selected = false,
                    onClick = { navController.navigate("movie") }
                )
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "principal"
        ) {

            composable("principal") {
                PrincipalScreen(navController, movies)
            }


            composable("movie") {
                MovieScreen(vm = movieViewModel)
            }

            composable("detail/{id}") { backStackEntry ->

                val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0

                DetailScreen(id, movies)

            }

        }
    }
}
