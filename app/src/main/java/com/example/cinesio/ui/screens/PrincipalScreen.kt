package com.example.cinesio.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.cinesio.RedTitle
import com.example.cinesio.data.model.Movie
import com.example.cinesio.listElMetier

@Composable
fun PrincipalScreen(navController: NavController, movies: List<Movie>) {


    Column {

        RedTitle()

        listElMetier(movies) { movie ->

            navController.navigate("detail/${movie.id}")

        }

    }

}