package com.example.movieTracker.presentation.movie_list

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieTracker.common.Resource
import com.example.movieTracker.domain.model.Movie
import com.example.movieTracker.domain.usecase.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    private val _state = mutableStateOf(MovieListState())
    val state: State<MovieListState> = _state

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = searchText
        .debounce(300L)
        .onEach {
            _isSearching.value = true
        }
        .combine(_movies) { query, movies ->
            if (query.isBlank()) {
                movies
            } else {
                Log.d(
                    "query-movies",
                    movies.filter { it.title.contains(query, ignoreCase = true) }.toString()
                )
                movies.filter { it.title.contains(query, ignoreCase = true) }
            }
        }
        .onEach {
            _isSearching.value = false
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _movies.value
        )

    init {
        getMovies()
    }

    fun onSearchTextChanged(text: String) {
        Log.d("onsearchtext", " value: $text")
        _searchText.value = text
        Log.d("onsearchtext", "_searchText  ${_searchText.value}")
    }

    private fun getMovies() {
        getMoviesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val movies = result.data ?: emptyList()
                    _state.value = MovieListState(movies = movies)
                    _movies.value = movies
                }

                is Resource.Error -> {
                    _state.value =
                        MovieListState(error = result.message ?: "Unexpected error has occurred.")
                }

                is Resource.Loading -> {
                    _state.value = MovieListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
