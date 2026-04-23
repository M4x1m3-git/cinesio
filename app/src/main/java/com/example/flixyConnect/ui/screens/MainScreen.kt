package com.example.flixyConnect.ui.screens

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
import com.example.flixyConnect.data.repository.GenreRepository
import com.example.flixyConnect.data.repository.MovieRepository
import com.example.flixyConnect.data.repository.VideoRepository
import com.example.flixyConnect.network.NetworkModule
import com.example.flixyConnect.viewmodel.MovieDetailViewModel
import com.example.flixyConnect.viewmodel.MovieViewModel
import android.content.Context
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flixyConnect.data.local.database.AppDatabase
import com.example.flixyConnect.data.repository.CommentaireRepository
import com.example.flixyConnect.data.repository.UserFilmRepository
import com.example.flixyConnect.data.repository.UserRepository
import com.example.flixyConnect.viewmodel.CommentaireViewModel
import com.example.flixyConnect.viewmodel.UserFilmViewModel
import com.example.flixyConnect.viewmodel.UserViewModel

@Composable
fun MainScreen(darkTheme: Boolean, onThemeUpdated: () -> Unit, context: Context) {

    val navController = rememberNavController()

    val database = AppDatabase.getDatabase(context)

    /**
     * DAO
     * */
    val movieDao = database.movieDao()
    val userDao = database.userDao()
    val commentaireDao = database.commentaireDao()
    val userFilmDao = database.userFilmDao()

    /**
     * Repositories
     * */
    val movieRepository = remember { MovieRepository(NetworkModule.movieApi, movieDao) }
    val genreRepository = remember { GenreRepository(NetworkModule.movieApi) }
    val videoRepository = remember { VideoRepository(NetworkModule.movieApi) }
    val userRepository = remember { UserRepository(userDao) }
    val commentaireRepository = remember { CommentaireRepository(commentaireDao) }
    val userFilmRepository = remember { UserFilmRepository(userFilmDao) }

    /**
     * View Models
     * */
    val movieViewModel = remember { MovieViewModel(movieRepository, genreRepository) }
    val movieDetailViewModel = remember { MovieDetailViewModel(movieRepository, videoRepository) }

    val userViewModel: UserViewModel = viewModel(viewModelStoreOwner = LocalContext.current as androidx.activity.ComponentActivity)
    val commentaireViewModel = remember { CommentaireViewModel(commentaireRepository) }
    val userFilmViewModel = remember { UserFilmViewModel(userFilmRepository) }


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            val activeColor = MaterialTheme.colorScheme.primary
            val inactiveColor = Color(0xFF9A9A9A)
            val borderColor = MaterialTheme.colorScheme.outline
            val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val savedEmail = sharedPreferences.getString("email", null)

            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.height(100.dp).drawBehind {
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
                if (savedEmail != null) {
                    navItem({ Icon(Icons.Rounded.Bookmark, contentDescription = "Enregistré", modifier = Modifier.size(22.dp)) }, "Favoris", "favorites")
                }
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
                    vm = movieViewModel,
                    context= context,
                    userFilmViewModel = userFilmViewModel
                )
            }
            composable("movie") { MovieScreen(vm = movieViewModel, navController = navController, context= context, userFilmViewModel = userFilmViewModel) }
            composable("profile") { ProfilScreen(navController, darkTheme = darkTheme, onThemeUpdated = onThemeUpdated) }
            composable("login") { LoginScreen(navController) }
            composable("register") { RegisterScreen(navController) }
            composable("favorites") { FavoritesScreen(
                navController,
                context,
                userFilmViewModel,
                allMovies = movieViewModel.state.collectAsState().value.movies
            ) }
            composable("detail/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
                DetailScreen(vm = movieDetailViewModel, movieId = id, context = context, userFilmViewModel=userFilmViewModel)
            }
        }
    }
}