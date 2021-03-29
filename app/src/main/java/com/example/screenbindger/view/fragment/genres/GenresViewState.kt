package com.example.screenbindger.view.fragment.genres

import android.provider.MediaStore
import com.example.screenbindger.model.domain.genre.GenreEntity

sealed class GenresViewState {
    object Loading : GenresViewState()
    data class Fetched(val list: List<GenreEntity>) : GenresViewState()
    data class NotFetched(val messageStringResId: Int) : GenresViewState()
}