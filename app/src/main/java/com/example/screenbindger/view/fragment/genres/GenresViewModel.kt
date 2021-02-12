package com.example.screenbindger.view.fragment.genres

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.response.genre.AllGenresResponse
import com.example.screenbindger.model.domain.GenreEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class GenresViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource
) : ViewModel() {

    val response: MutableLiveData<Response<AllGenresResponse>?> = MutableLiveData(null)
    val list: List<GenreEntity>? get() = response.value?.body()?.list

    init {
        fetchData()
    }

    private fun fetchData(){
        CoroutineScope(IO).launch {
            response.postValue(remoteDataSource.getGenres())
        }
    }
}