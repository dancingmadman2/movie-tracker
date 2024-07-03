package com.example.movieTracker.presentation.movie_list.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.movieTracker.domain.model.Movie

@Composable
fun MovieListItem(
    movie: Movie,
    onItemClick: (Movie) -> Unit
) {

    Column(
        modifier = Modifier

            .clickable { onItemClick(movie) }
            .padding(16.dp)
            .fillMaxWidth()


    ) {
        val imageUrl = "https://image.tmdb.org/t/p/w185${movie.posterPath}"


        Row {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(6.dp))

            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Movie Poster",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .scale(1f)
                        .size(50.dp)


                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = movie.title, modifier = Modifier.padding(top = 8.dp))
        }

        //Text(text = movie.releaseDate, modifier = Modifier.padding(top = 4.dp))

    }
}

