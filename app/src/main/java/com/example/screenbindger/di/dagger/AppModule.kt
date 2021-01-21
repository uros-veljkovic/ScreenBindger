package com.example.screenbindger.di.dagger

import android.app.Application
import androidx.room.Room.databaseBuilder
import com.example.screenbindger.db.local.repo.ScreenBindgerLocalDatabase
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.db.remote.service.genre.GenreApi
import com.example.screenbindger.db.remote.service.genre.GenreService
import com.example.screenbindger.db.remote.service.movie.MovieApi
import com.example.screenbindger.db.remote.service.movie.MovieService
import com.example.screenbindger.util.constants.API_BASE_URL
import com.example.screenbindger.util.constants.LOCAL_DATABASE_NAME
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideScreenBindgerDatabase(application: Application): ScreenBindgerLocalDatabase {
        return databaseBuilder(
            application.applicationContext,
            ScreenBindgerLocalDatabase::class.java,
            LOCAL_DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

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
    fun provideMovieService(movieApi: MovieApi) = MovieService(movieApi)


    @Singleton
    @Provides
    fun provideMovieApi(retrofit: Retrofit): MovieApi = retrofit.create(MovieApi::class.java)


    @Singleton
    @Provides
    fun provideGenreService(api: GenreApi) = GenreService(api)


    @Singleton
    @Provides
    fun provideGenreApi(retrofit: Retrofit): GenreApi = retrofit.create(GenreApi::class.java)


    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}