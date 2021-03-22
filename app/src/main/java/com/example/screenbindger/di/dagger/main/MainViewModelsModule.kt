package com.example.screenbindger.di.dagger.main

import androidx.lifecycle.ViewModel
import com.example.screenbindger.di.dagger.ViewModelKey
import com.example.screenbindger.view.fragment.favorite_movies.FavoritesViewModel
import com.example.screenbindger.view.fragment.genre_movies.GenreMoviesViewModel
import com.example.screenbindger.view.fragment.genres.GenresViewModel
import com.example.screenbindger.view.fragment.details.movie.MovieDetailsViewModel
import com.example.screenbindger.view.fragment.details.tv_show.TvShowDetailsViewModel
import com.example.screenbindger.view.fragment.profile.ProfileViewModel
import com.example.screenbindger.view.fragment.review.ReviewViewModel
import com.example.screenbindger.view.fragment.trending.TrendingViewModel
import com.example.screenbindger.view.fragment.upcoming.UpcomingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelsModule {


    @Binds
    @IntoMap
    @ViewModelKey(GenreMoviesViewModel::class)
    abstract fun bindGenreMoviesViewModel(viewModel: GenreMoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GenresViewModel::class)
    abstract fun bindGenresViewModel(viewModel: GenresViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun bindMovieDetailsViewModel(viewModel: MovieDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TvShowDetailsViewModel::class)
    abstract fun bindTvShowDetailsViewModel(viewModel: TvShowDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TrendingViewModel::class)
    abstract fun bindTrendingViewModel(viewModel: TrendingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpcomingViewModel::class)
    abstract fun bindUpcomingViewModel(viewModel: UpcomingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    abstract fun bindFavoriteMoviesViewModel(viewModel: FavoritesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReviewViewModel::class)
    abstract fun bindReviewFragmentViewModel(viewModel: ReviewViewModel): ViewModel
}