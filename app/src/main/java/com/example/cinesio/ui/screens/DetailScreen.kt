package com.example.cinesio.ui.screens

import YouTubeButton
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cinesio.viewmodel.MovieDetailViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
@Composable
fun DetailScreen(vm: MovieDetailViewModel, movieId: Int) {
    LaunchedEffect(movieId) { vm.loadMovieDetails(movieId) }

    val state by vm.state.collectAsState()

    when {
        state.loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        state.error != null -> Text(
            text = "Erreur : ${state.error}",
            modifier = Modifier.padding(16.dp)
        )

        state.movie != null -> {
            val movie = state.movie!!

            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
            ) {
                Text(movie.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))

                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxWidth().height(400.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(movie.overview ?: "")

                movie.vote_average?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    val note = String.format("%.1f", it)
                    Text("Note : $note")
                }

                Spacer(modifier = Modifier.height(16.dp))

                state.trailer?.let { trailer ->

                    YouTubeButton(
                        videoKey = trailer.key
                    )

                }
            }
        }
    }
}
