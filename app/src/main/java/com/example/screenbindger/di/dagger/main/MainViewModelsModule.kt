package com.example.screenbindger.di.dagger.main

import androidx.lifecycle.ViewModel
import com.example.screenbindger.di.dagger.ViewModelKey
import com.example.screenbindger.view.fragment.favorite_movies.FavoriteMoviesFragmentViewModel
import com.example.screenbindger.view.fragment.genre_movies.GenreMoviesViewModel
import com.example.screenbindger.view.fragment.genres.GenresFragmentViewModel
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsFragmentViewModel
import com.example.screenbindger.view.fragment.profile.ProfileFragmentViewModel
import com.example.screenbindger.view.fragment.review.ReviewFragmentViewModel
import com.example.screenbindger.view.fragment.trending.TrendingFragmentViewModel
import com.example.screenbindger.view.fragment.upcoming.UpcomingFragmentViewModel
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
    @ViewModelKey(GenresFragmentViewModel::class)
    abstract fun bindGenresViewModel(viewModel: GenresFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsFragmentViewModel::class)
    abstract fun bindMovieDetailsViewModel(viewModel: MovieDetailsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileFragmentViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TrendingFragmentViewModel::class)
    abstract fun bindTrendingViewModel(viewModel: TrendingFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpcomingFragmentViewModel::class)
    abstract fun bindUpcomingViewModel(viewModel: UpcomingFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteMoviesFragmentViewModel::class)
    abstract fun bindFavoriteMoviesViewModel(viewModel: FavoriteMoviesFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReviewFragmentViewModel::class)
    abstract fun bindReviewFragmentViewModel(viewModel: ReviewFragmentViewModel): ViewModel
}