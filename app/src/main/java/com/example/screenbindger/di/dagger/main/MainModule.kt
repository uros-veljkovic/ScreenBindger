package com.example.screenbindger.di.dagger.main

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.model.state.ObjectState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.view.fragment.favorite_movies.FavoriteMoviesViewAction
import com.example.screenbindger.view.fragment.favorite_movies.FavoriteMoviesViewEvent
import com.example.screenbindger.view.fragment.favorite_movies.FavoriteMoviesViewState
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsState
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsViewAction
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsViewEvent
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsViewState
import com.example.screenbindger.view.fragment.profile.FragmentStateObservable
import com.example.screenbindger.view.fragment.trending.TrendingViewState
import com.example.screenbindger.view.fragment.upcoming.UpcomingViewState
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideUser(): UserEntity = UserEntity()

    @MainScope
    @Provides
    fun provideChangeableFragmentStateObservable(): FragmentStateObservable {
        return FragmentStateObservable()
    }

    @MainScope
    @Provides
    fun provideUserState(userEntity: UserEntity): ObjectState<UserEntity> {
        return ObjectState.InitialState(userEntity)
    }

    @MainScope
    @Provides
    fun provideProfilePictureUri(): MutableLiveData<Uri?> {
        return MutableLiveData(null)
    }

    @MainScope
    @Provides
    fun provideUserStateObservable(
        userState: ObjectState<UserEntity>,
        profilePictureUri: MutableLiveData<Uri?>,
        userEntity: UserEntity
    ): UserStateObservable {
        return UserStateObservable(MutableLiveData(userState), profilePictureUri, userEntity)
    }

    @MainScope
    @Provides
    fun provideTrendingViewState(): MutableLiveData<TrendingViewState> =
        MutableLiveData(TrendingViewState())

    @MainScope
    @Provides
    fun provideUpcomingViewState(): MutableLiveData<UpcomingViewState> =
        MutableLiveData(UpcomingViewState())

    @MainScope
    @Provides
    fun provideMovieDetailsViewState(): MutableLiveData<MovieDetailsViewState> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideMovieDetailsViewAction(): MutableLiveData<Event<MovieDetailsViewAction>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideMovieDetailsViewEvent(): MutableLiveData<Event<MovieDetailsViewEvent>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideFavoriteMoviesViewState(): MutableLiveData<FavoriteMoviesViewState> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideFavoriteMoviesViewAction(): MutableLiveData<Event<FavoriteMoviesViewAction>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideFavoriteMoviesViewEvent(): MutableLiveData<Event<FavoriteMoviesViewEvent>> {
        return MutableLiveData()
    }


}