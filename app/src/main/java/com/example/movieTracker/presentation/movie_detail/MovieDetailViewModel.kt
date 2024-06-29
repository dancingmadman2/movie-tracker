package com.example.movieTracker.presentation.movie_detail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieTracker.common.Constants
import com.example.movieTracker.common.Resource

import com.example.movieTracker.domain.usecase.GetMovieDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _state = mutableStateOf(MovieDetailState())
    val state: State<MovieDetailState> = _state
    init {
        val movieIdString = savedStateHandle.get<String>(Constants.MOVIE_ID)
        movieIdString?.toIntOrNull()?.let { movieId ->
            Log.d("MOVIE_ID","Movie Id: $movieId")
            getMovieDetail(movieId)
        }
    }
/*
    init {
        savedStateHandle.get<Int>(Constants.MOVIE_ID)?.let { movieId ->
            getMovieDetail(movieId)
        }
    }


 */
    private fun getMovieDetail(movieId: Int) {
        getMovieDetailUseCase(movieId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = MovieDetailState(movie = result.data)
                }
                is Resource.Error -> {
                    _state.value = MovieDetailState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _state.value = MovieDetailState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
