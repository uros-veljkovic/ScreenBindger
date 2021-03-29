package com.example.screenbindger.view.fragment.genre_movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.response.movie.MoviesByGenreResponse
import com.example.screenbindger.db.remote.response.movie.MoviesResponse
import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.model.global.Genres
import com.example.screenbindger.util.constants.POSITION_TAB_MOVIES
import com.example.screenbindger.util.constants.POSITION_TAB_TV_SHOWS
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.view.fragment.*
import com.example.screenbindger.view.fragment.upcoming.UpcomingFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class GenreMoviesViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val viewState: MutableLiveData<ShowListViewState>
) : ViewModel() {

    var genreId: String? = null
    var currentPage: Int = 1

    fun executeAction(action: ShowListViewAction) {
        when (action) {
            is FetchMovies -> {
                fetchMovies()
            }
            is FetchTvShows -> {
                fetchTvShows()
            }
            is GotoNextPage -> {
                nextPage()
            }
            is GotoPreviousPage -> {
                previousPage()
            }
            is ResetState -> {
                viewState.value = ShowListViewState.Fetching
            }
        }
    }

    private fun fetchMovies() = executeActionAndSetState {
        remoteDataSource.getMoviesByGenre(genreId!!)
    }

    private fun fetchTvShows() = executeActionAndSetState {
        remoteDataSource.getTvShowsByGenre(genreId!!)
    }

    private fun nextPage() {
        currentPage++
        fetchAccordingToState()
    }

    private fun previousPage() {
        currentPage--
        fetchAccordingToState()
    }

    private fun fetchAccordingToState() {
        when (viewState.value) {
            is ShowListViewState.FetchedTvShows -> fetchTvShows()
            is ShowListViewState.Fetching,
            is ShowListViewState.FetchedMovies -> fetchMovies()
            else -> return
        }
    }

    fun getNavDirection(showId: Int): NavDirections? = when (viewState.value) {
        is ShowListViewState.FetchedMovies -> {
            GenreMoviesFragmentDirections.actionGenreMoviesFragmentToMovieDetailsFragment(showId)
        }
        is ShowListViewState.FetchedTvShows -> {
            GenreMoviesFragmentDirections.actionGenreMoviesFragmentToTvShowDetailsFragment(showId)
        }
        else -> {
            null
        }
    }

    private fun executeActionAndSetState(actionReturningState: suspend () -> ShowListViewState) =
        viewModelScope.launch(IO) {
            val newState = actionReturningState()
            viewState.postValue(newState)
        }

    fun tabSelected(position: Int) {
        currentPage = 1
        when (position) {
            POSITION_TAB_MOVIES -> executeAction(FetchMovies)
            POSITION_TAB_TV_SHOWS -> executeAction(FetchTvShows)
        }
    }
}