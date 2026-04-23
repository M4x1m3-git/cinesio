package com.example.flixyConnect.ui.screens

import android.app.Application
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.flixyConnect.data.local.database.AppDatabase
import com.example.flixyConnect.data.local.entity.CommentaireEntity
import com.example.flixyConnect.data.local.entity.UserFilmEntity
import com.example.flixyConnect.ui.components.CommentItem
import com.example.flixyConnect.ui.components.ErrorScreen
import com.example.flixyConnect.ui.components.YouTubeButton
import com.example.flixyConnect.viewmodel.CommentaireViewModel
import com.example.flixyConnect.viewmodel.MovieDetailViewModel
import com.example.flixyConnect.viewmodel.UserFilmViewModel
import com.example.flixyConnect.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun DetailScreen(
    vm: MovieDetailViewModel,
    movieId: Int,
    context: Context,
    userFilmViewModel: UserFilmViewModel
) {
    val commentaireVm = remember {
        CommentaireViewModel(
            repository = com.example.flixyConnect.data.repository.CommentaireRepository(
                AppDatabase.getDatabase(context).commentaireDao()
            )
        )
    }

    val userVm = remember { UserViewModel(context.applicationContext as Application) }

    var userFilm by remember { mutableStateOf<UserFilmEntity?>(null) }
    val state by vm.state.collectAsState()
    val commentaires by commentaireVm.commentaires.collectAsState()
    val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getInt("userId", -1)
    var newComment by remember { mutableStateOf("") }

    // Map userFilmId -> username
    var authorMap by remember { mutableStateOf(mapOf<Int, String>()) }

    // Charger les données au lancement
    LaunchedEffect(movieId) {
        vm.loadMovieDetails(movieId)
        commentaireVm.loadCommentairesByFilm(movieId)
        userVm.loadUsers()
        if (userId != -1) {
            userFilmViewModel.getOrCreateUserFilm(userId, movieId) { userFilm = it }
        }
    }

    val users by userVm.users.collectAsState()

    // Construire le map des auteurs correctement
    LaunchedEffect(commentaires, users) {
        val db = AppDatabase.getDatabase(context)
        val map = mutableMapOf<Int, String>()

        withContext(Dispatchers.IO) {
            commentaires.forEach { c ->
                val uf = db.userFilmDao().getById(c.userFilmId)
                val username = uf?.let { u ->
                    users.find { it.id == u.userId }?.username
                } ?: "Anonyme"
                map[c.userFilmId] = username
            }
        }

        authorMap = map
    }

    val userComment = userFilm?.let { uc ->
        commentaires.find { it.userFilmId == uc.id }
    }

    when {
        state.loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        state.error != null -> {
            ErrorScreen(
                error = state.error ?: "Erreur",
                onRetry = {
                    vm.loadMovieDetails(movieId)
                }
            )
        }

        state.movie != null -> {
            val movie = state.movie!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Section détails film
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(movie.title, style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                            contentDescription = movie.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(movie.overview ?: "", style = MaterialTheme.typography.bodyLarge)
                        movie.vote_average?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Note générale : ${String.format("%.1f", it)}", style = MaterialTheme.typography.bodyLarge)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        state.trailer?.let { trailer ->
                            YouTubeButton(videoKey = trailer.key)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Section note utilisateur
                Text("Votre note", style = MaterialTheme.typography.titleMedium)
                var userRating by remember { mutableStateOf(userFilm?.rating ?: 0f) }
                Row {
                    for (i in 1..5) {
                        IconButton(onClick = {
                            userRating = i.toFloat()
                            userFilm?.let { uf ->
                                userFilmViewModel.updateRating(uf.id, userRating)
                                userFilm = uf.copy(rating = userRating)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Noter le film",
                                tint = if (i <= userRating) Color.Yellow else Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Commentaires", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))

                // Section commentaires
                if (commentaires.isEmpty()) {
                    Text("Aucun commentaire pour ce film")
                } else {
                    commentaires.forEach { c ->
                        CommentItem(
                            commentaire = c,
                            authorName = authorMap[c.userFilmId] ?: "Anonyme",
                            isOwner = userFilm?.id == c.userFilmId,
                            onUpdate = { updated ->
                                commentaireVm.updateCommentaire(updated, movieId)
                            },
                            onDelete = { toDelete ->
                                commentaireVm.deleteCommentaire(toDelete, movieId)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Ajouter un nouveau commentaire
                if (userComment == null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = newComment,
                        onValueChange = { newComment = it },
                        label = { Text("Ajouter un commentaire") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (userId != -1 && newComment.isNotBlank()) {
                                userFilm?.let { uf ->
                                    commentaireVm.addCommentaire(
                                        CommentaireEntity(
                                            userFilmId = uf.id,
                                            comment = newComment,
                                            createdAt = System.currentTimeMillis()
                                        ),
                                        movieId
                                    )
                                    newComment = ""
                                }
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Poster")
                    }
                }
            }
        }
    }
}