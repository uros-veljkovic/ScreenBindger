package com.example.screenbindger.view.fragment.favorite_movies

interface OnFavoriteItemClickListener {
    fun onContainerClick(movieId: Int)
    fun onCommentsButtonClick(movieId: Int)
}