package com.example.cinesio.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Theaters
import androidx.compose.material.icons.rounded.Person
import com.example.cinesio.data.repository.GenreRepository
import com.example.cinesio.data.repository.MovieRepository
import com.example.cinesio.data.repository.VideoRepository
import com.example.cinesio.network.NetworkModule
import com.example.cinesio.viewmodel.MovieDetailViewModel
import com.example.cinesio.viewmodel.MovieViewModel
import android.content.Context
import com.example.cinesio.data.local.database.AppDatabase
import com.example.cinesio.data.repository.CommentaireRepository
import com.example.cinesio.data.repository.FriendRepository
import com.example.cinesio.data.repository.UserFilmRepository
import com.example.cinesio.data.repository.UserRepository
import com.example.cinesio.viewmodel.CommentaireViewModel
import com.example.cinesio.viewmodel.FriendViewModel
import com.example.cinesio.viewmodel.UserFilmViewModel
import com.example.cinesio.viewmodel.UserViewModel

@Composable
fun MainScreen(darkTheme: Boolean, onThemeUpdated: () -> Unit, context: Context) {

    val navController = rememberNavController()

    val database = AppDatabase.getDatabase(context)

    /**
     * DAO
     * */
    val movieDao = database.movieDao()
    val userDao = database.userDao()
    val friendDao = database.friendDao()
    val commentaireDao = database.commentaireDao()
    val userFilmDao = database.userFilmDao()

    /**
     * Repositories
     * */
    val movieRepository = remember { MovieRepository(NetworkModule.movieApi, movieDao) }
    val genreRepository = remember { GenreRepository(NetworkModule.movieApi) }
    val videoRepository = remember { VideoRepository(NetworkModule.movieApi) }

    val userRepository = remember { UserRepository(userDao) }
    val friendRepository = remember { FriendRepository(friendDao) }
    val commentaireRepository = remember { CommentaireRepository(commentaireDao) }
    val userFilmRepository = remember { UserFilmRepository(userFilmDao) }

    /**
     * View Models
     * */
    val movieViewModel = remember { MovieViewModel(movieRepository, genreRepository) }
    val movieDetailViewModel = remember { MovieDetailViewModel(movieRepository, videoRepository) }

    val userViewModel = remember { UserViewModel(userRepository) }
    val friendViewModel = remember { FriendViewModel(friendRepository) }
    val commentaireViewModel = remember { CommentaireViewModel(commentaireRepository) }
    val userFilmViewModel = remember { UserFilmViewModel(userFilmRepository) }


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            val activeColor = MaterialTheme.colorScheme.primary
            val inactiveColor = Color(0xFF9A9A9A)
            val borderColor = MaterialTheme.colorScheme.outline

            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.height(95.dp).drawBehind {
                    val width = 3.dp.toPx()
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = width
                    )
                }
            ) {
                @Composable
                fun navItem(icon: @Composable () -> Unit, label: String, route: String) {
                    NavigationBarItem(
                        icon = icon,
                        label = { Text(label, color = if (currentRoute == route) activeColor else inactiveColor) },
                        selected = currentRoute == route,
                        onClick = { navController.navigate(route) },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = activeColor,
                            unselectedIconColor = inactiveColor,
                            selectedTextColor = activeColor,
                            unselectedTextColor = inactiveColor,
                            indicatorColor = Color.Transparent
                        )
                    )
                }

                navItem({ Icon(Icons.Rounded.Home, contentDescription = "Principal", modifier = Modifier.size(22.dp)) }, "Principal", "principal")
                navItem({ Icon(Icons.Rounded.Theaters, contentDescription = "Films", modifier = Modifier.size(22.dp)) }, "Films", "movie")
                navItem({ Icon(Icons.Rounded.Person, contentDescription = "Profil", modifier = Modifier.size(22.dp)) }, "Profil", "profile")
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "principal",
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            composable("principal") {
                PrincipalScreen(
                    navController = navController,
                    vm = movieViewModel
                )
            }
            composable("movie") { MovieScreen(vm = movieViewModel, navController = navController) }
            composable("profile") { ProfilScreen(navController, darkTheme = darkTheme, onThemeUpdated = onThemeUpdated) }
            composable("login") { LoginScreen(navController) }
            composable("register") { RegisterScreen(navController) }
            composable("detail/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
                DetailScreen(vm = movieDetailViewModel, movieId = id)
            }
        }
    }
}