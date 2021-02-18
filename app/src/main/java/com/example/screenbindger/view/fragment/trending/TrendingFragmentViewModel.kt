package com.example.screenbindger.view.fragment.trending

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrendingFragmentViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val trendingViewState: MutableLiveData<TrendingFragmentViewState>
) : ViewModel() {

    fun fetchData() {
        CoroutineScope(IO).launch {
            remoteDataSource.getTrending(trendingViewState)
        }
    }

}