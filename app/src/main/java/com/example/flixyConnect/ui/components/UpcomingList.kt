package com.example.flixyConnect.ui.components

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flixyConnect.data.model.Movie
import com.example.flixyConnect.viewmodel.UserFilmViewModel

@Composable
fun UpcomingList(
    movies: List<Movie>,
    repoGenre: Map<Int, String>? = null,
    onItemClick: (Movie) -> Unit,
    context: Context,
    userFilmViewModel: UserFilmViewModel
) {
    Column(modifier = Modifier.fillMaxWidth())
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Bientôt en salle",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            modifier = Modifier
            .padding(start = 16.dp, bottom = 16.dp)
        ) {
            movies.forEach { movie ->
                movieCard(movie = movie, repoGenre = repoGenre, upcoming = true, onItemClick = onItemClick, context= context, userFilmViewModel = userFilmViewModel)
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}