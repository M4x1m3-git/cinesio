package com.example.cinesio.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.cinesio.data.model.Movie

@Composable
fun elMetierText(movie: Movie, onClick: (Movie) -> Unit) {

    Text(
        text = movie.title,
        fontSize = 20.sp,
        modifier = Modifier.clickable {
            onClick(movie)
        }
    )

}