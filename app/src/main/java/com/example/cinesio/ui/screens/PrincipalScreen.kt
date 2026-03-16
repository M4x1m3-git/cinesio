package com.example.cinesio.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cinesio.ui.components.MovieCarousel
import com.example.cinesio.ui.components.RedTitle
import com.example.cinesio.ui.components.UpcomingList
import com.example.cinesio.viewmodel.MovieViewModel

@Composable
fun PrincipalScreen(navController: NavController, vm: MovieViewModel) {
    val state by vm.state.collectAsState()

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        RedTitle("CinéSio")

        when {
            state.loading -> {
                CircularProgressIndicator()
            }
            state.error != null -> {
                Text("Erreur : ${state.error}")
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
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                UpcomingList(
                    movies = state.upcoming,
                    repoGenre = state.genreMap,
                    onItemClick = { movie ->
                        navController.navigate("detail/${movie.id}")
                    }
                )
            }
        }
    }
}