package com.example.cinesio.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.example.cinesio.data.model.Movie

@Composable
fun DetailScreen(id: Int, movies: List<Movie>) {

    val movie = movies.find { it.id == id }

    Column {

        Text(
            text = movie?.title ?: "Film inconnu",
            fontSize = 30.sp
        )

        Text(
            text = movie?.overview ?: "",
            fontSize = 20.sp
        )

    }

}
