package com.example.cinesio.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.cinesio.data.local.entity.CommentaireEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CommentItem(
    commentaire: CommentaireEntity,
    authorName: String = "Utilisateur inconnu",
    isOwner: Boolean = false,
    onUpdate: ((CommentaireEntity) -> Unit)? = null,
    onDelete: ((CommentaireEntity) -> Unit)? = null
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedText by remember { mutableStateOf(commentaire.comment) }
    var expanded by remember { mutableStateOf(false) }

    val formattedDate = remember(commentaire.createdAt) {
        val sdf = SimpleDateFormat("dd MMM yyyy 'à' HH:mm", Locale.getDefault())
        sdf.format(Date(commentaire.createdAt))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            if (isEditing) {
                OutlinedTextField(
                    value = editedText,
                    onValueChange = { editedText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Modifier le commentaire") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        onUpdate?.invoke(commentaire.copy(comment = editedText))
                        isEditing = false
                    }) {
                        Text("Valider")
                    }

                    OutlinedButton(onClick = {
                        isEditing = false
                        editedText = commentaire.comment
                    }) {
                        Text("Annuler")
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        // Auteur + date
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                text = authorName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = formattedDate,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = commentaire.comment,
                            modifier = Modifier.clickable { expanded = !expanded },
                            maxLines = if (expanded) Int.MAX_VALUE else 3,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (isOwner && (onUpdate != null || onDelete != null)) {
                        Row {
                            if (onUpdate != null) {
                                IconButton(onClick = { isEditing = true }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Modifier"
                                    )
                                }
                            }

                            if (onDelete != null) {
                                IconButton(onClick = { onDelete(commentaire) }) {
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
            }
        }
    }
}