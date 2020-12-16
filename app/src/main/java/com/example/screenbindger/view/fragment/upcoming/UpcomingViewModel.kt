package com.example.screenbindger.view.fragment.upcoming

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.db.remote.response.UpcomingMoviesResponse
import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.model.global.Genres
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Response

class UpcomingViewModel
@ViewModelInject constructor(
    val db: ScreenBindgerRemoteDatabase
) : ViewModel() {

    val response: MutableLiveData<Response<UpcomingMoviesResponse>?> = MutableLiveData(null)
    val list: List<MovieEntity>? get() = response.value?.body()?.list

    init {
        fetchData()
    }

    private fun fetchData() {
        CoroutineScope(IO).launch {
            val result = db.getUpcoming()

            result.body()?.list?.forEach {entity ->
                generateStringGenresFor(entity)
            }

            response.postValue(result)
        }
    }

    private fun generateStringGenresFor(entity: MovieEntity) {
        entity.genreIds?.forEach { singleEntityGenreId ->
            Genres.list.forEach {
                if (it.id == singleEntityGenreId) {
                    entity.genresString += "${it.name}, "
                }
            }
        }
        entity.genresString = entity.genresString.dropLast(2)
    }
}