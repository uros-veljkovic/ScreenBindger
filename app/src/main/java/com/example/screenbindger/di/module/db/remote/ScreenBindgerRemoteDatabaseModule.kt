package com.example.screenbindger.di.module.db.remote

import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.db.remote.service.genre.GenreApi
import com.example.screenbindger.db.remote.service.genre.GenreService
import com.example.screenbindger.db.remote.service.movie.MovieApi
import com.example.screenbindger.db.remote.service.movie.MovieService
import com.example.screenbindger.util.constants.API_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ScreenBindgerRemoteDatabaseModule {

    @Singleton
    @Provides
    fun provideRemoteRepository(
        movieService: MovieService,
        genreService: GenreService
    ): ScreenBindgerRemoteDatabase {
        return ScreenBindgerRemoteDatabase(
            movieService,
            genreService
        )
    }

    @Singleton
    @Provides
    fun provideMovieService(
        movieApi: MovieApi
    ): MovieService {
        return MovieService(movieApi)
    }

    @Singleton
    @Provides
    fun provideMovieApi(
        retrofit: Retrofit
    ): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGenreService(
        genreApi: GenreApi
    ): GenreService {
        return GenreService(genreApi)
    }

    @Singleton
    @Provides
    fun provideGenreApi(
        retrofit: Retrofit
    ): GenreApi {
        return retrofit.create(GenreApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}