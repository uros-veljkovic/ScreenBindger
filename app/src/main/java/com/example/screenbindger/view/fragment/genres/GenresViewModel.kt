package com.example.screenbindger.view.fragment.genres

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.db.remote.response.GenresResponse
import com.example.screenbindger.model.domain.GenreEntity
import com.example.screenbindger.model.domain.MovieEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Response

class GenresViewModel
@ViewModelInject constructor(
    val db: ScreenBindgerRemoteDatabase
) : ViewModel() {

    val response: MutableLiveData<Response<GenresResponse>?> = MutableLiveData(null)
    val list: List<GenreEntity>? get() = response.value?.body()?.list

    init {
        fetchData()
    }

    private fun fetchData(){
        CoroutineScope(IO).launch {
            response.postValue(db.getGenres())
        }
    }
}