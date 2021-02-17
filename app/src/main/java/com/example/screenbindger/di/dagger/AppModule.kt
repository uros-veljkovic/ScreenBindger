package com.example.screenbindger.di.dagger

import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.service.auth.firebase.FirebaseAuthService
import com.example.screenbindger.db.remote.service.auth.tmdb.TmdbAuthApi
import com.example.screenbindger.db.remote.service.auth.tmdb.TmdbAuthService
import com.example.screenbindger.db.remote.service.genre.GenreApi
import com.example.screenbindger.db.remote.service.genre.GenreService
import com.example.screenbindger.db.remote.service.movie.MovieApi
import com.example.screenbindger.db.remote.service.movie.MovieService
import com.example.screenbindger.db.remote.service.review.ReviewApi
import com.example.screenbindger.db.remote.service.review.ReviewService
import com.example.screenbindger.db.remote.service.storage.FirebaseStorageService
import com.example.screenbindger.db.remote.service.user.FirebaseUserService
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.domain.user.UserEntity
import com.example.screenbindger.util.constants.API_BASE_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {


    @Singleton
    @Provides
    fun provideRemoteDataSource(
        session: Session,
        movieService: MovieService,
        genreService: GenreService,
        tmdbAuthService: TmdbAuthService,
        authService: FirebaseAuthService,
        userService: FirebaseUserService,
        storageService: FirebaseStorageService,
        reviewService: ReviewService
    ): ScreenBindgerRemoteDataSource {
        return ScreenBindgerRemoteDataSource(
            session,
            movieService,
            genreService,
            tmdbAuthService,
            authService,
            userService,
            storageService,
            reviewService
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
    fun provideGenreApi(retrofit: Retrofit): GenreApi =
        retrofit.create(GenreApi::class.java)

    @Singleton
    @Provides
    fun provideTmdbAuthService(api: TmdbAuthApi): TmdbAuthService = TmdbAuthService(api)

    @Singleton
    @Provides
    fun provideTmdbAuthApi(retrofit: Retrofit): TmdbAuthApi =
        retrofit.create(TmdbAuthApi::class.java)

    @Singleton
    @Provides
    fun provideReviewService(api: ReviewApi): ReviewService = ReviewService(api)

    @Singleton
    @Provides
    fun provideReviewApi(retrofit: Retrofit): ReviewApi =
        retrofit.create(ReviewApi::class.java)

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
        auth: FirebaseAuth
    ): FirebaseUserService {
        return FirebaseUserService(database, auth)
    }

    @Singleton
    @Provides
    fun provideFirebaseStorageService(
        auth: FirebaseAuth
    ): FirebaseStorageService {
        return FirebaseStorageService(auth)
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

    @Singleton
    @Provides
    fun provideStorageReference(): StorageReference {
        return FirebaseStorage.getInstance().reference
    }

    @Singleton
    @Provides
    fun provideSession(
        userEntity: UserEntity
    ): Session {
        return Session(user = userEntity)
    }

}