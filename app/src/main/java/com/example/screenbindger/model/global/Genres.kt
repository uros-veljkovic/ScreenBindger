package com.example.screenbindger.model.global

import com.example.screenbindger.db.remote.service.genre.GenreService
import com.example.screenbindger.model.domain.GenreEntity
import javax.inject.Inject

object Genres {
    val list: MutableList<GenreEntity> = mutableListOf()
}