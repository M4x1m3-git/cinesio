package com.example.cinesio.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import coil.compose.AsyncImage
import com.example.cinesio.data.model.Movie
import com.example.cinesio.viewmodel.MovieViewModel

@Composable
fun MovieScreen(vm: MovieViewModel) {

    val state by vm.state.collectAsState()

    Column {

        Button(onClick = { vm.loadMovies() }) {
            Text("Charger les films au cinéma")
        }

        when {

            state.loading ->
                CircularProgressIndicator()

            state.error != null ->
                Text("Erreur : ${state.error}")

            else ->

                LazyColumn {

                    items(state.movies) { movie ->

                        MovieItem(movie)
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