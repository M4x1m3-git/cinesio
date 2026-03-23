package com.example.cinesio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cinesio.viewmodel.UserFilmViewModel

@Composable
fun BookmarkButton(
    userFilmViewModel: UserFilmViewModel,
    userId: Int,
    tmdbId: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black.copy(alpha = 0.7f),
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    iconSize: Dp = 20.dp,
    paddingInside: Dp = 6.dp
) {
    val userFilms by userFilmViewModel.userFilms.collectAsState()

    val isSaved = userFilms.firstOrNull { it.tmdbId == tmdbId }?.saved == true

    Box(
        modifier = modifier
            .background(backgroundColor, shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                userFilmViewModel.toggleSaved(userId, tmdbId)
            }
            .padding(paddingInside)
    ) {
        SavedIcon(
            isSaved = isSaved,
            iconSize = iconSize
        )
    }
}

@Composable
fun SavedIcon(
    isSaved: Boolean,
    iconSize: Dp
) {
    Icon(
        imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.Bookmark,
        contentDescription = "Bookmark",
        tint = if (isSaved) Color(0xFFFFA500) else Color.White
    )
}