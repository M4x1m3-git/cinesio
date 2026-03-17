package com.example.cinesio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.database.DatabaseProvider
import androidx.navigation.NavController
import com.example.cinesio.data.local.database.AppDatabase
import com.example.cinesio.data.repository.UserRepository
import com.example.cinesio.ui.theme.CinesioTheme
import com.example.cinesio.ui.theme.Inter
import com.example.cinesio.viewmodel.UserViewModel

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val repository = UserRepository(db.userDao())
    val viewModel = remember { UserViewModel(repository) }
    val currentUser by viewModel.currentUser.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            navController.navigate("profile")
        }
    }

    CinesioTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Movie,
                    contentDescription = "Movie Icon",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "BON RETOUR",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Inter
            )

            Text(
                text = "Connectez-vous pour continuer",
                fontSize = 16.sp,
                color = Color.Gray,
                fontFamily = Inter,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Text(
                text = "Adresse email",
                fontSize = 14.sp,
                fontFamily = Inter,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                },
                leadingIcon = { Icon(Icons.Default.MailOutline, contentDescription = "Email") },
                placeholder = { Text("Entrez votre email") },
                isError = emailError,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontFamily = Inter)
            )
            if (emailError) {
                Text(
                    text = "Veuillez entrer un email valide",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    fontFamily = Inter,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Mot de passe",
                fontSize = 14.sp,
                fontFamily = Inter,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                placeholder = { Text("Entrez votre mot de passe") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontFamily = Inter)
            )

            TextButton(
                onClick = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                contentPadding = PaddingValues(0.dp),
            ) {
                Text(
                    text = "Mot de passe oublié ?",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = Inter
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Se connecter", fontSize = 16.sp, fontFamily = Inter)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Pas encore de compte ?", fontSize = 14.sp, color = Color.Gray, fontFamily = Inter)
                TextButton(onClick = { navController.navigate("register") }) {
                    Text(text = "Créer un compte", fontSize = 14.sp, fontFamily = Inter)
                }
            }
        }
    }
}