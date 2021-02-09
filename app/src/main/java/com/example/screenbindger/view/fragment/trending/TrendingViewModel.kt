package com.example.screenbindger.view.fragment.trending

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.response.TrendingMoviesResponse
import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.model.global.Genres
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class TrendingViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val trendingViewState: MutableLiveData<TrendingViewState>
) : ViewModel() {

    fun fetchData() {
        CoroutineScope(IO).launch {
            remoteDataSource.getTrending(trendingViewState)
        }
    }

/*    private fun generateStringGenresFor(entity: MovieEntity) {
        entity.genreIds?.forEach { singleEntityGenreId ->
            Genres.list.forEach {
                if (it.id == singleEntityGenreId) {
                    entity.genresString += "${it.name}, "
                }
            }
        }
        entity.genresString = entity.genresString.dropLast(2)
    }*/

    fun getAccountDetails(): String {
        return remoteDataSource.getDetails()
    }
}