package com.example.movieTracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.movieTracker.presentation.watchlist.components.BookmarkButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    showBackButton: Boolean = true,
    collapseAppbar: Boolean = false,
    showBookmark: Boolean = false,
    movieId: Int = 0,
    navController: NavController?,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    TopAppBar(
        //  modifier = Modifier.statusBarsPadding(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,

            ),
        title = {
            Text(title)

        },
        actions = {
            if (showBookmark)
                BookmarkButton(movieId = movieId)
        },

        scrollBehavior = if (collapseAppbar) null else scrollBehavior,
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = {
                    navController?.popBackStack()
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "")
                }
            }

        })


}