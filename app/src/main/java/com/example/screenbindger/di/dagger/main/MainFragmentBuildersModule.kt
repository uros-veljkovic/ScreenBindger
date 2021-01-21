package com.example.screenbindger.di.dagger.main

import com.example.screenbindger.view.fragment.genre_movies.GenreMoviesFragment
import com.example.screenbindger.view.fragment.genres.GenresFragment
import com.example.screenbindger.view.fragment.login.LoginFragment
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsFragment
import com.example.screenbindger.view.fragment.profile.ProfileFragment
import com.example.screenbindger.view.fragment.register.RegisterFragment
import com.example.screenbindger.view.fragment.splash.SplashFragment
import com.example.screenbindger.view.fragment.trending.TrendingFragment
import com.example.screenbindger.view.fragment.upcoming.UpcomingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.InternalCoroutinesApi

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeGenreMoviesFragment() : GenreMoviesFragment

    @ContributesAndroidInjector
    abstract fun contributeGenresFragment() : GenresFragment

    @ContributesAndroidInjector
    abstract fun contributeMovieDetailsFragment() : MovieDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment() : ProfileFragment


    @ContributesAndroidInjector
    abstract fun contributeTrendingFragment() : TrendingFragment

    @ContributesAndroidInjector
    abstract fun contributeUpcomingFragment() : UpcomingFragment
}