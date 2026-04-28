package com.example.flixyConnect.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flixyConnect.ui.components.MovieCarousel
import com.example.flixyConnect.ui.components.RedTitle
import com.example.flixyConnect.ui.components.UpcomingList
import com.example.flixyConnect.viewmodel.MovieViewModel
import com.example.flixyConnect.viewmodel.UserFilmViewModel
import com.example.flixyConnect.data.model.Movie
import com.example.flixyConnect.ui.components.ErrorScreen

@Composable
fun PrincipalScreen(navController: NavController, vm: MovieViewModel, context: Context, userFilmViewModel: UserFilmViewModel) {
    val state by vm.state.collectAsState()

    val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getInt("userId", -1)

    LaunchedEffect(userId) {
        if (userId != -1) {
            userFilmViewModel.loadUserFilms(userId)
        }
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        RedTitle("Flixy Connect")

        when {
            state.loading -> {
                CircularProgressIndicator()
            }
            state.error != null -> {
                ErrorScreen(
                    error = state.error ?: "Erreur",
                    onRetry = {
                        vm.loadMovies()
                    }
                )
            }

            else -> {

                MovieCarousel(
                    movies = state.movies,
                    repoGenre = state.genreMap,
                    onItemClick = { movie ->
                        navController.navigate("detail/${movie.id}")
                    },
                    onSeeAllClick = {
                        navController.navigate("movie")
                    },
                    context = context,
                    userFilmViewModel = userFilmViewModel
                )

                Spacer(modifier = Modifier.height(16.dp))

                UpcomingList(
                    movies = state.upcoming,
                    repoGenre = state.genreMap,
                    onItemClick = { movie ->
                        navController.navigate("detail/${movie.id}")
                    }, context= context, userFilmViewModel = userFilmViewModel
                )
            }
        }
    }
}