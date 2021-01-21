package com.example.screenbindger.view.fragment.genre_movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.db.remote.response.GenreMoviesResponse
import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.model.global.Genres
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class GenreMoviesViewModel
@Inject constructor(
    val db: ScreenBindgerRemoteDatabase
) : ViewModel() {

    var response: MutableLiveData<Response<GenreMoviesResponse>?> = MutableLiveData(null)
    val list: List<MovieEntity>? get() = response.value?.body()?.list

    fun fetchData(genreId: Int) {
        CoroutineScope(IO).launch {
            val result = db.getMoviesByGenre(genreId.toString())

            CoroutineScope(Default).launch {
                result.body()?.list?.forEach { entity ->
                    generateStringGenresFor(entity)
                }
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