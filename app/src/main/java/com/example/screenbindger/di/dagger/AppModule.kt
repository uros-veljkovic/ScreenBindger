package com.example.screenbindger.di.dagger

import android.app.Application
import androidx.room.Room.databaseBuilder
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.db.remote.service.auth.FirebaseAuthService
import com.example.screenbindger.db.remote.service.genre.GenreApi
import com.example.screenbindger.db.remote.service.genre.GenreService
import com.example.screenbindger.db.remote.service.movie.MovieApi
import com.example.screenbindger.db.remote.service.movie.MovieService
import com.example.screenbindger.db.remote.service.user.FirebaseUserService
import com.example.screenbindger.util.constants.API_BASE_URL
import com.example.screenbindger.util.constants.LOCAL_DATABASE_NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {


    @Singleton
    @Provides
    fun provideRemoteRepository(
        movieService: MovieService,
        genreService: GenreService,
        authService: FirebaseAuthService,
        userService: FirebaseUserService
    ): ScreenBindgerRemoteDatabase {
        return ScreenBindgerRemoteDatabase(
            movieService,
            genreService,
            authService,
            userService
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

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseUserService(
        database: FirebaseFirestore,
        currentUser: FirebaseUser?
    ): FirebaseUserService {
        return FirebaseUserService(database, currentUser)
    }

    @Singleton
    @Provides
    fun provideDatabaseReference(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Singleton
    @Provides
    fun provideFirebaseCurrentUser(auth: FirebaseAuth): FirebaseUser? {
        return auth.currentUser
    }
}