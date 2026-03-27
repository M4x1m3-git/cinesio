package com.example.flixyConnect.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.flixyConnect.data.model.Movie
import com.example.flixyConnect.viewmodel.UserFilmViewModel
import kotlin.math.round

@Composable
fun movieCard(
    movie: Movie,
    repoGenre: Map<Int, String>? = null,
    upcoming: Boolean = false,
    onItemClick: (Movie) -> Unit,
    context: Context,
    userFilmViewModel: UserFilmViewModel
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .clickable { onItemClick(movie) }
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Box {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                AsyncImage(
                    model = movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
                    contentDescription = movie.title,
                    modifier = Modifier
                        .width(150.dp)
                        .height(225.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {

                    Text(
                        text = movie.title,
                        fontSize = 24.sp,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (upcoming) {
                        movie.releaseDate?.let { date ->
                            Text(
                                text = "Sortie : ${formatReleaseDate(date)}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    } else {
                        movie.voteAverage?.let { vote ->
                            val note = String.format("%.1f", vote)
                            val vCount = movie.voteCount ?: 0
                            val nbVote = formatNumber(vCount)

                            Text(
                                text = "Notation: $note ($nbVote avis)",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val genreNames = movie.genre_ids.mapNotNull { id ->
                        repoGenre?.get(id)
                    }

                    if (genreNames.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            genreCards(names = genreNames)
                        }
                    }

                }

            }

            val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("userId", -1)

            if (userId != -1) {
                BookmarkButton(
                    userFilmViewModel = userFilmViewModel,
                    userId = userId,
                    tmdbId = movie.id,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .zIndex(1f),
                    backgroundColor = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(12.dp),
                    iconSize = 20.dp,
                    paddingInside = 6.dp
                )
            }
        }
    }
}

@Composable
fun genreCards(names: List<String>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        names.forEach { name ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = name,
                    fontSize = 14.sp
                )
            }
        }
    }
}