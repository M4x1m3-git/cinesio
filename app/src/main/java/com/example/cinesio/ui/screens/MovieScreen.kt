package com.example.cinesio.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.cinesio.data.model.Movie
import com.example.cinesio.ui.components.movieCard
import com.example.cinesio.viewmodel.MovieViewModel
import com.example.cinesio.viewmodel.UserFilmViewModel

@Composable
fun MovieScreen(vm: MovieViewModel, navController: NavController, context: Context, userFilmViewModel: UserFilmViewModel) {
    val state by vm.state.collectAsState()

    Column {
        when {
            state.loading -> CircularProgressIndicator()
            state.error != null -> Text("Erreur : ${state.error}")
            else -> LazyColumn {
                items(state.movies) { movie ->
                    movieCard(
                        movie,
                        repoGenre = state.genreMap,
                        upcoming = false,
                        onItemClick = { navController.navigate("detail/${movie.id}") },
                        context= context,
                        userFilmViewModel = userFilmViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie) {

    Column {
        Text(movie.title)
        movie.posterPath?.let {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500$it",
                contentDescription = movie.title
            )
        }
    }
}