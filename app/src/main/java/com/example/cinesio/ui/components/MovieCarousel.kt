package com.example.cinesio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cinesio.data.model.Movie

@Composable
fun MovieCarousel(
    movies: List<Movie>,
    repoGenre: Map<Int, String>? = null,
    onItemClick: (Movie) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "À l'affiche",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Voir tout",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = 16.dp, bottom = 16.dp)
        ) {
            movies.forEach { movie ->
                MovieCarouselCard(movie = movie, repoGenre = repoGenre, onClick = onItemClick)
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}

@Composable
fun MovieCarouselCard(
    movie: Movie,
    repoGenre: Map<Int, String>? = null,
    onClick: (Movie) -> Unit
) {
    Column(
        modifier = Modifier
            .width(180.dp)
            .clickable { onClick(movie) }
    ) {
        Box(modifier = Modifier.height(270.dp)) {
            AsyncImage(
                model = movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(4.dp)
            )

            movie.voteAverage?.let { vote ->
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.7f), shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.StarBorder,
                            contentDescription = "Étoile",
                            tint = Color(0xFFFFA500)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", vote),
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = movie.title,
            maxLines = 2,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        val genres = movie.genre_ids.mapNotNull { repoGenre?.get(it) }
        if (genres.isNotEmpty()) {
            Text(
                text = genres.joinToString(", "),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}