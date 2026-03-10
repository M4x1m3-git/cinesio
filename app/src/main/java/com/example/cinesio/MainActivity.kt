package com.example.cinesio

import android.R
import android.R.attr.onClick
import android.graphics.pdf.content.PdfPageGotoLinkContent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.cinesio.ui.screens.MainScreen
import com.example.cinesio.ui.theme.CinesioTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cinesio.data.model.Movie

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setContent {
            CinesioTheme {
                MainScreen()
            }
        }

    }
}



//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}

@Composable
fun elMetierText(film: Movie, onClick: (Movie) -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 240.dp, height = 100.dp)
            .clickable {
                onClick(film)
            }
    ) {
        Text(
            text = film.title,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(16.dp)
        )
    }

}

@Composable
fun listElMetier(
    films: List<Movie>,
    onItemClick: (Movie) -> Unit
) {

    Column (
        modifier = Modifier
            .padding(16.dp)

    ) {

        if (films.isEmpty()) {

            Text("Aucun film trouvé")

        } else {

            for (film in films) {

                elMetierText(film) {
                    onItemClick(it)
                }

            }

        }

    }

}

@Composable
fun RedTitle() {
    Text(
        "Cinesio",
        color = Color(211, 34, 34, 255),
        fontSize = 50.sp
    )
}

// Pour avoir un apercu du rendu
@Preview(showBackground = true)
@Composable
fun RedTitlePreview() {
    RedTitle()
}

// Pour avoir un apercu du rendu
//@Preview(showBackground = true)
//@Composable
//fun elMetierPreview() {
//    val films = listOf(
//        Movie(1, "Movie 1", "Problème de connexion"),
//        Movie(2, "Movie 2", "Erreur lors du paiement"),
//        Movie(3, "Movie 3", "Application qui crash")
//    )
//    listElMetier(*films)
//}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MobileManagerTheme {
//        Greeting("Android")
//    }
//}