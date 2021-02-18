package com.example.screenbindger.di.dagger.main

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.user.UserEntity
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.state.ObjectState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.view.fragment.favorite_movies.FavoriteMoviesFragmentViewAction
import com.example.screenbindger.view.fragment.favorite_movies.FavoriteMoviesFragmentViewEvent
import com.example.screenbindger.view.fragment.favorite_movies.FavoriteMoviesFragmentViewState
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsFragmentViewAction
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsFragmentViewEvent
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsFragmentViewState
import com.example.screenbindger.view.fragment.profile.FragmentStateObservable
import com.example.screenbindger.view.fragment.review.ReviewFragmentViewAction
import com.example.screenbindger.view.fragment.review.ReviewFragmentViewEvent
import com.example.screenbindger.view.fragment.review.ReviewFragmentViewState
import com.example.screenbindger.view.fragment.trending.TrendingFragmentViewState
import com.example.screenbindger.view.fragment.upcoming.UpcomingFragmentViewState
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideUser(): UserEntity =
        UserEntity()

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
    fun provideTrendingViewState(): MutableLiveData<TrendingFragmentViewState> =
        MutableLiveData(TrendingFragmentViewState())

    @MainScope
    @Provides
    fun provideUpcomingViewState(): MutableLiveData<UpcomingFragmentViewState> =
        MutableLiveData(UpcomingFragmentViewState())

    @MainScope
    @Provides
    fun provideMovieDetailsViewState(): MutableLiveData<MovieDetailsFragmentViewState> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideMovieDetailsViewAction(): MutableLiveData<Event<MovieDetailsFragmentViewAction>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideMovieDetailsViewEvent(): MutableLiveData<Event<MovieDetailsFragmentViewEvent>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideFavoriteMoviesViewState(): MutableLiveData<FavoriteMoviesFragmentViewState> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideFavoriteMoviesViewAction(): MutableLiveData<Event<FavoriteMoviesFragmentViewAction>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideFavoriteMoviesViewEvent(): MutableLiveData<Event<FavoriteMoviesFragmentViewEvent>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideReviewFragmentViewState(): MutableLiveData<ReviewFragmentViewState> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideReviewFragmentViewAction(): MutableLiveData<Event<ReviewFragmentViewAction>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideReviewFragmentViewEvent(): MutableLiveData<Event<ReviewFragmentViewEvent>> {
        return MutableLiveData()
    }


}