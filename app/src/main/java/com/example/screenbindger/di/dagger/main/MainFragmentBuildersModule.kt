package com.example.screenbindger.di.dagger.main

import com.example.screenbindger.view.fragment.favorite_movies.FavoriteMoviesFragment
import com.example.screenbindger.view.fragment.genre_movies.GenreMoviesFragment
import com.example.screenbindger.view.fragment.genres.GenresFragment
import com.example.screenbindger.view.fragment.details.movie.MovieDetailsFragment
import com.example.screenbindger.view.fragment.details.tv_show.TvShowDetailsFragment
import com.example.screenbindger.view.fragment.profile.ProfileFragment
import com.example.screenbindger.view.fragment.review.ReviewFragment
import com.example.screenbindger.view.fragment.trending.TrendingFragment
import com.example.screenbindger.view.fragment.upcoming.UpcomingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeGenreMoviesFragment(): GenreMoviesFragment

    @ContributesAndroidInjector
    abstract fun contributeGenresFragment(): GenresFragment

    @ContributesAndroidInjector
    abstract fun contributeMovieDetailsFragment(): MovieDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeTvShowDetailsFragment(): TvShowDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeTrendingFragment(): TrendingFragment

    @ContributesAndroidInjector
    abstract fun contributeUpcomingFragment(): UpcomingFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoriteMoviesFragment(): FavoriteMoviesFragment

    @ContributesAndroidInjector
    abstract fun contributeReviewFragment(): ReviewFragment
}