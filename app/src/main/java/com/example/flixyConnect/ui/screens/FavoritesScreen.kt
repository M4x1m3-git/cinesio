package com.example.flixyConnect.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flixyConnect.ui.components.movieCard
import com.example.flixyConnect.ui.components.RedTitle
import com.example.flixyConnect.viewmodel.UserFilmViewModel
import com.example.flixyConnect.data.model.Movie

@Composable
fun FavoritesScreen(
    navController: NavController,
    context: Context,
    userFilmViewModel: UserFilmViewModel,
    allMovies: List<Movie> // on a besoin de cette liste pour matcher tmdbId
) {
    val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getInt("userId", -1)

    // Charger les films de l'utilisateur au lancement
    LaunchedEffect(userId) {
        if (userId != -1) {
            userFilmViewModel.loadUserFilms(userId)
        }
    }

    val userFilms by userFilmViewModel.userFilms.collectAsState()

    val favoriteMovies = remember(userFilms) {
        // on garde uniquement les films où saved == true
        userFilms.filter { it.saved }.mapNotNull { userFilm ->
            allMovies.find { it.id == userFilm.tmdbId }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        RedTitle("Films enregistré")

        if (userId == -1) {
            Text("Vous devez être connecté pour voir vos films enregistré.")
            return@Column
        }

        if (favoriteMovies.isEmpty()) {
            Text("Aucun film favori pour le moment.")
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            favoriteMovies.forEach { movie ->
                movieCard(
                    movie = movie,
                    onItemClick = { navController.navigate("detail/${movie.id}")},
                    context= context,
                    userFilmViewModel = userFilmViewModel
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}