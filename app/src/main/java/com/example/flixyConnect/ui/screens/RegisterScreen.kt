package com.example.flixyConnect.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.flixyConnect.data.local.database.AppDatabase
import com.example.flixyConnect.data.local.entity.UserEntity
import com.example.flixyConnect.data.repository.UserRepository
import com.example.flixyConnect.ui.theme.FlixyConnectTheme
import com.example.flixyConnect.ui.theme.Inter
import com.example.flixyConnect.viewmodel.UserViewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {

    var PREFS_KEY = "prefs"
    var EMAIL_KEY = "email"
    var USERNAME_KEY = "username"

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

    val db = AppDatabase.getDatabase(context)
    val repository = UserRepository(db.userDao())

    val userViewModel: UserViewModel = viewModel()
    val currentUser by userViewModel.currentUser.collectAsState()

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }

    val colors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onBackground,
        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
        disabledTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        errorTextColor = MaterialTheme.colorScheme.error,

        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        disabledContainerColor = MaterialTheme.colorScheme.surface,
        errorContainerColor = MaterialTheme.colorScheme.surface,

        cursorColor = MaterialTheme.colorScheme.primary,
        errorCursorColor = MaterialTheme.colorScheme.error,

        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
        errorBorderColor = MaterialTheme.colorScheme.error,

        focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
        errorLeadingIconColor = MaterialTheme.colorScheme.error,

        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
        errorPlaceholderColor = MaterialTheme.colorScheme.error
    )


    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            navController.navigate("profile") {
                // On vide la pile pour éviter de revenir sur Register avec le bouton retour
                popUpTo("register") { inclusive = true }
            }
        }
    }

    FlixyConnectTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
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
                text = "Créer un compte",
                fontSize = 28.sp,
                fontFamily = Inter,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Remplissez vos informations pour commencer",
                fontSize = 16.sp,
                fontFamily = Inter,
                color = Color.Gray,
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
                placeholder = {
                    Text(
                        "Entrez votre email",
                        fontFamily = Inter,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                isError = emailError,
                textStyle = LocalTextStyle.current.copy(
                    fontFamily = Inter,
                ),
                colors = colors,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
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
                text = "Nom d'utilisateur",
                fontSize = 14.sp,
                fontFamily = Inter,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                leadingIcon = { Icon(Icons.Default.PersonOutline, contentDescription = "Nom d'utilisateur") },
                placeholder = { Text("Entrez votre nom", fontFamily = Inter, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontFamily = Inter
                ),
                colors = colors,
            )

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
                placeholder = { Text("Entrez votre mot de passe", fontFamily = Inter, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontFamily = Inter),
                colors = colors,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isNotEmpty() && !emailError && username.isNotEmpty() && password.isNotEmpty()) {
                        val newUser = UserEntity(email = email, password = password, username = username)
                        userViewModel.register(newUser) { success ->
                            if (!success) {
                                Toast.makeText(context, "Email déjà utilisé", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Champs invalides", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Créer un compte", fontSize = 16.sp, fontFamily = Inter)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Déjà un compte ?",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = Inter
                )
                TextButton(onClick = { navController.navigate("login") }) {
                    Text(text = "Connexion", fontSize = 14.sp, fontFamily = Inter)
                }
            }
        }
    }
}