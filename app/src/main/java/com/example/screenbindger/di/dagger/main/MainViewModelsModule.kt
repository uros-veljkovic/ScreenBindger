package com.example.screenbindger.di.dagger.main

import androidx.lifecycle.ViewModel
import com.example.screenbindger.di.dagger.ViewModelKey
import com.example.screenbindger.view.fragment.favorite_movies.FavoriteMoviesViewModel
import com.example.screenbindger.view.fragment.genre_movies.GenreMoviesViewModel
import com.example.screenbindger.view.fragment.genres.GenresViewModel
import com.example.screenbindger.view.fragment.login.LoginViewModel
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsViewModel
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsViewModel_Factory
import com.example.screenbindger.view.fragment.profile.ProfileViewModel
import com.example.screenbindger.view.fragment.register.RegisterViewModel
import com.example.screenbindger.view.fragment.splash.SplashViewModel
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
    @ViewModelKey(FavoriteMoviesViewModel::class)
    abstract fun bindFavoriteMoviesViewModel(viewModel: FavoriteMoviesViewModel): ViewModel
}