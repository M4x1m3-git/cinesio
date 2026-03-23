package com.example.cinesio.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cinesio.data.local.database.AppDatabase
import com.example.cinesio.data.repository.GenreRepository
import com.example.cinesio.data.repository.MovieRepository
import com.example.cinesio.network.NetworkModule
import com.example.cinesio.ui.components.ThemeSwitcher
import com.example.cinesio.ui.components.logout
import com.example.cinesio.ui.theme.Inter
import com.example.cinesio.viewmodel.MovieViewModel
import com.example.cinesio.viewmodel.UserViewModel
import kotlin.collections.emptyList

@Composable
fun ProfilScreen(
    navController: NavController,
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    val database = remember { AppDatabase.getDatabase(context) }
    val movieDao = remember { database.movieDao() }

    val movieRepository = remember {
        MovieRepository(
            api = NetworkModule.movieApi,
            dao = movieDao
        )
    }

    val genreRepository = remember {
        GenreRepository(NetworkModule.movieApi)
    }

    val movieViewModel = remember {
        MovieViewModel(movieRepository, genreRepository)
    }
    val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    val snackbarHostState = remember { SnackbarHostState() }
    val currentUser by viewModel.currentUser.collectAsState()
    val reviewCount by viewModel.reviewCount.collectAsState(initial = 0)
    val savedMoviesCount by viewModel.savedMoviesCount.collectAsState(initial = 0)
    val comments by viewModel.comments.collectAsState(initial = emptyList())
    val movieState by movieViewModel.state.collectAsState()
    val userFilms by viewModel.userFilms.collectAsState(initial = emptyList())

    val filmTitleMap = remember(movieState, userFilms) {
        val map = mutableMapOf<Int, String>()
        userFilms.forEach { uf ->
            val movie = movieState.movies.find { it.id == uf.tmdbId }
                ?: movieState.upcoming.find { it.id == uf.tmdbId }
            if (movie != null) map[uf.tmdbId] = movie.title
        }
        map
    }

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        val userId = sharedPreferences.getInt("userId", -1)
        if (userId != -1) {
            viewModel.loadUser(userId)
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                ThemeSwitcher(darkTheme = darkTheme, size = 30.dp, onClick = onThemeUpdated)
            }

            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                if (currentUser == null) {

                    Spacer(modifier = Modifier.height(80.dp))

                    Icon(
                        imageVector = Icons.Default.PersonOutline,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Bienvenue", fontFamily = Inter, fontSize = 24.sp)
                    Text(
                        "Connecte-toi pour accéder à ton profil",
                        fontFamily = Inter,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = { navController.navigate("login") },
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Se connecter", fontFamily = Inter) }

                } else {

                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(currentUser?.username ?: "", fontFamily = Inter, fontSize = 22.sp)
                    Text(currentUser?.email ?: "", fontFamily = Inter, color = MaterialTheme.colorScheme.secondary)

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(reviewCount.toString(), color = MaterialTheme.colorScheme.primary, fontFamily = Inter, fontSize = 20.sp)
                                Text("AVIS POSTÉS", fontFamily = Inter, fontSize = 12.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(savedMoviesCount.toString(), color = MaterialTheme.colorScheme.primary, fontFamily = Inter, fontSize = 20.sp)
                                Text("ENREGISTRÉS", fontFamily = Inter, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Sorties de films", fontFamily = Inter)
                                Text("Soyez alerté quand un film enregistré sort", fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.secondary, fontFamily = Inter
                                )
                            }
                            var enabled by remember { mutableStateOf(true) }
                            Switch(checked = enabled, onCheckedChange = { enabled = it })
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.MailOutline, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Mes commentaires", fontFamily = Inter)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (comments.isEmpty()) {
                        Text("Aucun commentaire pour le moment", fontFamily = Inter, color = MaterialTheme.colorScheme.secondary)
                    } else {
                        comments.forEach { comment ->

                            val userFilm = userFilms.find { it.id == comment.userFilmId }

                            val tmdbId = userFilm?.tmdbId

                            val movieTitle = filmTitleMap[userFilm?.tmdbId] ?: "Film inconnu"

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable {
                                        tmdbId?.let {
                                            navController.navigate("detail/$it")
                                        }
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = movieTitle,
                                            fontFamily = Inter,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = comment.comment,
                                            fontFamily = Inter,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 2,
                                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            val userId = currentUser?.id ?: return@IconButton
                                            viewModel.deleteComment(comment, userId)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Supprimer",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = {
                            logout(sharedPreferences, viewModel)
                            navController.navigate("login") { popUpTo("profile") { inclusive = true } }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Se déconnecter", fontFamily = Inter)
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}